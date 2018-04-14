package com.dom4j.sirius.core;


import com.dom4j.sirius.Common.Constant;
import com.sun.org.apache.xml.internal.security.Init;
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
    ////  父亲节点编号  portOfPackage编号 ownedElement编号
//    static Map<String,  LinkedHashMap<String, String>> portElement = new HashMap<>();
//    调用者  调用的类或者接口
    static Map<String, List<String>> callElement = new HashMap<>();
//    调用者 继承的类
    static Map<String, List<String>> generalizationElement = new HashMap<>();
    //    调用者 实现的接口
    static Map<String, List<String>> realizeElement = new HashMap<>();
    //    调用者 调用的目标组件
    static Map<String, Set<String>> dependencyElement = new HashMap<>();
    //源端口 目标端口
    static Map<String, Set<String>> portDependency = new HashMap<>();
    //父亲节点 当前端口是该节点下的第几个端口
    static Map<String, Integer> portNumOfFather = new HashMap<>();
    // 端口的编号 在实际组件/系统中的编号
    static Map<String, String> portAndOwnedElement = new HashMap<>();
//   起始编号 目标编号
    static Map<String, Set<String>> classRequiredElement = new HashMap<>();
    //   起始编号 目标编号
    static Map<String, Set<String>> portRequiredElement = new HashMap<>();
    public static void main(String[] args) throws DocumentException {
        String file = "E:\\git库\\华为项目\\architectureDesigner\\design\\org.eclipse.sirius.architecture.sample\\example.architecture";
        getNodes(file);
        for (String key : elementName.keySet()) {
            System.out.print("当前节点"+key+"------");
            for (String key2 : elementName.get(key).keySet()) {
                System.out.println("类型"+key2+"-------------"+"名称"+elementName.get(key).get(key2));
            }
        }
//        System.out.println("-----------call Element-------------");
//        System.out.println(callElement);
//        System.out.println("-----------all GlobalName-------------");
//        System.out.println(getNode.getGlobalNodeAllName(elementName));
//        System.out.println("-----------all dependency-------------");
//        System.out.println(dependencyElement);
//        System.out.println("------------get Children-------------");
//        System.out.println(getNode.getNodeChildren(elementName,"0.2.0"));
//        System.out.println("------------get GlobalName-------------");
//        System.out.println(getNode.getGlobalNodeName(elementName,"0.2.0.0"));
//        System.out.println(getNode.getComponentDependency("Component1",dependencyElement));
        System.out.println("-----------portDependency-------------");
        System.out.println(portDependency);
        System.out.println("-----------portAndOwnedElement-------------");
        System.out.println(portAndOwnedElement);
        System.out.println("-----------classRequiredElement-------------");
        System.out.println(classRequiredElement);
        System.out.println("-----------portRequiredElement-------------");
        System.out.println(portRequiredElement);
        System.out.println("-----------classAssociation-------------");
        System.out.println(getNode.getRequiredOfClass(portDependency, classRequiredElement, portRequiredElement, elementName));

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
        initRead(root, "0");
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
//                可能有多种关系，多种关系的数组
                String[] valueArr = value.split(" ");
                if (name.equals("association")) {
                    log.info("-------association read----------");
                    addRelation(callElement,valueArr,current);

                } else if (name.equals("generalization")) {
                    log.info("-------generalization read----------");
                    addRelation(generalizationElement, valueArr, current);

                }else if (name.equals("realize")) {
                    log.info("-------realize read----------");
                    addRelation(realizeElement, valueArr, current);
                } else if (name.equals("dependence")) {
                    log.info("-------dependency read----------");
//                    当前端口所属组件
                    String portOfComponent = current.substring(0,current.lastIndexOf("."));
                    if (dependencyElement.get(portOfComponent) == null) {
                        dependencyElement.put(portOfComponent, new HashSet<>());
                    }
                    if (portDependency.get(current) == null) {
                        portDependency.put(current, new HashSet<>());
                    }
                    for (int i = 0; i < valueArr.length; i++) {
                        valueArr[i] = "0" + valueArr[i];
                        dependencyElement.get(portOfComponent).add(valueArr[i].substring(0,valueArr[i].lastIndexOf(".")));
                        portDependency.get(current).add(portAndOwnedElement.get(valueArr[i]));
                    }
                }else if (name.equals("required")) {
                    log.info("-------required read----------");
                    boolean sourceIsPort = node.getName().equals("portOfPackage") ? true : false;
                    for (int i = 0; i < valueArr.length; i++) {
                        valueArr[i] = "0" + valueArr[i];
                        if (sourceIsPort) {
                            if (portRequiredElement.get(current) == null) {
                                portRequiredElement.put(current, new HashSet<>());
                            }
                            portRequiredElement.get(current).add(valueArr[i]);
                        } else {
                            if (classRequiredElement.get(current) == null) {
                                classRequiredElement.put(current, new HashSet<>());
                            }
                            classRequiredElement.get(current).add(portAndOwnedElement.get(valueArr[i]));
                        }

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
            current = current.substring(0,current.lastIndexOf("."));
        }
    }

    /**
     * 初始化读取，获取所有port信息
     * @param node 节点
     * @param current 当前读取到的节点
     */
    private static   void initRead(org.dom4j.Element node, String current){
        //        如果当前节点的类型是portOfPackage
        if (node.getName() != null && node.getName().equals("portOfPackage")) {
//           端口节点的父节点
            String father = current.substring(0, current.lastIndexOf("."));
            int    num    = portNumOfFather.getOrDefault(father, -1);
            num += 1;
            portAndOwnedElement.put(father + "." + num, current);
            portNumOfFather.put(father, num);
        }
//            ----------------------------------------------------------
            List<org.dom4j.Element> listElement =node.elements();//所有一级子节点的list
            for(int i=0;i<listElement.size();i++){
                current=current+"."+String.valueOf(i);
                initRead(listElement.get(i),current);//递归
                current = current.substring(0,current.lastIndexOf("."));
            }

    }
    private static void addRelation(Map<String, List<String>> map, String[] valueArr,String current){
        map.computeIfAbsent(current, t -> new ArrayList<>());
        for (int i = 0; i < valueArr.length; i++) {
            valueArr[i] = "0" + valueArr[i];
            map.get(current).add(valueArr[i]);
        }
    }

}
