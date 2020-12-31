package com.chua.tools.example;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public class Spider2Example {

    public static void main(String[] args) throws IOException {
        File file = new File("D:/administrativeDivisions.txt");
        if(file.exists()) {
            file.deleteOnExit();
        }
        System.out.println(file.getAbsolutePath());

        file.createNewFile();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

//        final Crawler crawler = new Crawler.Builder()
//                .setUrls("https://www.cnblogs.com/")
//                .setAllowSpread(true)
//                .setThreadCount(Runtime.getRuntime().availableProcessors() * 2)
//                .setPageParser(new XpathParser() {
//                    @Override
//                    public void parse(Document html, Map<String, List<String>> content) {
//                        String key = FinderHelper.firstElement(content.keySet());
//                        List<String> strings = content.get(key);
//                        if(null == strings || strings.size() <= 1) {
//                            return;
//                        }
//                        List<String> strings1 = strings.subList(1, strings.size());
//                        if(strings1.size() % 2 != 0) {
//                            strings1 = strings1.subList(1, strings1.size());
//                        }
//
//                        for (int i = 0; i < strings1.size(); i += 2) {
//                            String name = strings1.get(i);
//                            String code = strings1.get(i + 1);
//                            try {
//                                FileUtils.writeStringToFile(file, name + "\t" + code + "\n", "UTF-8", true);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                                continue;
//                            }
//                        }
//                    }
//
//                    @Override
//                    public Set<String> xpath() {
//                        return Sets.newHashSet("//a[@class=post-item-title]/text()");
//                    }
//                }).build();
//
//        crawler.runAsync();
//
//        executorService.execute(() -> {
//            while (true) {
//                int randomInt = RandomUtil.randomInt(100);
//                if(randomInt < 10) {
//                    System.out.println("------------------------------------------------");
//                    System.out.println("当前还有：" + crawler.getUrlLoader().nowNum());
//                    System.out.println("处理了：" + crawler.getUrlLoader().finishedNum());
//                }
//            }
//        });
    }
}
