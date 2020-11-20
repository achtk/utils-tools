package com.chua.utils.netx.datasource.info;

import com.chua.utils.netx.datasource.dialect.SQLDialectEnum;
import com.google.common.base.CaseFormat;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

/**
 * 表信息
 *
 * @author CH
 * @version 1.0
 * @since 2020/10/19 20:39
 */
@Data
@Slf4j
@EqualsAndHashCode
public class TableInfo<T> {
    private static final String ALL = "*:*";
    private static final String MATCH_ALL = "1 = 1";
    private static final String ANY = "*";
    private static final String ONE = "?";
    /**
     * 表名
     */
    private String tableName;
    /**
     * 字段
     */
    private Set<String> columns = new LinkedHashSet<>();
    /**
     * 属性
     */
    private Map<String, Field> attributes = new LinkedHashMap<>();

    private static final String INSERT_INFO = "INSERT INTO ";

    private static final Pattern PATTERN = Pattern.compile("(and|or){1}");
    private Class<?> aClass;
    private T obj;

    public static ConcurrentMap<String, String> javaProperty2SqlColumnMap = new ConcurrentHashMap<>();

    static {
        javaProperty2SqlColumnMap.put("Integer", "INTEGER");
        javaProperty2SqlColumnMap.put("Short", "tinyint");
        javaProperty2SqlColumnMap.put("Long", "bigint");
        javaProperty2SqlColumnMap.put("BigDecimal", "decimal(19,2)");
        javaProperty2SqlColumnMap.put("Double", "double precision not null");
        javaProperty2SqlColumnMap.put("Float", "float");
        javaProperty2SqlColumnMap.put("Boolean", "bit");
        javaProperty2SqlColumnMap.put("Timestamp", "datetime");
        javaProperty2SqlColumnMap.put("String", "VARCHAR(255)");
    }

    /**
     * 解析表
     *
     * @param aClass 类
     * @return 表信息
     */
    public TableInfo<?> objectToTable(final Class<?> aClass) {
        if (null == aClass) {
            throw new NullPointerException();
        }
        this.aClass = aClass;
        String name = aClass.getSimpleName();
        this.tableName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name).toUpperCase();
        this.toColumns(aClass);
        this.createTable();
        return this;
    }

    /**
     * 创建表
     */
    private void createTable() {
    }

    /**
     * 获取所有字段
     *
     * @param aClass
     * @return
     */
    private void toColumns(Class<?> aClass) {
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            columns.add(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_UNDERSCORE, field.getName()));
            attributes.put(field.getName(), field);
        }
    }

    /**
     * 批处理语句
     *
     * @return
     */
    public String prepareInsertBatch() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(INSERT_INFO).append(tableName);
        stringBuffer.append(" VALUES(");
        stringBuffer.append(Strings.repeat(",?", columns.size()).substring(1));
        stringBuffer.append(")");

        return stringBuffer.toString();
    }

    /**
     * 添加数据
     *
     * @param datum
     */
    public void addBatch(T datum, final BiConsumer<Integer, Object> biConsumer) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        for (Map.Entry<String, Field> entry : attributes.entrySet()) {
            try {
                Field field = entry.getValue();
                field.setAccessible(true);
                biConsumer.accept(atomicInteger.incrementAndGet(), field.get(datum));
            } catch (IllegalAccessException e) {
                biConsumer.accept(atomicInteger.incrementAndGet(), null);
            }
        }
    }

    /**
     * 解析ddl语句
     *
     * @param ddl
     * @return
     */
    public String parser(String ddl) {
        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM ");
        stringBuilder.append(tableName);
        stringBuilder.append(" WHERE 1 = 1 ");
        String newWhereSql = ddl;
        List<String> strings = Splitter.on(PATTERN).trimResults().omitEmptyStrings().splitToList(ddl);
        for (String string : strings) {
            String whereSql = transToSql(string);
            if (null == whereSql) {
                newWhereSql = newWhereSql.replace(string, MATCH_ALL);
                continue;
            }
            newWhereSql = newWhereSql.replace(string, whereSql);
        }

        if (!Strings.isNullOrEmpty(newWhereSql)) {
            stringBuilder.append(" AND ").append(newWhereSql);
        }
        return stringBuilder.toString();
    }

    /**
     * 转为sql
     *
     * @param string
     * @return
     */
    private String transToSql(String string) {
        int size = 2;
        List<String> strings = Splitter.on(":").trimResults().omitEmptyStrings().splitToList(string);
        if (null == strings || strings.size() != size) {
            return "";
        }
        String column = strings.get(0);
        String value = strings.get(1);
        if (ANY.equals(value.trim())) {
            return null;
        }
        column = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, column).toUpperCase();
        if (value.indexOf(ANY) == -1 && value.indexOf(ONE) == -1) {
            return column + "=" + value;
        }
        return column + " LIKE '" + (value.replace(ANY, "%").replace(ONE, "_")) + "'";

    }

    /**
     * 获取实体类
     *
     * @return
     */
    public Class<?> getObjClass() {
        return aClass;
    }

    /**
     * 创建表
     *
     * @param sqlDialectEnum
     * @return
     */
    public String initialConfig(SQLDialectEnum sqlDialectEnum) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE ");
        if (sqlDialectEnum == SQLDialectEnum.H2_MEMORY || sqlDialectEnum == SQLDialectEnum.H2_EMBEDDED) {
            sb.append(" MEMORY ");
        }
        sb.append(" TABLE ");
        sb.append(tableName).append(" ( \r\n");

        for (Map.Entry<String, Field> entry : attributes.entrySet()) {
            if ("serialVersionUID".equals(entry.getKey())) {
                continue;
            }
            //一般第一个是主键
            sb.append(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entry.getKey()).toUpperCase());
            sb.append(" ").append(javaProperty2SqlColumnMap.get(entry.getValue().getType().getSimpleName())).append(" ");
            sb.append(",");
        }
        String sql = null;
        sql = sb.toString();
        //去掉最后一个逗号
        int lastIndex = sql.lastIndexOf(",");
        sql = sql.substring(0, lastIndex) + sql.substring(lastIndex + 1);

        sql = sql.substring(0, sql.length() - 1) + " )";
        if (log.isDebugEnabled()) {
            log.debug("{} -> {}", aClass.getName(), sql);
        }
        return sql.replace("\r\n", "");
    }

    /**
     * 预处理
     *
     * @return
     */
    public String prepare() {
        return "DROP TABLE " + tableName;
    }
}
