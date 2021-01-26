package com.chua.utils.tools.prop.decorator;

import com.chua.utils.tools.prop.mapper.FileDataMapper;
import com.chua.utils.tools.prop.mapper.FileMapper;
import com.google.common.collect.HashMultimap;

import java.util.Map;
import java.util.Set;

/**
 * 文件修饰
 * @author CH
 */
public class FileDecorator {

    private final FileDataMapper fileDataMapper = new FileDataMapper();
    /**
     * 合并数据
     * @param fileMapper 文件映射
     */
    public void dissolves(FileMapper fileMapper) {
        converter2FileDataMapper(fileMapper);
    }

    /**
     * fileMapper -> fileDataMapper
     * @param fileMapper fileMapper
     */
    private void converter2FileDataMapper(FileMapper fileMapper) {
        HashMultimap<String, Object> result = HashMultimap.create();
        HashMultimap<String, Object> hashMultimap = fileMapper.getHashMultimap();
        Set<Map.Entry<String, Object>> entries = hashMultimap.entries();
        for (Map.Entry<String, Object> entry : entries) {
            if(entry.getValue() instanceof  Map) {
                converter2NoMap(entry.getKey(), (Map<String, Object>)entry.getValue(), result);
                continue;
            }

            result.put(entry.getKey(), entry.getValue());
        }
        FileMapper fileMapper1 = new FileMapper();
        fileMapper1.setName(fileMapper.getName());
        fileMapper1.setHashMultimap(result);

        fileDataMapper.addFileMapper(fileMapper1);
    }

    /**
     * 当结果里面还存在Map再次解析
     * @param key 索引
     * @param value 值
     * @param result 存储引用
     */
    private void converter2NoMap(String key, Map<String, Object> value, HashMultimap<String, Object> result) {
        String newKey;
        for (Map.Entry<String, Object> entry : value.entrySet()) {
            newKey = key + "." + entry.getKey();

            if(entry.getValue() instanceof  Map) {
                converter2NoMap(newKey, (Map<String, Object>)entry.getValue(), result);
                continue;
            }

            result.put(newKey, entry.getValue());
        }
    }

    /**
     *
     * @return
     */
    public FileDataMapper toMapper() {
        return fileDataMapper;
    }
}
