package com.main.model;

import java.io.Serializable;
import java.util.Arrays;

public class Original implements Serializable {
    private String link_Id;
    private String[] oiginal_link;
    private String insert_time;
    private String link_from;


    @Override
    public String toString() {
        return "Original{" +
                "link_Id='" + link_Id + '\'' +
                ", oiginal_link=" + Arrays.toString(oiginal_link) +
                ", insert_time='" + insert_time + '\'' +
                ", link_from='" + link_from + '\'' +
                '}';
    }

    public String getLink_from() {
        return link_from;
    }

    public void setLink_from(String link_from) {
        this.link_from = link_from;
    }

    public String getLink_Id() {
        return link_Id;
    }

    public void setLink_Id(String link_Id) {
        this.link_Id = link_Id;
    }

    public String[] getOiginal_link() {
        return oiginal_link;
    }

    public void setOiginal_link(String[] oiginal_link) {
        this.oiginal_link = oiginal_link;
    }

    public String getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }
}
