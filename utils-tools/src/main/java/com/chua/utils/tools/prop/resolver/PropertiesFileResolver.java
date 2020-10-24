package com.chua.utils.tools.prop.resolver;

import com.chua.utils.tools.prop.mapper.FileMapper;
import com.chua.utils.tools.function.AbstractConverter;
import com.chua.utils.tools.function.NoneAbstractConverter;
import com.google.common.collect.HashMultimap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * yaml解析
 * @author CH
 */
public class PropertiesFileResolver implements IFileResolver{

    private Properties properties = new Properties();

    @Override
    public void stream(InputStream inputStream) {
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FileMapper analysis(AbstractConverter abstractConverter) {
        if(null == properties) {
            return null;
        }
        if(null == abstractConverter) {
            abstractConverter = new NoneAbstractConverter();
        }
        HashMultimap hashMultimap = HashMultimap.create();
        FileMapper fileMapper = new FileMapper();
        fileMapper.setHashMultimap(hashMultimap);

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            hashMultimap.put(entry.getKey(), abstractConverter.doBackward(entry.getValue()));
        }
        return fileMapper;
    }

    @Override
    public String[] suffixes() {
        return new String[] {"properties"};
    }
}
