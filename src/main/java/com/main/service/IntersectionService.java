package com.main.service;

import com.main.Util.publicStaticParam;
import com.main.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class IntersectionService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Intersectionv2> getIntersection(String[] bounds) {
        String latlon1 = bounds[0].replace(","," ");
        String latlon2= bounds[1].replace(","," ");
        String latlon3=bounds[2].replace(","," ");
        String latlon4=bounds[3].replace(","," ");
        String latlons="POLYGON(("+latlon1+","+latlon2+","+latlon3+","+latlon4+","+latlon1;
      //  String sql1="SELECT id,link_ids, ST_AsText(wkt) as wkt ,road_id,name,type,direction  FROM dalian_road where ST_Intersects(wkt,ST_GeomFromText('"+latlons+"))',4326))";
        String sql="SELECT id,intersection_id,center_x,center_y, ST_AsText(center) as center,radius ,name  FROM "+ publicStaticParam.intersection+" where ST_Intersects(center,ST_GeomFromText('"+latlons+"))',4326))";
        List<Intersectionv2> Road_list=jdbcTemplate.query(sql, new RowMapper<Intersectionv2>(){
            @Override
            public Intersectionv2 mapRow(ResultSet rs, int rowNum) throws SQLException {
                Intersectionv2 intersection = new Intersectionv2();
                intersection.setId(rs.getInt("id"));
                intersection.setIntersection_id(rs.getString("intersection_id"));
                intersection.setCenter_y(rs.getString("center_y"));
                intersection.setCenter_x(rs.getString("center_x"));
                intersection.setRadius(rs.getString("radius"));
                double [] centerArr={Double.parseDouble(rs.getString("center_x")),Double.parseDouble(rs.getString("center_y"))};
                intersection.setCenter(centerArr);
                intersection.setName(rs.getString("name"));
                return intersection;
            }

        });
            return Road_list;
    }


    public void deleteIntersection(String[] ids_array) {

        String IntersectionIds="";
//        for(int i=0;i<ids_array.length;i++){
//            if (i==0){
//                IntersectionIds=ids_array[i];
//            }else{
//                IntersectionIds=IntersectionIds+", "+ids_array[i];
//            }
//
//        }

        for(int i=0;i<ids_array.length;i++){
            if (i==0){
                IntersectionIds="'"+ids_array[i]+"'";
            }else{
                IntersectionIds=IntersectionIds+", '"+ids_array[i]+"'";
            }

        }
        String deleteSql="delete from "+publicStaticParam.intersection+" where intersection_id in ("+IntersectionIds+")";
        String deleteSql2="delete from "+publicStaticParam.middle_intersection+"  where intersection_id in ("+IntersectionIds+")";
       // System.out.println(deleteSql);
       // System.out.println(deleteSql2);
        jdbcTemplate.execute(deleteSql);
        jdbcTemplate.execute(deleteSql2);
    }

    public List<NodeforAlive>  getnodeBound(String[] bounds) {
        String latlon1 = bounds[0].replace(","," ");
        String latlon2= bounds[1].replace(","," ");
        String latlon3=bounds[2].replace(","," ");
        String latlon4=bounds[3].replace(","," ");
        String latlons="POLYGON(("+latlon1+","+latlon2+","+latlon3+","+latlon4+","+latlon1;
        String boundSql="select * from dalian_intersection where ST_Intersects(center,ST_GeomFromText('"+latlons+"))',4326))";
        String sql="SELECT id,intersection_id,center_x,center_y, ST_AsText(center) as center,radius ,name  FROM "+publicStaticParam.intersection+" where ST_Intersects(center,ST_GeomFromText('"+latlons+"))',4326))";

        List<Intersectionv2> intersection_list=jdbcTemplate.query(sql, new RowMapper<Intersectionv2>(){
            @Override
            public Intersectionv2 mapRow(ResultSet rs, int rowNum) throws SQLException {
                Intersectionv2 intersection = new Intersectionv2();
                intersection.setId(rs.getInt("id"));
                intersection.setIntersection_id(rs.getString("intersection_id"));
                intersection.setCenter_y(rs.getString("center_y"));
                intersection.setCenter_x(rs.getString("center_x"));
                intersection.setRadius(rs.getString("radius"));
                double [] centerArr={Double.parseDouble(rs.getString("center_x")),Double.parseDouble(rs.getString("center_y"))};
                intersection.setCenter(centerArr);
                intersection.setName(rs.getString("name"));
                return intersection;
            }

        });
        String IntersectionIds="";
        for(int i=0;i<intersection_list.size();i++) {
            if (i == 0) {
                IntersectionIds = "'" + intersection_list.get(i).getIntersection_id() + "'";
            } else {
                IntersectionIds = IntersectionIds + ", '" + intersection_list.get(i).getIntersection_id() + "'";
            }

        }
        String deleteSql2 = "select * from (select *  from "+publicStaticParam.middle_intersection+"  where intersection_id in (" + IntersectionIds + ")"+")aa left join "+publicStaticParam.node+" as bb on aa.node_id=bb.id::character varying";

        List<NodeforAlive> node_list=jdbcTemplate.query(deleteSql2, new RowMapper<NodeforAlive>(){
            @Override
            public NodeforAlive mapRow(ResultSet rs, int rowNum) throws SQLException {
                NodeforAlive intersection = new NodeforAlive();
                intersection.setIntersection(rs.getString("intersection_id"));
                intersection.setId(rs.getLong("id")+"");

                StringBuffer latbuf=new StringBuffer(rs.getLong("lat")+"".trim());
                latbuf.insert(2,".");
                StringBuffer lonbuf=new StringBuffer(rs.getLong("lon")+"".trim());
                lonbuf.insert(3,".");

                String[] lonlat={lonbuf.toString(),latbuf.toString()};
                intersection.setCoordinates(lonlat);
                return intersection;
            }

        });
        return node_list;
    }

    public List<target> getTargetBound(String[] bounds) {
        String latlon1 = bounds[0].replace(","," ");
        String latlon2= bounds[1].replace(","," ");
        String latlon3=bounds[2].replace(","," ");
        String latlon4=bounds[3].replace(","," ");
        String latlons="POLYGON(("+latlon1+","+latlon2+","+latlon3+","+latlon4+","+latlon1;
        //String boundSql="select * from targetv1 where ST_Intersects(center,ST_GeomFromText('"+latlons+"))',4326))";
        String sql="SELECT id, name, comment_name, type, lng, lat, camera, police_region_id, forward_id, relation_jnc_id, ignore_alert, ST_AsText(wkt) as wkt from targetsv1 where ST_Intersects(wkt,ST_GeomFromText('"+latlons+"))',4326))";
//String sql1="id, name, comment_name, type, lng, lat, camera, police_region_id, forward_id, relation_jnc_id, ignore_alert, wkt from targetv1";
        List<target> intersection_list=jdbcTemplate.query(sql, new RowMapper<target>(){
            @Override
            public target mapRow(ResultSet rs, int rowNum) throws SQLException {
                target intersection = new target();
                intersection.setId(rs.getInt("id"));
                intersection.setName(rs.getString("name"));
                intersection.setWkt(rs.getString("wkt"));
                return intersection;
            }

        });
       return intersection_list;
    }
}
