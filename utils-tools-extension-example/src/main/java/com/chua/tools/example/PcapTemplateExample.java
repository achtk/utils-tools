package com.chua.tools.example;
//
//import com.chua.utils.tools.collects.collections.CollectionHelper;
//import com.chua.utils.tools.pcap.packet.Packet;
//import com.chua.utils.tools.pcap.template.PcapTemplate;
//import com.chua.utils.tools.pcap.template.StandardPcapTemplate;
//import org.pcap4j.packet.TcpPacket;

/**
 * pcap工具
 * @author CH
 * @version 1.0.0
 * @since 2020/11/20
 */
public class PcapTemplateExample {

    public static void main(String[] args) throws Exception {
//        PcapTemplate pcapTemplate = new StandardPcapTemplate("192.168.1.140", 6);
//        List<String> interfaceNetworks = pcapTemplate.getInterfaceNetworks();
//        System.out.println(CollectionHelper.toString(interfaceNetworks));
//        //pcapTemplate.dumpOpen(new File("D:/demo.dump"));
//        pcapTemplate.filter("tcp.port==6973", packet -> {
//            TcpPacket packets = packet.getOriginalPackage().get(TcpPacket.class);
//            System.out.println(packets);
//            List<Packet.Payload> payloads = packet.getPayloads();
//            for (Packet.Payload payload : payloads) {
//                System.out.println(payload.toString());
//            }
//        });
    }
}
