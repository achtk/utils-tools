package com.chua.utils.tools.classes.classloader;


import com.chua.utils.tools.common.UrlHelper;

import java.net.URL;

/**
 * 类加载器适配器
 * @author CH
 * @since 1.0
 */
public interface IClassLoaderAdaptor {
    /**
     * 设置URL
     * @param url
     */
    public void setUrl(URL url);
    /**
     * 解析的后缀类型
     * @param extension 后缀
     * @return
     */
    public boolean allow(String extension);
    /**
     * 解析文件
     * @param name
     * @return
     */
    public byte[] analyze(String name);

    /**
     * 格式化url
     * @param url
     * @return
     */
    default public String formatUrl(URL url) {
        String externalForm = url.toExternalForm();
        if((externalForm.endsWith("jar") ||
                externalForm.endsWith("war") ||
                externalForm.endsWith("zip")) && !externalForm.startsWith(UrlHelper.JAR_URL_PREFIX)) {
            externalForm = UrlHelper.JAR_URL_PREFIX + externalForm;
        }
        if (!externalForm.endsWith(UrlHelper.JAR_URL_SEPARATOR)) {
            externalForm += UrlHelper.JAR_URL_SEPARATOR;
        }

        return externalForm;
    }
}
