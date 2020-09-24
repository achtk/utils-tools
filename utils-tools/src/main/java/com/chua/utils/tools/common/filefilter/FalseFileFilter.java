package com.chua.utils.tools.common.filefilter;

import java.io.File;
import java.io.FileFilter;

/**
 * 永假
 * @author CH
 */
public class FalseFileFilter implements FileFilter {
    /**
     *
     */
    public static final FileFilter INSTANCE = new FalseFileFilter();
    public static final FileFilter FALSE = INSTANCE;

    @Override
    public boolean accept(File pathname) {
        return false;
    }
}
