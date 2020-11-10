package com.chua.utils.netx.elasticsearch.search;

import com.chua.utils.netx.elasticsearch.condition.Condition;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.NumberHelper;
import com.chua.utils.tools.common.StringHelper;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_ASTERISK;

/**
 * @author CHTK
 */
public class ElasticSearchHelper {

    private static final String TIMESTAMP = "timestamp";
    private static final String[] SIGNS = new String[] { "[", "]", "(", ")" };

    private static final long DAY = 60 * 60 * 24;
    /**
     * 获取查询条件
     * @param condition
     * @return
     */
    public static Map<String, Object> search(Condition condition) {
        Map<String, Object> conditions = Maps.newHashMap();

       // MapHelper.combine(conditions, generateQueryByFields(condition));
     //   MapHelper.combine(conditions, generateFieldSort(condition));
     //   MapHelper.combine(conditions, formAndSize(condition));
     //   MapHelper.combine(conditions, _source(condition));

        return conditions;
    }

    /**
     * _source
     * @param condition
     * @return
     */
    private static Map<String, Object> _source(Condition condition) {
        if(BooleanHelper.hasLength(condition.getFields())) {
            Map<String, Object> formAndSize = Maps.newHashMap();
            formAndSize.put("_source", condition.getFields());
            return formAndSize;
        }
        return null;
    }

    /**
     * 分页
     * @param condition
     * @return
     */
    private static Map<String, Object> formAndSize(Condition condition) {
        Map<String, Object> formAndSize = Maps.newHashMap();
        int size = condition.getSize();
        if(Objects.nonNull(size)) {
            if(size > 10000) {
                size = 10000;
            }
        }
        formAndSize.put("size", size);
        formAndSize.put("from", condition.getStart() - 1);
        return formAndSize;
    }

    /**
     * 字段排序
     *
     * @param condition
     * @return
     */
    public static Map<String, Object> generateFieldSort(Condition condition) {
        if (StringHelper.isNotBlank(condition.getSortFields())) {
            Map<String, Object> sort = Maps.newHashMap();
            Map<String, Object> field = Maps.newHashMap();
            Map<String, Object> order = Maps.newHashMap();
            order.put("order", null == condition.getSortOrder() ? "desc": condition.getSortOrder());
            field.put(condition.getSortFields(), order);
            sort.put("sort", field);
            return sort;
        } else {
            return null;
        }
    }
    /**
     * 获取查询条件
     * @param condition
     * @return
     */
    public static Map<String, Object> generateQueryByFields(Condition condition) {
        List<String> fields = condition.getFields();
        Map<String, Object> fieldsAndValue = condition.getFieldsAndValue();
        if (null == fieldsAndValue) {
            return null;
        }

        Map<String, Object> query = Maps.newHashMap();
        Map<String, Object> bool = Maps.newHashMap();
        Map<String, Object> boolQuery = Maps.newHashMap();
        String key, valueString = null;
        Object value;
        for (Map.Entry<String, Object> entry : fieldsAndValue.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if (Objects.nonNull(value)) {
                valueString = value.toString();
            }
            if (StringHelper.firstAndEndContains(valueString, SIGNS)) {
                Map<String, Object> generateQueryMatchByRange = generateQueryMatchByRange(key, valueString);
                if(null != generateQueryMatchByRange) {
                    boolQuery.putAll(generateQueryMatchByRange);
                }
            } else if (valueString.contains("*")) {
                Map<String, Object> generateQueryWildByFields = generateQueryWildByFields(key, valueString);
                if(null != generateQueryWildByFields) {
                    Object should = boolQuery.get("should");
                    if(null != should) {
                        Map<String, Object> shoulds = (Map<String, Object>) should;
                        generateQueryWildByFields.putAll(shoulds);
                        boolQuery.put("should", generateQueryWildByFields);
                    } else {
                        boolQuery.put("should", generateQueryWildByFields);
                    }
                }
            } else {
                Map<String, Object> generateQueryMatchByFields = generateQueryMatchByFields(key, value);
                if(null != generateQueryMatchByFields) {
                    Object must = boolQuery.get("must");
                    if(null != must) {
                        Map<String, Object> shoulds = (Map<String, Object>) must;
                        generateQueryMatchByFields.putAll(shoulds);
                        boolQuery.put("must", generateQueryMatchByFields);
                    } else {
                        boolQuery.put("must", generateQueryMatchByFields);
                    }
                }
            }
        }
        bool.put("bool", boolQuery);
        query.put("query", bool);
        return query;
    }

    /**
     * 模糊匹配
     *
     * @return
     */
    public static Map<String, Object> generateQueryWildByFields(String key, String value) {
        return generateQueryIKByFields(key,value);
    }

