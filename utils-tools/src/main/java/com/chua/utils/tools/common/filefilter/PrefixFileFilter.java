package com.chua.utils.tools.common.filefilter;

import com.chua.utils.tools.common.filecase.IOCase;

import java.io.File;
import java.io.FileFilter;

/**
 * 后缀
 *
 * @author CH
 */
public class PrefixFileFilter implements FileFilter {

    private String[] prefixes;

    private IOCase caseSensitivity = IOCase.SENSITIVE;

    public PrefixFileFilter(String[] prefixes) {
        this.prefixes = prefixes;
    }

    public PrefixFileFilter(String prefix) {
        this.prefixes = new String[]{prefix};
    }

    public PrefixFileFilter(final String prefix, final IOCase caseSensitivity) {
        if (prefix == null) {
            throw new IllegalArgumentException("The prefix must not be null");
        }
        this.prefixes = new String[]{prefix};
        this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
    }

    @Override
    public boolean accept(File pathname) {
        final String name = pathname.getName();
        for (final String prefix : this.prefixes) {
            if (caseSensitivity.checkStartsWith(name, prefix)) {
                return true;
            }
        }
        return false;
    }
}
