package com.main.model;

public class Ways {
    private String ways_ids;
    private String geom_way;
    private String osm_id;
    private String osm_name;
    private String osm_source_id;
    private String osm_target_id;
    private String id;
    private String clazz;


    @Override
    public String toString() {
        return "Ways{" +
                "ways_ids='" + ways_ids + '\'' +
                ", geom_way='" + geom_way + '\'' +
                ", osm_id='" + osm_id + '\'' +
                ", osm_name='" + osm_name + '\'' +
                ", osm_source_id='" + osm_source_id + '\'' +
                ", osm_target_id='" + osm_target_id + '\'' +
                ", id='" + id + '\'' +
                ", clazz='" + clazz + '\'' +
                '}';
    }

    public String getWays_ids() {
        return ways_ids;
    }

    public void setWays_ids(String ways_ids) {
        this.ways_ids = ways_ids;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getWays_id() {
        return ways_ids;
    }

    public void setWays_id(String ways_ids) {
        this.ways_ids = ways_ids;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getGeom_way() {
        return geom_way;
    }

    public void setGeom_way(String geom_way) {
        this.geom_way = geom_way;
    }

    public String getOsm_id() {
        return osm_id;
    }

    public void setOsm_id(String osm_id) {
        this.osm_id = osm_id;
    }

    public String getOsm_name() {
        return osm_name;
    }

    public void setOsm_name(String osm_name) {
        this.osm_name = osm_name;
    }

    public String getOsm_source_id() {
        return osm_source_id;
    }

    public void setOsm_source_id(String osm_source_id) {
        this.osm_source_id = osm_source_id;
    }

    public String getOsm_target_id() {
        return osm_target_id;
    }

    public void setOsm_target_id(String osm_target_id) {
        this.osm_target_id = osm_target_id;
    }
}
