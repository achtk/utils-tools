package com.chua.utils.tools.common.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * and
 * @author CH
 */
public class AndFileFilter implements FileFilter {

    /** The list of file filters. */
    private List<FileFilter> fileFilters;

    public AndFileFilter() {
        this.fileFilters = new ArrayList<>();
    }

    public AndFileFilter(final List<FileFilter> fileFilters) {
        if (fileFilters == null) {
            this.fileFilters = new ArrayList<>();
        } else {
            this.fileFilters = new ArrayList<>(fileFilters);
        }
    }

    public AndFileFilter(final FileFilter filter1, final FileFilter filter2) {
        if (filter1 == null || filter2 == null) {
            throw new IllegalArgumentException("The filters must not be null");
        }
        this.fileFilters = new ArrayList<>(2);
        addFileFilter(filter1);
        addFileFilter(filter2);
    }

    public void addFileFilter(final FileFilter ioFileFilter) {
        this.fileFilters.add(ioFileFilter);
    }

    @Override
    public boolean accept(File pathname) {
        if (this.fileFilters.isEmpty()) {
            return false;
        }
        for (final FileFilter fileFilter : fileFilters) {
            if (!fileFilter.accept(pathname)) {
                return false;
            }
        }
        return true;
    }
}
