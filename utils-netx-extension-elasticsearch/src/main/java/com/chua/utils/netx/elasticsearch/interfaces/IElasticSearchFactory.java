package com.chua.utils.netx.elasticsearch.interfaces;

import com.chua.utils.netx.elasticsearch.action.CallBack;
import com.chua.utils.netx.elasticsearch.condition.Condition;
import com.chua.utils.netx.elasticsearch.config.IndexConfig;
import com.chua.utils.netx.elasticsearch.dsl.DSL;
import com.chua.utils.netx.factory.INetxFactory;
import com.chua.utils.tools.common.JsonHelper;
import com.google.common.collect.HashMultimap;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

import java.io.IOException;
import java.util.*;

/**
 * es查询接口
 * @author CH
 * @since 1.0
 */
public interface IElasticSearchFactory extends INetxFactory {
    /**
     * 连接服务器
     *
     * @param properties 配置
     */
    public void connect(Properties properties);

    /**
     * 创建索引
     *
     * @param indexConfig 配置
     * @return
     */
    public boolean createIndex(IndexConfig indexConfig);

    /**
     * 创建索引,配置包括索引信息
     * <ul>
     *     <li>number_of_shards</li>
     *     <li>number_of_replicas</li>
     * </ul>
     *
     * @param index    索引
     * @param settings 特定的设置
     * @return
     */
    public boolean createIndex(String index, Map<String, Object> settings);

    /**
     * 创建索引
     *
     * @param index 索引
     * @return
     */
    default public boolean createIndex(String index) {
        return createIndex(index, null);
    }

    /**
     * 创建索引
     *
     * @param indexConfig 配置
     * @return
     */
    public boolean deleteIndex(IndexConfig indexConfig);

    /**
     * 添加文档
     *
     * @param indexConfig 配置
     * @return
     */
    public boolean addDocument(IndexConfig indexConfig);

    /**
     * 添加文档
     *
     * @param indexConfig 配置
     * @return
     */
    public boolean deleteDocument(IndexConfig indexConfig);

    /**
     * 删除文档
     *
     * @param index 索引
     * @param id    唯一标识
     * @return
     */
    default public boolean deleteDocument(final String id, final String index) {
        IndexConfig indexConfig = new IndexConfig();
        indexConfig.setIndex(index).setId(id);
        return deleteDocument(indexConfig);
    }

    /**
     * 添加文档
     *
     * @param index 索引
     * @param data  数据
     * @param id    唯一标识
     * @return
     */
    default public boolean addDocument(final String index, final String id, final String data) {
        return addDocument(index, "doc", id, data);
    }

    /**
     * 添加文档
     *
     * @param index 索引
     * @param type  类型
     * @param data  数据
     * @param id    唯一标识
     * @return
     */
    default public boolean addDocument(final String index, final String type, final String id, final String data) {
        IndexConfig indexConfig = new IndexConfig();
        indexConfig.setIndex(index).setType(type).setDoc(data).setId(id);
        return addDocument(indexConfig);
    }

    /**
     * 获取文档
     *
     * @param indexConfig 配置
     * @return
     */
    public String getDocument(IndexConfig indexConfig);

    /**
     * 更新文档
     *
     * @param indexConfig 配置
     * @return
     */
    public boolean updateDocument(IndexConfig indexConfig);

    /**
     * 批量添加文档
     *
     * @param indexConfig 配置
     * @return 异常数据
     */
    public List<IndexConfig> bulkAddDocument(List<IndexConfig> indexConfig) throws IOException;

    /**
     * 批量添加文档
     *
     * @param index 索引
     * @param type 类型
     * @param data 数据
     * @throws IOException
     */
    default public void bulkAddDocument(String index, String type, List<Map<String, Object>> data) throws IOException {
        List<IndexConfig> indexConfigList = new ArrayList<>(data.size());
        for (Map<String, Object> map : data) {
            IndexConfig indexConfig1 = new IndexConfig();
            Object id = map.get("id");
            if (null != id) {
                indexConfig1.setId(id.toString());
            }
            indexConfig1.setIndex(index);
            indexConfig1.setDoc(JsonHelper.toJson(map));
            indexConfigList.add(indexConfig1);
        }
        bulkAddDocument(indexConfigList);
    }

    /**
     * 批量更新文档
     *
     * @param indexConfig 配置
     * @return 异常数据
     */
    public List<?> bulkUpdateDocument(List<IndexConfig> indexConfig) throws IOException;

    /**
     * 批量删除文档
     *
     * @param indexConfig  配置
     * @return 异常数据
     */
    public List<?> bulkDeleteDocument(List<IndexConfig> indexConfig) throws IOException;

