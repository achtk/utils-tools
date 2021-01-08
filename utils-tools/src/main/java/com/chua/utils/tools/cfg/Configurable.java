package com.chua.utils.tools.cfg;

import static com.chua.utils.tools.constant.StringConstant.SYSTEM_PRIORITY_PROP;

/**
 * 可配置
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/7
 */
public interface Configurable {

    /**
     * 配置文件
     *
     * @return 配置文件
     */
    String configurationFile();

    /**
     * 排序字段
     *
     * @return 排序字段
     */
    default String order() {
        return SYSTEM_PRIORITY_PROP;
    }

}
