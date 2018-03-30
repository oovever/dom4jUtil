package com.dom4j.sirius.core;

import javax.swing.*;
import javax.xml.soap.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author OovEver
 * 2018/3/29 22:13
 * 获取节点信息
 */
public class getNode {
    /**
     * @param elementName 存储所有编号 类型 名称的hashmap
     * @param parent 父类节点名称
     * @return 父类节点下的所有节点的编号
     */
    public static Map<String,List<String>> getNodeChildren(Map<String, Map<String, String>> elementName,String parent){
        Map<String,List<String>> res = new HashMap<>();
        int index=0;
        String temp = parent;
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
     *
     * @param elementName 存储所有编号 类型 名称的hashmap
     * @param NodeId 元素的唯一标识
     * @return 返回元素的全局限定名
     */
    public static String getGlobalNodeName(Map<String, Map<String, String>> elementName,String NodeId) {
        String globalName = null;
        String []NodeIdArr=NodeId.split("\\.");
        if (NodeIdArr.length == 0) {
            return null;
        }
        String current = NodeIdArr[0];
        globalName = getSingleNodeName(elementName, current);
        for(int i=1;i<NodeIdArr.length;i++){
            current = current +"."+ NodeIdArr[i];
            if (globalName != null) {
                globalName = globalName + "." + getSingleNodeName(elementName, current);
            } else {
                globalName=getSingleNodeName(elementName, current);
            }

        }
        return globalName;
    }

    /**
     *  根据节点的唯一标识，返回节点在当前包下的名称
     * @param elementName 存储所有编号 类型 名称的hashmap
     * @param NodeId 节点编号 唯一标识
     * @return 元素的基本名称
     */
    public static String getSingleNodeName(Map<String, Map<String, String>> elementName,String NodeId) {
        String nodeSingleName = null;
        String nodeType = getNodeType(elementName, NodeId);
        Map<String, String> typeAndName = elementName.get(NodeId);
        nodeSingleName = typeAndName.get(nodeType);
        return nodeSingleName;
    }

    /**
     *
     * 根据节点唯一标识的编号返回元素类型
     * @param elementName 存储所有编号 类型 名称的hashmap
     * @param NodeId 节点编号 唯一标识
     * @return 该元素的类型
     */
    public static String getNodeType(Map<String, Map<String, String>> elementName,String NodeId) {
        String nodeType = null;
        Map<String, String> typeAndName = elementName.get(NodeId);
        for (String key : typeAndName.keySet()) {
            nodeType = key;
        }
        return nodeType;
    }

}
