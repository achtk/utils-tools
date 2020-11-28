package com.chua.utils.tools.classes.entity;

import com.chua.utils.tools.classes.ClassHelper;
import javassist.Modifier;
import lombok.Data;

import java.util.List;

/**
 * 类描述
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/18
 */
@Data
public class ClassDescription {

    public static final ClassDescription INSTANCE = new ClassDescription();

    /**
     * 类名
     */
    private String name;
    /**
     * 修饰符
     */
    private int accessFlags;
    /**
     * 类
     */
    private Class<?> selfClass;
    /**
     * 父类
     */
    private String superClass;
    /**
     * 接口
     */
    private List<String> interfaceNames;
    /**
     * 注解
     */
    private List<String> annotations;
    /**
     * 字段
     */
    private List<FieldDescription> fieldDescriptions;
    /**
     * 方法
     */
    private List<MethodDescription> methodDescriptions;


    public Class<?> getSelfClass() {
        if (selfClass == null) {
            selfClass = ClassHelper.forName(name);
        }
        return selfClass;
    }

    /**
     * 是父类子类
     *
     * @param parentClass 父类
     * @return 是父类子类返回true
     */
    public boolean isSubclass(Class<?> parentClass) {
        if (parentClass == null) {
            return false;
        }
        if (!Modifier.isPublic(accessFlags) ||
                Modifier.isEnum(accessFlags) ||
                Modifier.isInterface(accessFlags)) {
            return false;
        }

        if (null != superClass && superClass.equals(parentClass.getName())) {
            return true;
        }

        if (null == interfaceNames) {
            return false;
        }
        for (String interfaceName : interfaceNames) {
            if (interfaceName.equals(parentClass.getName())) {
                return true;
            }
        }
        Class<?> selfClass = getSelfClass();

        if (selfClass == null) {
            return false;
        }

        return parentClass.isAssignableFrom(selfClass);
    }

    /**
     * 是子类父类
     *
     * @param subClass 子类
     * @return 是子类父类返回true
     */
    public boolean isParentClass(Class<?> subClass) {
        if (subClass == null) {
            return false;
        }

        if (selfClass == null) {
            selfClass = ClassHelper.forName(name);
        }

        if (selfClass == null) {
            return false;
        }

        return selfClass.isAssignableFrom(subClass);
    }
}
