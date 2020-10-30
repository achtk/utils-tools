package com.chua.utils.netx.lucene.aware;

import com.chua.utils.netx.entity.DocumentData;
import com.chua.utils.netx.entity.DocumentMap;
import com.chua.utils.netx.entity.Search;
import com.chua.utils.netx.function.DocumentContextAware;
import com.chua.utils.netx.lucene.util.DocumentUtil;
import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.BeansHelper;
import com.chua.utils.tools.common.BooleanHelper;
import lombok.Getter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

/**
 * 内存模式启动lucene
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public class MemoryLuceneContextAware extends NioFSLuceneContextAware {

    public MemoryLuceneContextAware() throws IOException {
        super(null, new RAMDirectory());
    }

}
