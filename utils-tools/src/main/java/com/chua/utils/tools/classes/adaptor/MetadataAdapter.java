package com.chua.utils.tools.classes.adaptor;

import com.chua.utils.tools.classes.entity.ClassDescription;
import com.chua.utils.tools.classes.entity.FieldDescription;
import com.chua.utils.tools.classes.entity.MethodDescription;

import java.util.List;

/**
 * 元数据适配器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/18
 */
public interface MetadataAdapter {
    /**
     * 是否为空
     *
     * @return 空返回true
     */
    boolean isEmpty();

    /**
     * 修饰符
     *
     * @return 修饰符
     */
    int getAccessFlags();

    /**
     * 父类
     *
     * @return 父类
     */
    String getSuperClass();

    /**
     * 获取注解
     *
     * @return 注解
     */
    List<String> getAnnotations();

    /**
     * 父类
     *
     * @return 父类
     */
    List<String> getInterfaceName();

    /**
     * 类名
     *
     * @return 类名
     */
    String getClassName();

    /**
     * 字段
     *
     * @return 字段
     */
    List<FieldDescription> getFields();

    /**
     * 方法
     *
     * @return 字段
     */
    List<MethodDescription> getMethods();

    /**
     * 获取描述
     *
     * @return 类描述
     */
    default ClassDescription description() {
        if (isEmpty()) {
            return null;
        }
        ClassDescription classDescription = new ClassDescription();
        classDescription.setSuperClass(getSuperClass());
        classDescription.setAccessFlags(getAccessFlags());
        classDescription.setInterfaceNames(getInterfaceName());
        classDescription.setName(getClassName());
        classDescription.setAnnotations(getAnnotations());

        classDescription.setFieldDescriptions(getFields());
        classDescription.setMethodDescriptions(getMethods());

        return classDescription;
    }

    /**
     * 获取描述
     *
     * @return 类描述
     */
    default ClassDescription descriptionExtends() {
        if (isEmpty()) {
            return null;
        }
        ClassDescription classDescription = new ClassDescription();
        classDescription.setSuperClass(getSuperClass());
        classDescription.setInterfaceNames(getInterfaceName());
        return classDescription;
    }
}
