package com.chua.utils.tools.classes.classloader;


import com.chua.utils.tools.common.IoHelper;

import java.io.IOException;
import java.net.URL;

import static com.chua.utils.tools.constant.StringConstant.*;
import static com.chua.utils.tools.constant.SuffixConstant.SUFFIX_CLASS;
import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_DOT;
import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_LEFT_SLASH;

/**
 * @author CH
 * @since 1.0
 */
public class JarClassLoaderAdaptor implements IClassLoaderAdaptor {

    private URL url;

    @Override
    public boolean allow(String extension) {
        return extension.equals(URL_PROTOCOL_JAR) ||
                extension.equals(URL_PROTOCOL_ZIP) ||
                extension.equals(URL_PROTOCOL_WAR);
    }

    @Override
    public byte[] analyze(String name) {
        String formatUrl = formatUrl(url);
        String formatName = formatName(name);
        try {
            return IoHelper.toByteArray(new URL(formatUrl + formatName));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 格式化名称
     *
     * @param name 名称
     * @return 格式化名称
     */
    private String formatName(String name) {
        String newName = name.replace(SYMBOL_DOT, SYMBOL_LEFT_SLASH);
        if (name.startsWith(SYMBOL_LEFT_SLASH)) {
            newName = newName.substring(1);
        }
        if (name.endsWith(SYMBOL_LEFT_SLASH)) {
            newName = newName.substring(0, newName.length() - 1);
        }

        if (!name.endsWith(SUFFIX_CLASS)) {
            newName += SUFFIX_CLASS;
        }
        return newName;
    }

    @Override
    public void setUrl(URL url) {
        this.url = url;
    }

}
