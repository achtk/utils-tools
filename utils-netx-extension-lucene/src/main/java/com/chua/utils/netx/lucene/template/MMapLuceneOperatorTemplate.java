package com.chua.utils.netx.lucene.template;

import com.chua.utils.netx.function.DocumentContextAware;
import com.chua.utils.netx.lucene.aware.MMapLuceneContextAware;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * lucene操作模板
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/2
 */
public class MMapLuceneOperatorTemplate extends FileLuceneOperatorTemplate {

    @Override
    protected DocumentContextAware getDocumentContextAware(String name) throws IOException {
        return new MMapLuceneContextAware(Paths.get(database, name));
    }
}

