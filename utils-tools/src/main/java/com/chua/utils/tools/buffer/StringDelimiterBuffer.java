package com.chua.utils.tools.buffer;

import java.io.IOException;
import java.util.Arrays;

/**
 * 分隔符追加
 *
 * @author CH
 * @version 1.0
 * @since 2020/10/24 21:47
 */
public class StringDelimiterBuffer implements Appendable {
    private String delimiter;
    private StringBuffer stringBuffer;

    public StringDelimiterBuffer(String delimiter) {
        this.delimiter = delimiter == null ? "" : delimiter;
        this.stringBuffer = new StringBuffer(delimiter);
    }

    @Override
    public Appendable append(CharSequence csq) throws IOException {
        stringBuffer.append(csq);
        return this;
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) throws IOException {
        stringBuffer.append(csq, start, end);
        return this;
    }

    @Override
    public Appendable append(char c) throws IOException {
        stringBuffer.append(c);
        return this;
    }

    public synchronized Appendable append(Object obj) {
        stringBuffer.append(String.valueOf(obj));
        return this;
    }

    public synchronized Appendable append(String str) {
        stringBuffer.append(str);
        return this;
    }

    public synchronized Appendable append(StringBuffer sb) {
        stringBuffer.append(sb);
        return this;
    }

    @Override
    public synchronized String toString() {
        return stringBuffer.substring(delimiter.length());
    }


}
