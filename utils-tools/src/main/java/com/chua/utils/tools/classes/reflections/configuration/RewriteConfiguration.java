package com.chua.utils.tools.classes.reflections.configuration;

import com.chua.utils.tools.classes.reflections.scanner.AbstractRewriteScanner;
import com.chua.utils.tools.classes.reflections.scanner.RewriteSubTypesScanner;
import com.chua.utils.tools.classes.reflections.scanner.RewriteTypeAnnotationsScanner;
import com.chua.utils.tools.common.ThreadHelper;
import com.google.common.collect.Sets;
import org.reflections.scanners.Scanner;
import org.reflections.util.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 配置(重写)
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/12
 */
public class RewriteConfiguration extends ConfigurationBuilder {

    private Set<AbstractRewriteScanner> rewriteScanners = Sets.newHashSet(new RewriteTypeAnnotationsScanner(), new RewriteSubTypesScanner());

    {
        setExecutorService(ThreadHelper.newProcessorThreadExecutor("REWRITE REFLECTIONS"));
    }

    /**
     * 获取scanner
     *
     * @return
     */
    public Set<AbstractRewriteScanner> getRewriteScanners() {
        for (Scanner scanner : rewriteScanners) {
            scanner.setConfiguration(this);
        }
        return rewriteScanners;
    }

    /**
     * @param scanners
     * @return
     */
    public ConfigurationBuilder setRewriteScanners(AbstractRewriteScanner... scanners) {
        this.rewriteScanners.clear();
        this.rewriteScanners.addAll(Arrays.asList(scanners.clone()));
        return this;
    }
}
