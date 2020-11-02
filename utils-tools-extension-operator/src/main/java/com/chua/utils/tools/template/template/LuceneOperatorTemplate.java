package com.chua.utils.tools.template.template;

import java.util.List;
import java.util.Map;

/**
 * lucene操作模板
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/2
 */
public interface LuceneOperatorTemplate<T, Search> {
    /**
     * 创建表
     *
     * @param name 表名
     * @throws Exception Exception
     */
    void createTable(String name) throws Exception;

    /**
     * 表是否存在
     *
     * @param name 表名
     * @return boolean
     */
    boolean isExist(String name);

    /**
     * 添加文档
     *
     * @param name 表名
     * @param doc  文档
     * @throws Exception Exception
     */
    void updateDocument(String name, T doc) throws Exception;

    /**
     * 添加文档
     *
     * @param name 表名
     * @param doc  文档
     * @throws Exception Exception
     */
    void addDocument(String name, T doc) throws Exception;

    /**
     * 删除文档
     *
     * @param name       表名
     * @param expression 表达式
     * @return 成功个数
     * @throws Exception Exception
     */
    long deleteDocument(String name, String expression) throws Exception;

    /**
     * 添加文档
     *
     * @param name 表名
     * @param docs 文档
     * @throws Exception Exception
     */
    void addDocuments(String name, List<T> docs) throws Exception;

    /**
     * 查询数据
     *
     * @param name   表名
     * @param search 查询条件
     * @return List
     * @throws Exception Exception
     */
    List<Map<String, Object>> queryForList(String name, Search search) throws Exception;

    /**
     * 关键词查询数据
     *
     * @param name   表名
     * @param search 查询条件
     * @return List
     * @throws Exception Exception
     */
    List<Map<String, Object>> keyword(String name, Search search) throws Exception;

    /**
     * 查询数据
     *
     * @param name   表名
     * @param search 查询条件
     * @param tClass 类型
     * @return List
     * @throws Exception Exception
     */
    <Entity> List<Entity> queryForList(String name, Search search, Class<Entity> tClass) throws Exception;
}
