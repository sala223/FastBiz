package com.fastbiz.boot.main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathMatcher{

    private static final Pattern VARIABLE_PATTERN       = Pattern.compile("\\{[^/]+?\\}");

    public static final String   DEFAULT_PATH_SEPARATOR = "/";

    private String               pathSeparator          = DEFAULT_PATH_SEPARATOR;

    public void setPathSeparator(String pathSeparator){
        this.pathSeparator = (pathSeparator != null ? pathSeparator : DEFAULT_PATH_SEPARATOR);
    }

    public boolean isPattern(String path){
        return (path.indexOf('*') != -1 || path.indexOf('?') != -1);
    }

    public String getPatternStartPath(String pattern){
        if (pattern == null) {
            throw new NullPointerException("pattern cannot be null");
        }
        String[] pattDirs = tokenizeToStringArray(pattern, this.pathSeparator, false, true);
        StringBuilder builder = new StringBuilder();
        for (String patternDir : pattDirs) {
            if (isPattern(patternDir)) {
                break;
            } else {
                builder.append(patternDir);
                builder.append(pathSeparator);
            }
        }
        return builder.toString();
    }

    public boolean match(String pattern, String path){
        return doMatch(pattern, path, true);
    }

    protected boolean doMatch(String pattern, String path, boolean fullMatch){
        if (path.startsWith(this.pathSeparator) != pattern.startsWith(this.pathSeparator)) {
            return false;
        }
        String[] pattDirs = tokenizeToStringArray(pattern, this.pathSeparator, true, true);
        String[] pathDirs = tokenizeToStringArray(path, this.pathSeparator, true, true);
        int pattIdxStart = 0;
        int pattIdxEnd = pattDirs.length - 1;
        int pathIdxStart = 0;
        int pathIdxEnd = pathDirs.length - 1;
        while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
            String patDir = pattDirs[pattIdxStart];
            if ("**".equals(patDir)) {
                break;
            }
            if (!matchStrings(patDir, pathDirs[pathIdxStart])) {
                return false;
            }
            pattIdxStart++;
            pathIdxStart++;
        }
        if (pathIdxStart > pathIdxEnd) {
            if (pattIdxStart > pattIdxEnd) {
                return (pattern.endsWith(this.pathSeparator) ? path.endsWith(this.pathSeparator) : !path
                                .endsWith(this.pathSeparator));
            }
            if (!fullMatch) {
                return true;
            }
            if (pattIdxStart == pattIdxEnd && pattDirs[pattIdxStart].equals("*") && path.endsWith(this.pathSeparator)) {
                return true;
            }
            for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
                if (!pattDirs[i].equals("**")) {
                    return false;
                }
            }
            return true;
        } else if (pattIdxStart > pattIdxEnd) {
            return false;
        } else if (!fullMatch && "**".equals(pattDirs[pattIdxStart])) {
            return true;
        }
        while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
            String patDir = pattDirs[pattIdxEnd];
            if (patDir.equals("**")) {
                break;
            }
            if (!matchStrings(patDir, pathDirs[pathIdxEnd])) {
                return false;
            }
            pattIdxEnd--;
            pathIdxEnd--;
        }
        if (pathIdxStart > pathIdxEnd) {
            for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
                if (!pattDirs[i].equals("**")) {
                    return false;
                }
            }
            return true;
        }
        while (pattIdxStart != pattIdxEnd && pathIdxStart <= pathIdxEnd) {
            int patIdxTmp = -1;
            for (int i = pattIdxStart + 1; i <= pattIdxEnd; i++) {
                if (pattDirs[i].equals("**")) {
                    patIdxTmp = i;
                    break;
                }
            }
            if (patIdxTmp == pattIdxStart + 1) {
                pattIdxStart++;
                continue;
            }
            int patLength = (patIdxTmp - pattIdxStart - 1);
            int strLength = (pathIdxEnd - pathIdxStart + 1);
            int foundIdx = -1;
            strLoop:
            for (int i = 0; i <= strLength - patLength; i++) {
                for (int j = 0; j < patLength; j++) {
                    String subPat = pattDirs[pattIdxStart + j + 1];
                    String subStr = pathDirs[pathIdxStart + i + j];
                    if (!matchStrings(subPat, subStr)) {
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
        for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
            if (!pattDirs[i].equals("**")) {
                return false;
            }
        }
        return true;
    }

    private boolean matchStrings(String pattern, String str){
        PathStringMatcher matcher = new PathStringMatcher(pattern, str);
        return matcher.matchStrings();
    }

    public String combine(String pattern1, String pattern2){
        if (!hasText(pattern1) && !hasText(pattern2)) {
            return "";
        } else if (!hasText(pattern1)) {
            return pattern2;
        } else if (!hasText(pattern2)) {
            return pattern1;
        } else if (!pattern1.contains("{") && match(pattern1, pattern2)) {
            return pattern2;
        } else if (pattern1.endsWith("/*")) {
            if (pattern2.startsWith("/")) {
                return pattern1.substring(0, pattern1.length() - 1) + pattern2.substring(1);
            } else {
                return pattern1.substring(0, pattern1.length() - 1) + pattern2;
            }
        } else if (pattern1.endsWith("/**")) {
            if (pattern2.startsWith("/")) {
                return pattern1 + pattern2;
            } else {
                return pattern1 + "/" + pattern2;
            }
        } else {
            int dotPos1 = pattern1.indexOf('.');
            if (dotPos1 == -1) {
                if (pattern1.endsWith("/") || pattern2.startsWith("/")) {
                    return pattern1 + pattern2;
                } else {
                    return pattern1 + "/" + pattern2;
                }
            }
            String fileName1 = pattern1.substring(0, dotPos1);
            String extension1 = pattern1.substring(dotPos1);
            String fileName2;
            String extension2;
            int dotPos2 = pattern2.indexOf('.');
            if (dotPos2 != -1) {
                fileName2 = pattern2.substring(0, dotPos2);
                extension2 = pattern2.substring(dotPos2);
            } else {
                fileName2 = pattern2;
                extension2 = "";
            }
            String fileName = fileName1.endsWith("*") ? fileName2 : fileName1;
            String extension = extension1.startsWith("*") ? extension2 : extension1;
            return fileName + extension;
        }
    }

    public Comparator<String> getPatternComparator(String path){
        return new AntPatternComparator(path);
    }

    public static int countOccurrencesOf(String str, String sub){
        if (str == null || sub == null || str.length() == 0 || sub.length() == 0) {
            return 0;
        }
        int count = 0;
        int pos = 0;
        int idx;
        while ((idx = str.indexOf(sub, pos)) != -1) {
            ++count;
            pos = idx + sub.length();
        }
        return count;
    }

    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens){
        if (str == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return tokens.toArray(new String[tokens.size()]);
    }

    public static boolean hasText(CharSequence str){
        if (str == null || str.length() < 0) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static class AntPatternComparator implements Comparator<String>{

        private final String path;

        private AntPatternComparator(String path) {
            this.path = path;
        }

        public int compare(String pattern1, String pattern2){
            if (pattern1 == null && pattern2 == null) {
                return 0;
            } else if (pattern1 == null) {
                return 1;
            } else if (pattern2 == null) {
                return -1;
            }
            boolean pattern1EqualsPath = pattern1.equals(path);
            boolean pattern2EqualsPath = pattern2.equals(path);
            if (pattern1EqualsPath && pattern2EqualsPath) {
                return 0;
            } else if (pattern1EqualsPath) {
                return -1;
            } else if (pattern2EqualsPath) {
                return 1;
            }
            int wildCardCount1 = getWildCardCount(pattern1);
            int wildCardCount2 = getWildCardCount(pattern2);
            int bracketCount1 = countOccurrencesOf(pattern1, "{");
            int bracketCount2 = countOccurrencesOf(pattern2, "{");
            int totalCount1 = wildCardCount1 + bracketCount1;
            int totalCount2 = wildCardCount2 + bracketCount2;
            if (totalCount1 != totalCount2) {
                return totalCount1 - totalCount2;
            }
            int pattern1Length = getPatternLength(pattern1);
            int pattern2Length = getPatternLength(pattern2);
            if (pattern1Length != pattern2Length) {
                return pattern2Length - pattern1Length;
            }
            if (wildCardCount1 < wildCardCount2) {
                return -1;
            } else if (wildCardCount2 < wildCardCount1) {
                return 1;
            }
            if (bracketCount1 < bracketCount2) {
                return -1;
            } else if (bracketCount2 < bracketCount1) {
                return 1;
            }
            return 0;
        }

        private int getWildCardCount(String pattern){
            if (pattern.endsWith(".*")) {
                pattern = pattern.substring(0, pattern.length() - 2);
            }
            return countOccurrencesOf(pattern, "*");
        }

        private int getPatternLength(String pattern){
            Matcher m = VARIABLE_PATTERN.matcher(pattern);
            return m.replaceAll("#").length();
        }
    }
}
