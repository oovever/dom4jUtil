package com.dom4j.sirius.util;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.Objects;

/**
 * @author OovEver
 * 2018/3/24 15:13
 * 格式化XML文件
 */
public class DomWrite {
    /**
     * Dom4j通过XMLWriter将Document对象表示的XML树写入指定的文件，并使用OutputFormat格式对象指定写入的风格和编码方法。调用OutputFormat.createPrettyPrint()方法可以获得一个默认的pretty print风格的格式对象。
     * @param document document对象
     * @param filepath 文件路径
     * @return 判断是否写入成功
     */
    public static boolean doc2XmlFile(Document document, String filepath) {
        boolean flag = true;
        try {
            XMLWriter writer = new XMLWriter(new OutputStreamWriter(
                    new FileOutputStream(filepath), "UTF-8"));
            writer.write(document);
            writer.close();
        } catch (Exception ex) {
            flag = false;
            ex.printStackTrace();
        }
        return flag;
    }

    /**
     * 通过 XMLWriter 输出XML ducument
     * @param outputStream 给一个输出流就能往 输出流里输出XML文件内容啦
     * @param document document对象
     * @param format 可以指定任意的格式
     * @throws IOException 读取异常
     */
    public static void writeDocument(OutputStream outputStream, Document document, OutputFormat format)  {
        try {
            XMLWriter writer =  new XMLWriter(outputStream, format);
            write(writer, document);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param writer XMLWriter对象
     * @param document document对象
     */
    private static void write(XMLWriter writer, Document document){
        try {
            if (Objects.nonNull(writer)) {
                writer.write(document);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                try {
                    // 一定要记得flush 和 close
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 利用 OutputFormat得到美化的格式
     * 通过 XMLWriter 输出 Document
     * @param filePath 文件路径
     * @param document document对象
     */
    public static void writeDocument(String filePath, Document document) {
        OutputFormat format = OutputFormat.createPrettyPrint();
        try {
            XMLWriter writer = new XMLWriter(new FileWriter(filePath), format);
            write(writer, document);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
