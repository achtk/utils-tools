package com.chua.utils.tools.data.factory;

import com.chua.utils.tools.bean.copy.BeanCopy;
import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.collects.HashOperateMap;
import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.data.datasource.DataDataSource;
import com.chua.utils.tools.data.parser.BcpFileDataParser;
import com.chua.utils.tools.data.parser.CsvFileDataParser;
import com.chua.utils.tools.data.parser.DataParser;
import com.chua.utils.tools.data.parser.MemFileDataParser;
import com.chua.utils.tools.data.table.DataTable;
import com.chua.utils.tools.data.table.type.TableType;
import com.chua.utils.tools.spi.entity.ExtensionClass;
import com.chua.utils.tools.spi.extension.ExtensionLoader;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import com.chua.utils.tools.text.IdHelper;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * 数据工厂
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/8
 */
@Slf4j
public class StandardDataFactory implements DataFactory {
    private static final String CALCITE_URL = "jdbc:calcite:";
    private static final String DATA_SOURCE_FACTORY = "org.apache.calcite.adapter.jdbc.JdbcSchema$Factory";
    private static final String SOLR_FACTORY = "org.apache.calcite.adapter.solr.SolrTableFactory";
    private static final String REDIS_FACTORY = "org.apache.calcite.adapter.redis.RedisSchemaFactory";
    private static final String KAFKA_FACTORY = "org.apache.calcite.adapter.kafka.KafkaTableFactory";
    private static final String MONGO_FACTORY = "org.apache.calcite.adapter.mongodb.MongoSchemaFactory";
    private static final String DRUID_FACTORY = "org.apache.calcite.adapter.druid.DruidSchemaFactory";
    private static final String COMMON_FACTORY = "com.chua.utils.netx.data.schema.CalciteDataSchemaFactory";

    private final Multimap<String, DataTable> schemaDataTable = HashMultimap.create();
    private final static Map<String, DataTable> ID_DATA_TABLE = new ConcurrentHashMap();
    public final static Map<TableType, String> FACTORY_CACHE = new ConcurrentHashMap<>();
    public final static Map<String, DataParser> PARSER_CACHE = new ConcurrentHashMap<>();
    private final static LongAdder LONG_ADDER = new LongAdder();

    static {
        FACTORY_CACHE.put(TableType.FILE, COMMON_FACTORY);
        FACTORY_CACHE.put(TableType.DATA_SOURCE, DATA_SOURCE_FACTORY);
        //@calcite-elasticsearch.json
        FACTORY_CACHE.put(TableType.SOLR, SOLR_FACTORY);
        //@see calcite-redis.json
        FACTORY_CACHE.put(TableType.REDIS, REDIS_FACTORY);
        //@see calcite-kafka.json
        FACTORY_CACHE.put(TableType.KAFKA, KAFKA_FACTORY);
        //@see calcite-kafka.json
        FACTORY_CACHE.put(TableType.MONGO, MONGO_FACTORY);
        //@see calcite-druid.json
        FACTORY_CACHE.put(TableType.DRUID, DRUID_FACTORY);
        FACTORY_CACHE.put(TableType.DATA_SOURCE, DATA_SOURCE_FACTORY);
        FACTORY_CACHE.put(TableType.MEM, COMMON_FACTORY);
        FACTORY_CACHE.put(TableType.IO, COMMON_FACTORY);

        PARSER_CACHE.put("csv", new CsvFileDataParser());
        PARSER_CACHE.put("bcp", new BcpFileDataParser());
        PARSER_CACHE.put(TableType.MEM.name().toLowerCase(), new MemFileDataParser());

        ExtensionLoader<DataParser> extensionLoader = ExtensionFactory.getExtensionLoader(DataParser.class);
        Multimap<String, ExtensionClass<DataParser>> allSpiService = extensionLoader.getAllExtensionClassess();

        allSpiService.keySet().stream().filter(item -> !item.equals(DataParser.class.getName())).forEach(name -> {
            PARSER_CACHE.put(name, extensionLoader.getExtension(name));
        });
    }

    private CalciteInfo calciteInfo;
    private Properties info;

    /**
     * 获取数据表
     *
     * @param id id
     * @return 数据表
     */
    public static DataTable getDataTable(String id) {
        return ID_DATA_TABLE.get(id);
    }

    @Override
    public void addSchema(String schema, DataTable dataTable) {
        schemaDataTable.put(schema, repairDataTable(dataTable));
    }

    /**
     * 补充参数
     *
     * @param dataTable 数据表
     * @return 数据表
     */
    private DataTable repairDataTable(DataTable dataTable) {
        dataTable.setId(IdHelper.createUuid());
        if (Strings.isNullOrEmpty(dataTable.getParser())) {
            DataParser parser = createParser(dataTable);
            dataTable.setParser(null == parser ? null : parser.getClass().getName());
        }
        ID_DATA_TABLE.put(dataTable.getId(), dataTable);
        return dataTable;
    }

    /**
     * 创建解析器
     *
     * @param dataTable 表类型
     * @return
     */
    private DataParser createParser(DataTable dataTable) {
        if (dataTable.getTableType() == TableType.FILE) {
            Object source = dataTable.getSource();
            String extension = FileHelper.getExtension(Objects.toString(source));
            return PARSER_CACHE.get(extension);
        }
        return PARSER_CACHE.get(dataTable.getTableType().name().toLowerCase());
    }

