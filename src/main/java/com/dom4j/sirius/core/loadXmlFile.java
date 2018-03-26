package com.dom4j.sirius.core;

import com.dom4j.sirius.Entity.Element;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;

/**
 * 读取XML文件
 * @author OovEver
 * 2018/3/25 21:15
 */
public class loadXmlFile {
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
    public static void getNodes(org.dom4j.Element node, String parent){
        Element element = new Element();
        System.out.println("--------------------");
        //当前节点的名称、文本内容和属性
        System.out.println("当前节点名称："+node.getName());//当前节点名称
        System.out.println("当前节点的内容："+node.getTextTrim());//当前节点名称
        List<Attribute> listAttr =node.attributes();//当前节点的所有属性的list
        element.setParent(parent);
        System.out.println("第"+parent+"个");
        for(Attribute attr:listAttr){//遍历当前节点的所有属性
            String name=attr.getName();//属性名称
            String value=attr.getValue();//属性的值
            if(name.equals("type")){
                element.setType(value);
            }else if(name.equals("name")){
                element.setType(value);
            }
            System.out.println("属性名称："+name+"属性值："+value);
        }
        elements.add(element);
//        String parentname = null;
//        while (node.getParent()!=null&&node.getParent().attribute("type")!=null&&node.getParent().attribute("name")!=null){
//            parentname += node.getParent().attribute("name").getValue() + "/";
//        }
//        if(node.getParent()!=null&&node.getParent().attribute("type")!=null&&node.getParent().attribute("name")!=null) {
//            System.out.println("父亲" + node.getParent().attribute("type").getValue() + " " + node.getParent().attribute("name").getValue());
//        }
        //递归遍历当前节点所有的子节点
        List<org.dom4j.Element> listElement =node.elements();//所有一级子节点的list
        for(int i=0;i<listElement.size();i++){
            parent=parent+"."+String.valueOf(i);
            getNodes(listElement.get(i),parent);//递归
            parent = parent.substring(0, parent.length() - 2);
        }
//        for(Element e:listElement){//遍历所有一级子节点
//           getNodes(e,num);//递归
//        }
    }
    /**
     * 遍历当前节点元素下面的所有(元素的)子节点
     *
     * @param node
     */
    public static void listNodes(org.dom4j.Element node) {
        System.out.println("文档根元素是："+node.getName());
        //获得根元素的所有子元素
        List<org.dom4j.Element> childList =node.elements();
        for(int i=0;i<childList.size();i++) {
            org.dom4j.Element element =childList.get(i);
            System.out.println("第"+(i+1)+"个孩子节点是："+element.getName());
        }
    }
}
