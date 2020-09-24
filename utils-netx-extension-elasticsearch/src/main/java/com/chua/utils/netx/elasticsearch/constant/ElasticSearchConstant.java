package com.chua.utils.netx.elasticsearch.constant;

/**
 * 常量属性
 * @author CHTK
 */
public interface ElasticSearchConstant {


    public static final String ELASTIC_SEARCH = "elasticsearch";
    public static final String INDEX = "index";
    public static final String TYPE = "type";
    public static final String ID = "id";
    public static final String CREATE_INDEX = new StringBuffer()
            .append("<").append(ELASTIC_SEARCH).append(">/")
            .append("<").append(INDEX).append(">/")
            .append("<").append(TYPE).append(">/")
            .append("<").append(ID).append(">")
            .toString();

}
