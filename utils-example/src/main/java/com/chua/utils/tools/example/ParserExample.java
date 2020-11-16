package com.chua.utils.tools.example;

import com.chua.utils.tools.common.IoHelper;
import com.chua.utils.tools.office.word.WordFactory;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.ParserProcess;
import com.chua.utils.tools.resource.parser.vfs.ParserVfs;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/16
 */
public class ParserExample {

    public static void main(String[] args) throws Exception {
        //测试ParserVfs
        // testParserVfs();
        //测试 WordFactory
        testWordFactory();
    }

    private static void testWordFactory() throws Exception {
        WordFactory wordFactory = new WordFactory();
        File file = new File("D:/demo.html");
        URL url = new File("D:\\other\\device-consumer.doc").toURI().toURL();
     //   wordFactory.doc2Html(new File(url.getFile()), file);

       // wordFactory.html2Doc(file, new File("D:/demo.doc"));

    }

    private static void testParserVfs() throws Exception {
        ParserDir parserDir = ParserVfs.fromUrl(new File("D:\\other\\周报陈华-20201113.xlsx").toURI().toURL());

        Iterable<ParserFile> parserDirFiles = parserDir.getFiles();
        for (ParserFile parserDirFile : parserDirFiles) {
            if (parserDirFile instanceof ParserProcess) {
                ((ParserProcess<?>) parserDirFile).doWith(item -> {
                    System.out.println(item);
                });
            }
        }
    }
}
