package com.chua.utils.tools.classes.reflections.configuration;

import com.chua.utils.tools.classes.reflections.adaptor.RewriteJavassistAdapter;
import com.chua.utils.tools.classes.reflections.scanner.AbstractRewriteScanner;
import com.chua.utils.tools.classes.reflections.scanner.RewriteSubTypesScanner;
import com.chua.utils.tools.classes.reflections.scanner.RewriteTypeAnnotationsScanner;
import com.chua.utils.tools.common.ThreadHelper;
import com.google.common.collect.Sets;
import org.reflections.Reflections;
import org.reflections.adapters.JavaReflectionAdapter;
import org.reflections.adapters.JavassistAdapter;
import org.reflections.adapters.MetadataAdapter;
import org.reflections.scanners.Scanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * 配置(重写)
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/12
 */
public class RewriteConfiguration extends ConfigurationBuilder {

    private Predicate<String> inputsFilter;
    /**
     * lazy
     */
    protected MetadataAdapter metadataAdapter;

    private Set<AbstractRewriteScanner> rewriteScanners = Sets.newHashSet(new RewriteTypeAnnotationsScanner(), new RewriteSubTypesScanner());

    {
        setExecutorService(ThreadHelper.newProcessorThreadExecutor("REWRITE REFLECTIONS"));
    }

    @Override
    public MetadataAdapter getMetadataAdapter() {
        if (metadataAdapter != null) {
            return metadataAdapter;
        } else {
            try {
                return (metadataAdapter = new RewriteJavassistAdapter());
            } catch (Throwable e) {
                if (Reflections.log != null) {
                    Reflections.log.warn("could not create JavassistAdapter, using JavaReflectionAdapter", e);
                }
                return (metadataAdapter = new JavaReflectionAdapter());
            }
        }
    }

    /**
     * sets the metadata adapter used to fetch metadata from classes
     */
    @Override
    public ConfigurationBuilder setMetadataAdapter(final MetadataAdapter metadataAdapter) {
        this.metadataAdapter = metadataAdapter;
        return this;
    }

    @Override
    public Predicate<String> getInputsFilter() {
        return inputsFilter;
    }

    @Override
    public void setInputsFilter(Predicate<String> inputsFilter) {
        this.inputsFilter = inputsFilter;
    }

    @Override
    public ConfigurationBuilder filterInputsBy(Predicate<String> inputsFilter) {
        this.inputsFilter = inputsFilter;
        return this;
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
