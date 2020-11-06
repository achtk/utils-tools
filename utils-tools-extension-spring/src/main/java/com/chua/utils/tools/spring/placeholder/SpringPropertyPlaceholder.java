package com.chua.utils.tools.spring.placeholder;

import com.chua.utils.tools.prop.placeholder.PropertiesPropertyPlaceholder;
import lombok.NoArgsConstructor;
import org.springframework.core.env.Environment;

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
