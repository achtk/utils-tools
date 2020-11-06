package com.chua.utils.tools.example;

import com.chua.utils.netx.centor.resolver.TemplateResolver;
import com.chua.utils.netx.entity.DocumentData;
import com.chua.utils.netx.entity.DocumentMap;
import com.chua.utils.netx.entity.Search;
import com.chua.utils.netx.function.DocumentContextAware;
import com.chua.utils.netx.lucene.aware.MemoryLuceneContextAware;
import com.chua.utils.netx.lucene.aware.NioFSLuceneContextAware;
import com.chua.utils.netx.lucene.entity.DataDocument;
import com.chua.utils.netx.lucene.entity.HitData;
import com.chua.utils.netx.lucene.operator.DocumentOperatorTemplate;
import com.chua.utils.netx.lucene.operator.IndexOperatorTemplate;
import com.chua.utils.netx.lucene.operator.SearchOperatorTemplate;
import com.chua.utils.netx.lucene.resolver.LuceneTemplateResolver;
import com.chua.utils.netx.lucene.template.MMapLuceneOperatorTemplate;
import com.chua.utils.netx.lucene.template.SingleLuceneOperatorTemplate;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.template.template.LuceneOperatorTemplate;
import com.chua.utils.tools.text.IdHelper;
import lombok.Data;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static org.mockito.Mockito.mock;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public class LuceneContextExample {

    public static void main(String[] args) throws Exception {
        List<DataDocument> documentMapList = new ArrayList<>();
        for (int i = 1000; i < 100000; i++) {
            DataDocument documentMap = new DataDocument();
            Map<String, Object> properties = new HashMap<>();

            documentMap.setData(properties);
            properties.put("time", System.currentTimeMillis());
            properties.put("uuid", IdHelper.createUuid());
            properties.put("name", "demo" + i);
            properties.put("id", i);

            documentMapList.add(documentMap);
        }

        Search search = new Search();
        search.setMax(1300);
        search.setSearch("id:1~10");
        search.setMatch(Search.Match.FULL);

        String index = "t_test_info";

        LuceneTemplateResolver luceneTemplateResolver = new LuceneTemplateResolver();
        IndexOperatorTemplate indexOperatorTemplate = luceneTemplateResolver.getIndexOperatorTemplate();
        if(!indexOperatorTemplate.exist(index)) {
            indexOperatorTemplate.create(index, 5);
        }
       // DocumentOperatorTemplate documentOperatorTemplate = luceneTemplateResolver.getDocumentOperatorTemplate(index);
       // documentOperatorTemplate.addDocuments(documentMapList);

        SearchOperatorTemplate searchOperatorTemplate = luceneTemplateResolver.getSearchOperatorTemplate(index);
        HitData search1 = searchOperatorTemplate.search("*:*", null, null, 1, 10000);
        HitData search2 = searchOperatorTemplate.quickSearch("*:*", null, null, 1, 10000);
        System.out.println(search1);
        System.out.println(search2);
        System.out.println();
    }

    @Data
    public static class Demo {
        private String name;
        private Integer id;
    }
}
