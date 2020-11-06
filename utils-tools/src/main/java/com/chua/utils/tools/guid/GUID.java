package com.chua.utils.tools.guid;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * guid
 *
 * @author CH
 * @date 2020-09-28
 */
public class GUID {

    public static volatile String valueBeforeMD5 = "";

    public static volatile String valueAfterMD5 = "";

    private static Random myRand;

    private static SecureRandom mySecureRand;

    private static String sId;

    static {
        mySecureRand = new SecureRandom();
        long secureInitializer = mySecureRand.nextLong();
        myRand = new Random(secureInitializer);
        try {
            sId = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    /**
     * 随机生成GUID码
     */
    public static GUID randomGuid() {
        return randomGuid(false);
    }

    /**
     * 随机生成GUID码
     *
     * @param secure
     */
    public static GUID randomGuid(boolean secure) {
        MessageDigest md5 = null;
        StringBuffer buffer = new StringBuffer();

        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: " + e);
        }

        try {
            long time = System.currentTimeMillis();
            long rand = 0;

            if (secure) {
                rand = mySecureRand.nextLong();
            } else {
                rand = myRand.nextLong();
            }

            buffer.append(sId);
            buffer.append(":");
            buffer.append(Long.toString(time));
            buffer.append(":");
            buffer.append(Long.toString(rand));

            valueBeforeMD5 = buffer.toString();
            md5.update(valueBeforeMD5.getBytes());

            byte[] array = md5.digest();
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < array.length; ++j) {
                int b = array[j] & 0xFF;
                if (b < 0x10) {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(b));
            }

            valueAfterMD5 = sb.toString();

        } catch (Exception e) {
            System.out.println("Error:" + e);
        }
        return new GUID();
    }

    /**
     * 转换成标准的GUID码
     * 例如: C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
     */
    @Override
    public String toString() {
        String raw = valueAfterMD5.toUpperCase();
        StringBuffer sb = new StringBuffer();
        sb.append(raw.substring(0, 8));
        sb.append("-");
        sb.append(raw.substring(8, 12));
        sb.append("-");
        sb.append(raw.substring(12, 16));
        sb.append("-");
        sb.append(raw.substring(16, 20));
        sb.append("-");
        sb.append(raw.substring(20));
        sb.append(raw);
        return sb.toString();
    }

}
