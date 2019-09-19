package com.main.service;

import com.main.Util.publicStaticParam;
import com.main.model.Link;
import com.main.model.LinkComplete;
import com.main.model.Updata_LinkName;
import com.main.model.Ways;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class WaysService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public List<Ways> getListOfBound(String[] bounds) {
//        private String ways_id;
//        private String geom_way;
//        private String osm_id;
//        private String osm_name;
//        private String osm_source_id;
//        private String osm_target_id;
        String latlon1 = bounds[0].replace(","," ");
        String latlon2= bounds[1].replace(","," ");
        String latlon3=bounds[2].replace(","," ");
        String latlon4=bounds[3].replace(","," ");
        String latlons="POLYGON(("+latlon1+","+latlon2+","+latlon3+","+latlon4+","+latlon1;
        //  String latlons_end=latlons_first+")',4326))";
        String sql="SELECT id,ways_id,osm_id,clazz, ST_AsText(geom_way) as geom_way ,osm_name,osm_source_id,osm_target_id  FROM "+ publicStaticParam.ways_name+" where ST_Intersects(geom_way,ST_GeomFromText('"+latlons+"))',4326))";
        System.out.println("----------------------------");
        System.out.println(sql);

        return (List<Ways>) jdbcTemplate.query(sql, new RowMapper<Ways>(){
            @Override
            public Ways mapRow(ResultSet rs, int rowNum) throws SQLException {
                Ways ways = new Ways();
                ways.setId(rs.getString("id"));
                ways.setGeom_way(rs.getString("geom_way"));
                ways.setWays_id(rs.getString("ways_id"));
                ways.setOsm_id(rs.getString("osm_id"));
                ways.setOsm_name(rs.getString("osm_name"));
                ways.setOsm_source_id(rs.getString("osm_source_id"));
                ways.setOsm_target_id(rs.getString("osm_target_id"));
                ways.setClazz(rs.getString("clazz"));

                return ways;
            }

        });
    }

    public List<Link> LinkMerge(String[] waysid) {
        String waysids="";
        for(int i=0;i<waysid.length;i++){
            if (i==0){
                waysids="'"+waysid[i]+"'";
            }else{
                waysids=waysids+", '"+waysid[i]+"'";
            }

        }

        String sql1="SELECT \"id\",ways_id,osm_id,clazz,  ST_AsText(geom_way) as geom_way,osm_name,osm_source_id,osm_target_id  FROM hh_2po_4pgrv2 where ways_id in ("+waysids+")";
        List<Ways> ways_all= jdbcTemplate.query(sql1, new RowMapper<Ways>(){
            @Override
            public Ways mapRow(ResultSet rs, int rowNum) throws SQLException {
                Ways ways = new Ways();
                ways.setId(rs.getString("id"));
                ways.setGeom_way(rs.getString("geom_way"));
                ways.setWays_id(rs.getString("ways_id"));
                ways.setOsm_id(rs.getString("osm_id"));
                ways.setOsm_name(rs.getString("osm_name"));
                ways.setOsm_source_id(rs.getString("osm_source_id"));
                ways.setOsm_target_id(rs.getString("osm_target_id"));
                ways.setClazz(rs.getString("clazz"));

                return ways;
            }

        });
        List<Ways> sort_List=new ArrayList<Ways>();
        String out_id=ways_all.get(0).getOsm_target_id();

        //将OUT_Id的顺序排起来
        for (int i = 0; i <ways_all.size() ; i++) {
            if (i==0){
                if(null==sort_List){
                    sort_List=new ArrayList<Ways>();
                }
               // System.out.println("最开始的数据为"+ways_all.get(i));
                //  sort_List.add(links_all.get(i));
                sort_List.add(ways_all.get(i));
            }else {
                //   Long out_ids=sort_List.get(i-1).getOut_id();
                for (int j = 1; j <ways_all.size() ; j++) {
                    if (out_id.equals(ways_all.get(j).getOsm_source_id())){
                       // System.out.println("lalala");
                        sort_List.add(ways_all.get(j)); ;

                    }
                }
                out_id=ways_all.get(i).getOsm_target_id();
            }


        }


        //将IN_Id的顺序 也要假如
        String in_id =sort_List.get(0).getOsm_source_id();
        for (int i = 0; i <ways_all.size() ; i++) {

            // Long in_id=   sort_List.get(i-1).getIn_id();
            for (int j = 1; j <ways_all.size() ; j++) {
                if (in_id.equals(ways_all.get(j).getOsm_target_id())){
                    sort_List.add(0,ways_all.get(j)); ;

                }
            }
            in_id=sort_List.get(0).getOsm_source_id();


        }
        List<Link> links_all_number= jdbcTemplate.query("select id::integer from t_b_sectionv4 order by id desc limit 1", new RowMapper<Link>(){
            @Override
            public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
                Link link = new Link();
                link.setId(rs.getString("id"));

                return link;
            }

        });
        int id =Integer.parseInt(links_all_number.get(0).getId())+1;
        String ways_ids="";
        String wkt="";
        for (int i = 0; i <sort_List.size() ; i++) {
            if (i==0){
                ways_ids=sort_List.get(i).getWays_id();
                wkt="ST_GeomFromText('"+sort_List.get(i).getGeom_way()+"',4326)";
            }else{
                ways_ids=ways_ids+"|"+sort_List.get(i).getWays_id();
                wkt=wkt+",ST_GeomFromText('"+sort_List.get(i).getGeom_way()+"',4326)";
            }

        }

        wkt="ST_Union(array["+wkt+"])";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
       // System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        //sort_List 存储着 排序之后的 LINK_ID 现在开始聚合Link_id
        String newLink_id=sort_List.get(0).getOsm_source_id()+"-"+sort_List.get(sort_List.size()-1).getOsm_target_id();

        if(sort_List.size()==ways_all.size()){
           // System.out.println("数据排序完全正确");
            String MergeSql="insert into t_b_sectionv4 values('"+id+"','"+ways_ids+"',"+wkt+",'"+sort_List.get(0).getOsm_source_id()+"','"+sort_List.get(sort_List.size()-1).getOsm_target_id()+"','"+sort_List.get(0).getOsm_name()+"','7','"+ways_all.get(0).getClazz()+"',st_length("+wkt+",false),'10','"+newLink_id+"');";
            jdbcTemplate.execute(MergeSql);
            //插入数据到删除表中
            //  String InsertSql= "insert into delete_link select * from t_b_sectionv4 where link_id in("+waysids+")";
            //删除原始数据表
         //   String DeleteSql="delete from t_b_sectionv4 where link_id where link_id in ("+waysids+")";
            //添加数据到中间表中 来表示数据由哪里合并的

            String IntertOriginal="insert into original values('"+newLink_id+"','"+waysids.replace(",","|").replace("'","")+"','"+df.format(new Date())+"','Way')";
//
            jdbcTemplate.execute(IntertOriginal);

            String sql3 ="SELECT \"id\",link_id,type, ST_AsText(wkt) as wkt ,ways_ids,name,in_id,out_id  FROM t_b_sectionv4 where link_id in ('"+newLink_id+"')";

            String result_query_sql ="SELECT id,link_id,type, ST_AsText(wkt) as wkt ,ways_ids,name,in_id,out_id  FROM t_b_sectionv4 where link_id in ("+newLink_id+")";
           // System.out.println("result_query_sql:"+result_query_sql);
            List<Link> MergeWays= jdbcTemplate.query(sql3, new RowMapper<Link>(){
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
                    return link;
                }

            });
                if(MergeWays.size()!=0){
                    return MergeWays;
                }else{
                    return null;
                }
        }else{
            return null;
        }


    }

    public List<Ways> UpdataWays(Updata_LinkName updataname) {

        String name= "'"+updataname.getName()+"'";
        String link_id="";
        for (int i = 0; i < updataname.getLink_id().size(); i++) {
            if(i==0){
                link_id="'"+updataname.getLink_id().get(0)+"'";
            }else{
                link_id=link_id+",'"+updataname.getLink_id().get(i)+"'";
            }
        }
        String sql="UPDATE hh_2po_4pgrv2 SET osm_name = "+name+" WHERE ways_id in ("+link_id+")";
       // System.out.println("sql updata"+sql);
        jdbcTemplate.update(sql);
        String sql1="SELECT \"id\",ways_id,osm_id,clazz,  ST_AsText(geom_way) as geom_way,osm_name,osm_source_id,osm_target_id  FROM hh_2po_4pgrv2 where ways_id in ("+link_id+")";
        List<Ways> ways_all= jdbcTemplate.query(sql1, new RowMapper<Ways>(){
            @Override
            public Ways mapRow(ResultSet rs, int rowNum) throws SQLException {
                Ways ways = new Ways();
                ways.setId(rs.getString("id"));
                ways.setGeom_way(rs.getString("geom_way"));
                ways.setWays_id(rs.getString("ways_id"));
                ways.setOsm_id(rs.getString("osm_id"));
                ways.setOsm_name(rs.getString("osm_name"));
                ways.setOsm_source_id(rs.getString("osm_source_id"));
                ways.setOsm_target_id(rs.getString("osm_target_id"));
                ways.setClazz(rs.getString("clazz"));

                return ways;
            }

        });
      return ways_all;
    }
}
