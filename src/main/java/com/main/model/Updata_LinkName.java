package com.main.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Updata_LinkName implements Serializable {
    private List<String> link_id;
    private String name;

    public List<String> getLink_id() {
        return link_id;
    }

    public void setLink_id(List<String> link_id) {
        this.link_id = link_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Updata_LinkName{" +
                "link_id=" + link_id +
                ", name='" + name + '\'' +
                '}';
    }
}
