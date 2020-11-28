package com.chua.utils.tools.resource.parser.vfs;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.empty.EmptyOrBase;
import com.chua.utils.tools.exceptions.NotSupportedException;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.resource.log.Log;
import com.chua.utils.tools.resource.parser.Parser;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.compress.*;
import com.chua.utils.tools.resource.parser.file.ClassParser;
import com.chua.utils.tools.resource.parser.file.JavaParser;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.reflections.ReflectionsException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * parser vfs
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
@SuppressWarnings("unused")
@Slf4j
public class ParserVfs {

    private static final List<Parser> DEFAULT_COMPRESS_TYPES = Lists.newArrayList(CompressEnum.values());
    private static final List<Parser> DEFAULT_CLASS_TYPES = Lists.newArrayList(ClassEnum.values());
    private static final List<Parser> DEFAULT_FILE_TYPES = Lists.newArrayList(FileEnum.values());

    /**
     * 获取所有压缩文件夹
     *
     * @param url url
     * @return ParserDir
     */
    public static ParserDir fromUrl(final URL url) {
        return fromUrl(url, item -> {
        });
    }

    /**
     * 获取所有压缩文件夹
     *
     * @param url     url
     * @param matcher 匹配器
     * @return ParserDir
     */
    public static ParserDir fromUrl(final URL url, final Matcher<ParserFile> matcher) {
        List<Parser> all = new ArrayList<>();
        all.addAll(DEFAULT_COMPRESS_TYPES);
        all.addAll(DEFAULT_CLASS_TYPES);
        all.addAll(DEFAULT_FILE_TYPES);
        return fromUrl(url, all, matcher);
    }

    /**
     * 获取所有压缩文件夹
     *
     * @param url url
     * @return ParserDir
     */
    public static ParserDir fromCompressUrl(final URL url) {
        return fromCompressUrl(url, item -> {
        });
    }

    /**
     * 获取所有压缩文件夹
     *
     * @param url url
     * @return ParserDir
     */
    public static ParserDir fromFileUrl(final URL url) {
        return fromFileUrl(url, item -> {
        });
    }

    /**
     * 获取所有压缩文件夹
     *
     * @param url     url
     * @param matcher 匹配器
     * @return ParserDir
     */
    public static ParserDir fromCompressUrl(final URL url, final Matcher<ParserFile> matcher) {
        return fromUrl(url, DEFAULT_COMPRESS_TYPES, matcher);
    }

    /**
     * 获取所有压缩文件夹
     *
     * @param url     url
     * @param matcher 匹配器
     * @return ParserDir
     */
    public static ParserDir fromFileUrl(final URL url, final Matcher<ParserFile> matcher) {
        return fromUrl(url, DEFAULT_COMPRESS_TYPES, matcher);
    }

    /**
     * 获取所有压缩文件夹
     *
     * @param url      url
     * @param urlTypes 类型
     * @param matcher  匹配器
     * @return ParserDir
     */
    public static ParserDir fromUrl(final URL url, final List<Parser> urlTypes, final Matcher<ParserFile> matcher) {
        for (Parser type : urlTypes) {
            try {
                if (type.matcher(url)) {
                    return type.path(url, matcher);
                }
            } catch (Throwable e) {
                Log.log.warn("could not create Dir using " + type + " from url " + url.toExternalForm() + ". skipping.", e);
            }
        }

        throw new IllegalStateException("could not create ParserDir from url, " +
                "no matching Parser was found [" + url.toExternalForm() + "]\n" +
                "either use fromURL(final URL url, final List<Parser> urlTypes) or " +
                "use the static setDefaultURLTypes(final List<Parser> urlTypes) " +
                "or addDefaultURLTypes(Parser urlType) " +
                "with your specialized Parser.");
    }

    /**
     * 添加解析类型
     *
     * @param parser 解析类型
     */
    public static void addDefaultParser(Parser parser) {
        DEFAULT_COMPRESS_TYPES.add(0, parser);
    }

