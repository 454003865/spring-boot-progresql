package com.main.model;

import java.io.Serializable;
import java.util.List;

public class AmapBind implements Serializable {


    private List<AmapPoint> data;
    private List<List<Double>> bounds;

    public List<List<Double>> getBounds() {
        return bounds;
    }

    public void setBounds(List<List<Double>> bounds) {
        this.bounds = bounds;
    }

    public List<AmapPoint> getData() {
        return data;
    }

    public void setData(List<AmapPoint> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        String allString="";

        for (int i = 0; i <data.size() ; i++) {
            allString="\n"+allString+i+"|||"+data.get(i).toString();
          //  System.out.println(i+"|||"+data.get(i).toString());
        }
        return allString;
    }
}
