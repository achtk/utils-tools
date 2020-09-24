package com.chua.utils.tools.common.filecase;

import com.chua.utils.tools.common.FileHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * IOCase
 * @author CH
 */
@Getter
@AllArgsConstructor
public enum  IOCase {
    /**
     *
     */
    SENSITIVE("Sensitive", true),
    /**
     *
     */
    INSENSITIVE("Insensitive", false),
    /**
     *
     */
    SYSTEM("System", !FileHelper.isSystemWindows());
    private final String name;

    /**
     * The sensitivity flag.
     */
    private final transient boolean sensitive;

    /**
     * @param str
     * @param strStartIndex
     * @param search
     * @return
     */
    public int checkIndexOf(final String str, final int strStartIndex, final String search) {
        final int endIndex = str.length() - search.length();
        if (endIndex >= strStartIndex) {
            for (int i = strStartIndex; i <= endIndex; i++) {
                if (checkRegionMatches(str, i, search)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * @param str
     * @param strStartIndex
     * @param search
     * @return
     */
    public boolean checkRegionMatches(final String str, final int strStartIndex, final String search) {
        return str.regionMatches(!sensitive, strStartIndex, search, 0, search.length());
    }

    /**
     * 检查后缀
     *
     * @param str
     * @param end
     * @return
     */
    public boolean checkEndsWith(final String str, final String end) {
        final int endLen = end.length();
        return str.regionMatches(!sensitive, str.length() - endLen, end, 0, endLen);
    }

    /**
     * 检查前缀
     *
     * @param str
     * @param start
     * @return
     */
    public boolean checkStartsWith(final String str, final String start) {
        return str.regionMatches(!sensitive, 0, start, 0, start.length());
    }

    /**
     * 检查前缀
     *
     * @param str1
     * @param str2
     * @return
     */
    public boolean checkEquals(final String str1, final String str2) {
        if (str1 == null || str2 == null) {
            throw new NullPointerException("The strings must not be null");
        }
        return sensitive ? str1.equals(str2) : str1.equalsIgnoreCase(str2);
    }
}
