package com.main.model;

import java.io.Serializable;

public class LinkOrigin  implements Serializable {
    private String name;
    private String lat;
    private String lng;


    @Override
    public String toString() {
        return "LinkOrigin{" +
                "name='" + name + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
