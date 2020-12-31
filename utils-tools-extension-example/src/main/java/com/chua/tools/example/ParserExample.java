package com.chua.tools.example;

import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.ParserProcess;
import com.chua.utils.tools.resource.parser.vfs.ParserVfs;

import java.io.File;

/**
 * 文件解析
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/16
 */
public class ParserExample {

    public static void main(String[] args) throws Exception {
        //测试ParserVfs
        testParserVfs("D:\\other\\20201113.class");
    }

    /**
     * 测试ParserVfs
     *
     * @param name 文件名
     * @throws Exception
     */
    private static void testParserVfs(final String name) throws Exception {
        ParserDir parserDir = ParserVfs.fromUrl(new File(name).toURI().toURL());
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
