package com.chua.utils.tools.cfg;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.prop.loader.PropertiesLoader;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.chua.utils.tools.constant.StringConstant.SYSTEM_PRIORITY_PROP;

/**
 * cfg配置
 * @author CH
 * @since 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class CfgConfig {
    /**
     * 配置文件名称
     */
    private String master;
    /**
     * 类加载器
     */
    private ClassLoader classLoader = ClassHelper.getDefaultClassLoader();
    /**
     * <p>排序字段</p>
     * <p>越大优先级越小</p>
     */
    private String order = SYSTEM_PRIORITY_PROP;
    /**
     * 文件类型
     */
    private Set<String> suffix = Sets.newHashSet("json");
    /**
     * 文件所在目录
     */
    private Set<String> slaver = Sets.newHashSet("", "extension/", "META-INF/extension/", "extensions/", "META-INF/extensions/");

    /**
     * 添加配置
     * @param strings
     * @return
     */
    public CfgConfig addSlaver(String... strings) {
        if(null == strings || strings.length == 0) {
            return this;
        }
        for (String string : strings) {
            if(Strings.isNullOrEmpty(string)) {
                continue;
            }
            slaver.add(string);
        }
        return this;
    }

    /**
     * 获取排序字段
     * @return
     */
    public String getOrder() {
        return Strings.isNullOrEmpty(order) ? SYSTEM_PRIORITY_PROP : order;
    }
}
