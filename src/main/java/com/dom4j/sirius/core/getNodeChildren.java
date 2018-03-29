package com.dom4j.sirius.core;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author OovEver
 * 2018/3/29 22:13
 * 获取节点下的所有子节点
 */
public class getNodeChildren {
    /**
     *
     * @param elementName 存储所有编号 类型 名称的hashmap
     * @param parent 父类节点名称
     * @return 父类节点下的所有节点的编号
     */
    public static Map<String,List<String>> getChildren(Map<String, Map<String, String>> elementName,String parent){
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
}