    /**
     * 分词模糊匹配
     *
     * @return
     */
    public static Map<String, Object> generateQueryIKByFields(String key, String value) {
        if (!StringHelper.isNullOrEmpty(key, value) && value.contains(SYMBOL_ASTERISK)) {
            Map<String, Object> wildCard = Maps.newHashMap();
            Map<String, Object> keyValue = Maps.newHashMap();
            Map<String, Object> valueConfig = Maps.newHashMap();
            valueConfig.put("value", value);
            valueConfig.put("boost", "1.0");
            valueConfig.put("rewrite", "constant_score");
            keyValue.put(key, valueConfig);
            wildCard.put("wildcard", keyValue);
            return wildCard;
        }
        return null;
    }

    /**
     * 模糊匹配
     *
     * @return
     */
    public static Map<String, Object> generateQueryRegexpByFields(String key, String value) {
        if (!StringHelper.isNullOrEmpty(key, value) && StringHelper.firstOrEndContains(value, "*")) {
            Map<String, Object> regexp = Maps.newHashMap();
            Map<String, Object> keyValue = Maps.newHashMap();
            Map<String, Object> valueConfig = Maps.newHashMap();
            valueConfig.put("value", value);
            valueConfig.put("flags", "ALL");
            valueConfig.put("max_determinized_states", "10000");
            valueConfig.put("rewrite", "constant_score");
            keyValue.put(key, valueConfig);
            regexp.put("regexp", keyValue);
            return regexp;
        }
        return null;
    }

    /**
     * 字段匹配
     *
     * @return
     */
    public static Map<String, Object> generateQueryMatchByFields(String key, Object value) {
        if (!StringHelper.isNullOrEmpty(key) && null != value && !"".equals(value.toString())) {
            Map<String, Object> matchPhrase = Maps.newHashMap();
            Map<String, Object> keyValue = Maps.newHashMap();
            keyValue.put(key, value);
            matchPhrase.put("match_phrase", keyValue);
            return matchPhrase;
        }
        return null;
    }

    /**
     * 区间匹配
     *
     * @return
     */
    public static Map<String, Object> generateQueryMatchByRange(String key, String value) {
        if (!StringHelper.isNullOrEmpty(key, value) && StringHelper.firstAndEndContains(value, SIGNS)) {
            String newValue = value.substring(1, value.length() - 1);
            if (StringHelper.isBlank(newValue)) {
                return null;
            }
            String[] split = newValue.split(",");
            if (split.length == 0) {
                return null;
            }
            String first = value.substring(0, 1);
            String end = value.substring(value.length() - 1);
            String firstValue = split[0];
            String endValue = split[1];

            firstValue = firstValue.trim();
            endValue = endValue.trim();

            if (NumberHelper.isNumber(firstValue) && NumberHelper.isNumber(endValue)) {
                long start = Long.valueOf(firstValue);
                long ends = Long.valueOf(endValue);
                if(firstValue.length() == 10 && endValue.length() == 0) {
                    return generateQueryByTime(key, start, ends);
                } else {
                    Map<String, Object> filter = Maps.newHashMap();
                    Map<String, Object> rangeFilter = Maps.newHashMap();
                    Map<String, Object> range = Maps.newHashMap();
                    Map<String, Object> rangeField = Maps.newHashMap();
                    if ("[".equals(first)) {
                        rangeField.put("gte", start);
                    } else if ("(".equals(first)) {
                        rangeField.put("gt", start);
                    }
                    if ("]".equals(end)) {
                        rangeField.put("lte", ends);
                    } else if (")".equals(first)) {
                        rangeField.put("lt", ends);
                    }

                    range.put(key, rangeField);
                    rangeFilter.put("range", range);
                    filter.put("filter", rangeFilter);
                    return filter;
                }

            }

        }
        return null;
    }

    /**
     * 获取elastic语句
     *
     * @return
     */
    public static Map<String, Object> generateQueryByTime(String timeField, long startTime, long endTime) {
        if (StringHelper.isBlank(timeField)) {
            return null;
        }

        if (endTime <= startTime || endTime <= 0L || endTime > System.currentTimeMillis() / 1000) {
            endTime = System.currentTimeMillis() / 1000;
        }
        if (startTime < 0L || startTime >= endTime) {
            startTime = endTime - DAY;
        }

        Map<String, Object> filter = Maps.newHashMap();
        Map<String, Object> range = Maps.newHashMap();
        Map<String, Object> rangeField = Maps.newHashMap();
        rangeField.put("gte", startTime);
        rangeField.put("lte", endTime);
        range.put(timeField, rangeField);
        filter.put("filter", range);
        return filter;
    }

}
