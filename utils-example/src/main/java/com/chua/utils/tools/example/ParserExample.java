//package com.chua.utils.tools.example;
//
//import com.chua.utils.tools.common.IoHelper;
//import com.chua.utils.tools.office.word.WordFactory;
//import com.chua.utils.tools.resource.parser.ParserDir;
//import com.chua.utils.tools.resource.parser.ParserFile;
//import com.chua.utils.tools.resource.parser.ParserProcess;
//import com.chua.utils.tools.resource.parser.vfs.ParserVfs;
//import com.youbenzi.md2.export.FileFactory;
//import org.reflections.Reflections;
//
//import java.io.File;
//import java.net.MalformedURLException;
//import java.net.URL;
//
///**
// * @author CH
// * @version 1.0.0
// * @since 2020/11/16
// */
//public class ParserExample {
//
//    public static void main(String[] args) throws Exception {
//        //测试ParserVfs
//         testParserVfs();
//    }
//
//    private static void testParserVfs() throws Exception {
//        ParserDir parserDir = ParserVfs.fromUrl(new File("D:\\other\\20201113.class").toURI().toURL());
//
//        Iterable<ParserFile> parserDirFiles = parserDir.getFiles();
//        for (ParserFile parserDirFile : parserDirFiles) {
//            if (parserDirFile instanceof ParserProcess) {
//                ((ParserProcess<?>) parserDirFile).doWith(item -> {
//                    System.out.println(item);
//                });
//            }
//        }
//    }
//}
