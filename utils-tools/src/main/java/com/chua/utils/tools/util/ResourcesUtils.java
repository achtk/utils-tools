package com.chua.utils.tools.util;

import com.google.common.base.Strings;
import com.google.common.io.Resources;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 资源文件
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/19
 */
public class ResourcesUtils {
    /**
     * 获取资源文件
     *
     * @param source 数据源
     * @return URL
     */
    public static URL getResource(String source) {
        if (Strings.isNullOrEmpty(source)) {
            return null;
        }
        File temp = new File(source);
        try {
            if (temp.exists()) {
                return temp.toURI().toURL();
            }
        } catch (MalformedURLException e) {
        }
        return Resources.getResource(source);
    }
}
