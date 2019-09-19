package com.main.model;

import java.io.Serializable;
import java.util.Arrays;

public class NodeforAlive implements Serializable {
    private String intersection;
    private String id;
    private String[] coordinates;

    @Override
    public String toString() {
        return "NodeforAlive{" +
                "intersection='" + intersection + '\'' +
                ", id='" + id + '\'' +
                ", coordinates=" + Arrays.toString(coordinates) +
                '}';
    }

    public String getIntersection() {
        return intersection;
    }

    public void setIntersection(String intersection) {
        this.intersection = intersection;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String[] coordinates) {
        this.coordinates = coordinates;
    }
}
