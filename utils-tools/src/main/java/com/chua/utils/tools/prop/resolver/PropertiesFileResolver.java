package com.chua.utils.tools.prop.resolver;

import com.chua.utils.tools.function.FileConverter;
import com.chua.utils.tools.function.NoneFileConverter;
import com.chua.utils.tools.prop.mapper.FileMapper;
import com.google.common.collect.HashMultimap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * yaml解析
 *
 * @author CH
 */
public class PropertiesFileResolver implements IFileResolver {

    private final Properties properties = new Properties();

    @Override
    public void stream(InputStream inputStream) {
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FileMapper analysis(FileConverter fileConverter) {
        if (null == properties) {
            return null;
        }
        if (null == fileConverter) {
            fileConverter = new NoneFileConverter();
        }
        HashMultimap<Object, Object> hashMultimap = HashMultimap.create();
        FileMapper fileMapper = new FileMapper();
        fileMapper.setHashMultimap(hashMultimap);

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            hashMultimap.put(entry.getKey(), fileConverter.doBackward(entry.getValue()));
        }
        return fileMapper;
    }

    @Override
    public String[] suffixes() {
        return new String[]{"properties"};
    }
}
