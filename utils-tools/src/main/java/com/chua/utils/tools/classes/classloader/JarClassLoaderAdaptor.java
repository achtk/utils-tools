package com.chua.utils.tools.classes.classloader;


import com.chua.utils.tools.common.IoHelper;
import com.chua.utils.tools.common.UrlHelper;

import java.io.IOException;
import java.net.URL;

import static com.chua.utils.tools.constant.StringConstant.*;

/**
 * @author CH
 * @since 1.0
 */
public class JarClassLoaderAdaptor implements IClassLoaderAdaptor{

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
     * @param name
     * @return
     */
    private String formatName(String name) {
        String newName = name.replace(".", "/");
        if(name.startsWith("/")) {
            newName = newName.substring(1);
        }
        if(name.endsWith("/")) {
            newName = newName.substring(0, newName.length() - 1);
        }

        if(!name.endsWith(".class")) {
            newName += ".class";
        }
        return newName;
    }

    @Override
    public void setUrl(URL url) {
        this.url = url;
    }

}
