package com.chua.unified.properties;


import java.util.Properties;

/**
 * netx查询配置项
 * @author CH
 * @version 1.0.0
 * @className ActionProperties
 * @since 2020/8/5 12:02
 */
public class QueryProperties extends Properties {

    private String query;
    private int start = 0;
    private int row = 30;

    public void setQuery(String query) {
        this.query = query;
        this.put("query", query);
    }

    public void setStart(int start) {
        this.start = start;
        this.put("start", start);
    }

    public void setRow(int row) {
        this.row = row;
        this.put("row", row);
    }
}

