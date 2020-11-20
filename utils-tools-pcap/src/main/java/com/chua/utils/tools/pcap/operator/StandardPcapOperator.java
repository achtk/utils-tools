package com.chua.utils.tools.pcap.operator;

import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.NetHelper;
import lombok.Getter;
import lombok.Setter;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.util.NifSelector;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;

/**
 * 标准操作器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/20
 */
@Getter
@Setter
public class StandardPcapOperator implements PcapOperator {

    private int messageLength = 65536;
    private int timeout = 10;
    private int packetCount = -1;
    private static final String LOOP = "LoopBack Driver";
    private String promiscuousMode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS.name();


    @Override
    public PcapNetworkInterface selectNetwork(String name) throws Exception {
        if (null == name) {
            List<PcapNetworkInterface> devs = Pcaps.findAllDevs();
            PcapNetworkInterface random = null;

            for (PcapNetworkInterface dev : devs) {
                String description = dev.getDescription();
                if (description.indexOf(LOOP) > -1) {
                    random = dev;
                    break;
                }
            }
            return null == random ? FinderHelper.firstElement(devs) : random;
        }
        if (!NetHelper.isIpv4Host(name)) {
            return Pcaps.getDevByName(name);
        }
        Inet4Address inet4Address = (Inet4Address) Inet4Address.getByName(name);
        return Pcaps.getDevByAddress(inet4Address);
    }

    @Override
    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
    }

    @Override
    public List<String> getInterfaceNetworks() throws Exception {
        List<PcapNetworkInterface> devs = Pcaps.findAllDevs();
        List<String> result = new ArrayList<>(devs.size());
        for (PcapNetworkInterface dev : devs) {
            result.add(dev.toString());
        }
        return result;
    }

}
