package com.chua.utils.tools.common.filefilter;

import java.io.File;
import java.io.FileFilter;

/**
 * 目录
 *
 * @author CH
 */
public class DirectoryFileFilter implements FileFilter {

    public static final FileFilter DIRECTORY = new DirectoryFileFilter();

    public static final FileFilter INSTANCE = DIRECTORY;

    @Override
    public boolean accept(File pathname) {
        return pathname.isDirectory();
    }
}