    @Override
    public Connection getConnection() throws SQLException {
        String url = getUrl();
        return DriverManager.getConnection(CALCITE_URL, info);
    }

    @Override
    public String schema() {
        return JsonHelper.toFormatJson(calciteInfo);
    }

    @Override
    public String getUrl() {
        List<Map<String, Object>> schemas = schemaDataTable.asMap().entrySet().parallelStream().map(entry -> {
            Collection<DataTable> entryValue = entry.getValue();
            Map<String, Object> result = new HashMap<>();
            Schema schema = new Schema();
            schema.setName(entry.getKey());
            //只判断第一个类型
            DataTable dataTable = FinderHelper.firstElement(entryValue);
            schema.setFactory(FACTORY_CACHE.get(dataTable.getTableType()));
            HashOperateMap operateMap = HashOperateMap.create("id", entryValue.stream().map(dataTable1 -> dataTable1.getId()).collect(Collectors.toList()));
            //创建参数
            this.createOperate(schema, operateMap, dataTable);
            schema.setOperand(operateMap);
            result.putAll(BeanCopy.of(schema).asMap());
            if(null != dataTable.getOperate2()) {
                result.putAll(dataTable.getOperate2());
            }
            return result;
        }).collect(Collectors.toList());

        this.calciteInfo = new CalciteInfo();
        calciteInfo.setSchemas(schemas);

        this.info = new Properties();
        info.put("model", "inline:" + JsonHelper.toFormatJson(calciteInfo));
        return CALCITE_URL + "model=inline:" + JsonHelper.toJson(calciteInfo);
    }

    /**
     * 补充数据源参数
     *
     * @param schema
     * @param operateMap 参数集合
     * @param dataTable  表
     * @return 集合
     */
    private void createOperate(Schema schema, HashOperateMap operateMap, DataTable dataTable) {
        HashOperateMap operate = dataTable.getOperate();
        if (dataTable.getTableType() == TableType.DATA_SOURCE) {
            if (null != dataTable.getOperate()) {
                operateMap.putAll(dataTable.getOperate());
            }
            Object source = dataTable.getSource();
            if (null != source && source instanceof DataSource) {
                DataDataSource dataDataSource = new DataDataSource((DataSource) source);
                operateMap.put("dataSource", dataDataSource.getDataSourceClass());
            }
        } else if (dataTable.getTableType() == TableType.MONGO) {
            if (operate.isValids("host", "database")) {
                log.error("调用mongo至少需要配置[host][database]");
                return;
            }
        } else if (dataTable.getTableType() == TableType.REDIS) {
            if (operate.isValids("host", "port", "table", "database")) {
                log.error("调用redis至少需要配置" +
                        "==>[host]\r\n" +
                        "==>[port]\r\n" +
                        "==>[table]\r\n" +
                        "==>[database]");
                return;
            }
        } else if (dataTable.getTableType() == TableType.SOLR) {
            if (operate.isValids("solrServerURL", "solrCollection", "columns", "columnMapping")) {
                log.error("调用solr至少需要配置" +
                        "==>[solrServerURL]\r\n" +
                        "==>[solrCollection]\r\n" +
                        "==>[columns]\r\n" +
                        "==>[columnMapping]");
                return;
            }
        }
        operateMap.putAll(operate);
    }

    /**
     * 文件解析器
     *
     * @param extension 类型
     * @return 解析器
     */
    public static DataParser getParser(String extension) {
        if (Strings.isNullOrEmpty(extension)) {
            log.info("could not create DataParser from extension, " +
                    "no matching Parser was found [" + extension + "]\n" +
                    "either use addDefaultParser(Parser urlType) " +
                    "with your specialized Parser.");
            return null;
        }
        if (PARSER_CACHE.containsKey(extension)) {
            return ClassHelper.forObject(PARSER_CACHE.get(extension).getClass());
        }
        return ClassHelper.forObject(PARSER_CACHE.get("text").getClass());
    }

    /**
     * 添加解析器
     *
     * @param extension      类型
     * @param dataFileParser 解析器
     */
    public static void addDefaultParser(String extension, DataParser dataFileParser) {
        if (Strings.isNullOrEmpty(extension) || PARSER_CACHE.containsKey(extension)) {
            log.error("Extension cannot be empty or the parser already exists.Either use putParser to set up special parsers!");
            return;
        }
        PARSER_CACHE.put(extension, dataFileParser);
    }

    /**
     * 添加解析器
     *
     * @param extension      类型
     * @param dataFileParser 解析器
     */
    public static void putParser(String extension, DataParser dataFileParser) {
        if (Strings.isNullOrEmpty(extension)) {
            log.error("Extension cannot be empty");
            return;
        }
        PARSER_CACHE.put(extension, dataFileParser);
    }

    /**
     * Calcite信息
     */
    @Getter
    @Setter
    class CalciteInfo {
        /**
         * 版本
         */
        private String version = IdHelper.createVersion(3, 20, LONG_ADDER.longValue());
        /**
         * 默认数据库
         */
        private String defaultSchema = "system";
        /**
         * 数据库
         */
        private List<Map<String, Object>> schemas = Lists.newArrayList();
    }

    /**
     * 数据库
     */
    @Getter
    @Setter
    class Schema  {
        /**
         * 数据库名称
         */
        private String name;
        /**
         * 创建类型
         */
        private String type = "custom";
        /**
         * 实现类
         */
        private String factory = "";
        /**
         * 额外参数
         */
        private HashOperateMap operand;
    }
}
