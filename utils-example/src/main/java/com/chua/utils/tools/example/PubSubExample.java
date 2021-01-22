//package com.chua.utils.tools.example;
//
//import com.chua.utils.netx.resolver.NetResolver;
//import com.chua.utils.netx.resolver.entity.NetPubSubConf;
//import com.chua.utils.netx.resolver.mq.NetPubSub;
//import com.chua.utils.tools.common.ByteHelper;
//import com.chua.utils.tools.common.ThreadHelper;
//import com.chua.utils.tools.spi.extension.ExtensionLoader;
//import com.chua.utils.tools.spi.factory.ExtensionFactory;
//import com.chua.utils.tools.text.IdHelper;
//import redis.clients.jedis.BinaryJedis;
//
//import java.io.IOException;
//import java.util.Properties;
//import java.util.concurrent.ExecutorService;
//import java.util.function.Consumer;
//
///**
// * @author CH
// * @version 1.0.0
// * @since 2020/11/21
// */
//public class PubSubExample {
//
//    public static void main(String[] args) throws IOException, InterruptedException {
//        //测试 consumer
//        testConsumer();
//
//    }
//
//    private static void testConsumer() throws InterruptedException {
//        System.out.println("*****************************测试consumer***************************");
//        ExtensionLoader<NetResolver> extensionLoader = ExtensionFactory.getExtensionLoader(NetResolver.class);
//        NetPubSub extension = (NetPubSub) extensionLoader.getExtension("rabbit");
//        NetResolver netResolver = (NetResolver) extension;
//        netResolver.setProperties(new Properties());
//
//        NetPubSubConf netPubSubConf = new NetPubSubConf();
//        ExecutorService executorService = ThreadHelper.newSingleThreadExecutor();
//        executorService.execute(() -> {
//            try {
//                extension.consumer(netPubSubConf, new Consumer<byte[]>() {
//                    @Override
//                    public void accept(byte[] bytes) {
//                        System.out.println(ByteHelper.toString(bytes));
//                    }
//                });
//            } catch (IOException e) {
//            }
//        });
//    }
//
//}
