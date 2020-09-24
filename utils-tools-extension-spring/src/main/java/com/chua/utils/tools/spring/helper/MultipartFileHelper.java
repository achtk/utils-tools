package com.chua.utils.tools.spring.helper;

import com.chua.utils.tools.common.IOHelper;
import com.chua.utils.tools.common.StringHelper;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * MultipartFile 工具类
 * @author CH
 * @version 1.0.0
 * @since 2020/4/9 10:49
 */
public class MultipartFileHelper {
    /**
     * 创建文件
     *  @param multipartFile 文件
     * @param path          文件路径
     * @return
     */
    public static URL createFile(final MultipartFile multipartFile, final String path) {
        return createFile(multipartFile, path, null);
    }

    /**
     * 创建文件
     *  @param multipartFile 文件
     * @param path          文件路径
     * @param name          文件名
     * @return
     */
    public static URL createFile(final MultipartFile multipartFile, final String path, final String name) {
        if (StringHelper.isBlank(path)) {
            return null;
        }
        if (null == multipartFile) {
            return null;
        }
        long size = multipartFile.getSize();
        if (size <= 0) {
            return null;
        }
        String originalFilename = StringHelper.defaultIfBlank(name, multipartFile.getOriginalFilename());
        File partFile = new File(path, originalFilename);
        try {
            IOHelper.write(multipartFile.getBytes(), new FileOutputStream(partFile));
            return partFile.toURL();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
