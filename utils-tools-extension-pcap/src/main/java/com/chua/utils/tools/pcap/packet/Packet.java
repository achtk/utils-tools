package com.chua.utils.tools.pcap.packet;

import lombok.Data;

import java.util.Arrays;
import java.util.List;


/**
 * 包
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/20
 */
@Data
public class Packet {
    /**
     * 原始包
     */
    private org.pcap4j.packet.Packet originalPackage;

    private int originalLength;
    /**
     * 报文
     */
    private byte[] rawData;
    /**
     * 报文头
     */
    private Header header;

    private List<Payload> payloads;

    /**
     *
     */
    @Data
    public static class Header {
        byte[] rawData;
        int length;
    }

    /**
     *
     */
    @Data
    public static class Payload {
        byte[] rawData;
        int length;
    }

    @Override
    public String toString() {
        return originalLength + "";
    }
}
