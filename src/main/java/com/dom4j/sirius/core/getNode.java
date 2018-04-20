package com.dom4j.sirius.core;

import com.dom4j.sirius.Common.Constant;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * @author OovEver
 * 2018/3/29 22:13
 * 获取节点信息
 */
public class getNode {
    Logger logger = LoggerFactory.getLogger(getNode.class);
//元素下的子系统与子组件
    private static Set<String> childrenComponentAndSystemId = new HashSet<>();
//    元素下的子类
    private static Set<String> childrenClass= new HashSet<>();

    /**
     * @param elementName 存储所有编号 类型 名称的hashmap
     * @param parent      父类节点名称
     * @return 父类节点下的所有节点的编号
     */
    public static Map<String, List<String>> getNodeChildren(Map<String, Map<String, String>> elementName, String parent) {
        Map<String, List<String>> res   = new HashMap<>();
        int                       index = 0;
        String                    temp  = parent;
        temp = temp + "." + String.valueOf(index);
        List<String> tempList = new ArrayList<>();
        while (elementName.containsKey(temp)) {
            tempList.add(temp);
            index++;
            temp = parent + "." + String.valueOf(index);
        }
        res.put(parent, tempList);

        return res;
    }

    /**
     * @param elementName 存储所有编号 类型 名称的hashmap
     * @param NodeId      元素的唯一标识
     * @return 返回元素的全局限定名
     */
    public static String getGlobalNodeName(Map<String, Map<String, String>> elementName, String NodeId) {
        String   globalName = null;
        String[] NodeIdArr  = NodeId.split("\\.");
        if (NodeIdArr.length == 0) {
            return null;
        }
        String current = NodeIdArr[0];
        globalName = getSingleNodeName(elementName, current);
        for (int i = 1; i < NodeIdArr.length; i++) {
            current = current + "." + NodeIdArr[i];
            if (globalName != null) {
                globalName = globalName + "." + getSingleNodeName(elementName, current);
            } else {
                globalName = getSingleNodeName(elementName, current);
            }

        }
        return globalName;
    }

    /**
     * 根据节点的唯一标识，返回节点在当前包下的名称
     *
     * @param elementName 存储所有编号 类型 名称的hashmap
     * @param NodeId      节点编号 唯一标识
     * @return 元素的基本名称
     */
    public static String getSingleNodeName(Map<String, Map<String, String>> elementName, String NodeId) {
        String nodeSingleName = null;
        String nodeType       = getNodeType(elementName, NodeId);
        if (nodeType != null) {
            Map<String, String> typeAndName = elementName.get(NodeId);
            nodeSingleName = typeAndName.get(nodeType);
        }

        return nodeSingleName;
    }

    /**
     * 根据节点唯一标识的编号返回元素类型
     *
     * @param elementName 存储所有编号 类型 名称的hashmap
     * @param NodeId      节点编号 唯一标识
     * @return 该元素的类型
     */
    public static String getNodeType(Map<String, Map<String, String>> elementName, String NodeId) {
        String              nodeType    = null;
        Map<String, String> typeAndName = elementName.get(NodeId);
        for (String key : typeAndName.keySet()) {
            nodeType = key;
        }
        return nodeType;
    }

    /**
     * 获取xml文件中的所有节点全局限定名
     *
     * @param elementName 存储所有编号 类型 名称的hashmap
     * @return 所有节点的全局限定名
     */
    public static List<String> getGlobalNodeAllName(Map<String, Map<String, String>> elementName) {
        List<String> res = new ArrayList<>();
        for (String key : elementName.keySet()) {
            String globalNodeName = getGlobalNodeName(elementName, key);
            if (globalNodeName != null)
                res.add(globalNodeName);
        }
        return res;
    }


    /**
     * 获取模型文件中的所有组件的全局限定名
     *
     * @param elementName 存储所有编号 类型 名称的hashmap
     * @param type 模型元素类型
     * @return 模型中的相应类型元素的全局限定名
     */
    public static List<String> getAllGlobalName(Map<String, Map<String, String>> elementName,String type) {
        List<String> res = new ArrayList<>();
        for (String key : elementName.keySet()) {
            if (getNodeType(elementName, key) != null && getNodeType(elementName, key).equals(type)) {
                String globalName = getGlobalNodeName(elementName, key);
                res.add(globalName);
            }
        }
        return res;
    }

    /**
     *
     * @return 所有组件的全局限定名
     */
    public static List<String> getAllComponentGlobalName(){
        return getAllGlobalName(loadXmlFile.elementName, "architecture:Component");
    }
    /**
     *
     * @return 所有系统的全局限定名
     */
    public static List<String> getAllSystemGlobalName(){
        return getAllGlobalName(loadXmlFile.elementName, "architecture:System");
    }

    /**
     * 获取全局限定名与NodeId的映射
     * @param elementName 存储所有编号 类型 名称的hashmap
     * @return 全局限定名与NodeId的映射
     */

