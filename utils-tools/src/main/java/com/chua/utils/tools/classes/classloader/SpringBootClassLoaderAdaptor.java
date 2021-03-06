package com.chua.utils.tools.classes.classloader;


import com.chua.utils.tools.common.IoHelper;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.chua.utils.tools.constant.StringConstant.CLASS_FILE_EXTENSION;
import static com.chua.utils.tools.constant.SymbolConstant.*;

/**
 * springboot 文件解析
 *
 * @author CH
 * @since 1.0
 */
public class SpringBootClassLoaderAdaptor implements IClassLoaderAdaptor {

    private static final String META_INFO = "META-INF/";
    private static final String MF = "MANIFEST.MF";
    private static final String SPRING_BOOT_MAIN = "Spring-Boot-Classes";
    private URL url;
    private String springBootClassName;

    @Override
    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public boolean allow(String extension) {
        return isSpringBoot(url);
    }

    @Override
    public byte[] analyze(String name) {
        return springBootMainClass(name);
    }


    /**
     * 处理springboot main
     *
     * @param name 类名
     * @return
     */
    private byte[] springBootMainClass(String name) {
        String replaceSource = springBootClassName.replace(SYMBOL_COLON, SYMBOL_EMPTY).replace(SPRING_BOOT_MAIN, SYMBOL_EMPTY).trim();
        String newUrl = url.toExternalForm() + replaceSource + (name.replace(SYMBOL_DOT, SYMBOL_LEFT_SLASH)) + CLASS_FILE_EXTENSION;
        try {
            return IoHelper.toByteArray(new URL(newUrl));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 检测是否是springboot编译的jar
     *
     * @param url url
     * @return
     */
    private boolean isSpringBoot(final URL url) {
        List<String> strings = parsingMf(url);
        if (null == strings) {
            return false;
        }
        for (String source : strings) {
            if (source.indexOf(SPRING_BOOT_MAIN) > -1) {
                this.springBootClassName = source;
                return true;
            }
        }
        return false;
    }

    /**
     * 解析MF文件
     *
     * @param url url
     * @return
     */
    private List<String> parsingMf(URL url) {
        if (null != url) {
            String mf = formatUrl(url) + META_INFO + MF;
            try {
                return IoHelper.toList(new URL(mf), "UTF-8");
            } catch (IOException e) {
                return null;
            }
        }

        return null;
    }
}
