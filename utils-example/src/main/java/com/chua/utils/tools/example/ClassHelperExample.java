package com.chua.utils.tools.example;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.reflections.RewriteReflections;
import com.chua.utils.tools.classes.reflections.configuration.RewriteConfiguration;
import com.chua.utils.tools.example.entity.TDemoInfo;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import java.util.Set;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/12
 */
public class ClassHelperExample {

    public static void main(String[] args) {
        System.out.println("获取类所在的资源文件: " + ClassHelper.getUrlByClass(LoggerFactory.class));
        System.out.println("判断类是否是Object: " + ClassHelper.isObject(Object.class));
        System.out.println("字符串转类: " + ClassHelper.forName(LoggerFactory.class.getName()));
        System.out.println("字符串转对象: " + ClassHelper.forObject(ClassHelper.class.getName()));
        System.out.println("判断类是否存在: " + ClassHelper.isPresent(ClassHelper.class.getName()));
        System.out.println("判断类是否是另一个类的子类: " + ClassHelper.isAssignableFrom(ClassHelper.class, Object.class));
        System.out.println("获取默认的类加载器: " + ClassHelper.getDefaultClassLoader());
        System.out.println("获取对象的字段: " + ClassHelper.getFields(new TDemoInfo()));
        System.out.println("获取对象含有[@Column]的字段: " + ClassHelper.getAnnotationFields(new TDemoInfo(), Column.class));
        System.out.println("获取类加载器加载的URL: " + ClassHelper.getUrlsByClassLoader(ClassHelper.getDefaultClassLoader()));
    }
}
