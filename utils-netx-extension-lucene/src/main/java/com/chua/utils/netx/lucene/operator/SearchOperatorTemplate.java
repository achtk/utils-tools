package com.chua.utils.netx.lucene.operator;

import com.chua.utils.netx.lucene.entity.HitData;


/**
 * 查询模板
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/3
 */
public interface SearchOperatorTemplate {
    /**
     * 查询数据
     *
     * @param keyword  关键词
     * @param sort     排序字段
     * @param columns  返回字段
     * @param start    开始位置(默认: 0)
     * @param pageSize 每页条数
     * @return HitData
     */
    HitData search(String keyword, String sort, String columns, int start, int pageSize);
    /**
     * 查询数据
     *
     * @param keyword  关键词
     * @param sort     排序字段
     * @param columns  返回字段
     * @param start    开始位置(默认: 0)
     * @param pageSize 每页条数
     * @return HitData
     */
    HitData quickSearch(String keyword, String sort, String columns, int start, int pageSize);
}
