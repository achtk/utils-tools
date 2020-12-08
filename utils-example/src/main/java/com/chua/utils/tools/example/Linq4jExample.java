package com.chua.utils.tools.example;

import com.chua.utils.tools.example.entity.TDemoInfo;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.calcite.linq4j.tree.Expression;
import org.apache.calcite.linq4j.tree.ExpressionType;
import org.apache.calcite.linq4j.tree.LambdaExpression;

import java.util.List;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/8
 */
public class Linq4jExample {

    public static void main(String[] args) {
        List<TDemoInfo> tDemoInfos1 = DataFactoryExample.createTDemoInfos();
        List<TDemoInfo> tDemoInfos = Linq4j.asEnumerable(tDemoInfos1).where(item -> {
            return item.getId() < 10;
        }).toList();

        Expression expression = new LambdaExpression(ExpressionType.And, TDemoInfo.class);
        System.out.println();
    }
}
