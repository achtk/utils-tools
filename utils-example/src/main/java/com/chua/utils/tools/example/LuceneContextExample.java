package com.chua.utils.tools.example;

import com.chua.utils.netx.entity.DocumentData;
import com.chua.utils.netx.entity.DocumentMap;
import com.chua.utils.netx.entity.Search;
import com.chua.utils.netx.function.DocumentContextAware;
import com.chua.utils.netx.lucene.aware.MemoryLuceneContextAware;
import com.chua.utils.netx.lucene.aware.NioFSLuceneContextAware;
import com.chua.utils.netx.lucene.template.MMapLuceneOperatorTemplate;
import com.chua.utils.netx.lucene.template.SingleLuceneOperatorTemplate;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.template.template.LuceneOperatorTemplate;
import lombok.Data;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public class LuceneContextExample {

    public static void main(String[] args) throws Exception {
        List<DocumentMap> documentMapList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            DocumentMap documentMap = new DocumentMap();
            documentMap.append("time", System.currentTimeMillis())
                    .append("uuid", StringHelper.uuid())
                    .append("name", "demo" + i)
                    .append("id", i);

            documentMapList.add(documentMap);
        }

        Search search = new Search();
        search.setMax(1300);
        search.setSearch("id:1~10");
        search.setMatch(Search.Match.FULL);
//
//        DocumentContextAware documentContextAware = new MemoryLuceneContextAware();
//        documentContextAware.addDocuments(documentMapList);
//        DocumentData documentData = documentContextAware.search(search);
//        System.out.println(documentData);
        String name = "demo";

        LuceneOperatorTemplate luceneOperatorTemplate = new SingleLuceneOperatorTemplate("demo");
        luceneOperatorTemplate.createTable(name);
        luceneOperatorTemplate.addDocuments(name, documentMapList);
        List list = luceneOperatorTemplate.queryForList(name, search);
        System.out.println(list);
    }

    @Data
    public static class Demo {
        private String name;
        private Integer id;
    }
}
