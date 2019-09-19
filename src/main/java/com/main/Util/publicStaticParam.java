package com.main.Util;

import java.io.Serializable;

public class publicStaticParam implements Serializable {
    //public static String ="t_b_sectionv4";
    public static final String link_name = "t_b_section";
    public static final String ways_name = "hh_2po_4pgrv2";
    //link的删除数据库 original
    public static final String deletelink = "delete_link";
    //删除的数据记录表 主要记录关联关系  从哪个表删除的  original t_b_intersection
    public static final String delete_plugin = "original";
    //路口表的设置
    public static final String intersection = "t_b_intersection";
    //路口中间表的
    public static final String middle_intersection= "node_intersection";
    //node节点表
    public static final String node = "planet_osm_nodes";
    public static final String road_main = "t_b_roadway";
    //分车道表
    public static final String road_direction = "t_b_direction_roadway";
    //road和link中间表
    public static final String linkandRoad = "link_road";


    public static double getDistance2(double long1, double lat1, double long2, double lat2) {
        double a, b, R;
        R = 6378137; // 地球半径
        lat1 = lat1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        a = lat1 - lat2;
        b = (long1 - long2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
        return d;
    }

    public static  double getAngle1(double lat_a, double lng_a, double lat_b, double lng_b) {

        double y = Math.sin(lng_b-lng_a) * Math.cos(lat_b);
        double x = Math.cos(lat_a)*Math.sin(lat_b) - Math.sin(lat_a)*Math.cos(lat_b)*Math.cos(lng_b-lng_a);
        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        if(brng < 0){
            brng = brng +360;}

        return brng;

    }

}
