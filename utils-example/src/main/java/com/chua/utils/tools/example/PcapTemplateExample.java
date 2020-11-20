package com.chua.utils.tools.example;

import com.chua.utils.tools.collects.collections.CollectionHelper;
import com.chua.utils.tools.pcap.template.PcapTemplate;
import com.chua.utils.tools.pcap.template.StandardPcapTemplate;
import org.pcap4j.packet.ArpPacket;

import java.io.File;
import java.util.List;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/20
 */
public class PcapTemplateExample {

    public static void main(String[] args) throws Exception {
        PcapTemplate pcapTemplate = new StandardPcapTemplate("192.168.1.140", 1);
        List<String> interfaceNetworks = pcapTemplate.getInterfaceNetworks();
        System.out.println(CollectionHelper.toString(interfaceNetworks));
        //pcapTemplate.dumpOpen(new File("D:/demo.dump"));
        pcapTemplate.filter(packet -> {
            ArpPacket packets = packet.getOriginalPackage().get(ArpPacket.class);
            System.out.println(packets);
        });
    }
}
