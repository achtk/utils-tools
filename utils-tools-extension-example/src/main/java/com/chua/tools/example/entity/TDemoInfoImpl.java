package com.chua.tools.example.entity;

import com.chua.utils.tools.text.IdHelper;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.Date;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/10/28
 */
@Data
@Path("users")
@Accessors(chain = true)
public class TDemoInfoImpl implements TDemoInfo {
    public Integer id;
    public Date time;
    public String name;
    public String title;
    public String demoTitle;
    public String test;
    private String uuid = IdHelper.createUuid();
    @Override
    @POST
    @Path("register")
    @Consumes({MediaType.APPLICATION_JSON})
    public String getUuid() {
        return IdHelper.createUuid();
    }
}