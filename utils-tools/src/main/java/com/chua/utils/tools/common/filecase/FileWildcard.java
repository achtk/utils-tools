package com.chua.utils.tools.common.filecase;

import java.util.ArrayList;
import java.util.Stack;

import static com.chua.utils.tools.constant.NumberConstant.INDEX_NOT_FOUND;
import static com.chua.utils.tools.constant.SymbolConstant.*;

/**
 * 通配符
 *
 * @author CH
 */
public class FileWildcard {
    /**
     * @param filename
     * @param wildcardMatcher
     * @param caseSensitivity
     * @return
     */
    public static boolean wildcardMatch(final String filename, final String wildcardMatcher, IOCase caseSensitivity) {
        if (filename == null && wildcardMatcher == null) {
            return true;
        }
        if (filename == null || wildcardMatcher == null) {
            return false;
        }
        if (caseSensitivity == null) {
            caseSensitivity = IOCase.SENSITIVE;
        }
        final String[] wcs = splitOnTokens(wildcardMatcher);
        boolean anyChars = false;
        int textIdx = 0;
        int wcsIdx = 0;
        final Stack<int[]> backtrack = new Stack<>();

        // loop around a backtrack stack, to handle complex * matching
        do {
            if (backtrack.size() > 0) {
                final int[] array = backtrack.pop();
                wcsIdx = array[0];
                textIdx = array[1];
                anyChars = true;
            }

            // loop whilst tokens and text left to process
            while (wcsIdx < wcs.length) {

                if ("?".equals(wcs[wcsIdx])) {
                    // ? so move to next text char
                    textIdx++;
                    if (textIdx > filename.length()) {
                        break;
                    }
                    anyChars = false;

                } else if ("*".equals(wcs[wcsIdx])) {
                    // set any chars status
                    anyChars = true;
                    if (wcsIdx == wcs.length - 1) {
                        textIdx = filename.length();
                    }

                } else {
                    // matching text token
                    if (anyChars) {
                        // any chars then try to locate text token
                        textIdx = caseSensitivity.checkIndexOf(filename, textIdx, wcs[wcsIdx]);
                        if (textIdx == INDEX_NOT_FOUND) {
                            // token not found
                            break;
                        }
                        final int repeat = caseSensitivity.checkIndexOf(filename, textIdx + 1, wcs[wcsIdx]);
                        if (repeat >= 0) {
                            backtrack.push(new int[]{wcsIdx, repeat});
                        }
                    } else {
                        // matching from current position
                        if (!caseSensitivity.checkRegionMatches(filename, textIdx, wcs[wcsIdx])) {
                            // couldnt match token
                            break;
                        }
                    }

                    // matched text token, move text index to end of matched token
                    textIdx += wcs[wcsIdx].length();
                    anyChars = false;
                }

                wcsIdx++;
            }

            // full match
            if (wcsIdx == wcs.length && textIdx == filename.length()) {
                return true;
            }

        } while (backtrack.size() > 0);

        return false;
    }

    static String[] splitOnTokens(final String text) {
        // used by wildcardMatch
        // package level so a unit test may run on this

        if (text.indexOf(SYMBOL_QUESTION_CHAR) == INDEX_NOT_FOUND && text.indexOf(SYMBOL_ASTERISK_CHAR) == INDEX_NOT_FOUND) {
            return new String[]{text};
        }

        final char[] array = text.toCharArray();
        final ArrayList<String> list = new ArrayList<>();
        final StringBuilder buffer = new StringBuilder();
        char prevChar = 0;
        for (final char ch : array) {
            if (ch == SYMBOL_QUESTION_CHAR || ch == SYMBOL_ASTERISK_CHAR) {
                if (buffer.length() != 0) {
                    list.add(buffer.toString());
                    buffer.setLength(0);
                }
                if (ch == SYMBOL_QUESTION_CHAR) {
                    list.add(SYMBOL_QUESTION);
                } else if (prevChar != SYMBOL_ASTERISK_CHAR) {
                    // ch == '*' here; check if previous char was '*'
                    list.add(SYMBOL_ASTERISK);
                }
            } else {
                buffer.append(ch);
            }
            prevChar = ch;
        }
        if (buffer.length() != 0) {
            list.add(buffer.toString());
        }

        return list.toArray(new String[list.size()]);
    }
}
