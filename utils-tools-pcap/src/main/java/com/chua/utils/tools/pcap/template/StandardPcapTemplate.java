package com.chua.utils.tools.pcap.template;

import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.pcap.operator.PcapOperator;
import com.chua.utils.tools.pcap.operator.StandardPcapOperator;
import com.chua.utils.tools.pcap.packet.Packet;
import com.chua.utils.tools.util.SystemUtil;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.pcap4j.core.*;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 标准模板
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/20
 */
@AllArgsConstructor
@NoArgsConstructor
public class StandardPcapTemplate implements PcapTemplate {

    private String networkName;
    private int packetCount = -1;
    private PcapOperator pcapOperator = new StandardPcapOperator();
    private final ConcurrentMap<String, List<PcapHandle>> pcapHandles = new ConcurrentHashMap<>();


    public StandardPcapTemplate(int packetCount) {
        this.packetCount = packetCount;
        pcapOperator.setPacketCount(packetCount);
    }

    public StandardPcapTemplate(String networkName) {
        this.networkName = networkName;
    }

    public StandardPcapTemplate(String networkName, int packetCount) {
        this.networkName = networkName;
        this.packetCount = packetCount;
        pcapOperator.setPacketCount(packetCount);
    }

    @Override
    public void sendPacket(byte[] rawData) throws Exception {
        // 实例化一个捕获报文的对象，设置抓包参数：长度，混杂模式，超时时间等
        final PcapHandle handle = createHandle("");
        handle.sendPacket(rawData);
    }

    @Override
    public void sendPacket(org.pcap4j.packet.Packet packet) throws Exception {
        // 实例化一个捕获报文的对象，设置抓包参数：长度，混杂模式，超时时间等
        final PcapHandle handle = createHandle("");
        handle.sendPacket(packet);
    }

    @Override
    public void dumpOpen(String filter, File file, Matcher<Packet> matcher) throws Exception {
        PcapHandle pcapHandle = Pcaps.openOffline(file.getAbsolutePath());
        pcapHandle.setFilter(filter, BpfProgram.BpfCompileMode.NONOPTIMIZE);
        Iterator<PcapPacket> iterator = pcapHandle.stream().iterator();
        while (iterator.hasNext()) {
            PcapPacket packets = iterator.next();
            packagePacket(packets, matcher);
        }
    }

    @Override
    public void dumpSave(String filter, File dumpFile) throws Exception {
        // 实例化一个捕获报文的对象，设置抓包参数：长度，混杂模式，超时时间等
        final PcapHandle handle = createHandle(filter);
        PcapDumper pcapDumper = handle.dumpOpen(dumpFile.getAbsolutePath());
        final AtomicLong dumped = new AtomicLong(0);
        // 6. prepare listener
        PacketListener listener = packet -> {
            try {
                pcapDumper.dump(packet, Instant.now());
                dumped.incrementAndGet();
            } catch (NotOpenException e) {
                e.printStackTrace();
            }
        };
        // 7. start looper
        try {
            handle.loop(pcapOperator.getPacketCount(), listener);
            // Print out handle statistics
            PcapStat stats = handle.getStats();
            System.out.println("Pakcets dumped: " + dumped.get());
            System.out.println("Packets received: " + stats.getNumPacketsReceived());
            System.out.println("Packets dropped: " + stats.getNumPacketsDropped());
            System.out.println("Packets dropped by interface: " + stats.getNumPacketsDroppedByIf());
            // Supported by WinPcap only
            if (SystemUtil.isWindow()) {
                System.out.println("Packets captured: " + stats.getNumPacketsCaptured());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            handle.close();
            pcapDumper.close();
        }

    }

    @Override
    public void filter(String filter, Matcher<Packet> matcher) throws Exception {
// 实例化一个捕获报文的对象，设置抓包参数：长度，混杂模式，超时时间等
        final PcapHandle handle = createHandle(filter);

        // 观察者模式，抓到报文回调gotPacket方法处理报文内容
        PacketListener listener = packet -> {
            packagePacket(packet, matcher);
        };

        // 直接使用loop无限循环处理包
        try {
            // COUNT设置为抓包个数，当为-1时无限抓包
            handle.loop(pcapOperator.getPacketCount(), listener);

            // Print out handle statistics
            PcapStat stats = handle.getStats();
            System.out.println("Packets received: " + stats.getNumPacketsReceived());
            System.out.println("Packets dropped: " + stats.getNumPacketsDropped());
            System.out.println("Packets dropped by interface: " + stats.getNumPacketsDroppedByIf());
            // Supported by WinPcap only
            if (SystemUtil.isWindow()) {
                System.out.println("Packets captured: " + stats.getNumPacketsCaptured());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            handle.close();
        }

    }

    /**
     * 打包
     *
     * @param packet  包
     * @param matcher 匹配器
     */
    private void packagePacket(PcapPacket packet, Matcher<Packet> matcher) {
        org.pcap4j.packet.Packet packetPacket = packet.getPacket();
        org.pcap4j.packet.Packet.Header header = packetPacket.getHeader();
        org.pcap4j.packet.Packet payload = packetPacket.getPayload();

        byte[] rawData = packetPacket.getRawData();

        Packet.Header header1 = new Packet.Header();
        header1.setRawData(header.getRawData());
        header1.setLength(header.length());

        List<Packet.Payload> payloadList = new ArrayList<>();
        for (org.pcap4j.packet.Packet packet1 : payload) {
            Packet.Payload payload1 = new Packet.Payload();
            payload1.setRawData(packet1.getRawData());
            payload1.setLength(packet1.length());

            payloadList.add(payload1);
        }

        // 抓到报文走这里...
        Packet packet1 = new Packet();
        packet1.setOriginalLength(packet.getOriginalLength());
        packet1.setHeader(header1);
        packet1.setRawData(rawData);
        packet1.setPayloads(payloadList);

        packet1.setOriginalPackage(packet);

        try {
            matcher.doWith(packet1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 PcapHandle
     *
     * @param filter 过滤器
     * @return PcapHandle
     * @throws Exception Exception
     */
    private PcapHandle createHandle(String filter) throws Exception {
        PcapNetworkInterface network = pcapOperator.selectNetwork(networkName);
        // 实例化一个捕获报文的对象，设置抓包参数：长度，混杂模式，超时时间等
        final PcapHandle handle = network.openLive(
                pcapOperator.getMessageLength(),
                PcapNetworkInterface.PromiscuousMode.valueOf(pcapOperator.getPromiscuousMode()),
                pcapOperator.getTimeout());

        // 设置过滤器
        if (filter.length() != 0) {
            handle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);
        }
        System.out.println("majorVersion: " + handle.getMajorVersion());
        System.out.println("minorVersion: " + handle.getMinorVersion());
        pcapHandles.computeIfAbsent(filter, input -> new ArrayList<>()).add(handle);
        return handle;
    }

    @Override
    public List<String> getInterfaceNetworks() throws Exception {
        return pcapOperator.getInterfaceNetworks();
    }

    @Override
    public void close() throws Exception {
        for (List<PcapHandle> value : pcapHandles.values()) {
            for (PcapHandle pcapHandle : value) {
                pcapHandle.close();
            }
        }
        pcapHandles.clear();
    }
}
