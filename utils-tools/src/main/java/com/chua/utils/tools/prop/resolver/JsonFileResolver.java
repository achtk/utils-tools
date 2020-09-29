package com.chua.utils.tools.prop.resolver;

import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.function.Converter;
import com.chua.utils.tools.function.NoneConverter;
import com.chua.utils.tools.prop.mapper.FileMapper;
import com.google.common.collect.HashMultimap;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * json解析工具
 * @author CH
 */
public class JsonFileResolver implements IFileResolver {

    private Properties properties;

    @Override
    public void stream(InputStream inputStream) {
        this.properties = JsonHelper.fromJson(inputStream, Properties.class);
    }

    @Override
    public FileMapper analysis(Converter converter) {
        if(null == properties) {
            return null;
        }
        if(null == converter) {
            converter = new NoneConverter();
        }
        HashMultimap hashMultimap = HashMultimap.create();
        FileMapper fileMapper = new FileMapper();
        fileMapper.setHashMultimap(hashMultimap);

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            hashMultimap.put(entry.getKey(), converter.doBackward(entry.getValue()));
        }
        return fileMapper;
    }

    @Override
    public String[] suffixes() {
        return new String[] {"json"};
    }
}
