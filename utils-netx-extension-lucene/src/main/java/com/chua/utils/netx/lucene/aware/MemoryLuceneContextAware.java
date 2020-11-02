package com.chua.utils.netx.lucene.aware;

import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

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
