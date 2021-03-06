package com.chua.utils.tools.prop.loader;

import com.chua.utils.tools.aware.NamedAware;
import com.chua.utils.tools.common.PropertiesHelper;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_COMMA;

/**
 * json解析为properties数据加载器
 *
 * @author CH
 */
public class PropertiesProfileLoader implements ProfileLoader, NamedAware {
    @Override
    public String[] suffix() {
        return new String[]{"properties"};
    }

    @Override
    public Properties toProp(InputStream inputStream) {
        if (null == inputStream) {
            return PropertiesHelper.emptyProperties();
        }
        Properties properties = new Properties();
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charsets.UTF_8)) {
            properties.load(inputStreamReader);
            return properties;
        } catch (IOException e) {
            return PropertiesHelper.emptyProperties();
        }
    }

    @Override
    public String named() {
        return Joiner.on(SYMBOL_COMMA).join(suffix());
    }
}
