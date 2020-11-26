package com.chua.utils.tools.example.entity;

import com.chua.utils.tools.text.IdHelper;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/10/28
 */
@Data
@Entity
@Table(name = "t_demo_info")
public class TDemoInfo {
    @Id
    @GeneratedValue
    private Integer id;

    private Date time;

    @Column
    private String name;
    @Column
    private String title;
    @Column
    private String demoTitle;
    @Column
    private String test;

    private String uuid = IdHelper.createUuid();

    public String getUuid() {
        return IdHelper.createUuid();
    }
}