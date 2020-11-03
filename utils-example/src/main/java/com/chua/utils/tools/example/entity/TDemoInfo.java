package com.chua.utils.tools.example.entity;

import lombok.Data;

import javax.persistence.*;

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

    @Column
    private String name;
    @Column
    private String title;
    @Column
    private String demoTitle;
    @Column
    private String test;
}