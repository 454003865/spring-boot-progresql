package com.main.model;

import java.io.Serializable;

public class Intersection_node implements Serializable {
 private String    intersection_id;
 //character varying(100),
  private String  node_id;
  //character varying(100)

    @Override
    public String toString() {
        return "Intersection_node{" +
                "intersection_id='" + intersection_id + '\'' +
                ", node_id='" + node_id + '\'' +
                '}';
    }

    public String getIntersection_id() {
        return intersection_id;
    }

    public void setIntersection_id(String intersection_id) {
        this.intersection_id = intersection_id;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }
}
