package com.main.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 * 道路表
 */
public class Road implements Serializable {
    private int id;
   private String road_id;


    private String[] linksIds;
   private String name;
   //道路类型
   private String type;
   //道路空间集合
   private  String wkt;
   //道路方向
    private int direction;
    //道路起点
    private String[] in_coordinates;
    //道路终点
    private String[] out_coordinates;
    //Link对象集合
    private List<Link> links;
    //方向名称
    private String direction_name;

    @Override
    public String toString() {
        return "Road{" +
                "id=" + id +
                ", road_id='" + road_id + '\'' +
                ", linksIds=" + Arrays.toString(linksIds) +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", wkt='" + wkt + '\'' +
                ", direction=" + direction +
                ", in_coordinates=" + Arrays.toString(in_coordinates) +
                ", out_coordinates=" + Arrays.toString(out_coordinates) +
                ", links=" + links +
                ", direction_name='" + direction_name + '\'' +
                '}';
    }

    public String getDirection_name() {
        return direction_name;
    }

    public void setDirection_name(String direction_name) {
        this.direction_name = direction_name;
    }

    public String[] getLinksIds() {
        return linksIds;
    }

    public void setLinksIds(String[] linksIds) {
        this.linksIds = linksIds;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String[] getIn_coordinates() {
        return in_coordinates;
    }

    public void setIn_coordinates(String[] in_coordinates) {
        this.in_coordinates = in_coordinates;
    }

    public String[] getOut_coordinates() {
        return out_coordinates;
    }

    public void setOut_coordinates(String[] out_coordinates) {
        this.out_coordinates = out_coordinates;
    }

    public String getRoad_id() {
        return road_id;
    }

    public void setRoad_id(String road_id) {
        this.road_id = road_id;
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

    public String getWkt() {
        return wkt;
    }

    public void setWkt(String wkt) {
        this.wkt = wkt;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
