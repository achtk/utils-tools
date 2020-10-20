package com.chua.utils.netx.lucene.analyzer;

import com.chua.utils.netx.lucene.tokenizer.IKTokenizer6x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

/**
 * Analyzer
 * @author CH
 * @version 1.0
 * @since 2020/10/20 17:45
 */
public class IKAnalyzer6x extends Analyzer {

    private boolean useSmart;

    public boolean useSmart() {
        return useSmart;
    }

    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    /**
     * IK分词器Lucene Analyzer接口实现类;默认细粒度切分算法
     */
    public IKAnalyzer6x() {
        this(false);
    }

    /**
     * IK分词器Lucene Analyzer接口实现类;当为true时，分词器进行智能切分
     * @param useSmart
     */
    public IKAnalyzer6x(boolean useSmart) {
        super();
        this.useSmart = useSmart;
    }

    /**
     * 重写最新版本的createComponents;重载Analyzer接口，构造分词组件
     * @param fieldName
     * @return
     */
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer ikTokenizer6x = new IKTokenizer6x(this.useSmart());
        return new TokenStreamComponents(ikTokenizer6x);
    }
}
