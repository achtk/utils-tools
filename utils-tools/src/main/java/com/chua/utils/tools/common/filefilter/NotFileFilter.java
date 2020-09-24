package com.chua.utils.tools.common.filefilter;

import java.io.File;
import java.io.FileFilter;

/**
 * 非文件
 * @author CH
 */
public class NotFileFilter implements FileFilter {

    private final FileFilter filter;

    public NotFileFilter(final FileFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("The filter must not be null");
        }
        this.filter = filter;
    }
    @Override
    public boolean accept(File pathname) {
        return !pathname.isFile();
    }
}
