package com.dom4j.sirius.Common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定义公用的基本参数设置
 * @author OovEver
 * 2018/3/28 23:16
 */
public class Constant   {
//    过滤ownedElement
    public static final String ownedElement = "@ownedElement";
    //    过滤portOfPackage
    public static final String portOfPackage = "@portOfPackage";
    //    过滤/
    public static final String symbol = "/";
//    组件类型
    public static final String Component = "architecture:Component";
    //    系统类型
    public static final String System = "architecture:System";
//    类类型
    public static final String ClassName = "architecture:Class";
//    调用关系
    public static final String callRelation = "callRelation";
//    实现关系
    public static final String implementRelation = "implementRelation";
//    继承关系
    public static final String extendRelation = "extendRelation";
}
