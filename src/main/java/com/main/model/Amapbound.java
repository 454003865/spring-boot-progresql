package com.main.model;

import java.io.Serializable;

public class Amapbound implements Serializable {
    //id,link_id,wkt,line,point_geom,point_distance,ST_LineLocatePoint(wkt,point_geom),st_asText(ST_ClosestPoint(wkt,point_geom))
    private int id;
    private String link_id;
   // private String wkt;
    private String line;
    private String point_distance;
    private double point_line_scala;
    private String pointOnLine;


    @Override
    public String toString() {
        return "Amapbound{" +
                "id=" + id +
                ", link_id='" + link_id + '\'' +
                ", line='" + line + '\'' +
                ", point_distance='" + point_distance + '\'' +
                ", point_line_scala=" + point_line_scala +
                ", pointOnLine='" + pointOnLine + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink_id() {
        return link_id;
    }

    public void setLink_id(String link_id) {
        this.link_id = link_id;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getPoint_distance() {
        return point_distance;
    }

    public void setPoint_distance(String point_distance) {
        this.point_distance = point_distance;
    }

    public double getPoint_line_scala() {
        return point_line_scala;
    }

    public void setPoint_line_scala(double point_line_scala) {
        this.point_line_scala = point_line_scala;
    }

    public String getPointOnLine() {
        return pointOnLine;
    }

    public void setPointOnLine(String pointOnLine) {
        this.pointOnLine = pointOnLine;
    }
}