    /**
     *
     */
    public enum ClassEnum implements Parser {
        /**
         * class
         */
        CLASS {
            private final Parser parser = new ClassParser();

            @Override
            public boolean matcher(URL url) {
                return parser.matcher(url);
            }

            @Override
            public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
                return parser.path(url, matcher);
            }
        },
        /**
         * class
         */
        JAVA {
            private final Parser parser = new JavaParser();

            @Override
            public boolean matcher(URL url) {
                return parser.matcher(url);
            }

            @Override
            public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
                return parser.path(url, matcher);
            }
        }
    }

    /**
     * 文件枚举
     */
    public enum FileEnum implements Parser {
        /**
         * xls
         */
        XLS {
            private Parser parser;
            {
                if (ClassHelper.isPresent(EmptyOrBase.EXCEL_PARSER)) {
                    this.parser = ClassHelper.forObject(EmptyOrBase.EXCEL_PARSER, Parser.class);
                } else {
                    log.warn("excel 解析依赖不存在");
                }
            }

            @Override
            public boolean matcher(URL url) {
                return null != parser && parser.matcher(url);
            }

            @Override
            public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
                return null == parser ? null : parser.path(url, matcher);
            }
        },
        /**
         * xlsx
         */
        XLSX {
            private Parser parser;
            {
                if (ClassHelper.isPresent(EmptyOrBase.EXCEL_PARSER)) {
                    this.parser = ClassHelper.forObject(EmptyOrBase.EXCEL_PARSER, Parser.class);
                } else {
                    log.warn("excel 解析依赖不存在");
                }
            }

            @Override
            public boolean matcher(URL url) {
                return null != parser && parser.matcher(url);
            }

            @Override
            public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
                return null == parser ? null : parser.path(url, matcher);
            }
        },
        /**
         * csv
         */
        CSV {
            private Parser parser;
            {
                if (ClassHelper.isPresent(EmptyOrBase.CSV_PARSER)) {
                    this.parser = ClassHelper.forObject(EmptyOrBase.CSV_PARSER, Parser.class);
                } else {
                    log.warn("csv解析依赖不存在");
                }
            }

            @Override
            public boolean matcher(URL url) {
                return null != parser && parser.matcher(url);
            }

            @Override
            public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
                return null == parser ? null : parser.path(url, matcher);
            }
        }
    }

    /**
     *
     */
    public enum CompressEnum implements Parser {
        /**
         * 压缩包
         */
        ZIP {
            private final Parser parser = new JarParser();

            @Override
            public boolean matcher(URL url) {
                return parser.matcher(url);
            }

            @Override
            public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
                return parser.path(url, matcher);
            }
        },
        /**
         * 压缩包
         */
        JAR {
            private final Parser parser = new JarParser();

            @Override
            public boolean matcher(URL url) {
                return parser.matcher(url);
            }

            @Override
            public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
                return parser.path(url, matcher);
            }
        },
        /**
         * 压缩包
         */
        JAR_URL {
            private final Parser parser = new JarUrlParser();

            @Override
            public boolean matcher(URL url) {
                return parser.matcher(url);
            }

            @Override
            public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
                return parser.path(url, matcher);
            }
        },
        /**
         * 压缩包
         */
        JAR_IO {
            private final Parser parser = new JarInputStreamParser();

            @Override
            public boolean matcher(URL url) {
                return parser.matcher(url);
            }

            @Override
            public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
                return parser.path(url, matcher);
            }
        },
        /**
         * 压缩包
         */
        JBOSS_VFS {
            private final Parser parser = new JbossVfsParser();

            @Override
            public boolean matcher(URL url) {
                return parser.matcher(url);
            }

            @Override
            public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
                return parser.path(url, matcher);
            }
        },
        /**
         * 压缩包
         */
        JBOSS_VFS_FILE {
            private final Parser parser = new JbossVfsFileParser();

            @Override
            public boolean matcher(URL url) {
                return parser.matcher(url);
            }

            @Override
            public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
                return parser.path(url, matcher);
            }
        },
        /**
         * 压缩包
         */
        DIRECTORY {
            private final Parser parser = new DirectoryParser();

            @Override
            public boolean matcher(URL url) {
                return parser.matcher(url);
            }

            @Override
            public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
                return parser.path(url, matcher);
            }
        },
        /**
         * 压缩包
         */
        BUNDLE {
            private final Parser parser = new BundleParser();

            @Override
            public boolean matcher(URL url) {
                return parser.matcher(url);
            }

            @Override
            public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
                return parser.path(url, matcher);
            }
        }
    }
}
