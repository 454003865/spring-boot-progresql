package com.main.model;

import java.io.Serializable;

public class Roadway implements Serializable {
    private String road_id;
    private String road_name;
    private int road_type;
    private double road_length;
    private String wkt;

    @Override
    public String toString() {
        return "Roadway{" +
                "road_id='" + road_id + '\'' +
                ", road_name='" + road_name + '\'' +
                ", road_type=" + road_type +
                ", road_length=" + road_length +
                ", wkt='" + wkt + '\'' +
                '}';
    }

    public String getRoad_id() {
        return road_id;
    }

    public void setRoad_id(String road_id) {
        this.road_id = road_id;
    }

    public String getRoad_name() {
        return road_name;
    }

    public void setRoad_name(String road_name) {
        this.road_name = road_name;
    }

    public int getRoad_type() {
        return road_type;
    }

    public void setRoad_type(int road_type) {
        this.road_type = road_type;
    }

    public double getRoad_length() {
        return road_length;
    }

    public void setRoad_length(double road_length) {
        this.road_length = road_length;
    }

    public String getWkt() {
        return wkt;
    }

    public void setWkt(String wkt) {
        this.wkt = wkt;
    }
}
