package com.chua.utils.tools.spring.entity;

import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.spring.enums.BeanStatus;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * @author CH
 * @date 2020-09-26
 */
@Data
@NoArgsConstructor(staticName = "newLoader")
@EqualsAndHashCode
public class BeanLoader<T> {
    /**
     * 加载的对象
     */
    private HashMultimap<String, T> beans = HashMultimap.create();
    /**
     * 异常信息
     */
    private Map<String, BeanStatus> throwable = new HashMap<>();

    /**
     * 添加异常信息
     * @param name bean名称
     * @param beanStatus 导致原因
     * @return
     */
    public BeanLoader<T> throwable(String name, BeanStatus beanStatus) {
        this.throwable.put(name, beanStatus);
        return this;
    }
    /**
     * 添加异常信息
     * @param name bean名称
     * @return
     */
    public BeanLoader<T> throwable(String name) {
        this.throwable.put(name, BeanStatus.BEAN_NOT_EXIST);
        return this;
    }
    /**
     * 添加bean
     * @param entity 数据
     * @return
     */
    public synchronized BeanLoader<T> add(T entity) {
        if (null == entity) {
            return this;
        }
        beans.put(entity.getClass().getName(), entity);
        return this;
    }
    /**
     * 添加bean
     * @param params 数据
     * @return
     */
    public synchronized BeanLoader<T> addAll(Map<String, T> params) {
        if (null == params) {
            return this;
        }
        for (Map.Entry<String, T> entry : params.entrySet()) {
            beans.put(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * 获取所有bean
     * @return
     */
    public Collection<T> toCollection() {
        return beans.values();
    }

    /**
     * 根据名称获取bean
     * @param name 名称
     * @return
     */
    public Collection<T> get(final String name) {
        return Strings.isNullOrEmpty(name) ? Collections.emptyList() : beans.get(name);
    }

    /**
     * 返回有效的第一个实体
     * @return
     */
    public T findOne() {
        if(beans.isEmpty()) {
            return null;
        }
        return FinderHelper.firstElement(beans.values());
    }

}
