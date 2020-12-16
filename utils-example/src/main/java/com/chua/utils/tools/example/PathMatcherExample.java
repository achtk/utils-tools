package com.chua.utils.tools.example;

import com.chua.utils.tools.matcher.AntPathMatcher;
import com.chua.utils.tools.matcher.ApachePathMatcher;
import com.chua.utils.tools.matcher.PathMatcher;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/16
 */
public class PathMatcherExample {

    public static void main(String[] args) {
        PathMatcher antPathMatcher = new AntPathMatcher();
        System.out.println(antPathMatcher.match("**/*.class", "org/springframework/Demo.class"));
        PathMatcher apachePathMatcher = new ApachePathMatcher();
        System.out.println(apachePathMatcher.match("**/*.class", "org/springframework/Demo.class"));
    }
}
