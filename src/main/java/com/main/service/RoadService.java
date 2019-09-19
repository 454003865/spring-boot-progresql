package com.main.service;

import com.main.Util.publicStaticParam;
import com.main.model.Link;
import com.main.model.Road;
import com.main.model.RoadAndLinkConmerge;
import com.main.model.Road_link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class RoadService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Road> getRounds(String[] bounds) {
        String latlon1 = bounds[0].replace(","," ");
        String latlon2= bounds[1].replace(","," ");
        String latlon3=bounds[2].replace(","," ");
        String latlon4=bounds[3].replace(","," ");
        String latlons="POLYGON(("+latlon1+","+latlon2+","+latlon3+","+latlon4+","+latlon1;
        //  String latlons_end=latlons_first+")',4326))";
        String sql="SELECT id,link_ids, ST_AsText(wkt) as wkt ,road_id,name,type,direction,direction_name  FROM dalian_road where ST_Intersects(wkt,ST_GeomFromText('"+latlons+"))',4326))";
       // System.out.println("----------------------------");
       // System.out.println("RoadService.getRounds: "+sql);

        String sqlRoad="SELECT road_id,link_ids, ST_AsText(wkt) as wkt ,droad_id,droad_name,droad_type,droad_angle FROM "+publicStaticParam.road_direction+" where ST_Intersects(wkt,ST_GeomFromText('"+latlons+"))',4326))";
        List<Road> Road_list=jdbcTemplate.query(sqlRoad, new RowMapper<Road>(){
            @Override
            public Road mapRow(ResultSet rs, int rowNum) throws SQLException {
                Road road = new Road();
                road.setId(rs.getInt("droad_id"));
                road.setWkt(rs.getString("wkt"));
                road.setName(rs.getString("droad_name"));
                road.setRoad_id(rs.getString("droad_id"));
                road.setLinksIds(rs.getString("link_ids").replace("[","").replace("\"","").replace("]","").split(","));
                road.setDirection(rs.getInt("droad_angle"));
                String wktlist[]=rs.getString("wkt").replace("LINESTRING(","").replace(")\"","").split(",");
                road.setType(rs.getString("droad_type"));
                String[]  ingeom= new String[2];
                ingeom[0] = wktlist[0].split(" ")[0];
                ingeom[1] = wktlist[0].split(" ")[1];
                road.setIn_coordinates(ingeom);
                String[]  outgeom=new String[2];
                outgeom[0] = wktlist[wktlist.length-1].split(" ")[0];
                outgeom[1] = wktlist[wktlist.length-1].split(" ")[1].replace(")","");
                road.setOut_coordinates(outgeom);
                return road;
            }

        });

       // return  Road_list;

        for (int i = 0; i <Road_list.size() ; i++) {
            String[] links=Road_list.get(i).getLinksIds();
            String links_ids="";
            for (int j = 0; j <links.length ; j++) {
                if (j==0){
                    links_ids="'"+links[j]+"'";
                }else {
                    links_ids=links_ids+",'"+links[j]+"'";
                }

            }
            System.out.println("links_ids"+links_ids);
            String sql3 ="SELECT id,link_id, wkt ,ways_ids,name,in_id,out_id  FROM "+ publicStaticParam.link_name+"  where link_id in ("+links_ids+")";

            List<Link> RecoverReductionLinks= jdbcTemplate.query(sql3, new RowMapper<Link>(){
                @Override
                public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Link link = new Link();
                    link.setId(rs.getString("id"));
                    link.setLink_id(rs.getString("link_id"));
                    link.setWkt(rs.getString("wkt"));
                    link.setWays_ids(rs.getString("ways_ids"));
                    link.setName(rs.getString("name"));
                    link.setIn_id(rs.getLong("in_id"));
                    link.setOut_id(rs.getLong("out_Id"));
                    String wktlist[]=rs.getString("wkt").replace("LINESTRING(","").replace(")\"","").split(",");
                    String[]  ingeom= new String[2];
                    ingeom[0] = wktlist[0].split(" ")[0];
                    ingeom[1] = wktlist[0].split(" ")[1];
                    link.setIn_coordinates(ingeom);
                    String[]  outgeom=new String[2];
                    outgeom[0] = wktlist[wktlist.length-1].split(" ")[0];
                    outgeom[1] = wktlist[wktlist.length-1].split(" ")[1].replace(")","");
                    link.setOut_coordinates(outgeom);
                    return link;
                }


            });
            Road_list.get(i).setLinks(RecoverReductionLinks);
        }


        return Road_list;
    }

    public List<Road> updataOfId(List<String> list2, String updataname,String direction_name) {
        String road_ids="";
        for (int i = 0; i < list2.size(); i++) {
            if(i==0){
                road_ids="'"+list2.get(0)+"'";
            }else{
                road_ids=road_ids+",'"+list2.get(i)+"'";
            }
        }
        String upnameSql ="update dalian_road set name='"+updataname+"' ,direction_name ='"+direction_name+"'  where road_id in ("+road_ids+")";
        jdbcTemplate.execute(upnameSql);

        String sql="SELECT id,link_ids, ST_AsText(wkt) as wkt ,road_id,name,type,direction,direction_name  FROM dalian_road where road_id in ("+road_ids+")";
       // System.out.println("----------------------------");
       // System.out.println("RoadService.getRounds: "+sql);

        List<Road> Road_list=jdbcTemplate.query(sql, new RowMapper<Road>(){
            @Override
            public Road mapRow(ResultSet rs, int rowNum) throws SQLException {
                Road road = new Road();
                road.setId(rs.getInt("id"));
                road.setWkt(rs.getString("wkt"));
                road.setName(rs.getString("name"));
                road.setRoad_id(rs.getString("road_id"));
                road.setLinksIds(rs.getString("link_ids").split("\\|"));
                road.setDirection(rs.getInt("direction"));
                String wktlist[]=rs.getString("wkt").replace("LINESTRING(","").replace(")\"","").split(",");
                road.setType(rs.getString("type"));
                road.setDirection_name(rs.getString("direction_name"));
                String[]  ingeom= new String[2];
                ingeom[0] = wktlist[0].split(" ")[0];
                ingeom[1] = wktlist[0].split(" ")[1];
                road.setIn_coordinates(ingeom);
                String[]  outgeom=new String[2];
                outgeom[0] = wktlist[wktlist.length-1].split(" ")[0];
                outgeom[1] = wktlist[wktlist.length-1].split(" ")[1].replace(")","");
                road.setOut_coordinates(outgeom);
                return road;
            }

        });
        if (Road_list.size()!=0&&Road_list!=null){
            return Road_list;
        }else{
            return null;
        }
    }

    /**
     * 删除数据
     * @param list2
     * @return
     */
    public void deleteOfId(List<String> list2) {

        String road_ids="";
        for (int i = 0; i < list2.size(); i++) {
            if(i==0){
                road_ids="'"+list2.get(0)+"'";
            }else{
                road_ids=road_ids+",'"+list2.get(i)+"'";
            }
        }
        String sql="SELECT id,link_ids, ST_AsText(wkt) as wkt ,road_id,name,type,direction  FROM dalian_road where road_id in ("+road_ids+")";
        String deleteSql1 ="delete from  dalian_road where road_id in ("+road_ids+")";
        String deleteSql2 ="delete from  link_road where road_id in ("+road_ids+")";
       // System.out.println("----------------------------");
       // System.out.println("RoadService.getRounds: "+sql);
        jdbcTemplate.execute(deleteSql1);
        jdbcTemplate.execute(deleteSql2);
        List<Road> Road_list=jdbcTemplate.query(sql, new RowMapper<Road>(){
            @Override
            public Road mapRow(ResultSet rs, int rowNum) throws SQLException {
                Road road = new Road();
                road.setId(rs.getInt("id"));
                road.setWkt(rs.getString("wkt"));
                road.setName(rs.getString("name"));
                road.setRoad_id(rs.getString("road_id"));
                road.setLinksIds(rs.getString("link_ids").split("\\|"));
                road.setDirection(rs.getInt("direction"));
                String wktlist[]=rs.getString("wkt").replace("LINESTRING(","").replace(")\"","").split(",");
                road.setType(rs.getString("type"));
                String[]  ingeom= new String[2];
                ingeom[0] = wktlist[0].split(" ")[0];
                ingeom[1] = wktlist[0].split(" ")[1];
                road.setIn_coordinates(ingeom);
                String[]  outgeom=new String[2];
                outgeom[0] = wktlist[wktlist.length-1].split(" ")[0];
                outgeom[1] = wktlist[wktlist.length-1].split(" ")[1].replace(")","");
                road.setOut_coordinates(outgeom);
                return road;
            }

        });

    }


    /**
     * 合并多个road和link的数据
     * @param amapupdate
     */
    public void conmergeLink(RoadAndLinkConmerge amapupdate) {
        //给LINK id 的wkt聚合起来
        String linkidsWkt="";
        List<String> link_ids=amapupdate.getLink_ids();
        for (int i = 0; i <amapupdate.getLink_ids().size() ; i++) {
            if (i==0){
                linkidsWkt= "'"+link_ids.get(i)+"'";
            }else {
                linkidsWkt=linkidsWkt+",'"+link_ids.get(i)+"'";
            }
        }


        String RoadidsWkt="";
        List<String> Roadids=amapupdate.getRoad_ids();
        for (int i = 0; i <Roadids.size() ; i++) {
            if (i==0){
                RoadidsWkt= "'"+Roadids.get(i)+"'";
            }else {
                RoadidsWkt=RoadidsWkt+",'"+Roadids.get(i)+"'";
            }
        }

        String RoadWktSql="select ST_AsText(ST_LineMerge(ST_Union(wkt))) as wkt  from (select droad_id,wkt from "+
                publicStaticParam.road_direction+" where droad_id in("+RoadidsWkt+")) aa ";
       // System.out.println("RoadService.RoadWktSql::"+RoadWktSql);
        List<Link> RoadWktString=jdbcTemplate.query(RoadWktSql, new RowMapper<Link>(){
            @Override
            public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
                Link link = new Link();
                link.setWkt(rs.getString("wkt"));
                return link;
            }

        });
        String updataRoadSql="";
        //测试出如若link_ID为空 则
        if (amapupdate.getLink_ids().size()>0){
            String linkWktSql="select ST_AsText(ST_LineMerge(ST_Union(wkt))) as wkt from (select id,link_id,wkt_geom as wkt from "+publicStaticParam.link_name+" where link_id in("+linkidsWkt+")) aa ";
           // System.out.println("linkWktSql:"+linkWktSql);
            List<Link> LinkWktString=jdbcTemplate.query(linkWktSql, new RowMapper<Link>(){
                @Override
                public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Link link = new Link();
                    link.setWkt(rs.getString("wkt"));
                    return link;
                }

            });
            updataRoadSql="update "+publicStaticParam.road_direction+" SET wkt=ST_LineMerge(ST_Union(ST_GeomFromText('"+RoadWktString.get(0).getWkt()+"',4326),ST_GeomFromText('"+LinkWktString.get(0).getWkt()+"',4326))) WHERE droad_id ='"+amapupdate.getMain_road_id()+"'";

        }else {
             updataRoadSql="update "+publicStaticParam.road_direction+" SET wkt=ST_LineMerge(ST_GeomFromText('"+RoadWktString.get(0).getWkt()+"',4326)) WHERE droad_id ='"+amapupdate.getMain_road_id()+"'";

        }

       // System.out.println("updataRoadSql:"+updataRoadSql);

        jdbcTemplate.execute(updataRoadSql);




        String deleteRoadidsWkt="";
      //  List<String> deleteRoadids=amapupdate.getRoad_ids().remove();

