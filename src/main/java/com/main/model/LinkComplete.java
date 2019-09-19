package com.main.model;

import java.io.Serializable;

public class LinkComplete implements Serializable {

//
//    id character varying(50) COLLATE pg_catalog."default",
//    ways_ids character varying(1000) COLLATE pg_catalog."default",
//    wkt_geom geometry(Geometry,4326),
//    wkt character varying COLLATE pg_catalog."default",
//    in_id character varying(50) COLLATE pg_catalog."default",
//    out_id character varying(50) COLLATE pg_catalog."default",
//    name character varying(50) COLLATE pg_catalog."default",
//    angle bigint,
//    type bigint,
//    length double precision,
//    up_ids character varying(500) COLLATE pg_catalog."default",
//    link_id text COLLATE pg_catalog."default",
//    starting_point json,
//    end_point json,
//    up_intersection bigint,
//    end_intersection bigint
    private String id ;
    private String  ways_ids ;
    private String  wkt ;
    private String wkt_geom;
    private Long  in_id ;
    private Long  out_id ;
    private String  name;
    private int  angle ;
    private Long   type ;
    private Double  length;
    private String   up_ids;
    private String  link_id ;
    private String starting_point;
    private String end_point;
    private Long up_intersection;
    private Long end_intersection;

    @Override
    public String toString() {
        return "LinkComplete{" +
                "id='" + id + '\'' +
                ", ways_ids='" + ways_ids + '\'' +
                ", wkt='" + wkt + '\'' +
                ", wkt_geom='" + wkt_geom + '\'' +
                ", in_id=" + in_id +
                ", out_id=" + out_id +
                ", name='" + name + '\'' +
                ", angle=" + angle +
                ", type=" + type +
                ", length=" + length +
                ", up_ids='" + up_ids + '\'' +
                ", link_id='" + link_id + '\'' +
                ", starting_point='" + starting_point + '\'' +
                ", end_point='" + end_point + '\'' +
                ", up_intersection=" + up_intersection +
                ", end_intersection=" + end_intersection +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWays_ids() {
        return ways_ids;
    }

    public void setWays_ids(String ways_ids) {
        this.ways_ids = ways_ids;
    }

    public String getWkt() {
        return wkt;
    }

    public void setWkt(String wkt) {
        this.wkt = wkt;
    }

    public String getWkt_geom() {
        return wkt_geom;
    }

    public void setWkt_geom(String wkt_geom) {
        this.wkt_geom = wkt_geom;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public String getUp_ids() {
        return up_ids;
    }

    public void setUp_ids(String up_ids) {
        this.up_ids = up_ids;
    }

    public String getLink_id() {
        return link_id;
    }

    public void setLink_id(String link_id) {
        this.link_id = link_id;
    }

    public String getStarting_point() {
        return starting_point;
    }

    public void setStarting_point(String starting_point) {
        this.starting_point = starting_point;
    }

    public String getEnd_point() {
        return end_point;
    }

    public void setEnd_point(String end_point) {
        this.end_point = end_point;
    }

    public Long getUp_intersection() {
        return up_intersection;
    }

    public void setUp_intersection(Long up_intersection) {
        this.up_intersection = up_intersection;
    }

    public Long getEnd_intersection() {
        return end_intersection;
    }

    public void setEnd_intersection(Long end_intersection) {
        this.end_intersection = end_intersection;
    }
}

