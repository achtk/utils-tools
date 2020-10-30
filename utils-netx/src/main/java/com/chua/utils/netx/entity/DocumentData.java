package com.chua.utils.netx.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 文档结果
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public class DocumentData<DTO> {

    private final Date startTime = new Date();

    /**
     * 匹配数量
     */
    @Getter
    @Setter
    private long hit;
    /**
     * 数据
     */
    @Getter
    @Setter
    private List<DTO> data;
    /**
     * 耗时
     */
    private long timestamp;

    public void accomplish() {
        this.timestamp = System.currentTimeMillis() - startTime.getTime();
    }

    /**
     * 是否有数据
     * @return boolean
     */
    public boolean isEmpty() {
        return hit == 0;
    }
}
