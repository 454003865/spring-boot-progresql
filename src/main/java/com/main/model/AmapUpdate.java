package com.main.model;

import java.io.Serializable;
import java.util.List;

public class AmapUpdate implements Serializable {
    private int id;
    private List<String> coordinates;
    private List<List<Double>> bounds;

    @Override
    public String toString() {
        return "AmapUpdate{" +
                "id=" + id +
                ", coordinates=" + coordinates +
                ", bounds=" + bounds +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<String> coordinates) {
        this.coordinates = coordinates;
    }

    public List<List<Double>> getBounds() {
        return bounds;
    }

    public void setBounds(List<List<Double>> bounds) {
        this.bounds = bounds;
    }
}
