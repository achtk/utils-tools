package com.chua.utils.tools.spring.resolver;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import sun.nio.ch.IOUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

/**
 * ResponseEntity解释器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public class ResponseEntityResolver {
    /**
     * 文件返回值
     *
     * @param path 文件
     * @return ResponseEntity
     */
    public static ResponseEntity<byte[]> fileResponseEntity(Path path) throws IOException {
        return fileResponseEntity(path.toFile());
    }

    /**
     * 文件返回值
     *
     * @param file 文件
     * @return ResponseEntity
     */
    public static ResponseEntity<byte[]> fileResponseEntity(File file) throws IOException {
        return fileResponseEntity(Files.readAllBytes(file.toPath()), file.getName());
    }

    /**
     * 文件返回值
     *
     * @param bytes    数据
     * @param fileName 文件名
     * @return ResponseEntity
     */
    public static ResponseEntity<byte[]> fileResponseEntity(byte[] bytes, String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + fileName);
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));

        return org.springframework.http.ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(bytes);
    }
}
