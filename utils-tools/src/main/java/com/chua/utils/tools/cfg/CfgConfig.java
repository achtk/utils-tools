package com.chua.utils.tools.cfg;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.chua.utils.tools.constant.StringConstant.SYSTEM_PRIORITY_PROP;

/**
 * cfg配置
 * @author CH
 * @since 1.0
 */
@Getter
@Setter
public class CfgConfig {
    /**
     * 主要配置文件
     */
    private String master;
    /**
     * 类加载器
     */
    private ClassLoader classLoader;
    /**
     * <p>排序字段</p>
     * <p>越大优先级越小</p>
     */
    private String order;
    /**
     * 次要配置文件
     */
    private List<String> slavers = new ArrayList<>();
    /**
     * 次要配置文件名称
     */
    private String slaverName;

    /**
     * <p>次要配置文件名称</p>
     * <p>主要配置文件可以通过该属性去解析文件</p>
     */
    private String slaverKey;

    /**
     * 添加配置
     * @param strings
     * @return
     */
    public CfgConfig addSlaver(String... strings) {
        if(null != strings || strings.length == 0) {
            return this;
        }
        for (String string : strings) {
            if(Strings.isNullOrEmpty(string)) {
                continue;
            }
            slavers.add(string);
        }
        return this;
    }

    public String getOrder() {
        return Strings.isNullOrEmpty(order) ? SYSTEM_PRIORITY_PROP : order;
    }
}
