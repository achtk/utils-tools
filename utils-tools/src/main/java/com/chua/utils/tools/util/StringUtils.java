package com.chua.utils.tools.util;

import com.chua.utils.tools.common.StringHelper;
import com.google.common.base.Strings;

/**
 * 字符串工具类
 * @author CH
 * @version 1.0.0
 * @since 2020/12/18
 */
public class StringUtils extends StringHelper {

    /**
     * 统计{symbol}出现的次数
     * @param source 数据
     * @param symbol 符号
     * @return
     */
    public static int count(String source, String symbol) {
        if(Strings.isNullOrEmpty(source) || Strings.isNullOrEmpty(symbol)) {
            return 0;
        }
        int index = -1;
        int count = 0;
        while ((index = source.indexOf(symbol, index + 1)) != -1) {
            ++ count;
        }
        return count;
    }
}
