package com.dom4j.sirius.util;

/**
 * @author OovEver
 * 2018/3/28 14:48
 */
public class RegexMatches {
    /**
     * 过滤@ownedElement字符串
     * @param source 要处理的字符串
     * @param replace 正则表达式
     * @return 返回正则表达式处理结果
     */
    public static String regexMatchOwnedElement(String source,String replace) {
        String target=source.replaceAll(replace, "");
        return target;
    }

}
