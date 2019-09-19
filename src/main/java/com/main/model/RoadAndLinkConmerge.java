package com.main.model;

import java.io.Serializable;
import java.util.List;

public class RoadAndLinkConmerge implements Serializable {


    private List<String> road_ids;
    private String main_road_id;
    private List<String> link_ids;

    public List<String> getRoad_ids() {
        return road_ids;
    }

    public void setRoad_ids(List<String> road_ids) {
        this.road_ids = road_ids;
    }

    public String getMain_road_id() {
        return main_road_id;
    }

    public void setMain_road_id(String main_road_id) {
        this.main_road_id = main_road_id;
    }

    public List<String> getLink_ids() {
        return link_ids;
    }

    public void setLink_ids(List<String> link_ids) {
        this.link_ids = link_ids;
    }

    @Override
    public String toString() {
      String   roadids="";
        for (int i = 0; i <road_ids.size() ; i++) {
            roadids= roadids+road_ids.get(i);
        }
        String   linkids="";
        for (int i = 0; i <link_ids.size() ; i++) {
            linkids= linkids+link_ids.get(i);
        }


        return "RoadAndLinkConmerge{" +
                "road_ids=" + roadids +
                ", main_road_id='" + main_road_id + '\'' +
                ", link_ids=" + linkids +
                '}';
    }
}
