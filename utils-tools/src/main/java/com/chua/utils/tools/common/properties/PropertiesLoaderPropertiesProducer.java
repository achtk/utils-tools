package com.chua.utils.tools.common.properties;

import java.util.Map;
import java.util.Properties;

/**
 * 属性加载器的数据提供实现
 * @author CH
 * @date 2020-09-29
 */
public class PropertiesLoaderPropertiesProducer extends AbstractPropertiesProducer {

    public PropertiesLoaderPropertiesProducer() {
    }

    /**
     * 初始化
     *
     * @param source        数据源
     * @param configuration 配置
     */
    public PropertiesLoaderPropertiesProducer(Map<String, Properties> source, PropertiesConfiguration configuration) {
        super(source, configuration);
    }
}
