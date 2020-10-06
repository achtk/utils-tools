package com.chua.utils.netx.elasticsearch.interfaces;

import com.chua.utils.tools.properties.NetxProperties;
import com.chua.utils.netx.elasticsearch.action.CallBack;
import com.chua.utils.netx.elasticsearch.condition.Condition;
import com.chua.utils.netx.elasticsearch.config.IndexConfig;
import com.chua.utils.netx.elasticsearch.configure.ElasticSearchConfigurator;
import com.chua.utils.netx.elasticsearch.ql.QlUtils;
import com.chua.utils.tools.common.ArraysHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.StringHelper;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.JestResultHandler;
import io.searchbox.cluster.Health;
import io.searchbox.cluster.NodesInfo;
import io.searchbox.core.*;
import io.searchbox.indices.*;
import io.searchbox.indices.mapping.GetMapping;
import io.searchbox.indices.mapping.PutMapping;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 异步接口
 *
 * @author CH
 * @since 1.0
 */
@Slf4j
public class ElasticSearchAsyncFactory implements IElasticSearchAsyncFactory {

    private ElasticSearchConfigurator elasticSearchConfigurator;
    private JestClientFactory jestClientFactory;
    private NetxProperties netxProperties;

    @Override
    public void connect(Properties properties) {
        start();
    }

