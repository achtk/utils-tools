package com.chua.utils.tools.resource.parser.compress.dir;

import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.compress.file.JarInputParserFile;
import org.reflections.ReflectionsException;
import org.reflections.util.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

/**
 * JarInputDir
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class JarInputParserDir implements ParserDir {
    private final URL url;
    private final Matcher<ParserFile> matcher;
    public JarInputStream jarInputStream;
    public long cursor = 0;
    long nextCursor = 0;

    public JarInputParserDir(URL url, Matcher<ParserFile> matcher) {
        this.url = url;
        this.matcher = matcher;
    }

    @Override
    public String getPath() {
        return url.getPath();
    }

    @Override
    public Iterable<ParserFile> getFiles() {
        return () -> new Iterator<ParserFile>() {
            ParserFile entry = null;

            {
                try {
                    jarInputStream = new JarInputStream(url.openConnection().getInputStream());
                } catch (Exception e) {
                    throw new ReflectionsException("Could not open url connection", e);
                }
            }

            @Override
            public boolean hasNext() {
                return entry != null || (entry = computeNext()) != null;
            }

            @Override
            public ParserFile next() {
                ParserFile next = entry;
                Matcher.doWith(matcher, next);
                entry = null;
                return next;
            }

            private ParserFile computeNext() {
                while (true) {
                    try {
                        ZipEntry entry = jarInputStream.getNextJarEntry();
                        if (entry == null) {
                            return null;
                        }

                        long size = entry.getSize();
                        if (size < 0) {
                            //JDK-6916399
                            size = 0xffffffffL + size;
                        }
                        nextCursor += size;
                        if (!entry.isDirectory()) {
                            return new JarInputParserFile(entry, JarInputParserDir.this, cursor, nextCursor);
                        }
                    } catch (IOException e) {
                        throw new ReflectionsException("could not get next zip entry", e);
                    }
                }
            }
        };
    }

    @Override
    public void close() {
        Utils.close(jarInputStream);
    }
}
