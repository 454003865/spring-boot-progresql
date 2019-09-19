package com.main.model;

import java.io.Serializable;

public class AmapPoint implements Serializable {
    private int id;
    private String name;
    //省份
    private String province;
    //城市
    private String city;
    //区域
    private String district;
    //
    private int positon_type;
    private int zone_used_id;
    private int x;
    private int y;
    private String lng;
    private String lat;
    private String lng84;
    private String lat84;
    private int forward_id;
    private String service_tags;


    @Override
    public String toString() {
        return "AmapPoint{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", positon_type=" + positon_type +
                ", zone_used_id=" + zone_used_id +
                ", x=" + x +
                ", y=" + y +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", lng84='" + lng84 + '\'' +
                ", lat84='" + lat84 + '\'' +
                ", forward_id=" + forward_id +
                ", service_tags='" + service_tags + '\'' +
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getPositon_type() {
        return positon_type;
    }

    public void setPositon_type(int positon_type) {
        this.positon_type = positon_type;
    }

    public int getZone_used_id() {
        return zone_used_id;
    }

    public void setZone_used_id(int zone_used_id) {
        this.zone_used_id = zone_used_id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng84() {
        return lng84;
    }

    public void setLng84(String lng84) {
        this.lng84 = lng84;
    }

    public String getLat84() {
        return lat84;
    }

    public void setLat84(String lat84) {
        this.lat84 = lat84;
    }

    public int getForward_id() {
        return forward_id;
    }

    public void setForward_id(int forward_id) {
        this.forward_id = forward_id;
    }

    public String getService_tags() {
        return service_tags;
    }

    public void setService_tags(String service_tags) {
        this.service_tags = service_tags;
    }
}