    public static Map<String, String> getGlobalNodeNameAndNodeId(Map<String, Map<String, String>> elementName) {
        Map<String, String> res = new HashMap<>();
        for (String nodeId : elementName.keySet()) {
            String globalName = getGlobalNodeName(elementName, nodeId);
            res.put(globalName, nodeId);
        }
        return res;
    }

    /**
     * 左边 出关系
     * 右边 入关系
     *
     * @param selectComponent   选定的组件
     * @param dependencyElement 依赖关系map
     * @return 选定组件的依赖关系
     */
    public static Map<Set<String>, Set<String>> getComponentDependency(String selectComponent, Map<String, Set<String>> dependencyElement) {
        Map<String, String> NodeId2globalName = getGlobalNodeNameAndNodeId(loadXmlFile.elementName);
        selectComponent = NodeId2globalName.get(selectComponent);
        Map<Set<String>, Set<String>> res    = new HashMap<>();
        Set<String>                   source = new HashSet<>();
        Set<String>                   target = new HashSet<>();
        if (dependencyElement.get(selectComponent) != null) {
            for (String component : dependencyElement.get(selectComponent)) {
                target.add(getGlobalNodeName(loadXmlFile.elementName, component));
            }
        }
        for (String key : dependencyElement.keySet()) {
            for (String component : dependencyElement.get(key)) {
                if (component != null && component.equals(selectComponent)) {
                    source.add(getGlobalNodeName(loadXmlFile.elementName, key));
                    break;
                }
            }
        }
        res.put(source, target);
        return res;
    }

    /**
     * 获取不同包下类的关联关系
     * @param portDependency 端口间的依赖关系
     * @param classRequiredElement 类请求端口的消息
     * @param portRequiredElement 端口请求类的消息
     * @param elementName elementName 存储所有编号 类型 名称的hashmap
     * @return 获取不同包下类的关联关系
     */
    public static Map<String,Set<String>> getRequiredOfClass( Map<String, Set<String>> portDependency,Map<String, Set<String>> classRequiredElement,Map<String, Set<String>> portRequiredElement,Map<String, Map<String, String>> elementName){
        Map<String, Set<String>> dependencyClass = new HashMap<>();
        for (String key : classRequiredElement.keySet()) {
            String globalSourceClassName = getGlobalNodeName(elementName, key);
            if (dependencyClass.get(globalSourceClassName) == null) {
                dependencyClass.put(globalSourceClassName, new HashSet<>());
            }
            for (String port : classRequiredElement.get(key)) {
                for (String targetPort : portDependency.get(port)) {
                    for (String targetClass : portRequiredElement.get(targetPort)) {
                        String globalTargetClassName = getGlobalNodeName(elementName, targetClass);
                        dependencyClass.get(globalSourceClassName).add(globalTargetClassName);
                    }
                }
            }

        }
        return dependencyClass;
    }

    /**
     * 返回一个节点的所有孩子节点ID
     * @param fatherId 父亲节点编号
     * @param elementName elementName 存储所有编号 类型 名称的hashmap
     * @return 返回所有孩子节点ID
     */
    public static Set<String> getNodeChildrenId(String fatherId,  Map<String, Map<String, String>> elementName) {
        Set<String> childrens = new HashSet<>();
        int index=0;
        while (true) {
            String children = fatherId + "." + index++;
            if (elementName.containsKey(children)) {
                childrens.add(children);
                for (String key : elementName.get(children).keySet()) {
//                    获取包下的子组件与子系统
                    if (key.equals(Constant.Component) || key.equals(Constant.System)) {
                        childrenComponentAndSystemId.add(children);
                    }
//                    获取包下的类类型
                    else if (key.equals(Constant.ClassName)) {
                        childrenClass.add(children);
                    }
                }
            } else {
                break;
            }
        }
        return childrens;
    }
    /**
     * 返回一个节点的所有孩子节点Name
     * @param fatherId 父亲节点编号
     * @param elementName elementName 存储所有编号 类型 名称的hashmap
     * @return 返回所有孩子节点Name
     */
    public static Set<String> getNodeChildrenName(String fatherId, Map<String, Map<String, String>> elementName) {
        Set<String> childrens = new HashSet<>();
        int index=0;
        while (true) {
            String children = fatherId + "." + index++;
            if (elementName.containsKey(children)) {
                for (String key : elementName.get(children).keySet()) {
                    childrens.add(elementName.get(children).get(key));

                }
            } else {
                break;
            }
        }
        return childrens;
    }

