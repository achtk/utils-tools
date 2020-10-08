package com.chua.utils.tools.cfg;

import com.chua.utils.tools.common.BooleanHelper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
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
     * 默认 slaver
     */
    private static final List<String> DEFAULT_SLAVERS = Lists.newArrayList("extension/", "META-INF/extension/");
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
     * <p>处理时和 @see #slavers 合并处理</p>
     */
    private String slaverName;
    /**
     * <p>次要配置文件名称</p>
     * <p>主要配置文件可以通过该属性去解析文件</p>
     * <p>e.g.C://xx.json</p>
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

    /**
     * 文件目录
     * @return
     */
    public List<String> getSlavers() {
        return BooleanHelper.hasLength(slavers) ? slavers : DEFAULT_SLAVERS;
    }

    /**
     * 获取排序字段
     * @return
     */
    public String getOrder() {
        return Strings.isNullOrEmpty(order) ? SYSTEM_PRIORITY_PROP : order;
    }
}
