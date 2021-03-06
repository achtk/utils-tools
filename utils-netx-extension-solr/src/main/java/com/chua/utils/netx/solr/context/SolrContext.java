package com.chua.utils.netx.solr.context;

import com.chua.utils.netx.factory.INetFactory;
import com.chua.utils.netx.solr.factory.CloudSolrFactory;
import com.chua.utils.netx.solr.factory.SingleSolrFactory;
import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.common.ArraysHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.function.ToolsFunction;
import com.chua.utils.tools.properties.NetProperties;
import com.chua.utils.tools.properties.QueryProperties;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 单例solr上下文
 * @author CH
 */
public class SolrContext implements AutoCloseable {

    private INetFactory netxFactory;
    private SolrClient solrClient;

    public SolrContext(NetProperties netProperties) {
        String[] host = netProperties.getHost();
        if(BooleanHelper.hasLength(host, 2)) {
            this.netxFactory = new CloudSolrFactory(netProperties);
        } else {
            this.netxFactory = new SingleSolrFactory(netProperties);
        }
        this.netxFactory.start();
        this.solrClient = (SolrClient) this.netxFactory.client();
    }

    /**
     * <table border=1 colpadding=3>
     *     <thead>
     *         <tr>
     *             <td width=50>属性</td>
     *             <td >类型</td>
     *             <td >描述</td>
     *         </tr>
     *     </thead>
     *     <tbody>
     *         <tr>
     *             <td>id</td>
     *             <td>java.lang.String</td>
     *             <td>文档id，多个以，隔开</td>
     *         </tr>
     *          <tr>
     *             <td>collection</td>
     *             <td>索引</td>
     *             <td>索引，默认Collection</td>
     *         </tr>
     *     </tbody>
     *
     * </table>
     */
    public void deleteAction( String dataId, String collection, int commit) {
        List<String> ids = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(dataId);
        try {
            if (Strings.isNullOrEmpty(collection)) {
                solrClient.deleteById(ids, commit);
            } else {
                solrClient.deleteById(collection, ids, commit);
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <table border=1 colpadding=3>
     *     <thead>
     *         <tr>
     *             <td width=50>属性</td>
     *             <td >类型</td>
     *             <td >描述</td>
     *         </tr>
     *     </thead>
     *     <tbody>
     *         <tr>
     *             <td>doc</td>
     *             <td>java.util.Map</td>
     *             <td>
     *                 文档
     *                 <ul>
     *                     <li>key为id</li>
     *                     <li>value为任意文档</li>
     *                 </ul>
     *             </td>
     *         </tr>
     *         <tr>
     *             <td>commitWithinMs</td>
     *             <td>int</td>
     *             <td>提交时间，默认-1</td>
     *         </tr>
     *         <tr>
     *             <td>collection</td>
     *             <td>索引</td>
     *             <td>索引，默认Collection</td>
     *         </tr>
     *     </tbody>
     *
     * </table>
     */
    public void createAction(Map<Object, Object> docs, String collection, int commit) {
        List<SolrInputDocument> documents = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : docs.entrySet()) {
            Object key = entry.getKey();
            if (null == key) {
                continue;
            }
            SolrInputDocument document = new SolrInputDocument();
            document.addField(key.toString(), entry.getValue());
        }
        try {
            if (Strings.isNullOrEmpty(collection)) {
                solrClient.addBeans(documents, commit);
            } else {
                solrClient.addBeans(collection, documents, commit);
            }
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    public <I, O> O query(QueryProperties queryProperties, ToolsFunction<I, O> functionO) {
        //使用这个对象做查询
        SolrQuery query = new SolrQuery();
        //查询所有数据
//        params.setQuery("*:*");
        query.setQuery(MapOperableHelper.getString(queryProperties, "query"));
        //分页，默认是分页从0开始，每页显示10行
        query.setStart(MapOperableHelper.getIntValue(queryProperties, "start", 0));
        query.setRows(MapOperableHelper.getIntValue(queryProperties, "row", 30));

        Map<Object, Object> sorts = (Map<Object, Object>) MapOperableHelper.getMap(queryProperties, "sorts");
        if (null != sorts) {
            for (Map.Entry<Object, Object> entry : sorts.entrySet()) {
                String value = entry.getValue().toString();
                query.addSort(entry.getKey().toString(), "desc".equals(value) ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc);
            }
        }
        List<String> fields = MapOperableHelper.getList(queryProperties, "fields", String.class);
        if (null != fields) {
            query.setFields(ArraysHelper.toArray(fields));
        }

        List<String> hiList = MapOperableHelper.getList(queryProperties, "highlight", String.class);
        if (null != hiList) {
            query.setHighlightSimplePre("<em>");
            query.setHighlightSimplePost("</em>");
            for (String field : hiList) {
                query.addHighlightField(field);
            }
            query.setHighlight(true);
            query.setHighlightFragsize(72);
            query.setHighlightSnippets(3);

        }

        String timeAllowed = MapOperableHelper.getString(queryProperties, "timeAllowed");
        if (StringHelper.isNotBlank(timeAllowed)) {
            query.set("timeAllowed", timeAllowed);
        }
        String wt = MapOperableHelper.getString(queryProperties, "wt");
        if (StringHelper.isNotBlank(wt)) {
            query.set("wt", wt);
        }


        QueryResponse queryResponse = null;
        try {
            queryResponse = solrClient.query(query);
            //拿到数据集合,返回查询结果
            List<SolrDocument> list = queryResponse.getResults();
            return null == functionO ? (O) list : functionO.accept((I) list);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        if(null != this.netxFactory) {
            netxFactory.close();
        }
    }
}
