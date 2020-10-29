package com.chua.utils.tools.spring.placeholder;

import com.chua.utils.tools.common.MapHelper;
import com.chua.utils.tools.prop.placeholder.PropertiesPropertyPlaceholder;
import com.chua.utils.tools.prop.placeholder.PropertyPlaceholder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.Map;
import java.util.function.Consumer;

/**
 * spring占位符
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/29
 */
@NoArgsConstructor
public class SpringPropertyPlaceholder extends PropertiesPropertyPlaceholder {

    private Environment environment;

    public SpringPropertyPlaceholder(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Object placeholder(Object value) {
        if (!(value instanceof String)) {
            return value;
        }
        return environment.resolvePlaceholders(value.toString());
    }
}
