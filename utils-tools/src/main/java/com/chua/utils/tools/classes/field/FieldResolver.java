package com.chua.utils.tools.classes.field;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.exceptions.NonUniqueException;
import com.chua.utils.tools.function.Filter;
import org.checkerframework.checker.initialization.qual.NotOnlyInitialized;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 字段解释器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/24
 */
public interface FieldResolver {
    /**
     * 获取所有字段
     *
     * @return
     */
    Set<Field> fields();

    /**
     * 查找字段
     *
     * @param fieldName 字段名称
     * @param fieldType 字段类型
     * @return
     */
    Field findField(String fieldName, Class<?> fieldType);

    /**
     * 查找字段
     *
     * @param fieldName 字段名称
     * @return
     */
    Set<Field> findField(String fieldName);

    /**
     * 查找字段
     *
     * @param fieldFilter 过滤
     * @return
     */
    default Set<Field> findField(Filter<Field> fieldFilter) {
        if (null == fieldFilter) {
            return Collections.emptySet();
        }
        Set<Field> result = new HashSet<>();
        fields().parallelStream().forEach(new Consumer<Field>() {
            @Override
            public void accept(Field field) {
                boolean matcher = fieldFilter.matcher(field);
                if (matcher) {
                    result.add(field);
                }
            }
        });
        return result;
    }

    /**
     * 获取字段值
     *
     * @param fieldName 字段名称
     * @param tClass    类型
     * @param <T>
     * @return
     */
    <T> T getValue(String fieldName, Class<T> tClass) throws IllegalAccessException;
    /**
     * 获取字段值
     *
     * @param fieldName 字段名称
     * @param <T>
     * @return
     */
    default <T> T getValue(String fieldName) throws NonUniqueException, IllegalAccessException {
        Set<Field> field = findField(fieldName);
        Field element = FinderHelper.safeIfOnlyElement(field);
        if(null == element) {
            throw new NonUniqueException();
        }
        return (T) getValue(element.getName(), element.getType());
    }
}
