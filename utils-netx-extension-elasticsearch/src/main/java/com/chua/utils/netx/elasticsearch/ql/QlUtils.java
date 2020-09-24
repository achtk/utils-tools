package com.chua.utils.netx.elasticsearch.ql;

import com.chua.utils.netx.elasticsearch.condition.Condition;
import com.chua.utils.tools.common.StringHelper;
import com.google.common.base.Joiner;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QlUtils {

    private static final  Pattern compile = Pattern.compile("\\[(.*?),(.*?)\\]");
    /**
     *
     * <pre>
     *     condition#fieldsAndValue 查询条件
     *     <pre>
     *         e.q. name: 测试* 通配符匹配
     *         e.q. name: 测试? 通配符匹配
     *         e.q. name: 测试% 模糊匹配
     *         e.q. name: #测试 前缀匹配
     *         e.q. name: 测试 全词匹配
     *         e.q. name: [1, 2] 区间匹配
     *         e.q. name: 1,2,3 多词匹配
     *         e.q. name: 1|2|3 正则匹配
     *         e.q. *: 测试 查询匹配
     *     </pre>
     * </pre>
     * @param condition
     * @return
     */
    public static BoolQueryBuilder makeQl(Condition condition) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        Map<String, Object> fieldsAndValue = condition.getFieldsAndValue();
        String key = null, valueStr = null;
        Object value = null;
        for (Map.Entry<String, Object> entry : fieldsAndValue.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if(value instanceof String) {
                if(StringHelper.isNotBlank(key)) {
                    valueStr = (String) value;
                    if(valueStr.indexOf("[") > -1 && valueStr.indexOf("]") > -1) {
                        Matcher matcher = compile.matcher(valueStr);
                        if(matcher.find()) {
                            String group1 = matcher.group(1);
                            String group2 = matcher.group(2);
                            if(group1.indexOf("*") > -1 && group2.indexOf("*") == -1) {
                                boolQueryBuilder.must(QueryBuilders.rangeQuery(key).lte(group2));
                            } else  if(group1.indexOf("*") == -1 && group2.indexOf("*") > -1) {
                                boolQueryBuilder.must(QueryBuilders.rangeQuery(key).gte(group1));
                            } else {
                                boolQueryBuilder.must(QueryBuilders.rangeQuery(key)
                                        .from(group1)
                                        .to(group2)
                                        .includeLower(true)
                                        .includeUpper(false));
                            }
                        }
                    } else if(valueStr.indexOf("\\|") > -1) {
                        boolQueryBuilder.must(QueryBuilders.regexpQuery(key, valueStr));
                    } else if(valueStr.indexOf("%") > -1) {
                        String join = Joiner.on(" ").join(valueStr.split("%"));
                        boolQueryBuilder.must(QueryBuilders.fuzzyLikeThisQuery(key).likeText(join));
                    } else if(valueStr.indexOf(",") > -1) {
                        boolQueryBuilder.must(QueryBuilders.termQuery(key, valueStr));
                    }else if(valueStr.startsWith("#")) {
                        boolQueryBuilder.must(QueryBuilders.prefixQuery(key, valueStr));
                    } else if(valueStr.indexOf("*") > -1){
                        WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery(key, valueStr);
                        boolQueryBuilder.must(wildcardQueryBuilder);
                    } else {
                        boolQueryBuilder.must(QueryBuilders.matchQuery(key, valueStr));
                    }
                }   else {
                    boolQueryBuilder.must(QueryBuilders.queryStringQuery(valueStr));
                }
            } else if(value instanceof Integer){
                Integer valueInt = (Integer) value;
                if(StringHelper.isNotBlank(key)) {
                    boolQueryBuilder.must(QueryBuilders.queryStringQuery(String.valueOf(valueInt)));
                } else {
                    boolQueryBuilder.must(QueryBuilders.matchQuery(key, value));
                }
            }
        }

        return boolQueryBuilder;

    }
}
