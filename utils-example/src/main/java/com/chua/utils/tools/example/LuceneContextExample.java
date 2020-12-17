package com.chua.utils.tools.example;

import com.chua.utils.netx.entity.Search;
import com.chua.utils.netx.lucene.entity.HitData;
import com.chua.utils.netx.lucene.operator.IndexOperatorTemplate;
import com.chua.utils.netx.lucene.operator.SearchOperatorTemplate;
import com.chua.utils.netx.lucene.resolver.LuceneTemplateResolver;
import lombok.Data;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public class LuceneContextExample {

    public static void main(String[] args) throws Exception {
//        List<DataDocument> documentMapList = new ArrayList<>();
//        for (int i = 1000; i < 100000; i++) {
//            DataDocument documentMap = new DataDocument();
//            Map<String, Object> properties = new HashMap<>();
//
//            documentMap.setData(properties);
//            properties.put("time", System.currentTimeMillis());
//            properties.put("uuid", IdHelper.createUuid());
//            properties.put("name", "demo" + i);
//            properties.put("id", i);
//
//            documentMapList.add(documentMap);
//        }

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
       //DocumentOperatorTemplate documentOperatorTemplate = luceneTemplateResolver.getDocumentOperatorTemplate(index);
       // documentOperatorTemplate.addDocuments(documentMapList);

        SearchOperatorTemplate searchOperatorTemplate = luceneTemplateResolver.getSearchOperatorTemplate(index);
        HitData search1 = searchOperatorTemplate.search("*:*", 1, 10000);
        HitData search2 = searchOperatorTemplate.quickSearch("*:*", 1, 10000);
        HitData search3 = searchOperatorTemplate.quickSearch("id:[1 TO 1001]", 1, 10000);
        System.out.println(search1);
        System.out.println(search2);
        System.out.println(search3);
        System.out.println();
    }

    @Data
    public static class Demo {
        private String name;
        private Integer id;
    }
}
