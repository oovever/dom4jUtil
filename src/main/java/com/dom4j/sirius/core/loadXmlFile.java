package com.dom4j.sirius.core;

import com.dom4j.sirius.Entity.Element;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 读取XML文件
 * @author OovEver
 * 2018/3/25 21:15
 */
public class loadXmlFile {
//    当前编号，类型，名称
    static Map<String, Map<String, String>> elementName = new HashMap<>();
//    调用者  调用的类或者接口
    static Map<String, List<String>> callElement = new HashMap<>();
    static Map<String, List<String>> generalizationElement = new HashMap<>();
    static Map<String, List<String>> realizeElement = new HashMap<>();
    static List<Element> elements = new ArrayList<>();
    public static void main(String[] args) throws DocumentException {
        SAXReader         sax      =new SAXReader();//创建一个SAXReader对象
        File              xmlFile  =new File("E:\\git库\\华为项目\\architectureDesigner\\design\\org.eclipse.sirius.architecture.sample\\example.architecture");//根据指定的路径创建file对象
        Document          document =sax.read(xmlFile);//获取document对象,如果文档无节点，则会抛出Exception提前结束
        org.dom4j.Element root     =document.getRootElement();//获取根节点
        getNodes(root,"0");//从根节点开始遍历所有节点
    }
    /**
     * 获取XML中的信息
     */
    /**
     * 从指定节点开始,递归遍历所有子节点
     * @author oovever
     */
    public static void getNodes(org.dom4j.Element node, String current){

        Element element = new Element();
        System.out.println("--------------------");
        //当前节点的名称、文本内容和属性
        System.out.println("当前节点名称："+node.getName());//当前节点名称
        System.out.println("当前节点的内容："+node.getTextTrim());//当前节点名称
        List<Attribute> listAttr =node.attributes();//当前节点的所有属性的list
        element.setParent(current);
        System.out.println("第"+current+"个");
        for(Attribute attr:listAttr){//遍历当前节点的所有属性
//            类型，名称
            Map<String, String> typeAndName = new HashMap<>();
            String name=attr.getName();//属性名称
            String value=attr.getValue();//属性的值
            String typeOfAttr=null;
            String nameOfAttr = null;
            if(name.equals("type")){
                element.setType(value);
            }else if(name.equals("name")){
                element.setType(value);
            }else if(name.equals("association")){
                value = regexMatchOwnedElement(value);
                value = regexMatchSymbol(value);
                String []valueArr=value.split(" ");
                if (callElement.get(current) == null) {
                    callElement.put(current, new ArrayList<String>());
                }
                System.out.println("-------association----------");
                for(int i=0;i<valueArr.length;i++) {
                    valueArr[i] = "0" + valueArr[i];
                    callElement.get(current).add(valueArr[i]);
                }
            }
            if(typeOfAttr!=null&&nameOfAttr!=null){
                typeAndName.put(typeOfAttr, nameOfAttr);
                elementName.put(current, typeAndName);
            }
            System.out.println("属性名称："+name+"属性值："+value);
        }
        elements.add(element);
        List<org.dom4j.Element> listElement =node.elements();//所有一级子节点的list
        for(int i=0;i<listElement.size();i++){
            current=current+"."+String.valueOf(i);
            getNodes(listElement.get(i),current);//递归
            current = current.substring(0, current.length() - 2);
        }
    }

    public static String regexMatchOwnedElement(String source) {
        String target=source.replaceAll("@ownedElement", "");

        return target;
    }
    public static String regexMatchSymbol(String source) {
        String target=source.replaceAll("/", "");
        return target;
    }
}
