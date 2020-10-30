package com.chua.utils.netx.elasticsearch.condition;

import com.chua.utils.netx.elasticsearch.type.SearchType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 条件
 * @author CH
 */
@Getter
@Setter
public class Condition {
    /**
     * 索引
     */
    private String index;
    /**
     * 类型
     */
    private String type = "doc";
    /**
     * 查询类型
     */
    private SearchType searchType;
    /**
     * 是否查询总数
     */
    private boolean needTotal;
    /**
     * 高亮字段
     */
    private String highlight;
    /**
     * 模板
     */
    private String template;
    /**
     * 级别，可以为空
     */
    private String level;
    /**
     * 信息关键字可以为空
     */
    private String message;
    /**
     * 起始时间，可以为空
     */
    private long startTime;
    /**
     * 结束时间，可以为空
     */
    private long endTime;
    /**
     * 查询时间字段
     */
    private String timeField;
    /**
     * 开始页码
     */
    private int start = 1;
    /**
     * 返回字段
     */
    private List<String> fields;
    /**
     * 查询字段以及值
     */
    private Map<String, Object> fieldsAndValue;
    /**
     * 过滤字段以及值
     */
    @Deprecated
    private Map<String, Object> fieldsAndValueFilter;

    /**
     * 是否同步
     */
    private boolean isAsync;
    /**
     * 超时时间
     */
    private long timeout = 30L;
    /**
     * 滚动时间
     */
    private long scrollTime = 60L;
    /**
     * 返回记录数，可以为空，默认最大返回10条。该值必须小于10000，如果超过10000请使用
     */
    private int size = 10;
    /**
     * 排序字段
     */
    private String sortFields;
    /**
     * 排序
     */
    private Object sortOrder;

}