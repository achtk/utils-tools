package com.chua.utils.tools.prop.mapper;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件映射
 *
 * @author CH
 */
@Getter
@Setter
@Accessors(chain = true)
public class FileDataMapper {
    /**
     * 文件
     */
    private List<String> names = new ArrayList<>();
    /**
     * 属性
     */
    private List<FileMapper> fileMapperList = new ArrayList<>();

    /**
     * 添加FileMapper
     *
     * @param fileMapper FileMapper
     */
    public void addFileMapper(FileMapper fileMapper) {
        names.add(fileMapper.getName());
        fileMapperList.add(fileMapper);
    }
}
