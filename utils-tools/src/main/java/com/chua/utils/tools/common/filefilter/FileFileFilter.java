package com.chua.utils.tools.common.filefilter;

import java.io.File;
import java.io.FileFilter;

/**
 * 目录
 *
 * @author CH
 */
public class FileFileFilter implements FileFilter {

    public static final FileFilter FILE = new FileFileFilter();

    @Override
    public boolean accept(File pathname) {
        return pathname.isFile();
    }
}
