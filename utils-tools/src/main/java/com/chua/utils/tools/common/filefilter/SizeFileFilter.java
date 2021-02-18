package com.chua.utils.tools.common.filefilter;

import java.io.File;
import java.io.FileFilter;

/**
 * SizeFileFilter
 *
 * @author CH
 */
public class SizeFileFilter implements FileFilter {

    /**
     * The size threshold.
     */
    private final long size;
    /**
     * Whether the files accepted will be larger or smaller.
     */
    private final boolean acceptLarger;

    public SizeFileFilter(final long size) {
        this(size, true);
    }

    public SizeFileFilter(final long size, final boolean acceptLarger) {
        if (size < 0) {
            throw new IllegalArgumentException("The size must be non-negative");
        }
        this.size = size;
        this.acceptLarger = acceptLarger;
    }

    @Override
    public boolean accept(File pathname) {
        final boolean smaller = pathname.length() < size;
        return acceptLarger != smaller;
    }
}
