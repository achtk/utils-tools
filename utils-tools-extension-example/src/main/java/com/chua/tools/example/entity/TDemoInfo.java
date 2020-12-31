package com.chua.tools.example.entity;

import com.chua.utils.tools.text.IdHelper;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/10/28
 */
@Data
@Accessors(chain = true)
public class TDemoInfo {
    public Integer id;
    public Date time;
    public String name;
    public String title;
    public String demoTitle;
    public String test;
    private String uuid = IdHelper.createUuid();
    public Integer getId() {
        return id;
    }
}