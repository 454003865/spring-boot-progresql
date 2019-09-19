package com.main.service;

import com.main.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AmapService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public  List<Amapbound> binding(AmapBind amapbind) {
        //查询完的数据返回的
        List<Amapbound> list1 = new ArrayList<Amapbound>();
        List<List<Double>> boundslist=amapbind.getBounds();
        List<AmapPoint> amapPoint=amapbind.getData();
        //删除掉之前相同ID的数据
        String ids="";
        for (int i = 0; i <amapPoint.size() ; i++) {

        }
        for (int i = 0; i <amapPoint.size() ; i++) {
          String lat84=  amapPoint.get(i).getLat84();
            String lng84=  amapPoint.get(i).getLng84();
            String boundString=boundslist.get(0).get(0)+" "+boundslist.get(0).get(1)+","+boundslist.get(1).get(0)+" "+boundslist.get(1).get(1)+","+
                    boundslist.get(2).get(0)+" "+boundslist.get(2).get(1)+","+boundslist.get(3).get(0)+" "+boundslist.get(3).get(1)+","+boundslist.get(0).get(0)+" "+boundslist.get(0).get(1);
           // System.out.println("boundString:"+boundString);
            String selectsql="select id,link_id,wkt,line,point_geom,point_distance,ST_LineLocatePoint(wkt,point_geom) as point_line_scala,st_asText(ST_ClosestPoint(wkt,point_geom)) as pointOnLine from (SELECT id,link_id,ST_LineMerge(wkt) as wkt,st_asText(wkt) as line,ST_GeomFromText('POINT("+lng84+" "+lat84+")',4326) as point_geom,ST_Distance_Sphere(st_Point("+lng84+","+lat84+"),wkt) as point_distance from t_b_sectionv4 " +
                    "where ST_Intersects(wkt,ST_GeomFromText('POLYGON(("+boundString+"))',4326)) order by ST_Distance_Sphere(st_Point("+lng84+","+lat84+"),wkt) asc limit 1) as abc";
            //ST_Intersects(wkt,ST_GeomFromText('POLYGON((121.5988418346 38.9143956122,121.6278509873 38.9138833146,121.6038894653 38.9013869366,121.6344451904 38.9033239895,121.5988418346 38.9143956122))',4326))

           // System.out.println("sql="+selectsql);
            List<Amapbound> Road_list=jdbcTemplate.query(selectsql, new RowMapper<Amapbound>(){
                @Override
                public Amapbound mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Amapbound amapbound = new Amapbound();
                    //amapbound.setId(rs.getInt("id"));
                    amapbound.setLine(rs.getString("line"));
                    amapbound.setLink_id(rs.getString("link_id"));
                    amapbound.setPoint_distance(rs.getString("point_distance"));
                    amapbound.setPoint_line_scala(rs.getDouble("point_line_scala"));
                    amapbound.setPointOnLine(rs.getString("pointOnLine"));
                   // System.out.println(amapbound);
                    return amapbound;

                }

            });
            String Newlng84=Road_list.get(0).getPointOnLine().split(" ")[0].replace("POINT(","").replace("'","");
            String Newlat84=Road_list.get(0).getPointOnLine().split(" ")[1].replace(")","").replace("'","");
            //INSERT INTO public.amap_link(
            //	id, name, province, city, district, positon_type, zone_used_id, x, y, lng, lat, lng84, lat84, forward_id, service_tags, wkt)
            //	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
            String insertSqlamapPoint="INSERT INTO amap_point(id, name, province, city, district, positon_type, zone_used_id, x, y, lng, lat, lng84, lat84, forward_id, service_tags, wkt,link_id) " +
                    "VALUES ("+amapPoint.get(i).getId()+", '"+amapPoint.get(i).getName()+"', '"+amapPoint.get(i).getProvince()+"', '"+amapPoint.get(i).getCity()+"', '"+amapPoint.get(i).getDistrict()+"',"+amapPoint.get(i).getPositon_type()+", "+amapPoint.get(i).getZone_used_id()+", "+amapPoint.get(i).getX()+", "+amapPoint.get(i).getY()+", '"+amapPoint.get(i).getLng()+"', '"+amapPoint.get(i).getLat()+"','"+Newlng84+"', '"+Newlat84+"', "+amapPoint.get(i).getForward_id()+", '"+amapPoint.get(i).getService_tags()+"',"
                    +"ST_GeomFromText('POINT("+Newlng84+" "+Newlat84+")',4326)"+",'"+Road_list.get(0).getLink_id()+"')";
            //ST_GeomFromText('POINT("+Newlng84+" "+Newlat84+")',4326)
            //st_Point("+Newlng84+","+Newlat84+")
           // System.out.println("insertSqlamap:"+insertSqlamapPoint);
            String insertAmapLink="INSERT INTO amap_link VALUES ('"+Road_list.get(0).getLink_id()+"',"+amapPoint.get(i).getId()+")";
           // System.out.println("AmapService.binding insertAmapLink :"+insertAmapLink);

            jdbcTemplate.execute(insertSqlamapPoint);
            jdbcTemplate.execute(insertAmapLink);

            list1.addAll(Road_list);
        }
        return list1;
       // String selectsql2="select id,link_id,wkt,line,point_geom,point_distance,ST_LineLocatePoint(wkt,point_geom),st_asText(ST_ClosestPoint(wkt,point_geom)) from (SELECT id,link_id,wkt,st_asText(wkt) as line,ST_GeomFromText('POINT(121.618392944336 38.909812927246)',4326) as point_geom,ST_Distance_Sphere(st_Point(121.618392944336,38.909812927246),wkt) as point_distance from t_b_sectionv4 order by ST_Distance_Sphere(st_Point(121.618392944336,38.909812927246),wkt) asc limit 1) as abc";



    }

    public List<AmapPointSimple> Amapindex(String[] bounds) {
        String latlon1 = bounds[0].replace(","," ");
        String latlon2= bounds[1].replace(","," ");
        String latlon3=bounds[2].replace(","," ");
        String latlon4=bounds[3].replace(","," ");
        String latlons="POLYGON(("+latlon1+","+latlon2+","+latlon3+","+latlon4+","+latlon1;

        String sql="SELECT id,name,lng84,lat84, link_id  FROM amap_point where ST_Intersects(wkt,ST_GeomFromText('"+latlons+"))',4326))";
        List<AmapPointSimple> point_list=jdbcTemplate.query(sql, new RowMapper<AmapPointSimple>(){
            @Override
            public AmapPointSimple mapRow(ResultSet rs, int rowNum) throws SQLException {
                AmapPointSimple amapbound = new AmapPointSimple();
                amapbound.setId(rs.getInt("id"));
                amapbound.setLat84(rs.getString("lat84"));
                amapbound.setLng84(rs.getString("lng84"));
                amapbound.setLink_id(rs.getString("link_Id"));
                amapbound.setName(rs.getString("name"));
                return amapbound;

            }

        });

        return point_list;
    }

    public void Amapupdate(AmapUpdate amapupdate) {

        List<List<Double>> boundslist= amapupdate.getBounds();
        int id =amapupdate.getId();
        String lng84=amapupdate.getCoordinates().get(0);
        String lat84=amapupdate.getCoordinates().get(1);
        String boundString=boundslist.get(0).get(0)+" "+boundslist.get(0).get(1)+","+boundslist.get(1).get(0)+" "+boundslist.get(1).get(1)+","+
                boundslist.get(2).get(0)+" "+boundslist.get(2).get(1)+","+boundslist.get(3).get(0)+" "+boundslist.get(3).get(1)+","+boundslist.get(0).get(0)+" "+boundslist.get(0).get(1);

        String selectsql="select id,link_id,wkt,line,point_geom,point_distance,ST_LineLocatePoint(wkt,point_geom) as point_line_scala,st_asText(ST_ClosestPoint(wkt,point_geom)) as pointOnLine from (SELECT id,link_id,ST_LineMerge(wkt) as wkt,st_asText(wkt) as line,ST_GeomFromText('POINT("+lng84+" "+lat84+")',4326) as point_geom,ST_Distance_Sphere(st_Point("+lng84+","+lat84+"),wkt) as point_distance from t_b_sectionv4 " +
                "where ST_Intersects(wkt,ST_GeomFromText('POLYGON(("+boundString+"))',4326)) order by ST_Distance_Sphere(st_Point("+lng84+","+lat84+"),wkt) asc limit 1) as abc";
        List<Amapbound> Road_list=jdbcTemplate.query(selectsql, new RowMapper<Amapbound>(){
            @Override
            public Amapbound mapRow(ResultSet rs, int rowNum) throws SQLException {
                Amapbound amapbound = new Amapbound();
               // amapbound.setId(rs.getInt("id"));
                amapbound.setLine(rs.getString("line"));
                amapbound.setLink_id(rs.getString("link_id"));
                amapbound.setPoint_distance(rs.getString("point_distance"));
                amapbound.setPoint_line_scala(rs.getDouble("point_line_scala"));
                amapbound.setPointOnLine(rs.getString("pointOnLine"));
               // System.out.println(amapbound);
                return amapbound;

            }
        });
        String Newlng84=Road_list.get(0).getPointOnLine().split(" ")[0].replace("POINT(","").replace("'","");
        String Newlat84=Road_list.get(0).getPointOnLine().split(" ")[1].replace(")","").replace("'","");

        String updateSql="update amap_point set link_id='"+Road_list.get(0).getLink_id()+"',lng84='"+Newlng84+"',lat84='"+Newlat84+"',wkt=ST_GeomFromText('POINT("+Newlng84+" "+Newlat84+")',4326) where id="+id;
        String updateSql2="update amap_link set link_id='"+Road_list.get(0).getLink_id()+"' where amap_id="+id;

        jdbcTemplate.execute(updateSql);
        jdbcTemplate.execute(updateSql2);
       // System.out.println(updateSql);
    }

    public void Amapdelete(String[] ids) {
        String waysids="";
        for(int i=0;i<ids.length;i++){
            if (i==0){
                waysids="'"+ids[i]+"'";
            }else{
                waysids=waysids+", '"+ids[i]+"'";
            }

        }
        for (int i = 0; i <ids.length ; i++) {
            String selectsql = "select id,forward_id from amap_point where id ="+ids[i];
               List<AmapPoint> Road_list=jdbcTemplate.query(selectsql, new RowMapper<AmapPoint>(){
                @Override
                public AmapPoint mapRow(ResultSet rs, int rowNum) throws SQLException {
                    AmapPoint amapPoint = new AmapPoint();
                    // amapbound.setId(rs.getInt("id"));
                    amapPoint.setId(rs.getInt("id"));
                    amapPoint.setForward_id(rs.getInt("forward_id"));
                    return amapPoint;

                }
            });
            String updatesql = "update amap_point set forward_id="+Road_list.get(0).getForward_id() +"where forward_id="+ids[i];
            jdbcTemplate.execute(updatesql);
        }
        String sql1 ="delete from  amap_point where id in ("+waysids+")";
        String sql2 ="delete from  amap_link where amap_id in ("+waysids+")";
        jdbcTemplate.execute(sql1);
        jdbcTemplate.execute(sql2);



    }
}
