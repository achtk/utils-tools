package com.chua.utils.tools.example.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/10/28
 */
@Data
@Entity
@Accessors(chain = true, fluent = true)
@Table(name = "t_demo_info")
public class TDemoInfo {
    @Id
    @GeneratedValue
    public String id;

    public Date time;

    @Column
    public String name;
    @Column
    public String title;
    @Column
    public String demoTitle;
    @Column
    public String test;

    private String uuid = "1";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDemoTitle() {
        return demoTitle;
    }

    public void setDemoTitle(String demoTitle) {
        this.demoTitle = demoTitle;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}