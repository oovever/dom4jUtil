package com.dom4j.sirius.util;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
/**
 * @author OovEver
 * 2018/3/24 15:04
 */
public class DomGetter {

    /**
     * 从文件获取DOM
     * @param fileName 文件名称
     * @return 返回一个Document对象，这个对象代表了整个XML文档，用于各种Dom运算
     */
    public static Document getDomFromFile(File fileName) {
        if (!fileName.exists()) {
            return null;
        }
        try {
            /**
             * 当然SAXReader 不仅仅能读file
             */
            return new SAXReader().read(fileName);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从URL获取DOM
     * @param url url地址
     * @return 返回一个Document对象，这个对象代表了整个XML文档，用于各种Dom运算
     */
    public static Document getDomFromURL(URL url) {
        if (null == url) {
            return null;
        }
        try {
            return new SAXReader().read(url);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 从imputStream中获取文件
     * @param inputStream 输入流
     * @return 返回一个Document对象，这个对象代表了整个XML文档，用于各种Dom运算
     */
    public static Document getDomFromInputStream(InputStream inputStream) {
        if (null == inputStream) {
            return null;
        }
        try {
            return new SAXReader().read(inputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从reader中获取文件
     * @param reader reader对象
     * @return 返回一个Document对象，这个对象代表了整个XML文档，用于各种Dom运算
     */
    public static Document getDomFromReader(Reader reader) {
        if (null == reader) {
            return null;
        }
        try {
            return new SAXReader().read(reader);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * xml格式的字符串总获取DOM
     * @param xmlString xml字符串
     * @return 返回一个Document对象，这个对象代表了整个XML文档，用于各种Dom运算
     */
    public static Document getDomFromXMLStr(String xmlString) {
        if (null == xmlString || "" == xmlString) {
            return null;
        }
        try {
            return DocumentHelper.parseText(xmlString);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}
