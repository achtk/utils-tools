package com.chua.utils.tools.matcher;


import com.chua.utils.tools.common.StringHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.chua.utils.tools.constant.SymbolConstant.*;

/**
 * ant 风格匹配
 *
 * @author CH
 * @since 1.0
 */
public class AntPathMatcher implements PathMatcher {
    /**
     * Default path separator: "/"
     */
    public static final String DEFAULT_PATH_SEPARATOR = SYMBOL_LEFT_SLASH;

    private static final int CACHE_TURNOFF_THRESHOLD = 65536;

    private static final char[] WILDCARD_CHARS = {SYMBOL_ASTERISK_CHAR, SYMBOL_QUESTION_CHAR, SYMBOL_LEFT_BIG_PARANTHESES_CHAR};


    private String pathSeparator;

    private PathSeparatorPatternCache pathSeparatorPatternCache;

    private boolean caseSensitive = true;

    private boolean trimTokens = false;

    private volatile Boolean cachePatterns;

    private final Map<String, String[]> tokenizedPatternCache = new ConcurrentHashMap<>(256);

    final Map<String, AntPathStringMatcher> stringMatcherCache = new ConcurrentHashMap<>(256);

    public AntPathMatcher() {
        this.pathSeparator = DEFAULT_PATH_SEPARATOR;
        this.pathSeparatorPatternCache = new PathSeparatorPatternCache(DEFAULT_PATH_SEPARATOR);
    }

    public AntPathMatcher(String pathSeparator) {
        this.pathSeparator = pathSeparator;
        this.pathSeparatorPatternCache = new PathSeparatorPatternCache(pathSeparator);
    }

    public void setPathSeparator(String pathSeparator) {
        this.pathSeparator = (pathSeparator != null ? pathSeparator : DEFAULT_PATH_SEPARATOR);
        this.pathSeparatorPatternCache = new PathSeparatorPatternCache(this.pathSeparator);
    }

    @Override
    public boolean isPattern(String path) {
        return (path.indexOf('*') != -1 || path.indexOf('?') != -1);
    }

    @Override
    public boolean match(String pattern, String path) {
        return doMatch(pattern, path, true, null);
    }

    @Override
    public boolean matchStart(String pattern, String path) {
        return doMatch(pattern, path, false, null);
    }

    /**
     * 数据匹配
     *
     * @param pattern              表达式
     * @param path                 路径
     * @param fullMatch            全词匹配
     * @param uriTemplateVariables 模板
     * @return boolean
     */
    protected Boolean doMatch(final String pattern, final String path, final boolean fullMatch, final Map<String, String> uriTemplateVariables) {
        String[] patternDirs = getPatternDirs(path, fullMatch, pattern);
        if (null == patternDirs) {
            return false;
        }
        String[] pathDirs = tokenizePath(path);

        int pattIdxStart = 0;
        int pattIdxEnd = patternDirs.length - 1;
        int pathIdxStart = 0;
        int pathIdxEnd = pathDirs.length - 1;

        // Match all elements up to the first **
        boolean element = matchAllElement(patternDirs, pathDirs, pattIdxEnd, pattIdxStart, pathIdxEnd, pathIdxStart, uriTemplateVariables);
        if (!element) {
            return false;
        }

        if (pathIdxStart > pathIdxEnd) {
            return isMatcher(pattIdxStart, pattIdxEnd, pattern, path, patternDirs, fullMatch);
        } else if (pattIdxStart > pattIdxEnd) {
            return false;
        } else if (!fullMatch && SYMBOL_ASTERISKS.equals(patternDirs[pattIdxStart])) {
            return true;
        }

        // up to last '**'
        while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
            String partDir = patternDirs[pattIdxEnd];
            if (SYMBOL_ASTERISKS.equals(partDir)) {
                break;
            }
            if (!matchStrings(partDir, pathDirs[pathIdxEnd], uriTemplateVariables)) {
                return false;
            }
            pattIdxEnd--;
            pathIdxEnd--;
        }

