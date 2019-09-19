package com.main.model;

import java.io.Serializable;
import java.util.List;

public class RoadMerge implements Serializable {
    private int road_id;
    private String road_name;
    private String droad_name;
    private String direction_name;
    private List<String> link_ids;

    @Override
    public String toString() {
        return "RoadMerge{" +
                "road_id=" + road_id +
                ", road_name='" + road_name + '\'' +
                ", droad_name='" + droad_name + '\'' +
                ", link_ids=" + link_ids +
                '}';
    }

    public String getDirection_name() {
        return direction_name;
    }

    public void setDirection_name(String direction_name) {
        this.direction_name = direction_name;
    }

    public int getRoad_id() {
        return road_id;
    }

    public void setRoad_id(int road_id) {
        this.road_id = road_id;
    }

    public String getRoad_name() {
        return road_name;
    }

    public void setRoad_name(String road_name) {
        this.road_name = road_name;
    }

    public String getDroad_name() {
        return droad_name;
    }

    public void setDroad_name(String droad_name) {
        this.droad_name = droad_name;
    }

    public List<String> getLink_ids() {
        return link_ids;
    }

    public void setLink_ids(List<String> link_ids) {
        this.link_ids = link_ids;
    }
}
