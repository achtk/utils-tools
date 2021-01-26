package com.chua.utils.netx.solr.factory;

import com.chua.utils.netx.factory.INetFactory;
import com.chua.utils.tools.function.ToolsFunction;
import com.chua.utils.tools.properties.NetProperties;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.util.*;

/**
 * solr工具
 *
 * @author CH
 * @version 1.0.0
 * @className SolrFactory
 * @since 2020/8/5 12:52
 */
@Slf4j
@NoArgsConstructor
@RequiredArgsConstructor
public abstract class SolrFactory implements INetFactory<SolrClient> {

    /**
     * solr地址
     */
    private static final String FIELD_SOLR_URL = "url";
    private static final String FIELD_DOC = "doc";
    private static final String FIELD_COMMIT_TIME = "commitWithinMs";
    private static final String FIELD_ID = "id";
    /**
     * 默认集合
     */
    protected static final String DEFAULT_COLLECTION = "Collection";
    private static final String FIELD_COLLEACTION = "collection";

    @NonNull
    protected NetProperties netProperties;

    @Getter
    @Setter
    private SolrClient solrClient;

    @Override
    public void configure(NetProperties netProperties) {
        this.netProperties = netProperties;
    }

    @Override
    public SolrClient client() {
        return solrClient;
    }

    @Override
    public boolean isStart() {
        return null != solrClient;
    }


    @Override
    public void close() throws Exception {
        if (null != solrClient) {
            solrClient.close();
        }
    }

    /**
     *
     */
    public static class MapListFunction implements ToolsFunction<QueryResponse, List<Map<String, Object>>> {

        @Override
        public List<Map<String, Object>> accept(QueryResponse response) {
            if (null != response) {
                // 查询结果
                SolrDocumentList results = response.getResults();
                // 查询结果总数
                long cnt = results.getNumFound();
                log.debug("查询结果总数:", cnt);

                List<Map<String, Object>> result = new ArrayList<>();
                for (SolrDocument solrDocument : results) {
                    Set<String> keySet = solrDocument.keySet();
                    Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

                    Map<String, Object> map = new HashMap<>();

                    for (String key : keySet) {
                        Object object = solrDocument.get(key);
                        Map<String, List<String>> highlightingEntity = highlighting.get(object);
                        if (null != highlightingEntity) {
                            for (Map.Entry<String, List<String>> entity : highlightingEntity.entrySet()) {
                                map.put(entity.getKey(), entity.getValue().get(0));
                            }
                        } else {
                            map.put(key, object);
                        }
                    }
                    result.add(map);
                }
                return result;
            }
            return null;
        }
    }
}
