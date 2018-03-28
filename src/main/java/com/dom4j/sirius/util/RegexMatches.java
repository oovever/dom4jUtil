package com.dom4j.sirius.util;

/**
 * @author OovEver
 * 2018/3/28 14:48
 */
public class RegexMatches {
    public static String regexMatchOwnedElement(String source) {
        String target=source.replaceAll("@ownedElement", "");

        return target;
    }
    public static String regexMatchSymbol(String source) {
        String target=source.replaceAll("/", "");
        return target;
    }
}
