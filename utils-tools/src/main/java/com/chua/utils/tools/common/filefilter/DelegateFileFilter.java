package com.chua.utils.tools.common.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

/**
 * DelegateFileFilter
 * @author CH
 */
public class DelegateFileFilter implements FileFilter {

    /** The Filename filter */
    private FilenameFilter filenameFilter;
    /** The File filter */
    private FileFilter fileFilter;

    public DelegateFileFilter(final FilenameFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("The FilenameFilter must not be null");
        }
        this.filenameFilter = filter;
        this.fileFilter = null;
    }

    public DelegateFileFilter(final FileFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("The FileFilter must not be null");
        }
        this.fileFilter = filter;
        this.filenameFilter = null;
    }

    @Override
    public boolean accept(File pathname) {
        if (fileFilter != null) {
            return fileFilter.accept(pathname);
        } else {
            return accept(new File(pathname.getParentFile(), pathname.getName()));
        }
    }
}
