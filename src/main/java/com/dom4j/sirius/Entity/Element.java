package com.dom4j.sirius.Entity;

/**
 * 节点实体类
 * @author OovEver
 * 2018/3/26 9:51
 */
public class Element {
//    父亲
    private String    parent;
//    名称
    private String    name;
//    类型
    private String    type;
//    与其关联的目标节点
    private Element[] relation;
    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Element[] getRelation() {
        return relation;
    }

    public void setRelation(Element[] relation) {
        this.relation = relation;
    }


}
