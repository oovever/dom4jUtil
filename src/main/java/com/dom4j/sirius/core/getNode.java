package com.dom4j.sirius.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.xml.soap.Node;
import java.util.*;


/**
 * @author OovEver
 * 2018/3/29 22:13
 * 获取节点信息
 */
public class getNode {
    Logger logger = LoggerFactory.getLogger(getNode.class);


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

}
