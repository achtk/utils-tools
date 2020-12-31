package com.chua.tools.example;

import com.chua.utils.tools.matcher.AntPathMatcher;
import com.chua.utils.tools.matcher.PathMatcher;

/**
 * 路径匹配器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/16
 */
public class PathMatcherExample extends BaseExample {

    private static final String SOURCE = "**/*.class";
    private static final String TARGET = "org/springframework/Demo.class";

    public static void main(String[] args) {
        //创建Ant匹配器
        PathMatcher antPathMatcher = new AntPathMatcher();
        log.info("匹配值: {}, 匹配表达式 -> {}", TARGET, SOURCE, antPathMatcher.match(SOURCE, TARGET));
    }
}
