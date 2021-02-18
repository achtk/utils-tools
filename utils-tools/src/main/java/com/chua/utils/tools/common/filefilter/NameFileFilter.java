package com.chua.utils.tools.common.filefilter;

import com.chua.utils.tools.common.filecase.IOCase;

import java.io.File;
import java.io.FileFilter;

/**
 * 后缀
 *
 * @author CH
 */
public class NameFileFilter implements FileFilter {
    private final String[] names;

    private IOCase caseSensitivity = IOCase.SENSITIVE;

    public NameFileFilter(String[] names) {
        this.names = names;
    }

    public NameFileFilter(String name) {
        this.names = new String[]{name};
    }

    public NameFileFilter(final String name, final IOCase caseSensitivity) {
        if (name == null) {
            throw new IllegalArgumentException("The prefix must not be null");
        }
        this.names = new String[]{name};
        this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
    }

    @Override
    public boolean accept(File pathname) {
        final String name = pathname.getName();
        for (final String name2 : this.names) {
            if (caseSensitivity.checkEquals(name, name2)) {
                return true;
            }
        }
        return false;
    }
}
