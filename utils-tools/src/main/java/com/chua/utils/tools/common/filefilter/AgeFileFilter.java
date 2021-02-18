package com.chua.utils.tools.common.filefilter;

import com.chua.utils.tools.common.FileHelper;

import java.io.File;
import java.io.FileFilter;
import java.util.Date;

/**
 * AgeFileFilter
 *
 * @author CH
 */
public class AgeFileFilter implements FileFilter {

    /**
     * The cutoff time threshold.
     */
    private final long cutoff;
    /**
     * Whether the files accepted will be older or newer.
     */
    private final boolean acceptOlder;

    public AgeFileFilter(final long cutoff) {
        this(cutoff, true);
    }

    public AgeFileFilter(final long cutoff, final boolean acceptOlder) {
        this.acceptOlder = acceptOlder;
        this.cutoff = cutoff;
    }

    public AgeFileFilter(final Date cutoffDate) {
        this(cutoffDate, true);
    }

    public AgeFileFilter(final Date cutoffDate, final boolean acceptOlder) {
        this(cutoffDate.getTime(), acceptOlder);
    }

    public AgeFileFilter(final File cutoffReference) {
        this(cutoffReference, true);
    }

    public AgeFileFilter(final File cutoffReference, final boolean acceptOlder) {
        this(cutoffReference.lastModified(), acceptOlder);
    }

    @Override
    public boolean accept(File file) {
        final boolean newer = FileHelper.isFileNewer(file, cutoff);
        return acceptOlder != newer;
    }
}
