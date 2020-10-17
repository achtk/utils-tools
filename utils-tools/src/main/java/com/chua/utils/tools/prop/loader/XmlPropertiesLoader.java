package com.chua.utils.tools.prop.loader;

import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.common.MapHelper;
import com.chua.utils.tools.common.PropertiesHelper;
import com.google.common.base.Charsets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

/**
 * json解析为properties数据加载器
 * @author CH
 */
public class XmlPropertiesLoader implements PropertiesLoader {
    @Override
    public String[] suffix() {
        return new String[] {"json"};
    }

    @Override
    public Properties toProp(InputStream inputStream) {
        if(null == inputStream) {
            return PropertiesHelper.emptyProperties();
        }
        try(InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charsets.UTF_8)) {
            Map properties = JsonHelper.fromXmlJson(inputStreamReader, Map.class);
            return MapHelper.map2Yaml(properties);
        } catch (IOException e) {
            return PropertiesHelper.emptyProperties();
        }
    }
}