        if (pathIdxStart > pathIdxEnd) {
            for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
                if (!SYMBOL_ASTERISKS.equals(patternDirs[i])) {
                    return false;
                }
            }
            return true;
        }

        boolean loop = doWithLoop(
                pattIdxStart,
                pattIdxEnd,
                pathIdxStart,
                pathIdxEnd,
                patternDirs,
                pathDirs,
                uriTemplateVariables);

        if (!loop) {
            return loop;
        }
        for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
            if (!SYMBOL_ASTERISKS.equals(patternDirs[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * 循环查询
     *
     * @param pattIdxStart         pattIdxStart
     * @param pattIdxEnd           pattIdxEnd
     * @param pathIdxStart         pathIdxStart
     * @param pathIdxEnd           pathIdxEnd
     * @param patternDirs          patternDirs
     * @param pathDirs             pathDirs
     * @param uriTemplateVariables uriTemplateVariables
     * @return boolean
     */
    private boolean doWithLoop(
            int pattIdxStart,
            int pattIdxEnd,
            int pathIdxStart,
            int pathIdxEnd,
            String[] patternDirs,
            String[] pathDirs,
            Map<String, String> uriTemplateVariables) {
        while (pattIdxStart != pattIdxEnd && pathIdxStart <= pathIdxEnd) {
            int patIdxTmp = -1;
            for (int i = pattIdxStart + 1; i <= pattIdxEnd; i++) {
                if (SYMBOL_ASTERISKS.equals(patternDirs[i])) {
                    patIdxTmp = i;
                    break;
                }
            }
            if (patIdxTmp == pattIdxStart + 1) {
                // '**/**' situation, so skip one
                pattIdxStart++;
                continue;
            }
            // Find the pattern between padIdxStart & padIdxTmp in str between
            // strIdxStart & strIdxEnd
            int patLength = (patIdxTmp - pattIdxStart - 1);
            int strLength = (pathIdxEnd - pathIdxStart + 1);
            int foundIdx = -1;

            strLoop:
            for (int i = 0; i <= strLength - patLength; i++) {
                for (int j = 0; j < patLength; j++) {
                    String subPat = patternDirs[pattIdxStart + j + 1];
                    String subStr = pathDirs[pathIdxStart + i + j];
                    if (!matchStrings(subPat, subStr, uriTemplateVariables)) {
                        continue strLoop;
                    }
                }
                foundIdx = pathIdxStart + i;
                break;
            }

            if (foundIdx == -1) {
                return false;
            }

            pattIdxStart = patIdxTmp;
            pathIdxStart = foundIdx + patLength;
        }
        return true;
    }

    /**
     * 匹配
     *
     * @param pattIdxStart pattIdxStart
     * @param pattIdxEnd   pattIdxEnd
     * @param pattern      pattern
     * @param path         path
     * @param patternDirs  patternDirs
     * @param fullMatch    fullMatch
     * @return Boolean
     */
    private Boolean isMatcher(int pattIdxStart, int pattIdxEnd, String pattern, String path, String[] patternDirs, boolean fullMatch) {
        if (pattIdxStart > pattIdxEnd) {
            return (pattern.endsWith(this.pathSeparator) == path.endsWith(this.pathSeparator));
        }
        if (!fullMatch) {
            return true;
        }
        if (pattIdxStart == pattIdxEnd && SYMBOL_ASTERISK.equals(patternDirs[pattIdxStart]) && path.endsWith(this.pathSeparator)) {
            return true;
        }
        for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
            if (!SYMBOL_ASTERISKS.equals(patternDirs[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Match all elements up to the first **
     *
     * @param patternDirs          patternDirs
     * @param pathDirs             pathDirs
     * @param pattIdxEnd           pattIdxEnd
     * @param pattIdxStart         pattIdxStart
     * @param pathIdxEnd           pathIdxEnd
     * @param pathIdxStart         pathIdxStart
     * @param uriTemplateVariables uriTemplateVariables
     * @return boolean
     */
    private boolean matchAllElement(String[] patternDirs, String[] pathDirs, int pattIdxEnd, int pattIdxStart, int pathIdxEnd, int pathIdxStart, Map<String, String> uriTemplateVariables) {
        while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
            String pattDir = patternDirs[pattIdxStart];
            if (SYMBOL_ASTERISKS.equals(pattDir)) {
                break;
            }
            if (!matchStrings(pattDir, pathDirs[pathIdxStart], uriTemplateVariables)) {
                return false;
            }
            pattIdxStart++;
            pathIdxStart++;
        }
        return true;
    }

    /**
     * 路径匹配
     *
     * @param pattern   表达式
     * @param path      路径
     * @param fullMatch 全词匹配
     * @return 匹配
     */
    private String[] getPatternDirs(String path, boolean fullMatch, String pattern) {
        if (path.startsWith(this.pathSeparator) != pattern.startsWith(this.pathSeparator)) {
            return null;
        }

        String[] patternDirs = tokenizePattern(pattern);
        if (fullMatch && this.caseSensitive && !isPotentialMatch(path, patternDirs)) {
            return null;
        }
        return patternDirs;
    }

    /**
     * 忽略匹配
     *
     * @param path        匹配路径
     * @param patternDirs 匹配目录
     * @return boolean
     */
    private boolean isPotentialMatch(String path, String[] patternDirs) {
        if (!this.trimTokens) {
            int pos = 0;
            for (String pattDir : patternDirs) {
                int skipped = skipSeparator(path, pos, this.pathSeparator);
                pos += skipped;
                skipped = skipSegment(path, pos, pattDir);
                if (skipped < pattDir.length()) {
                    return (skipped > 0 || (pattDir.length() > 0 && isWildcardChar(pattDir.charAt(0))));
                }
                pos += skipped;
            }
        }
        return true;
    }

    /**
     * 跳过通配符
     *
     * @param path   路径
     * @param pos    索引
     * @param prefix 前缀
     * @return int
     */
    private int skipSegment(String path, int pos, String prefix) {
        int skipped = 0;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (isWildcardChar(c)) {
                return skipped;
            }
            int currPos = pos + skipped;
            if (currPos >= path.length()) {
                return 0;
            }
            if (c == path.charAt(currPos)) {
                skipped++;
            }
        }
        return skipped;
    }

    /**
     * 跳过分隔符
     *
     * @param path      路径
     * @param pos       索引
     * @param separator 分隔符
     * @return int
     */
    private int skipSeparator(String path, int pos, String separator) {
        int skipped = 0;
        while (path.startsWith(separator, pos + skipped)) {
            skipped += separator.length();
        }
        return skipped;
    }

    /**
     * 是否是正则
     *
     * @param source 元数据
     * @return boolean
     */
    private boolean isWildcardChar(char source) {
        for (char candidate : WILDCARD_CHARS) {
            if (source == candidate) {
                return true;
            }
        }
        return false;
    }

    /**
     * 正则转数组
     *
     * @param pattern 正则
     * @return String[]
     */
    protected String[] tokenizePattern(String pattern) {
        String[] tokenized = null;
        Boolean cachePatterns = this.cachePatterns;
        if (cachePatterns == null || cachePatterns) {
            tokenized = this.tokenizedPatternCache.get(pattern);
        }
        if (tokenized == null) {
            tokenized = tokenizePath(pattern);
            if (cachePatterns == null && this.tokenizedPatternCache.size() >= CACHE_TURNOFF_THRESHOLD) {
                deactivatePatternCache();
                return tokenized;
            }
            if (cachePatterns == null || cachePatterns) {
                this.tokenizedPatternCache.put(pattern, tokenized);
            }
        }
        return tokenized;
    }

    /**
     * 路径转数组
     *
     * @param path 路径
     * @return String[]
     */
    protected String[] tokenizePath(String path) {
        return StringHelper.tokenizeToStringArray(path, this.pathSeparator, this.trimTokens, true);
    }

    /**
     * 匹配字符串
     *
     * @param pattern              表达式
     * @param str                  字符串
     * @param uriTemplateVariables 模版
     * @return boolean
     */
    private Boolean matchStrings(final String pattern, final String str, final Map<String, String> uriTemplateVariables) {
        return getStringMatcher(pattern).matchStrings(str, uriTemplateVariables);
    }

    /**
     * 生成 AntPathStringMatcher
     *
     * @param pattern 正则
     * @return com.chua.utils.tools.matcher.AntPathStringMatcher
     */
    protected AntPathStringMatcher getStringMatcher(final String pattern) {
        AntPathStringMatcher matcher = null;
        Boolean cachePatterns = this.cachePatterns;
        if (cachePatterns == null || cachePatterns) {
            matcher = this.stringMatcherCache.get(pattern);
        }
        if (matcher == null) {
            matcher = new AntPathStringMatcher(pattern, this.caseSensitive);
            if (cachePatterns == null && this.stringMatcherCache.size() >= CACHE_TURNOFF_THRESHOLD) {
                deactivatePatternCache();
                return matcher;
            }
            if (cachePatterns == null || cachePatterns) {
                this.stringMatcherCache.put(pattern, matcher);
            }
        }
        return matcher;
    }

    /**
     * 清空缓存
     */
    private void deactivatePatternCache() {
        this.cachePatterns = false;
        this.tokenizedPatternCache.clear();
        this.stringMatcherCache.clear();
    }

    /**
     * AntPathStringMatcher 对象
     */
    protected static class AntPathStringMatcher {

        private static final Pattern GLOB_PATTERN = Pattern.compile("\\?|\\*|\\{((?:\\{[^/]+?\\}|[^/{}]|\\\\[{}])+?)\\}");

        private static final String DEFAULT_VARIABLE_PATTERN = "(.*)";

        private final Pattern pattern;

        private final List<String> variableNames = new LinkedList<String>();

        public AntPathStringMatcher(String pattern) {
            this(pattern, true);
        }

        public AntPathStringMatcher(String pattern, boolean caseSensitive) {
            StringBuilder patternBuilder = new StringBuilder();
            Matcher matcher = GLOB_PATTERN.matcher(pattern);
            int end = 0;
            while (matcher.find()) {
                patternBuilder.append(quote(pattern, end, matcher.start()));
                String match = matcher.group();
                if ("?".equals(match)) {
                    patternBuilder.append('.');
                } else if ("*".equals(match)) {
                    patternBuilder.append(".*");
                } else if (match.startsWith("{") && match.endsWith("}")) {
                    int colonIdx = match.indexOf(':');
                    if (colonIdx == -1) {
                        patternBuilder.append(DEFAULT_VARIABLE_PATTERN);
                        this.variableNames.add(matcher.group(1));
                    } else {
                        String variablePattern = match.substring(colonIdx + 1, match.length() - 1);
                        patternBuilder.append('(');
                        patternBuilder.append(variablePattern);
                        patternBuilder.append(')');
                        String variableName = match.substring(1, colonIdx);
                        this.variableNames.add(variableName);
                    }
                }
                end = matcher.end();
            }
            patternBuilder.append(quote(pattern, end, pattern.length()));
            this.pattern = (caseSensitive ? Pattern.compile(patternBuilder.toString()) :
                    Pattern.compile(patternBuilder.toString(), Pattern.CASE_INSENSITIVE));
        }

        private String quote(String s, int start, int end) {
            if (start == end) {
                return "";
            }
            return Pattern.quote(s.substring(start, end));
        }

        /**
         * @param str
         * @param uriTemplateVariables
         * @return boolean
         */
        public Boolean matchStrings(String str, Map<String, String> uriTemplateVariables) {
            Matcher matcher = this.pattern.matcher(str);
            if (matcher.matches()) {
                if (uriTemplateVariables != null) {
                    if (this.variableNames.size() != matcher.groupCount()) {
                        throw new IllegalArgumentException("");
                    }
                    for (int i = 1; i <= matcher.groupCount(); i++) {
                        String name = this.variableNames.get(i - 1);
                        String value = matcher.group(i);
                        uriTemplateVariables.put(name, value);
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     *
     */
    private static class PathSeparatorPatternCache {

        private final String endsOnWildCard;

        private final String endsOnDoubleWildCard;

        public PathSeparatorPatternCache(String pathSeparator) {
            this.endsOnWildCard = pathSeparator + "*";
            this.endsOnDoubleWildCard = pathSeparator + "**";
        }

        public String getEndsOnWildCard() {
            return this.endsOnWildCard;
        }

        public String getEndsOnDoubleWildCard() {
            return this.endsOnDoubleWildCard;
        }
    }

}
