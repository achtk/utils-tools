package com.chua.utils.tools.function;

import java.util.Map;

/**
 * 模板
 * <p>对应spring template</p>
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/21
 */
public interface Template {
    /**
     * 获取模板
     *
     * @param template 模板
     * @param params   参数
     * @return 模板
     */
    String getTemplate(String template, Map<String, Object> params);

    /**
     * 输出文件
     *
     * @param template 模板
     * @param params   参数
     * @param path     路径
     */
    void writeAndClose(String template, Map<String, Object> params, String path);

}
