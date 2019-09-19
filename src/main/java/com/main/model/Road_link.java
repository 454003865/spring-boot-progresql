package com.main.model;

import java.io.Serializable;

public class Road_link implements Serializable {
    private String road_id;
    private String link_ids;
    private  String wkt;


    @Override
    public String toString() {
        return "Road_link{" +
                "road_id='" + road_id + '\'' +
                ", link_ids='" + link_ids + '\'' +
                ", wkt='" + wkt + '\'' +
                '}';
    }

    public String getRoad_id() {
        return road_id;
    }

    public void setRoad_id(String road_id) {
        this.road_id = road_id;
    }

    public String getLink_ids() {
        return link_ids;
    }

    public void setLink_ids(String link_ids) {
        this.link_ids = link_ids;
    }

    public String getWkt() {
        return wkt;
    }

    public void setWkt(String wkt) {
        this.wkt = wkt;
    }

}
