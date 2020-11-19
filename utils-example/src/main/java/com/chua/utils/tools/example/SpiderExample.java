package com.chua.utils.tools.example;

import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.spider.factory.SpiderFactory;
import com.chua.utils.tools.spider.interpreter.DocumentInterpreter;
import com.chua.utils.tools.spider.interpreter.IPageInterpreter;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author CH
 */
public class SpiderExample {

    @Test
    public void spider() throws IOException {
        File file = new File("./administrativeDivisions.txt");
        if(file.exists()) {
            file.deleteOnExit();
        }

        file.createNewFile();

        DocumentInterpreter documentInterpreter = new DocumentInterpreter();
        documentInterpreter.callback(new Consumer<Map<String, List<String>>> () {

            @Override
            public void accept(Map<String, List<String>> stringListMap) {
                List<String> strings = FinderHelper.firstElement(stringListMap.values());
                if(strings.size() <= 1) {
                    return;
                }
               List<String> strings1 = strings.subList(1, strings.size());
                if(strings1.size() % 2 != 0) {
                    strings1 = strings1.subList(1, strings1.size());
                }
                for (int i = 0; i < strings1.size(); i += 2) {
                    String name = strings1.get(i);
                    String code = strings1.get(i + 1);
                    try {
                        FileUtils.writeStringToFile(file, name + "\t" + code + "\n", "UTF-8", true);
                    } catch (IOException e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        }).addModifier("//tbody/tr/td[@bgcolor='#FFFFFF']/a/text()");

        SpiderFactory factory = SpiderFactory.newBuilder()
                .addUrl("https://xingzhengquhua.51240.com/")
                .addInterpreter(documentInterpreter)
                .newFactory();
        factory.runAsync();
    }
}
