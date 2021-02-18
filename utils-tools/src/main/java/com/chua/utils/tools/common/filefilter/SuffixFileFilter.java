package com.chua.utils.tools.common.filefilter;

import com.chua.utils.tools.common.filecase.IOCase;

import java.io.File;
import java.io.FileFilter;

/**
 * 后缀
 *
 * @author CH
 */
public class SuffixFileFilter implements FileFilter {
    private final String[] suffixes;

    private IOCase caseSensitivity = IOCase.SENSITIVE;

    public SuffixFileFilter(String[] suffixes) {
        this.suffixes = suffixes;
    }

    public SuffixFileFilter(String suffix) {
        this.suffixes = new String[]{suffix};
    }

    public SuffixFileFilter(final String suffix, final IOCase caseSensitivity) {
        if (suffix == null) {
            throw new IllegalArgumentException("The prefix must not be null");
        }
        this.suffixes = new String[]{suffix};
        this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
    }

    @Override
    public boolean accept(File pathname) {
        final String name = pathname.getName();
        for (final String suffix : this.suffixes) {
            if (caseSensitivity.checkEndsWith(name, suffix)) {
                return true;
            }
        }
        return false;
    }
}