    @Override
    public boolean createIndex(IndexConfig indexConfig) {
        CallBack callBack = indexConfig.getCallBack();
        CreateIndex createIndex = new CreateIndex.Builder(indexConfig.getIndex()).build();
        try (JestClient jestClient = this.jestClientFactory.getObject()) {

            jestClient.executeAsync(createIndex, new JestResultHandler<JestResult>() {
                @Override
                public void completed(JestResult result) {
                    if (null == callBack) {
                        return;
                    }
                    callBack.onResponse(result.isSucceeded());
                }

                @Override
                public void failed(Exception ex) {
                    if (null == callBack) {
                        return;
                    }
                    callBack.failture(ex);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean createIndex(String index, Map<String, Object> settings) {
        try (JestClient jestClient = this.jestClientFactory.getObject()) {
            CreateIndex.Builder builder = new CreateIndex.Builder(index);
            if(null != settings) {
                builder.settings(settings);
            }
            CreateIndex createIndex = builder.build();
            jestClient.executeAsync(createIndex, new JestResultHandler<JestResult>() {
                @Override
                public void completed(JestResult result) {

                }

                @Override
                public void failed(Exception ex) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean deleteIndex(IndexConfig indexConfig) {
        try (JestClient jestClient = this.jestClientFactory.getObject()) {

            CallBack callBack = indexConfig.getCallBack();

            CreateIndex createIndex = new CreateIndex.Builder(indexConfig.getIndex()).build();
            jestClient.executeAsync(createIndex, new JestResultHandler<JestResult>() {
                @Override
                public void completed(JestResult result) {
                    log.debug("索引:{} - 删除成功!", indexConfig.getIndex());
                    if (null == callBack) {
                        return;
                    }
                    callBack.onResponse(result.isSucceeded());
                }

                @Override
                public void failed(Exception ex) {
                    if (null == callBack) {
                        return;
                    }
                    callBack.failture(ex);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean addDocument(IndexConfig indexConfig) {
        try (JestClient jestClient = this.jestClientFactory.getObject()) {

            Index index = new Index.Builder(indexConfig.getDoc()).type(indexConfig.getType()).id(indexConfig.getId()).index(indexConfig.getIndex()).build();
            CallBack<Boolean> callBack = indexConfig.getCallBack();
            jestClient.executeAsync(index, new JestResultHandler<JestResult>() {
                @Override
                public void completed(JestResult result) {
                    log.debug("索引:{} - 数据添加成功!", indexConfig.getIndex());
                    if (null == callBack) {
                        return;
                    }
                    callBack.onResponse(result.isSucceeded());
                }

                @Override
                public void failed(Exception ex) {
                    if (null == callBack) {
                        return;
                    }
                    callBack.failture(ex);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean deleteDocument(IndexConfig indexConfig) {
        try (JestClient jestClient = this.jestClientFactory.getObject()) {

            Delete delete = new Delete.Builder(indexConfig.getId()).index(indexConfig.getIndex()).build();
            CallBack<Boolean> callBack = indexConfig.getCallBack();
            jestClient.executeAsync(delete, new JestResultHandler<JestResult>() {
                @Override
                public void completed(JestResult result) {
                    log.debug("索引:{} - 数据删除成功!", indexConfig.getIndex());
                    if (null == callBack) {
                        return;
                    }
                    callBack.onResponse(result.isSucceeded());
                }

                @Override
                public void failed(Exception ex) {
                    if (null == callBack) {
                        return;
                    }
                    callBack.failture(ex);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String getDocument(IndexConfig indexConfig) {
        try (JestClient jestClient = this.jestClientFactory.getObject()) {

            Get get = new Get.Builder(indexConfig.getIndex(), indexConfig.getId()).type(indexConfig.getType()).build();
            CallBack<String> callBack = indexConfig.getCallBack();
            jestClient.executeAsync(get, new JestResultHandler<JestResult>() {
                @Override
                public void completed(JestResult result) {
                    log.debug("索引:{} - 数据查询成功!", indexConfig.getIndex());
                    if (null == callBack) {
                        return;
                    }
                    callBack.onResponse(result.getJsonString());
                }

                @Override
                public void failed(Exception ex) {
                    if (null == callBack) {
                        return;
                    }
                    callBack.failture(ex);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateDocument(IndexConfig indexConfig) {
        try (JestClient jestClient = this.jestClientFactory.getObject()) {

            Update index = new Update.Builder(indexConfig.getDoc()).type(indexConfig.getType()).id(indexConfig.getId()).index(indexConfig.getIndex()).build();
            CallBack<Boolean> first = indexConfig.getCallBack();

            jestClient.executeAsync(index, new JestResultHandler<JestResult>() {
                @Override
                public void completed(JestResult result) {
                    log.debug("索引:{} - 数据更新成功!", indexConfig.getIndex());
                    if (null == first) {
                        return;
                    }
                    first.onResponse(result.isSucceeded());
                }

                @Override
                public void failed(Exception ex) {
                    if (null == first) {
                        return;
                    }
                    first.failture(ex);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<IndexConfig> bulkAddDocument(List<IndexConfig> indexConfigs) throws IOException {
        if (null != indexConfigs) {
            try (JestClient jestClient = this.jestClientFactory.getObject()) {
                Bulk.Builder bulk = new Bulk.Builder();
                for (IndexConfig indexConfig : indexConfigs) {
                    try {
                        bulk.addAction(new Index.Builder(indexConfig.getDoc()).index(indexConfig.getIndex()).type(indexConfig.getType()).id(indexConfig.getId()).build());
                    } catch (Exception e) {
                    }
                }

                Bulk build = bulk.build();
                CallBack callBack = FinderHelper.lastElement(indexConfigs).getCallBack();
                jestClient.executeAsync(build, new JestResultHandler<BulkResult>() {
                    @Override
                    public void completed(BulkResult result) {
                        if (null == callBack) {
                            return;
                        }
                        callBack.onResponse(result.getSourceAsStringList());
                    }

                    @Override
                    public void failed(Exception ex) {
                        log.debug("批量上传索引存在失败：{}", ex.getMessage());
                        if (null == callBack) {
                            return;
                        }
                        callBack.failture(ex);
                    }
                });
            }
        }
        return null;
    }

    @Override
    public List<?> bulkUpdateDocument(List<IndexConfig> indexConfigs) throws IOException {
        if (null != indexConfigs) {
            try (JestClient jestClient = this.jestClientFactory.getObject()) {
                Bulk.Builder bulk = new Bulk.Builder();
                for (IndexConfig indexConfig : indexConfigs) {
                    try {
                        bulk.addAction(new Update.Builder(indexConfig.getDoc())
                                .index(indexConfig.getIndex())
                                .type(indexConfig.getType())
                                .id(indexConfig.getId()).build());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Bulk build = bulk.build();
                CallBack callBack = FinderHelper.lastElement(indexConfigs).getCallBack();
                jestClient.executeAsync(build, new JestResultHandler<BulkResult>() {
                    @Override
                    public void completed(BulkResult result) {
                        if (null == callBack) {
                            return;
                        }
                        callBack.onResponse(result.getSourceAsStringList());
                    }

                    @Override
                    public void failed(Exception ex) {
                        log.debug("批量更新索引存在失败: {}", ex.getMessage());
                        if (null == callBack) {
                            return;
                        }
                        callBack.failture(ex);
                    }
                });
            }
        }
        return null;
    }

    @Override
    public List<?> bulkDeleteDocument(List<IndexConfig> indexConfigs) throws IOException {
        if (null != indexConfigs) {
            try (JestClient jestClient = this.jestClientFactory.getObject()) {
                Bulk.Builder bulk = new Bulk.Builder();
                for (IndexConfig indexConfig : indexConfigs) {
                    try {
                        bulk.addAction(new Delete.Builder(indexConfig.getId()).index(indexConfig.getIndex()).type(indexConfig.getType()).build());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Bulk build = bulk.build();
                CallBack<List<?>> callBack = FinderHelper.lastElement(indexConfigs).getCallBack();
                jestClient.executeAsync(build, new JestResultHandler<BulkResult>() {
                    @Override
                    public void completed(BulkResult result) {
                        if (null == callBack) {
                            return;
                        }
                        callBack.onResponse(result.getSourceAsStringList());
                    }

                    @Override
                    public void failed(Exception ex) {
                        log.debug("批量删除索引存在失败: {}", ex.getMessage());
                        if (null == callBack) {
                            return;
                        }
                        callBack.failture(ex);
                    }
                });
            }
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> queryAllByConditions(Condition condition, CallBack... callbacks) throws Exception {
        try (JestClient jestClient = this.jestClientFactory.getObject()) {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            BoolQueryBuilder boolQueryBuilder = QlUtils.makeQl(condition);

            if (null != boolQueryBuilder) {
                searchSourceBuilder.query(boolQueryBuilder);
                if (StringHelper.isNotBlank(condition.getHighlight())) {
                    searchSourceBuilder.highlight(new HighlightBuilder()
                            .field(condition.getHighlight())
                            .preTags("<em>")
                            .postTags("</em>")
                    );
                }

                if (condition.getSize() < 1) {
                    searchSourceBuilder.size(1000);
                } else {
                    searchSourceBuilder.size(condition.getSize());
                }

                if (condition.getStart() < 1) {
                    searchSourceBuilder.from(1);
                } else {
                    searchSourceBuilder.from(condition.getStart());
                }

                String sortFields = condition.getSortFields();
                if (StringHelper.isNotBlank(sortFields)) {
                    Object sortOrder = condition.getSortOrder();
                    if (null == sortOrder) {
                        searchSourceBuilder.sort(sortFields, SortOrder.DESC);
                    } else {
                        if (sortOrder instanceof String) {
                            searchSourceBuilder.sort(sortFields, SortOrder.valueOf((String) sortOrder));
                        } else if (sortOrder instanceof SortOrder) {
                            searchSourceBuilder.sort(sortFields, (SortOrder) sortOrder);
                        }
                    }
                }
                if (BooleanHelper.hasLength(condition.getFields())) {
                    searchSourceBuilder.fetchSource(ArraysHelper.toArray(condition.getFields()), new String[]{});
                }
                if (log.isDebugEnabled()) {
                    log.debug("当前查询语句:{}", searchSourceBuilder.toString());
                }
                searchSourceBuilder.query(searchSourceBuilder.toString());
                Search search = new Search.Builder(searchSourceBuilder.toString())
                        .addIndex(condition.getIndex())
                        .addType(condition.getType())
                        .refresh(true)
                        .build();

                CallBack callBack = FinderHelper.lastElement(callbacks);
                jestClient.executeAsync(search, new JestResultHandler<SearchResult>() {
                    @Override
                    public void completed(SearchResult result) {
                        if (null == callBack) {
                            return;
                        }
                        callBack.onResponse(dealSearchResponse(result));
                    }

                    @Override
                    public void failed(Exception ex) {
                        if (null == callBack) {
                            return;
                        }
                        callBack.failture(ex);
                    }
                });

            }
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> query(Search search, CallBack... callbacks) throws Exception {
        try (JestClient jestClient = this.jestClientFactory.getObject()) {
            CallBack callback = FinderHelper.lastElement(callbacks);
            jestClient.executeAsync(search, new JestResultHandler<SearchResult>() {
                @Override
                public void completed(SearchResult result) {
                    if (null == callback) {
                        return;
                    }
                    callback.onResponse(result.getSourceAsStringList());
                }

                @Override
                public void failed(Exception ex) {
                    if (null == callback) {
                        return;
                    }
                    callback.failture(ex);

                }
            });
        }
        return null;
    }

    @Override
    public String getDocumentById(IndexConfig indexConfig) throws Exception {
        try (JestClient jestClient = this.jestClientFactory.getObject()) {
            Get.Builder builder = new Get.Builder(indexConfig.getIndex(), indexConfig.getId()).type(indexConfig.getType());
            CallBack callBack = indexConfig.getCallBack();

            jestClient.executeAsync(builder.build(), new JestResultHandler() {
                @Override
                public void completed(Object result) {
                    if (null == callBack) {
                        return;
                    }
                    callBack.onResponse(result);
                }

                @Override
                public void failed(Exception ex) {
                    if (null == callBack) {
                        return;
                    }
                    callBack.failture(ex);
                }
            });
        }
        return null;
    }

    @Override
    public <T> T getDocumentById(IndexConfig indexConfig, Class<T> entity) throws Exception {
        try (JestClient jestClient = this.jestClientFactory.getObject()) {
            Get.Builder builder = new Get.Builder(indexConfig.getIndex(), indexConfig.getId()).type(indexConfig.getType());
            CallBack callBack = indexConfig.getCallBack();

            jestClient.executeAsync(builder.build(), new JestResultHandler() {
                @Override
                public void completed(Object result) {
                    if (null == callBack) {
                        return;
                    }
                    callBack.onResponse(result);
                }

                @Override
                public void failed(Exception ex) {
                    if (null == callBack) {
                        return;
                    }
                    callBack.failture(ex);
                }
            });
        }
        return null;
    }

    @Override
    public boolean checkIndexExists(String... indexs) {
        try (JestClient jestClient = this.jestClientFactory.getObject()) {
            IndicesExists indicesExists = new IndicesExists.Builder(Arrays.asList(indexs)).build();
            try {
                JestResult execute = jestClient.execute(indicesExists);
                return execute.getResponseCode() == 200;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void closeIndex(IndexConfig indexConfig) throws IOException {
        try (JestClient jestClient = this.jestClientFactory.getObject()) {
            CloseIndex closeIndex = new CloseIndex.Builder(indexConfig.getIndex()).build();
            jestClient.execute(closeIndex);
        }
    }

    @Override
    public void flushIndex(IndexConfig indexConfig) throws IOException {
        JestResult result;
        try (JestClient jestClient = this.jestClientFactory.getObject()) {
            Flush flush = new Flush.Builder().build();
            result = jestClient.execute(flush);
        }
        System.out.println(result.getJsonString());
    }

    @Override
    public void setFieldsMapping(IndexConfig indexConfig) {

    }

    @Override
    public void openIndex(IndexConfig indexConfig) throws IOException {
        try (JestClient jestClient = this.jestClientFactory.getObject()) {
            OpenIndex openIndex = new OpenIndex.Builder(indexConfig.getIndex()).build();
            jestClient.execute(openIndex);
        }
    }

    @Override
    public void optimizeIndex(IndexConfig indexConfig) throws IOException {
        JestResult result;
        try (JestClient jestClient = this.jestClientFactory.getObject()) {
            Optimize optimize = new Optimize.Builder().build();
            result = jestClient.execute(optimize);
        }
        log.debug(result.getJsonString());
    }

    @Override
    public void nodesInfo() throws IOException {
        JestResult result;
        try (JestClient jestClient = this.jestClientFactory.getObject()) {
            NodesInfo nodesInfo = new NodesInfo.Builder().build();
            result = jestClient.execute(nodesInfo);
        }
        log.debug(result.getJsonString());
    }

    @Override
    public void health() throws IOException {
        JestResult result;
        try (JestClient jestClient = this.jestClientFactory.getObject()) {
            Health health = new Health.Builder().build();
            result = jestClient.execute(health);
        }
        log.debug(result.getJsonString());
    }

    @Override
    public String getMapping(String indexName, String indexType) throws IOException {
        JestResult execute;
        try (JestClient jestClient = this.jestClientFactory.getObject()) {

            GetMapping.Builder builder = new GetMapping.Builder();
            builder.addIndex(indexName).addType(indexType);

            execute = jestClient.execute(builder.build());
        }
        return execute.getJsonString();
    }

    @Override
    public String putMapping(String indexName, String indexType, String source) throws IOException {
        JestResult execute;
        try (JestClient jestClient = this.jestClientFactory.getObject()) {

            PutMapping.Builder builder = new PutMapping.Builder(indexName, indexType, source);
            execute = jestClient.execute(builder.build());
        }
        return execute.getJsonString();
    }

    @Override
    public void configure(NetxProperties netxProperties) {
        this.netxProperties = netxProperties;
    }

    @Override
    public Object client() {
        return jestClientFactory;
    }

    @Override
    public void start() {
        this.elasticSearchConfigurator = new ElasticSearchConfigurator(netxProperties);
        this.jestClientFactory = this.elasticSearchConfigurator.configure();
    }

    @Override
    public boolean isStart() {
        return null != jestClientFactory;
    }

    @Override
    public void close() throws Exception {
        if(null != jestClientFactory) {
            jestClientFactory.getObject().close();
        }
    }
}
