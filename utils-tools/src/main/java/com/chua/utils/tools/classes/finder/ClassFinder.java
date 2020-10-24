package com.chua.utils.tools.classes.finder;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.reflections.ReflectionsHelper;
import com.chua.utils.tools.common.Assert;
import com.chua.utils.tools.function.Filter;

/**
 * 类查找器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/22
 */
public class ClassFinder {

    private transient Object obj;
    private Class<?> aClass;

    private transient FinderProperties finderProperties = new FinderProperties();

    public ClassFinder(Object obj) {
        this.obj = obj;
        this.aClass = null == obj ? null : obj.getClass();
    }

    public static ClassFinder newFinder(Object obj) {
        return new ClassFinder(obj);
    }

    /**
     * 是静态的
     *
     * @return
     */
    public ClassFinder isStatic() {
        finderProperties.setStatic(true);
        return this;
    }
    /**
     * 是静态的
     *
     * @return
     */
    public ClassFinder addFilter(Filter filter) {
        finderProperties.setFilter(filter);
        return this;
    }

    /**
     * 查找字段
     * @return
     */
    public ClassFieldFinder fieldFinder() {
        return new ClassFieldFinder(finderProperties);
    }
    /**
     * 查找方法
     * @return
     */
    public ClassMethodFinder methodFinder() {
        return new ClassMethodFinder(finderProperties);
    }
    /**
     * 查找子类
     * @return
     */
    public ClassAnnotationFinder subTypesFinder() {
        return new ClassAnnotationFinder(finderProperties);
    }
    /**
     * 查找子类
     * @return
     */
    public ClassSubTypeFinder fieldsAnnotatedTypesFinder() {
        return new ClassSubTypeFinder(finderProperties);
    }

}
