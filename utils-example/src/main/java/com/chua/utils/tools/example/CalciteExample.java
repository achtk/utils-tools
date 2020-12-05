package com.chua.utils.tools.example;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/4
 */
public class CalciteExample {


    public static void main(String[] args) throws Exception {
        String url = Resources.toString(Resources.getResource("calcite.json"), StandardCharsets.UTF_8);
        Connection connection = DriverManager.getConnection("jdbc:calcite:model=inline:" + url);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from SALES.SALES where ID = 'c'");
        System.out.println(JSON.toJSONString(getData(resultSet)));
    }

    public static List<Map<String,Object>> getData(ResultSet resultSet)throws Exception{
        List<Map<String,Object>> list = Lists.newArrayList();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnSize = metaData.getColumnCount();

        while (resultSet.next()) {
            Map<String, Object> map = Maps.newLinkedHashMap();
            for (int i = 1; i < columnSize + 1; i++) {
                map.put(metaData.getColumnLabel(i), resultSet.getObject(i));
            }
            list.add(map);
        }
        return list;
    }
}