    /**
     * 获取包内组件（系统）的依赖关系
     * @param selectElementId 所选组件
     * @param fatherName 父包名称
     * @return 组件与组件的关系
     */
    public static Map<Set<String>,Set<String>>getRelatedCompsAndSysOfComp(String selectElementId,String fatherName){
        Map<Set<String>, Set<String>> RelatedCompsAndSysOfClass = new HashMap<>();
        Set<String> source = new HashSet<>();
        Set<String> target = new HashSet<>();
//        全局限定名 编号的map
        Map<String, String> NodeId2globalName = getGlobalNodeNameAndNodeId(loadXmlFile.elementName);
//        获取相应全局限定名的编号
         String  selectElement = NodeId2globalName.get(selectElementId);
        String fatherId = NodeId2globalName.get(fatherName);
        Set<String> childrens = getNodeChildrenId(fatherId,loadXmlFile.elementName);
//        ------------------处理组件-------------------
        Map<Set<String>, Set<String>> componentDependency = getComponentDependency(selectElementId, loadXmlFile.dependencyElement);
        for (Set<String> key : componentDependency.keySet()) {
            for (String childrenElement : key) {
                if (childrens.contains(NodeId2globalName.get(childrenElement))) {
                    source.add(childrenElement);
                }
            }
            for (String childrenElement : componentDependency.get(key)) {
                if (childrens.contains(NodeId2globalName.get(childrenElement))) {
                    target.add(childrenElement);
                }
            }
            }

        RelatedCompsAndSysOfClass.put(source, target);
//---------------------------------------------
        return RelatedCompsAndSysOfClass;
    }

    /**
     *
     * @param selectElement 选择的组件
     * @param fatherName 父元素
     * @param classRequiredElement class请求的端口map
     * @param portRequiredElement port请求的类map
     * @return 组件与类的关系
     */
    public static Map<Set<String>,Set<String>> getRelatedClassesOfComp(String selectElement,String fatherName,Map<String, Set<String>> classRequiredElement,Map<String, Set<String>> portRequiredElement) {
        Map<Set<String>, Set<String>> relatedClassesOfComp = new HashMap<>();
        Set<String> source = new HashSet<>();
        Set<String> target = new HashSet<>();
        //        全局限定名 编号的map
        Map<String, String> NodeId2globalName = getGlobalNodeNameAndNodeId(loadXmlFile.elementName);
//        获取相应全局限定名的编号
        selectElement = NodeId2globalName.get(selectElement);
        String fatherId = NodeId2globalName.get(fatherName);
        Set<String> childrens = getNodeChildrenId(fatherId,loadXmlFile.elementName);
        for (String key : portRequiredElement.keySet()) {
            if (key.substring(0,key.lastIndexOf(".")).equals(selectElement)) {
                for (String children : portRequiredElement.get(key)) {
                    if (childrens.contains(children)) {
                        target.add(children);
                    }
                }
            }

        }
        for (String key : classRequiredElement.keySet()) {
            if (childrens.contains(key)) {
                for (String port : classRequiredElement.get(key)) {
                    if (port.substring(0, port.lastIndexOf(".")).equals(selectElement)) {
                        source.add(key);
                        break;
                    }
                }
            }
        }
        return relatedClassesOfComp;
    }

    /**
     *
     * @param selectClass 选择的class
     * @param fatherName 父亲元素
     * @return 与选定类的（关联、实现、继承）关系
     */
    public static Map<String,Map<Set<String>,Set<String>>> getRelatedClassesOfClass(String selectClass, String fatherName) {
        Map<String,Map<Set<String>,Set<String>>> relatedClassesOfClass = new HashMap<>();
        //        全局限定名 编号的map
        Map<String, String> NodeId2globalName = getGlobalNodeNameAndNodeId(loadXmlFile.elementName);
//        获取相应全局限定名的编号
        selectClass = NodeId2globalName.get(selectClass);
        String fatherId = NodeId2globalName.get(fatherName);
        Set<String> childrens = getNodeChildrenId(fatherId,loadXmlFile.elementName);
//        获取类之间的调用关系
        Map<Set<String>, Set<String>> callRelation = getChildClassRelation(loadXmlFile.callElement, childrens, selectClass);
        relatedClassesOfClass.put(Constant.callRelation, callRelation);
//        获取类之间的继承关系
        Map<Set<String>, Set<String>> extendRelation = getChildClassRelation(loadXmlFile.generalizationElement, childrens, selectClass);
// 获取类之间的实现关系
        Map<Set<String>, Set<String>> implmentRelation = getChildClassRelation(loadXmlFile.realizeElement, childrens, selectClass);
        return relatedClassesOfClass;
    }

    /**
     *
     * @param relationElement 请求关系map包含调用 继承 实现
     * @param childrens 子元素
     * @param selectClass 选择的类
     * @return 与选定类的（关联、实现、继承）关系
     */
    public static Map<Set<String>,Set<String>> getChildClassRelation(Map<String, List<String>> relationElement, Set<String> childrens,String selectClass) {
        Map<Set<String>, Set<String>> ClassRelation = new HashMap<>();
        Set<String> source = new HashSet<>();
        Set<String> target = new HashSet<>();
        for (String targetClass : relationElement.get(selectClass)) {
            if (childrens.contains(targetClass)) {
                target.add(targetClass);
            }
        }
        for (String sourceClass : relationElement.keySet()) {
            for (String targetClass : relationElement.get(sourceClass)) {
                if (targetClass.equals(selectClass)) {
                    source.add(sourceClass);
                    break;
                }
            }
        }
        ClassRelation.put(source, target);
        return ClassRelation;
    }
}
