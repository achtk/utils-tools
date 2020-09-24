package com.chua.utils.tools.common.filefilter;

import java.io.File;
import java.io.FileFilter;

/**
 * 永真
 * @author CH
 */
public class TrueFileFilter implements FileFilter {
    /**
     *
     */
    public static final FileFilter INSTANCE = new TrueFileFilter();
    public static final FileFilter TRUE = INSTANCE;

    @Override
    public boolean accept(File pathname) {
        return true;
    }
}
