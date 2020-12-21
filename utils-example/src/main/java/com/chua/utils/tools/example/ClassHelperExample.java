package com.chua.utils.tools.example;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.example.entity.TDemoInfo;
import com.chua.utils.tools.util.ClassUtils;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/12
 */
public class ClassHelperExample {

    public static void main(String[] args) throws Exception {
        System.out.println("获取类所在的资源文件: " + ClassHelper.getUrlByClass(LoggerFactory.class));
        System.out.println("判断类是否是Object: " + ClassHelper.isObject(Object.class));
        System.out.println("字符串转类: " + ClassHelper.forName(LoggerFactory.class.getName()));
        System.out.println("字符串转对象: " + ClassHelper.forObject(ClassHelper.class.getName()));
        System.out.println("判断类是否存在: " + ClassHelper.isPresent(ClassHelper.class.getName()));
        System.out.println("判断类是否是另一个类的子类: " + ClassHelper.isAssignableFrom(ClassHelper.class, Object.class));
        System.out.println("获取默认的类加载器: " + ClassHelper.getDefaultClassLoader());
        System.out.println("获取对象字段的值: " + ClassHelper.getFieldValue(new TDemoInfo(), "uuid", String.class.getName()));
        System.out.println("获取对象的字段: " + ClassHelper.getFields(new TDemoInfo()));
        System.out.println("获取对象含有[@Column]的字段: " + ClassHelper.getAnnotationFields(new TDemoInfo(), Column.class));
        System.out.println("获取类加载器加载的URL: " + ClassHelper.getUrlsByClassLoader(ClassHelper.getDefaultClassLoader()));
        System.out.println("获取字段值:" + ClassUtils.getFieldIfOnlyValue(new TDemoInfo(), "uuid"));
        System.out.println("方法插入日志:" + ClassUtils.insertCode(new TDemoInfo(), "getId", 0, "{System.out.println(this.getClass().toString());}"));
    }
}