//        for (String s : Roadids) {
//           // System.out.println(s);
//            if (amapupdate.getMain_road_id().equals(s)) {
//                Roadids.remove(s);
//            }
//        }
        //将整体的road_Id数据中的main数据删除
        for (int i = 0; i < Roadids.size(); i++) {

            if (Roadids.get(i).equals(amapupdate.getMain_road_id())) {

                Roadids.remove(i);

                i--;

            }

        }

        for (int i = 0; i <Roadids.size() ; i++) {

            if (i==0){
                deleteRoadidsWkt= "'"+Roadids.get(i)+"'";
            }else {
                deleteRoadidsWkt=deleteRoadidsWkt+",'"+Roadids.get(i)+"'";
            }
        }
       // System.out.println("RoadidsWkt"+RoadidsWkt);
       // System.out.println("deleteRoadidsWkt"+deleteRoadidsWkt);
       // System.out.println("main="+amapupdate.getMain_road_id());
        //如果road_ID只有一个 那就不执行这个修改和删除语句

        for (int i = 0; i < amapupdate.getLink_ids().size(); i++) {
            String insertSql="INSERT INTO "+publicStaticParam.linkandRoad+" (road_id, link_id) VALUES ('"+amapupdate.getMain_road_id()+"','"+amapupdate.getLink_ids().get(i)+"');";
            jdbcTemplate.execute(insertSql);
        }
        //改变LINK_ID整体改变到LINK_IDS
        String selectRoadsql="select link_id,road_id from "+publicStaticParam.linkandRoad+" where road_id in ("+RoadidsWkt+")";
        List<Road_link> road_links=jdbcTemplate.query(selectRoadsql, new RowMapper<Road_link>(){
            @Override
            public Road_link mapRow(ResultSet rs, int rowNum) throws SQLException {
                Road_link link = new Road_link();
                link.setLink_ids(rs.getString("link_id"));
                link.setRoad_id(rs.getString("road_id"));
                return link;
            }

        });
        String linkids="[";
        for (int i = 0; i < road_links.size(); i++) {
            if (i==0){
                linkids= linkids+"\""+ road_links.get(i).getLink_ids()+"\"";
            }else {
                linkids=linkids+",\""+road_links.get(i).getLink_ids()+"\"";
            }

        }

        for (int i = 0; i <amapupdate.getLink_ids().size() ; i++) {
            linkids=linkids+",\""+amapupdate.getLink_ids().get(i)+"\"";
        }
        linkids=linkids+"]";
        String updataRoad2Sql="update "+publicStaticParam.road_direction+" SET link_ids='"+linkids+"' WHERE road_id ='"+amapupdate.getMain_road_id()+"'";
       // System.out.println("updataRoadSql:"+updataRoad2Sql);

        jdbcTemplate.execute(updataRoad2Sql);

        System.out.println("deleteRoadidsWkt删除的数据为"+deleteRoadidsWkt);
        if (Roadids.size()>0){
           // System.out.println("已经执行修改和删除语句");
            String deleteRoadSql="delete from  "+publicStaticParam.road_direction+" where droad_id in ("+deleteRoadidsWkt+")";
            String deleteRoadSql2="update  "+publicStaticParam.linkandRoad+"  SET road_id="+amapupdate.getMain_road_id()+" where road_id in ("+deleteRoadidsWkt+")";
            jdbcTemplate.execute(deleteRoadSql);
            jdbcTemplate.execute(deleteRoadSql2);

        }
        String updatesql="select road_id from "+publicStaticParam.road_direction +"where droad_id = '"+amapupdate.getMain_road_id()+"'";

        List<String> road_id_new=jdbcTemplate.query(selectRoadsql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {

                return rs.getString("road_id");
            }

        });
            //要给总表的wkt重新加入 并且给长度重新计算





    }
}
