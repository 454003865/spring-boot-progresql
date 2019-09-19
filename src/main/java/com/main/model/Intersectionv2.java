package com.main.model;

import java.io.Serializable;

public class Intersectionv2 implements Serializable {
//    pritvate  string intersection_id;  //		路口id
//    link_id	string	路段id
//    name	string	路口名称
//    center_x	string	中心x坐标
//    center_y	string	中心y坐标
//    type_no	int	路口类型编码（如：1二叉路口，2三叉路口）
//    type_name	string	路口类型名称（如：三叉路口）
    //路口id
    private int id;
    private String intersection_id;
    //路段ids
    private String link_ids;
    //路口名称
    private String name;
    //中心x坐标
    private String center_x;
    //中心y坐标
    private String center_y;
    //路口类型编码（如：1二叉路口，2三叉路口）路口类型编码（如：1二叉路口，2三叉路口，3四岔路口 4五岔路口 5六叉路口 6其它）
    private int type_no;
    //路口类型名称（如：三叉路口）
    private int type_name;
    //所包含的node节点
    private String nodes;
    //半径米数
    private String radius;
    //中心点
    private double[] center;

    @Override
    public String toString() {
        return "Intersection{" +
                "id=" + id +
                ", intersection_id='" + intersection_id + '\'' +
                ", link_ids='" + link_ids + '\'' +
                ", name='" + name + '\'' +
                ", center_x='" + center_x + '\'' +
                ", center_y='" + center_y + '\'' +
                ", type_no=" + type_no +
                ", type_name=" + type_name +
                ", nodes='" + nodes + '\'' +
                ", radius='" + radius + '\'' +
                ", center='" + center + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntersection_id() {
        return intersection_id;
    }

    public void setIntersection_id(String intersection_id) {
        this.intersection_id = intersection_id;
    }

    public String getLink_ids() {
        return link_ids;
    }

    public void setLink_ids(String link_ids) {
        this.link_ids = link_ids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCenter_x() {
        return center_x;
    }

    public void setCenter_x(String center_x) {
        this.center_x = center_x;
    }

    public String getCenter_y() {
        return center_y;
    }

    public void setCenter_y(String center_y) {
        this.center_y = center_y;
    }

    public int getType_no() {
        return type_no;
    }

    public void setType_no(int type_no) {
        this.type_no = type_no;
    }

    public int getType_name() {
        return type_name;
    }

    public void setType_name(int type_name) {
        this.type_name = type_name;
    }

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public double[] getCenter() {
        return center;
    }

    public void setCenter(double[] center) {
        this.center = center;
    }
}
