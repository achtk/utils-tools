package com.chua.utils.tools.classes.classloader;


import com.chua.utils.tools.common.UrlHelper;

import java.net.URL;

import static com.chua.utils.tools.constant.StringConstant.*;

/**
 * 类加载器适配器
 *
 * @author CH
 * @since 1.0
 */
public interface IClassLoaderAdaptor {
    /**
     * 设置URL
     *
     * @param url
     */
    public void setUrl(URL url);

    /**
     * 解析的后缀类型
     *
     * @param extension 后缀
     * @return
     */
    public boolean allow(String extension);

    /**
     * 解析文件
     *
     * @param name
     * @return
     */
    public byte[] analyze(String name);

    /**
     * 格式化url
     *
     * @param url
     * @return
     */
    default String formatUrl(URL url) {
        String externalForm = url.toExternalForm();
        if (isSimpleUrl(externalForm)) {
            externalForm = JAR_URL_PREFIX + externalForm;
        }
        if (!externalForm.endsWith(JAR_URL_SEPARATOR)) {
            externalForm += JAR_URL_SEPARATOR;
        }

        return externalForm;
    }

    /**
     * 简单的url
     *
     * @param externalForm url
     * @return boolean
     */
    default boolean isSimpleUrl(String externalForm) {
        return (externalForm.endsWith(JAR) ||
                externalForm.endsWith(WAR) ||
                externalForm.endsWith(ZIP)) &&
                !externalForm.startsWith(JAR_URL_PREFIX);
    }
}
