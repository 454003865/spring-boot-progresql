package com.main.model;

import java.io.Serializable;

public class AmapPointSimple implements Serializable {
//     		  "id": 1,
//                      "link_id": "2168651354-2273511452",
//                      "lng84": "121.602668762207",
//                      "lat84": "38.993476867676",
//                      "name":"高德街"
    private int id;
    private String link_id;
    private String lng84;
    private String lat84;
    private String name;

    @Override
    public String toString() {
        return "AmapPointSimple{" +
                "id=" + id +
                ", link_id='" + link_id + '\'' +
                ", lng84='" + lng84 + '\'' +
                ", lat84='" + lat84 + '\'' +
                ", name='" + name + '\'' +
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
