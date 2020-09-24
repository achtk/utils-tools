package com.chua.utils.tools.resource;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.StringHelper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.JarEntry;

import static com.chua.utils.tools.common.UrlHelper.JAR_URL_SEPARATOR;
import static com.chua.utils.tools.constant.StringConstant.*;

/**
 * @author CH
 */
@Getter
@Setter
@EqualsAndHashCode
@Accessors(chain = true)
public class Resource implements Serializable {
    /**
     * 资源URL
     */
    private URL url;
    /**
     * 字符编码
     */
    private String charset = "UTF-8";
    /**
     * 资源路径
     */
    private String path;
    /**
     * 文件
     */
    private String local;
    /**
     * 大小
     */
    private long size;
    /**
     * 资源名称
     */
    private String name;
    /**
     * 资源类型
     */
    private String type;
    /**
     * 类
     */
    private Class<?> classes;
    /**
     * 前缀
     */
    private String prefix;
    /**
     * 是否是目录
     */
    private boolean directory = true;
    /**
     * 类注解
     */
    private Annotation[] annotations;
    /**
     *
     */
    private String fileInfo;

    public Annotation[] getAnnotations() {
        if(null != this.annotations) {
            return annotations;
        }
        if(null != this.classes) {
            this.annotations = this.classes.getDeclaredAnnotations();
        }
        return this.annotations;
    }

    /**
     * @return
     */
    public URL getUrl() {
        if(Lazy.LAZY == url) {
            return analysisResourceUrl();
        }
        return url;
    }

    public String getName() {
        return null == name ? (null == url ? null : url.toExternalForm()) : name;
    }

    /**
     *
     * @return
     */
    public Class<?> getClasses() {
        if(null == classes) {
            return renderClassInfo();
        }
        return this.classes;
    }

    /**
     *
     * @return
     */
    public Class<?> getClasses(ClassLoader classLoader) {
        if(null == classes) {
            return renderClassInfo(classLoader);
        }
        return this.classes;
    }

    /**
     * 解析resource中的url
     * @return
     */
    private URL analysisResourceUrl() {
        if(null != path) {
            try {
                this.url = new URL(path + name);
                return this.url;
            } catch (MalformedURLException e) {
                this.url = null;
                return null;
            }
        }
        if(null == prefix || null == local) {
            return null;
        }
        try {
            this.url = new URL(prefix + local + JAR_URL_SEPARATOR + path.substring(1));
            return this.url;
        } catch (MalformedURLException e) {
            this.url = null;
            return null;
        }
    }

    /**
     * @return
     */
    private Class<?> renderClassInfo(ClassLoader... classLoader) {
        if (null != name && name.endsWith(EXTENSION_CLASS)) {
            name = StringHelper.startsWithAndEmpty(name, "/");
            String classPath = name.replace(EXTENSION_LEFT_SLASH, EXTENSION_DOT).replace(EXTENSION_CLASS_SUFFIX, EXTENSION_EMPTY);
            this.classes = ClassHelper.forName(classPath, BooleanHelper.hasLength(classLoader) ? FinderHelper.firstElement(classLoader) : this.getClass().getClassLoader());
            return this.classes;
        }
        return null;
    }


    /**
     * 获取resource对象
     *
     * @param url url
     * @return
     */
    public static Resource getResource(URL url) {
        Resource resource = new Resource();
        resource.setUrl(url);
        resource.setPath(url.toExternalForm());
        return resource;
    }


    /**
     * 获取resource对象
     *
     * @param file 文件
     * @param subPath 子目录
     * @return
     */
    public static Resource getResource(File file, String subPath) {
        if(file.isDirectory()) {
            return null;
        }
        Resource resource = new Resource();
        try {
            resource.setUrl(file.toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        resource.setName(subPath);
        //resource.setSize(file.getTotalSpace());
       //resource.setPath(file.getPath());
       // resource.setLocal(file.getAbsolutePath());
        resource.setDirectory(file.isDirectory());
       // resource.setType("file");

        return resource;
    }

    /**
     * 获取resource对象
     *
     * @param jarEntry
     * @param url
     * @return
     */
    public static Resource getResource(JarEntry jarEntry, URL url) {
        Resource resource = new Resource();
        String urlExternalForm = url.toExternalForm();

        resource.setUrl(Lazy.LAZY);
        resource.setName(jarEntry.getName());
        resource.setPath(urlExternalForm);
        resource.setDirectory(jarEntry.isDirectory());
      //  resource.setType("jar");

        return resource;
    }

}
