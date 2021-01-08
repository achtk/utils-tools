package com.chua.tools.example;

import com.chua.utils.tools.collects.OperateHashMap;
import com.chua.utils.tools.util.AnnotationUtils;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author CH
 * @version 1.0.0
 * @since 2021/1/8
 */
@XmlRootElement
public class AnnotationUtilsExample {

    public static void main(String[] args) {
        //测试获取类注解的值
        testToGetTheValueOfTheClassAnnotation();
    }

    /**
     * 测试获取类注解的值
     */
    private static void testToGetTheValueOfTheClassAnnotation() {
        OperateHashMap operateHashMap = AnnotationUtils.findValuesByType(new AnnotationUtilsExample(), XmlRootElement.class);
        System.out.println(operateHashMap);
    }
}
