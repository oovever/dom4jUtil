package com.dom4j.sirius.core;


import com.dom4j.sirius.Common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import java.io.File;
import java.util.*;


import static com.dom4j.sirius.util.RegexMatches.regexMatchOwnedElement;

/**
 * 读取XML文件
 * @author OovEver
 * 2018/3/25 21:15
 */
@Slf4j
public class loadXmlFile {
//    当前编号，类型，名称
    static Map<String, Map<String, String>> elementName = new HashMap<>();
//    调用者  调用的类或者接口
    static Map<String, List<String>> callElement = new HashMap<>();
//    调用者 继承的类
    static Map<String, List<String>> generalizationElement = new HashMap<>();
    //    调用者 实现的接口
    static Map<String, List<String>> realizeElement = new HashMap<>();
    public static void main(String[] args) throws DocumentException {
        String file = "E:\\git库\\华为项目\\architectureDesigner\\design\\org.eclipse.sirius.architecture.sample\\example.architecture";
        getNodes(file);
        for (String key : elementName.keySet()) {
            System.out.print("当前节点"+key+"------");
            for (String key2 : elementName.get(key).keySet()) {
                System.out.println("类型"+key2+"-------------"+"名称"+elementName.get(key).get(key2));
            }
        }
        System.out.println("-----------call Element-------------");
        System.out.println(callElement);
        System.out.println("------------get Children-------------");
        System.out.println(getNode.getNodeChildren(elementName,"0.2.0"));
        System.out.println("------------get GlobalName-------------");
        System.out.println(getNode.getGlobalNodeName(elementName,"0.2.0.0"));
    }

    /**
     * 重载函数，根据输入的文件路径，存储所有节点信息
     * @param file sirius xml文件路径
     */
    public static void getNodes(String file) {
        SAXReader         sax      =new SAXReader();//创建一个SAXReader对象
        File xmlFile = new File(file);
        Document          document = null;//获取document对象,如果文档无节点，则会抛出Exception提前结束
        try {
            document = sax.read(xmlFile);
        } catch (DocumentException e) {
            log.error("无法读取文件",e);
        }
        org.dom4j.Element root     =document.getRootElement();//获取根节点
        getNodes(root,"0");//从根节点开始遍历所有节点
    }
    /**
     * 获取XML中的信息
     * 从指定节点开始,递归遍历所有子节点
     * @author oovever
     */
    public static void getNodes(org.dom4j.Element node, String current){

        log.info("--------------------");
        //当前节点的名称、文本内容和属性
        log.info("当前节点名称："+node.getName());//当前节点名称
        log.info("当前节点的内容："+node.getTextTrim());//当前节点名称
        List<Attribute> listAttr =node.attributes();//当前节点的所有属性的list
        log.info("第"+current+"个");
        if (!elementName.containsKey(current)) {
            elementName.put(current, new HashMap<>());
        }
        String typeOfAttr=null;
        String nameOfAttr = null;
        //            类型，名称
        Map<String, String> typeAndName = new HashMap<>();
        for(Attribute attr:listAttr){//遍历当前节点的所有属性
            String name=attr.getName();//属性名称
            String value=attr.getValue();//属性的值
            log.info("属性名称："+name+"属性值："+value);
            if(name.equals("type")){
                typeOfAttr = value;
            }else if(name.equals("name")){
                nameOfAttr = value;
            }else {
                value = regexMatchOwnedElement(value, Constant.ownedElement);
                value = regexMatchOwnedElement(value, Constant.portOfPackage);
                value = regexMatchOwnedElement(value,Constant.symbol);
                String[] valueArr = value.split(" ");
                if (name.equals("association")||name.equals("dependence")) {

                    if (callElement.get(current) == null) {
                        callElement.put(current, new ArrayList<String>());
                    }
                    log.info("-------association read----------");
                    for (int i = 0; i < valueArr.length; i++) {
                        valueArr[i] = "0" + valueArr[i];
                        callElement.get(current).add(valueArr[i]);
                    }

                } else if (name.equals("generalization")) {
                    log.info("-------generalization read----------");
                    if (generalizationElement.get(current) == null) {
                        generalizationElement.put(current, new ArrayList<String>());
                    }
                    for (int i = 0; i < valueArr.length; i++) {
                        valueArr[i] = "0" + valueArr[i];
                        generalizationElement.get(current).add(valueArr[i]);
                    }

                }else if (name.equals("realize")) {
                    log.info("-------realize read----------");
                    if (realizeElement.get(current) == null) {
                        realizeElement.put(current, new ArrayList<String>());
                    }
                    for (int i = 0; i < valueArr.length; i++) {
                        valueArr[i] = "0" + valueArr[i];
                        realizeElement.get(current).add(valueArr[i]);
                    }
                }
            }
        }
        typeAndName.put(typeOfAttr, nameOfAttr);
        elementName.put(current, typeAndName);
        List<org.dom4j.Element> listElement =node.elements();//所有一级子节点的list
        for(int i=0;i<listElement.size();i++){
            current=current+"."+String.valueOf(i);
            getNodes(listElement.get(i),current);//递归
            current = current.substring(0, current.length() - 2);
        }
    }


}
