package com.chua.utils.tools.example;

import org.zbus.mq.server.MqServer;
import org.zbus.mq.server.MqServerConfig;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/28
 */
public class ZBusExample {

    private static final DateTimeFormatter SDF_MONTH = DateTimeFormatter.ofPattern("yyyy-MM");

    public static void main(String[] args) {
        System.out.println(getYearMonths("2020"));
    }

    public static List<String> getYearMonths(String year) {
        LocalDate localDate = LocalDate.of(Integer.valueOf(year), 1, 1);

        return IntStream.range(0, 12).mapToObj(value -> {
            return localDate.plusMonths(value).format(SDF_MONTH);
        }).collect(Collectors.toList());
    }

    public static void main1(String[] args) throws Exception {
        MqServerConfig config = new MqServerConfig();
        config.serverPort = 15555;
        config.storePath = "./store";
        final MqServer server = new MqServer(config);
        server.start();
    }
}
