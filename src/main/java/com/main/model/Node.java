package com.main.model;

import java.io.Serializable;

public class Node implements Serializable {
    private Long  id;
    private Double lat;
    private Double lon;


    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
