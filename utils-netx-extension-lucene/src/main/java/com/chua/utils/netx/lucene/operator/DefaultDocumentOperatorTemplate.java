package com.chua.utils.netx.lucene.operator;

import com.chua.utils.netx.lucene.entity.DataDocument;
import com.chua.utils.netx.lucene.util.DocumentUtil;
import com.chua.utils.tools.common.CollectionHelper;
import com.chua.utils.tools.common.NumberHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import java.util.Collection;
import java.util.List;

/**
 * 默认的文档操作模板
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/3
 */
@Slf4j
@AllArgsConstructor
public class DefaultDocumentOperatorTemplate implements DocumentOperatorTemplate {

    /**
     * 索引模板
     */
    private IndexOperatorTemplate indexOperatorTemplate;
    /**
     * 索引
     */
    private String index;

    @Override
    public void addDocument(DataDocument dataDocument) throws Exception {
        if (!indexOperatorTemplate.exist(index)) {
            throw new IllegalStateException("索引不存在");
        }
        List<IndexWriter> indexWriterList = indexOperatorTemplate.indexWrite(index);
        try (IndexWriter indexWriter = CollectionHelper.getRandom(indexWriterList)) {
            indexWriter.addDocument(DocumentUtil.map2Document(dataDocument));
            indexWriter.commit();
        }
    }

    @Override
    public void addDocuments(List<DataDocument> dataDocument) throws Exception {
        if (!indexOperatorTemplate.exist(index)) {
            throw new IllegalStateException("索引不存在");
        }
        long startTime = 0L;
        if (log.isDebugEnabled()) {
            startTime = System.currentTimeMillis();
            log.debug("开始处理数据，数量:{}", dataDocument.size());
        }
        List<IndexWriter> indexWriterList = indexOperatorTemplate.indexWrite(index);
        int size = indexWriterList.size();
        for (int i = 0; i < dataDocument.size(); i++) {
            IndexWriter indexWriter = indexWriterList.get(i % size);
            indexWriter.addDocument(DocumentUtil.map2Document(dataDocument.get(i)));
        }
        for (IndexWriter indexWriter : indexWriterList) {
            indexWriter.commit();
            indexWriter.close();
        }
        if (log.isDebugEnabled()) {
            log.debug("处理数据完成, 处理数量:{}, 耗时: {}ms", dataDocument.size(), System.currentTimeMillis() - startTime);
        }
    }

    @Override
    public void deleteDocument(String dataId) throws Exception {
        if (!indexOperatorTemplate.exist(index)) {
            throw new IllegalStateException("索引不存在");
        }
        List<IndexWriter> indexWriterList = indexOperatorTemplate.indexWrite(index);
        for (IndexWriter indexWriter : indexWriterList) {
            indexWriter.deleteDocuments(new Term("dataId:" + dataId));
            indexWriter.commit();
            indexWriter.close();
        }
    }

    @Override
    public void updateDocument(DataDocument dataDocument) throws Exception {
        if (!indexOperatorTemplate.exist(index)) {
            throw new IllegalStateException("索引不存在");
        }
        List<IndexWriter> indexWriterList = indexOperatorTemplate.indexWrite(index);
        try (IndexWriter indexWriter = CollectionHelper.getRandom(indexWriterList)) {
            indexWriter.updateDocument(new Term("dataId:" + dataDocument.getDataId()), DocumentUtil.map2Document(dataDocument));
            indexWriter.commit();
        }
    }

    @Override
    public void updateDocuments(List<DataDocument> dataDocument) throws Exception {
        if (!indexOperatorTemplate.exist(index)) {
            throw new IllegalStateException("索引不存在");
        }
        List<IndexWriter> indexWriterList = indexOperatorTemplate.indexWrite(index);
        for (IndexWriter indexWriter : indexWriterList) {
            for (DataDocument document : dataDocument) {
                indexWriter.updateDocument(new Term("dataId:" + document.getDataId()), DocumentUtil.map2Document(document));
            }
        }
        for (IndexWriter indexWriter : indexWriterList) {
            indexWriter.commit();
            indexWriter.close();
        }
    }
}
