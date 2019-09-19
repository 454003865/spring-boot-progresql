package com.main.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Arrays;

public class Link implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty(value = "id")
    private String id;
    @JsonProperty(value = "link_id")
    private String link_id;
    @JsonProperty(value = "wkt")
    private String wkt;
    @JsonProperty(value = "ways_ids")
    private String ways_ids;
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "in_id")
    private Long in_id;
    @JsonProperty(value = "out_id")
    private Long out_id;
    private String[] in_coordinates;
    private String[] out_coordinates;
    private String insert_time;

    @Override
    public String toString() {
        return "Link{" +
                "id='" + id + '\'' +
                ", link_id='" + link_id + '\'' +
                ", wkt='" + wkt + '\'' +
                ", ways_ids='" + ways_ids + '\'' +
                ", name='" + name + '\'' +
                ", in_id=" + in_id +
                ", out_id=" + out_id +
                ", in_coordinates=" + Arrays.toString(in_coordinates) +
                ", out_coordinates=" + Arrays.toString(out_coordinates) +
                ", insert_time='" + insert_time + '\'' +
                '}';
    }

    public Link(String id, String link_id, String wkt, String ways_ids, String name, Long in_id, Long out_id) {
        this.id = id;
        this.link_id = link_id;
        this.wkt = wkt;
        this.ways_ids = ways_ids;
        this.name = name;
        this.in_id = in_id;
        this.out_id = out_id;
    }
    public Link() {
        super();
    }

    public String getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String[] getIn_coordinates() {
        return in_coordinates;
    }

    public void setIn_coordinates(String[] in_coordinates) {
        this.in_coordinates = in_coordinates;
    }

    public String[] getOut_coordinates() {
        return out_coordinates;
    }

    public void setOut_coordinates(String[] out_coordinates) {
        this.out_coordinates = out_coordinates;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }




    public String getLink_id() {
        return link_id;
    }

    public void setLink_id(String link_id) {
        this.link_id = link_id;
    }

    public String getWkt() {
        return wkt;
    }

    public void setWkt(String wkt) {
        this.wkt = wkt;
    }

    public String getWays_ids() {
        return ways_ids;
    }

    public void setWays_ids(String ways_ids) {
        this.ways_ids = ways_ids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIn_id() {
        return in_id;
    }

    public void setIn_id(Long in_id) {
        this.in_id = in_id;
    }

    public Long getOut_id() {
        return out_id;
    }

    public void setOut_id(Long out_id) {
        this.out_id = out_id;
    }
}
