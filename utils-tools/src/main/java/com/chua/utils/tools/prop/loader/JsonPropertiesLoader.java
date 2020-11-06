package com.chua.utils.tools.prop.loader;

import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.common.PropertiesHelper;
import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

/**
 * json解析为properties数据加载器
 * @author CH
 */
@Slf4j
public class JsonPropertiesLoader implements PropertiesLoader {
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
            Map properties1 = JsonHelper.fromJson(inputStreamReader, Map.class);
            return MapOperableHelper.toProfile(properties1);
        } catch (IOException e) {
            return PropertiesHelper.emptyProperties();
        }
    }
}
