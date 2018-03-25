package com.dom4j.sirius.core;

import com.dom4j.sirius.util.DomGetter;
import javafx.scene.Parent;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;

/**
 * 读取XML文件
 * @author OovEver
 * 2018/3/25 21:15
 */
public class loadXmlFile {
  static   Map<String, List<String>> p = new HashMap<>();
   static Map<String, String> parent = new HashMap<>();
    public static void main(String[] args) throws DocumentException {
        SAXReader sax=new SAXReader();//创建一个SAXReader对象
        File xmlFile=new File("E:\\git库\\华为项目\\architectureDesigner\\design\\org.eclipse.sirius.architecture.sample\\example.architecture");//根据指定的路径创建file对象
        Document document=sax.read(xmlFile);//获取document对象,如果文档无节点，则会抛出Exception提前结束
        Element root=document.getRootElement();//获取根节点
        listNodes(root);//从根节点开始遍历所有节点
    }
    /**
     * 获取XML中的信息
     */


    /**
     * 从指定节点开始,递归遍历所有子节点
     * @author oovever
     */
    public static void getNodes(Element node){
//        String parent=null;
////        父亲节点不为空
//        if(node.getParent()!=null){
//            if (node.getParent().attribute("type") != null && node.getParent().attribute("name") != null) {
//                parent = "type:" + node.getParent().attribute("type").getValue() + "name:" + node.getParent().attribute("type").getValue();
//            } else {
//                parent = node.getParent().getTextTrim();
//            }
//        }
//        if(p.get(parent)==null)
//        p.put(parent, new ArrayList<>());
        System.out.println("--------------------");
        //当前节点的名称、文本内容和属性
        System.out.println("当前节点名称："+node.getName());//当前节点名称
        System.out.println("当前节点的内容："+node.getTextTrim());//当前节点名称
        List<Attribute> listAttr =node.attributes();//当前节点的所有属性的list
        for(Attribute attr:listAttr){//遍历当前节点的所有属性
            String name=attr.getName();//属性名称
            String value=attr.getValue();//属性的值
            System.out.println("属性名称："+name+"属性值："+value);
        }
        String parentname = null;
        while (node.getParent()!=null&&node.getParent().attribute("type")!=null&&node.getParent().attribute("name")!=null){
            parentname += node.getParent().attribute("name").getValue() + "/";
        }
        if(node.getParent()!=null&&node.getParent().attribute("type")!=null&&node.getParent().attribute("name")!=null) {
            System.out.println("父亲" + node.getParent().attribute("type").getValue() + " " + node.getParent().attribute("name").getValue());
        }
        //递归遍历当前节点所有的子节点
        List<Element> listElement=node.elements();//所有一级子节点的list
        for(Element e:listElement){//遍历所有一级子节点
           getNodes(e);//递归
        }
    }
    /**
     * 遍历当前节点元素下面的所有(元素的)子节点
     *
     * @param node
     */
    public static void listNodes(Element node) {
        System.out.println("当前节点的名称：：" + node.getName());
        // 获取当前节点的所有属性节点
        List<Attribute> list = node.attributes();
        // 遍历属性节点
        for (Attribute attr : list) {
            System.out.println(attr.getText() + "-----" + attr.getName()
                    + "---" + attr.getValue());
        }

        if (!(node.getTextTrim().equals(""))) {
            System.out.println("文本内容：：：：" + node.getText());
        }

        // 当前节点下面子节点迭代器
        Iterator<Element> it = node.elementIterator();
        // 遍历
        while (it.hasNext()) {
            // 获取某个子节点对象
            Element e = it.next();
            // 对子节点进行遍历
            listNodes(e);
        }
    }
}
