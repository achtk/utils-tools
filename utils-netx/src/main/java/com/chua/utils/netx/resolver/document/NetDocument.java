package com.chua.utils.netx.resolver.document;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 文档
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
public interface NetDocument<Action> {
    /**
     * 查询文档
     *
     * @param action         动作
     * @param <T>            实体
     * @param entityClass    实体类型
     * @param collectionName 集合
     * @return 数据
     */
    <T> List<T> query(Action action, Class<T> entityClass, String collectionName);

    /**
     * 添加文档
     *
     * @param objectToSave   数据
     * @param collectionName 集合
     * @return 数据
     */
    <T> T addDocument(T objectToSave, String collectionName);

    /**
     * 添加文档
     *
     * @param objectToSave   数据
     * @param collectionName 集合
     * @return 数据
     */
    <T> Collection<T> addDocument(Collection<T> objectToSave, String collectionName);

    /**
     * 删除文档
     *
     * @param objectToSave   数据
     * @param collectionName 集合
     * @return 数据
     */
    <T> long removeDocument(T objectToSave, String collectionName);

    /**
     * 删除文档
     *
     * @param action         动作
     * @param collectionName 集合
     * @return 数据
     */
    long removeDocumentByAction(Action action, String collectionName);
}
