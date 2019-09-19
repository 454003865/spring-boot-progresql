package com.main.model;

import java.io.Serializable;

public class target implements Serializable {
   private int id;
   private String name;
   private String comment_name;
   private String type;
   private double lng;
   private double lat;
   private String camera;
   private int police_region_id;
   private int  forward_id;
   private int relation_jnc_id;
   private String ignore_alert;
    private String wkt;

    @Override
    public String toString() {
        return "target{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", comment_name='" + comment_name + '\'' +
                ", type='" + type + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", camera='" + camera + '\'' +
                ", police_region_id=" + police_region_id +
                ", forward_id=" + forward_id +
                ", relation_jnc_id=" + relation_jnc_id +
                ", ignore_alert='" + ignore_alert + '\'' +
                ", wkt='" + wkt + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment_name() {
        return comment_name;
    }

    public void setComment_name(String comment_name) {
        this.comment_name = comment_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public int getPolice_region_id() {
        return police_region_id;
    }

    public void setPolice_region_id(int police_region_id) {
        this.police_region_id = police_region_id;
    }

    public int getForward_id() {
        return forward_id;
    }

    public void setForward_id(int forward_id) {
        this.forward_id = forward_id;
    }

    public int getRelation_jnc_id() {
        return relation_jnc_id;
    }

    public void setRelation_jnc_id(int relation_jnc_id) {
        this.relation_jnc_id = relation_jnc_id;
    }

    public String getIgnore_alert() {
        return ignore_alert;
    }

    public void setIgnore_alert(String ignore_alert) {
        this.ignore_alert = ignore_alert;
    }

    public String getWkt() {
        return wkt;
    }

    public void setWkt(String wkt) {
        this.wkt = wkt;
    }
}