    /**
     * 批量添加文档
     *
     * @param index 索引
     * @param id 唯一标识
     * @throws IOException
     */
    default public void bulkDeleteDocument(String index, String... id) throws IOException {
        List<IndexConfig> indexConfigList = new ArrayList<>(id.length);
        for (String s : id) {
            IndexConfig indexConfig1 = new IndexConfig();
            indexConfig1.setId(s);
            indexConfig1.setIndex(index);
            indexConfigList.add(indexConfig1);
        }
        bulkAddDocument(indexConfigList);
    }

    /**
     * 查询
     *
     * @param condition 查询条件
     * @return
     */
    public List<Map<String, Object>> queryAllByConditions(Condition condition, CallBack... callbacks) throws Exception;

    /**
     * 查询
     *
     * @param search 查询
     * @return
     */
    public List<Map<String, Object>> query(Search search, CallBack... callbacks) throws Exception;

    /**
     * 查询
     *
     * @param dsl   语句
     * @param index 索引
     * @return
     */
    default public List<Map<String, Object>> queryByDsl(String dsl, String index, CallBack... callbacks) throws Exception {
        Search search = new Search.Builder(dsl).addIndex(index).addType("doc").build();
        return query(search, callbacks);
    }

    /**
     * 通过ID查询
     *
     * @param indexConfig 配置
     * @return
     */
    public String getDocumentById(IndexConfig indexConfig) throws Exception;

    /**
     * 通过ID查询
     *
     * @param indexConfig  配置
     * @return
     */
    public <T> T getDocumentById(IndexConfig indexConfig, Class<T> entity) throws Exception;

    /**
     * 检索索引是否存在
     *
     * @param indexs 索引
     * @return
     */
    public boolean checkIndexExists(String... indexs);

    /**
     * 关闭索引
     *
     * @param indexConfig 配置
     * @exception IOException
     */
    public void closeIndex(IndexConfig indexConfig) throws IOException;

    /**设置索引
     * @param indexConfig 配置
     */
    public void flushIndex(IndexConfig indexConfig) throws IOException;

    /**
     * 添加映射
     * @param indexConfig 配置
     */
    public void setFieldsMapping(IndexConfig indexConfig);

    /**
     * 打开索引
     * @param indexConfig 配置
     * @throws IOException
     */
    public void openIndex(IndexConfig indexConfig) throws IOException;

    /**
     * 配置索引
     * @param indexConfig 配置
     * @throws IOException
     */
    public void optimizeIndex(IndexConfig indexConfig) throws IOException;

    /**
     * @throws IOException
     */
    public void nodesInfo() throws IOException;

    /**
     * @throws IOException
     */
    public void health() throws IOException;

    /**
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @return
     * @throws IOException
     */
    public String getMapping(String indexName, String indexType) throws IOException;

    /**
     * 设置映射
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @param source    索引数据
     * @return
     * @throws IOException
     */
    public String putMapping(String indexName, String indexType, String source) throws IOException;

    /**
     * 格式化数据
     *
     * @param searchResult 查询结果
     * @return
     */
    default public List<Map<String, Object>> dealSearchResponse(SearchResult searchResult) {
        List<Map<String, Object>> result = new ArrayList<>();
        Long total = searchResult.getTotal();

        List<SearchResult.Hit<HashMap, Void>> hits = searchResult.getHits(HashMap.class);
        if (total > 0L) {
            for (SearchResult.Hit<HashMap, Void> hit : hits) {
                Map<String, List<String>> highlight = hit.highlight;
                HashMap source = hit.source;
                result.add(source);
            }
        }
        return result;
    }

    /**
     * dsl语句例子
     *
     * @return
     */
    default public HashMultimap<String, DSL> dsl() {
        HashMultimap<String, DSL> result = HashMultimap.create();
        DSL match1 = new DSL();
        match1.setUri("POST   http://192.168.1.181:9200/shop/_doc/_search");
        match1.setQuery("{\n" +
                "    \"query\":{\n" +
                "        \"match\":{\n" +
                "            \"desc\":\"我真帅\"\n" +
                "        }\n" +
                "    }\n" +
                "}");
        result.put("match", match1);
        DSL match2 = new DSL();
        match1.setUri(" GET  http://192.168.1.181:9200/shop/_doc/_search?q=desc:我真帅");
        match1.setQuery("{\n" +
                "    \"query\":{\n" +
                "        \"match\":{\n" +
                "            \"desc\":\"我真帅\"\n" +
                "        }\n" +
                "    }\n" +
                "}");
        result.put("match", match1);
        return result;
    }
}
