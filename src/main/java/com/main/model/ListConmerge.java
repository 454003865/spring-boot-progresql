package com.main.model;

import java.io.Serializable;

public class ListConmerge implements Serializable {
    private  String Link_id;
    private  String ways_ids;
    private  String in_id;
    private  String out_id;

    public String getLink_id() {
        return Link_id;
    }

    public void setLink_id(String link_id) {
        Link_id = link_id;
    }

    public String getWays_ids() {
        return ways_ids;
    }

    public void setWays_ids(String ways_ids) {
        this.ways_ids = ways_ids;
    }

    public String getIn_id() {
        return in_id;
    }

    public void setIn_id(String in_id) {
        this.in_id = in_id;
    }

    public String getOut_id() {
        return out_id;
    }

    public void setOut_id(String out_id) {
        this.out_id = out_id;
    }
}
