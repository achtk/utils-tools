package com.chua.utils.tools.spring.response;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *HttpServletResponse工具类
 * @author CH
 */
public class ResponseHelper {

    /**
     * 获取 ResponseEntity
     *
     * @return
     */
    public static ResponseEntity responseEntity(String methodType, String fileName, String file) {
        Path path = Paths.get(file).toAbsolutePath().normalize();
        Path normalize = path.resolve(fileName).normalize();
        UrlResource urlResource = null;
        try {
            urlResource = new UrlResource(normalize.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if(null != urlResource && urlResource.exists()) {
            return responseEntity(methodType, fileName, urlResource);
        }
        return null;
    }

    /**
     * 获取 ResponseEntity
     *
     * @return
     */
    public static ResponseEntity responseEntity(String methodType, String fileName, Resource resource) {
        if (null == methodType) {
            methodType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(methodType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    /**
     * 获取 HttpServletResponse
     * @return
     */
    public static HttpServletResponse getHttpServletResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }
}
