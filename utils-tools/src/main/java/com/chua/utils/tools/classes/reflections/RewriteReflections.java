package com.chua.utils.tools.classes.reflections;

import com.chua.utils.tools.classes.reflections.configuration.RewriteConfiguration;
import com.chua.utils.tools.classes.reflections.scan.RewriteScan;
import com.chua.utils.tools.classes.reflections.scanner.*;
import com.chua.utils.tools.common.ThreadHelper;
import lombok.Getter;
import org.reflections.Configuration;
import org.reflections.ReflectionsException;
import org.reflections.scanners.*;
import org.reflections.scanners.Scanner;
import org.reflections.util.Utils;
import org.reflections.vfs.Vfs;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.reflections.ReflectionUtils.*;
import static org.reflections.util.Utils.*;

/**
 * 反射处理工厂
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/22
 */
public class RewriteReflections extends RewriteScan {
    /**
     * 构造
     *
     * @param rewriteConfiguration 配置
     */
    public RewriteReflections(RewriteConfiguration rewriteConfiguration) {
        super.store = STORE;
        this.rewriteConfiguration = rewriteConfiguration;
        this.scan();
    }

    /**
     * 扫描URL
     *
     * @param url URL
     */
    public void scanUrl(URL url) {
        this.scan(url);
    }

    /**
     * 扫描URL
     *
     * @param urls URL
     */
    public void asyncScanUrl(URL... urls) {
        ExecutorService executorService = ThreadHelper.newProcessorThreadExecutor();
        for (URL url : urls) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    scan(url);
                }
            });
        }
        executorService.shutdown();
    }

}
