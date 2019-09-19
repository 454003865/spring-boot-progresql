package com.main.service;

import com.main.Util.CalulateTwoLanLon;
import com.main.Util.publicStaticParam;
import com.main.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class LinkService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据传入的ID集合  来进行LINK数据的合并 返回合并后的数据
     * 1.先查询出Link表里的数据
     * 2.对Link表的数据进行整合 尤其WKT数据
     *
     * @param ids
     * @return List<Link>
     */
    public  List<Link> getList(String[] ids){

        //String sql = "SELECT link_id, wkt,ways_ids,name,in_id,out_Id  FROM t_b_sectionv4 limit 10 ";
        String linkids="";
        for(int i=0;i<ids.length;i++){
            if (i==0){
                linkids="'"+ids[i]+"'";
           }else{
                linkids=linkids+", '"+ids[i]+"'";
            }

        }
//要加入一个List来判断是否最后都可以聚合起来

        String sql1 ="SELECT id, ways_ids, wkt_geom, wkt, in_id, out_id, name, angle, type, length, up_ids, link_id," +
                " starting_point, end_point, up_intersection, end_intersection FROM "+publicStaticParam.link_name+" where link_id in ("+linkids+")";
       // System.out.println("接受完 完整的参数为"+sql1);
        List<LinkComplete> links_all= jdbcTemplate.query(sql1, new RowMapper<LinkComplete>(){
            @Override
            public LinkComplete mapRow(ResultSet rs, int rowNum) throws SQLException {
                LinkComplete link = new LinkComplete();
                link.setId(rs.getString("id"));
                link.setLink_id(rs.getString("link_id"));
                link.setWkt_geom(rs.getString("wkt_geom"));
                link.setWkt(rs.getString("wkt"));
                link.setWays_ids(rs.getString("ways_ids"));
                link.setName(rs.getString("name"));
                link.setIn_id(rs.getLong("in_id"));
                link.setOut_id(rs.getLong("out_Id"));
                link.setAngle(rs.getInt("angle"));
                link.setType(rs.getLong("type"));
                link.setLength(rs.getDouble("length"));
                link.setUp_ids(rs.getString("up_ids"));
                link.setStarting_point(rs.getString("starting_point"));
                link.setEnd_point(rs.getString("end_point"));
                link.setUp_intersection(rs.getLong("up_intersection"));
                link.setEnd_intersection(rs.getLong("end_intersection"));
                System.out.println("返回数据为"+link.toString());
                return link;
            }

        });
        if(links_all.size()==0){
            return null;
        }

        List<LinkComplete> sort_List=new ArrayList<LinkComplete>();
        Long out_id=links_all.get(0).getOut_id();

        //将OUT_Id的顺序排起来
        for (int i = 0; i <links_all.size() ; i++) {
            if (i==0){
                if(null==sort_List){
                    sort_List=new ArrayList<LinkComplete>();
                }
               // System.out.println("最开始的数据为"+links_all.get(i));
              //  sort_List.add(links_all.get(i));
                sort_List.add(links_all.get(i));
            }else {
          //   Long out_ids=sort_List.get(i-1).getOut_id();
                for (int j = 1; j <links_all.size() ; j++) {
                    if (out_id.equals(links_all.get(j).getIn_id())){
                        //System.out.println("lalala");
                        sort_List.add(links_all.get(j)); ;

                    }
                }
                out_id=links_all.get(i).getOut_id();
            }


        }
        //复原指针

        //将IN_Id的顺序 也要假如
        Long in_id =sort_List.get(0).getIn_id();
        for (int i = 0; i <links_all.size() ; i++) {

               // Long in_id=   sort_List.get(i-1).getIn_id();
                for (int j = 1; j <links_all.size() ; j++) {
                    if (in_id.equals(links_all.get(j).getOut_id())){
                        sort_List.add(0,links_all.get(j)); ;

                    }
                }
            in_id=sort_List.get(0).getIn_id();


        }
        List<Link> links_all_number= jdbcTemplate.query("select id::integer from "+publicStaticParam.link_name+" order by id desc limit 1", new RowMapper<Link>(){
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
                ways_ids=sort_List.get(i).getWays_ids();
                wkt="ST_GeomFromText('"+sort_List.get(i).getWkt()+"',4326)";
            }else{
                ways_ids=ways_ids+"|"+sort_List.get(i).getWays_ids();
                wkt=wkt+",ST_GeomFromText('"+sort_List.get(i).getWkt()+"',4326)";
            }

        }
        wkt="ST_LineMerge(ST_Union(array["+wkt+"]))";
        //获取当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
      System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        //sort_List 存储着 排序之后的 LINK_ID 现在开始聚合Link_id
        String newLink_id=sort_List.get(0).getIn_id()+"-"+sort_List.get(sort_List.size()-1).getOut_id();
           if(sort_List.size()==links_all.size()){
               System.out.println("数据排序完全正确");
               System.out.println("ways_ids"+ways_ids);
//               String MergeSql="insert into "+publicStaticParam.link_name+"  " +
//                       "values('"+id+"','"+ways_ids+"',"+wkt+",'"+sort_List.get(0).getIn_id()+"','"+sort_List.get(sort_List.size()-1).getOut_id()+"'," +
//                       "'"+sort_List.get(0).getName()+"','7','"+links_all.get(0).getType()+"',st_length("+wkt+",false),'10','"+newLink_id+"');";
            //   String MergeSql02="INSERT INTO "+publicStaticParam.link_name+"( " +
            //           "id, ways_ids, wkt_geom, wkt, in_id, out_id, name, angle, type, length, up_ids, link_id, starting_point, end_point, up_intersection, end_intersection)" +
             //          " "+6+", "+sort_List.get(0).getType()+",st_length("+wkt+",false),'"+9+"','"+newLink_id+"','"+sort_List.get(0).getStarting_point()+"','"+sort_List.get(sort_List.size()-1).getEnd_point()+"',"+sort_List.get(0).getUp_intersection()+","+sort_List.get(sort_List.size()-1).getEnd_intersection()+")";


               String MergeSql03="INSERT INTO "+publicStaticParam.link_name+"( " +
                       "id, ways_ids, wkt_geom, wkt, in_id, out_id, name, angle, type, length, up_ids, link_id, starting_point, end_point, up_intersection, end_intersection) VALUES " +
                       "("+id+",'"+ways_ids+"',"+wkt+",ST_asText("+wkt+"),'"+sort_List.get(0).getIn_id()+"','"+sort_List.get(sort_List.size()-1).getOut_id()+"','"+sort_List.get(0).getName()+"',"+
                       " "+6+", "+sort_List.get(0).getType()+",st_length("+wkt+",false),'"+9+"','"+newLink_id+"','"+sort_List.get(0).getStarting_point()+"','"+sort_List.get(sort_List.size()-1).getEnd_point()+"',"+sort_List.get(0).getUp_intersection()+","+sort_List.get(sort_List.size()-1).getEnd_intersection()+")";

               System.out.println("MergeSql02"+MergeSql03);
               //插入数据到删除表中
               String InsertSql= "insert into "+publicStaticParam.deletelink+" select *,'"+df.format(new Date())+"'as insert_time from "+publicStaticParam.link_name+" where link_id in("+linkids+")";
               //删除原始数据表
               String DeleteSql="delete from "+publicStaticParam.link_name+"  where link_id in ("+linkids+")";
               //添加数据到中间表中 来表示数据由哪里合并的

               String IntertOriginal="insert into "+publicStaticParam.delete_plugin +" values('"+newLink_id+"','"+linkids.replace(",","|").replace("'","")+"','"+df.format(new Date())+"','Link')";
               System.out.println("InsertSql:"+MergeSql03);
              System.out.println("DeleteSql:"+DeleteSql);
               System.out.println("IntertOriginal:"+IntertOriginal);
              jdbcTemplate.execute(MergeSql03);
               jdbcTemplate.execute(InsertSql);
               jdbcTemplate.execute(DeleteSql);
              jdbcTemplate.execute(IntertOriginal);

               String sql3 ="SELECT id,link_id,type,  wkt ,ways_ids,name,in_id,out_id  FROM "+publicStaticParam.link_name+" where link_id in ('"+newLink_id+"')";
            // String result_query_sql ="SELECT id,link_id,type, ST_AsText(wkt) as wkt ,ways_ids,name,in_id,out_id  FROM t_b_sectionv4 where link_id in ("+newLink_id+")";
            System.out.println("sql3:"+sql3);
               List<Link> sql3return = jdbcTemplate.query(sql3, new RowMapper<Link>(){
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

              // System.out.println("LinkService.getList"+sql3return.get(0).toString());
               return sql3return;
           }else{
               return null;
           }





    }

    /**
     * 根据传入的ID集合  来进行LINK数据的合并 返回合并后的数据
     * 1.先查询出Link表里的数据
     * 2.对Link表的数据进行整合 尤其WKT数据
     *
     * @param ids
     * @return List<Link>
     */
//    public  List<Link> getList2(String[] ids){
//
//
//        String linkids="";
//        for(int i=0;i<ids.length;i++){
//            if (i==0){
//                linkids="'"+ids[i]+"'";
//            }else{
//                linkids=linkids+", '"+ids[i]+"'";
//            }
//
//        }
//        //要加入一个List来判断是否最后都可以聚合起来 where link_id in ("+linkids+")"
//        String sql1="select  ST_ASText(ST_LineMerge(ST_Union(wkt))) from (select wkt from "+publicStaticParam.link_name+" where link_id in ("+linkids+")) tablea";
//        jdbcTemplate.execute(sql1);
//        //MULTILINESTRING((
//       List<Link>  mutString=jdbcTemplate.query(sql1, new RowMapper<Link>(){
//            @Override
//            public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
//                Link link = new Link();
//                link.setWkt(rs.getString("wkt"));
//                return link;
//            }
//
//        });
//
//        String sql2 ="SELECT \"id\",link_id,type, ST_AsText(wkt) as wkt ,ways_ids,name,in_id,out_id  FROM t_b_sectionv4 where link_id in ("+linkids+")";
//       // System.out.println("接受完 完整的参数为"+sql1);
//        List<LinkComplete> links_all= jdbcTemplate.query(sql2, new RowMapper<LinkComplete>(){
//            @Override
//            public LinkComplete mapRow(ResultSet rs, int rowNum) throws SQLException {
//                LinkComplete link = new LinkComplete();
//                link.setId(rs.getString("id"));
//                link.setLink_id(rs.getString("link_id"));
//                link.setWkt(rs.getString("wkt"));
//                link.setWays_ids(rs.getString("ways_ids"));
//                link.setName(rs.getString("name"));
//                link.setIn_id(rs.getLong("in_id"));
//                link.setOut_id(rs.getLong("out_Id"));
//                link.setType(rs.getLong("type"));
//                return link;
//            }
//
//        });
//       //如果是多线的情况 就返回聚合不了 就返回null了
//        if (mutString.get(0).getWkt().contains("MULTILINE")){
//            return null;
//        }
//        String sql3="select * from ";
//
//
//        return null;
//
//    }

//
//
//    public List<Link> getListOfBound(String[] bounds) {
//
//       // String sql="SELECT link_id, ST_AsText(wkt) as wkt ,ways_ids,name,in_id,out_id  FROM t_b_sectionv4 where ST_Intersects(wkt,ST_GeomFromText('++'))";
//        //121.5988418346 38.9143956121,121.6278509873 38.9138833146,121.6038894653 38.9013869366,121.6344451904 38.9033239895,121.5988418346 38.9143956121))',4326))
//       // String latlons="";
//            String latlon1 = bounds[0].replace(","," ");
//            String latlon2= bounds[1].replace(","," ");
//            String latlon3=bounds[2].replace(","," ");
//            String latlon4=bounds[3].replace(","," ");
//        String latlons="POLYGON(("+latlon1+","+latlon2+","+latlon3+","+latlon4+","+latlon1;
//      //  String latlons_end=latlons_first+")',4326))";
//        String sql="SELECT id,link_id, ST_AsText(wkt) as wkt ,ways_ids,name,in_id,out_id  FROM t_b_sectionv4 where ST_Intersects(wkt,ST_GeomFromText('"+latlons+"))',4326))";
//       // System.out.println("----------------------------");
//       // System.out.println(sql);
//        return (List<Link>) jdbcTemplate.query(sql, new RowMapper<Link>(){
//            @Override
//            public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
//                Link link = new Link();
//                link.setId(rs.getString("id"));
//                link.setLink_id(rs.getString("link_id"));
//                link.setWkt(rs.getString("wkt"));
//                link.setWays_ids(rs.getString("ways_ids"));
//                link.setName(rs.getString("name"));
//                link.setIn_id(rs.getLong("in_id"));
//                link.setOut_id(rs.getLong("out_Id"));
//                return link;
//            }
//
//        });
//    }

    /**
     *
     * @param bounds
     * @return
     */
    public List<Link_apart> getListOfBoundV2(String[] bounds) {

        // String sql="SELECT link_id, ST_AsText(wkt) as wkt ,ways_ids,name,in_id,out_id  FROM t_b_sectionv4 where ST_Intersects(wkt,ST_GeomFromText('++'))";
        //121.5988418346 38.9143956121,121.6278509873 38.9138833146,121.6038894653 38.9013869366,121.6344451904 38.9033239895,121.5988418346 38.9143956121))',4326))
        // String latlons="";
//        String latlon1 = bounds.get(0).replace(","," ");
//        String latlon2= bounds.get(1).replace(","," ");
//        String latlon3=bounds.get(2).replace(","," ");
//        String latlon4=bounds.get(3).replace(","," ");
        System.out.println("LinkService.getListOfBoundV2接受到了数据");
        String latlon1 = bounds[0].replace(","," ");
        String latlon2= bounds[1].replace(","," ");
        String latlon3=bounds[2].replace(","," ");
        String latlon4=bounds[3].replace(","," ");
        String latlons="POLYGON(("+latlon1+","+latlon2+","+latlon3+","+latlon4+","+latlon1;
        String latlons2="'POLYGON(("+latlon1+","+latlon2+","+latlon3+","+latlon4+","+latlon1+"))'";
        //  String latlons_end=latlons_first+")',4326))";
        String sql_apart="select id,link_id, ST_AsText(wkt) as wkt,in_coordinates,out_coordinates,ways_ids,name,in_id,out_id,apart_state " +
                "from (select aa.*,bb.oiginal_link,CASE WHEN bb.oiginal_link is null or  trim(bb.oiginal_link)='' THEN 0 ELSE 1 END  as apart_state " +
                "from (SELECT id,link_id, ST_AsText(wkt_geom) as wkt ,ST_AsText(ST_StartPoint(wkt_geom)) as in_coordinates,ST_AsText(ST_EndPoint(wkt_geom)) as out_coordinates " +
                ",ways_ids,name,in_id,out_id  FROM  "+publicStaticParam.link_name+"  where ST_Intersects(wkt_geom,ST_GeomFromText("+latlons2+",4326))) aa left join original bb on aa.link_id=bb.link_id ) as cc ";


      System.out.println("----------------------------");
       System.out.println("sql_apart:"+sql_apart+new Date().toString());
        return (List<Link_apart>) jdbcTemplate.query(sql_apart, new RowMapper<Link_apart>(){
            @Override
            public Link_apart mapRow(ResultSet rs, int rowNum) throws SQLException {
                Link_apart link = new Link_apart();
                link.setId(rs.getString("id"));
                link.setLink_id(rs.getString("link_id"));
                link.setWkt(rs.getString("wkt"));
                link.setWays_ids(rs.getString("ways_ids"));
                link.setName(rs.getString("name"));
                link.setIn_id(rs.getLong("in_id"));
                link.setOut_id(rs.getLong("out_Id"));
                link.setApart_state(rs.getInt("apart_state"));
               // System.out.println("in_coordinates"+rs.getString("in_coordinates"));

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
    }

    //表替换为测试表了 t_b_sectionv4
    public List<Link> updataOfId(Updata_LinkName links) {

        String name= "'"+links.getName()+"'";
        String link_id="";
        for (int i = 0; i < links.getLink_id().size(); i++) {
            if(i==0){
                link_id="'"+links.getLink_id().get(0)+"'";
            }else{
                link_id=link_id+",'"+links.getLink_id().get(i)+"'";
            }
        }
       String sql="UPDATE t_b_sectionv4 SET name = "+name+" WHERE link_id in ("+link_id+")";
       // System.out.println("sql updata"+sql);
        jdbcTemplate.update(sql);
       // System.out.println("LinkService.updataOfId sql: "+sql);
        String sql1 ="SELECT \"id\",link_id, ST_AsText(wkt) as wkt ,ways_ids,name,in_id,out_id  FROM t_b_sectionv4 where link_id in ("+link_id+")";
       // System.out.println("接受完 完整的参数为"+sql1);
       List<Link> updataResult= jdbcTemplate.query(sql1, new RowMapper<Link>(){
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
       if(updataResult.size()==0){
           return null;
       }else{
           return updataResult;
       }
        //运行查询成功 看是否查询出来的数据和要删除的数据一致性

    }
    //表替换为测试表了 t_b_sectionv4

    /**
     * 删除LINK 不涉及还原表
     * @param ids
     * @return
     */
    public List<Link> deleteOfId(String[] ids) {
        String linkids="";
        for(int i=0;i<ids.length;i++){
            if (i==0){
                linkids="'"+ids[i]+"'";
            }else{
                linkids=linkids+", '"+ids[i]+"'";
            }

        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String sql1 = "insert into delete_link select *,'"+df.format(new Date())+"' as insert_time from t_b_sectionv4 where link_id in("+linkids+")";
        String sql2 ="delete from  t_b_sectionv4 where link_id in ("+linkids+")";
        String sql3 ="select *  from  delete_link where link_id in ("+linkids+")";
       // System.out.println(sql1+"\n"+sql2);
          jdbcTemplate.execute(sql1);
        jdbcTemplate.execute(sql2);
        List<Link> DeleteLinks= jdbcTemplate.query(sql3, new RowMapper<Link>(){
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
                link.setInsert_time("insert_time");
                return link;
            }


        });
        if(DeleteLinks.size()==0){
            return null;
        }else{
            return DeleteLinks;
        }
    }

    /**
     * 还原数据到原始表中
     * 从中间表中 original捞出数据
     * 在从删除表中捞出original 所整合前的数据
     * 删除所有结果表的数据
     * @param ids
     * @return
     */
    public List<Original> reductionById(String[] ids) {

        for (int i = 0; i <ids.length ; i++) {
           // System.out.println("接到数据为"+ids[i]);
        }
        String linkids="";
        for(int i=0;i<ids.length;i++){
            if (i==0){
                linkids="'"+ids[i]+"'";
            }else{
                linkids=linkids+", '"+ids[i]+"'";
            }

        }

        String sql="select * from original where link_id in ("+linkids+") ";
       // System.out.println("sql"+sql);
        List<Original> oldList= jdbcTemplate.query(sql, new RowMapper<Original>(){
            @Override
            public Original mapRow(ResultSet rs, int rowNum) throws SQLException {
                Original original = new Original();
                original.setLink_Id(rs.getString("link_id"));
                original.setInsert_time(rs.getString("insert_time"));
                original.setOiginal_link(rs.getString("oiginal_link").split("\\|"));
                original.setLink_from(rs.getString("Link_from"));

                return original;
            }

        });
        //如果要还原的数据没有在还原表里  则返回null
            if(oldList.size()==0){
                return null;
            }

        String old_linkids="";
        //聚合所有的数据到
        for (int i = 0; i <oldList.size() ; i++) {
            for (int j = 0; j <oldList.get(i).getOiginal_link().length; j++) {
                if (old_linkids==null||old_linkids.equals("")){
                    old_linkids ="'"+oldList.get(i).getOiginal_link()[j].trim()+"'";
                }else{
                    old_linkids=old_linkids+",'"+oldList.get(i).getOiginal_link()[j].trim()+"'";

                }

            }
            oldList.get(i).getOiginal_link();
        }
        //1.插入数据到原始表中
        String sql1="insert into t_b_sectionv4  select  id,ways_ids,wkt,in_id,out_id,name,direction,type,length,up_ids,link_id from  delete_link where link_id in("+old_linkids+")";
        //2.删除数据在删除数据表
        String sql2 ="delete from delete_link where link_id in ("+old_linkids+")";
        //3.
        //String sql3="delete from original where where link_id in ("+linkids+")";
        String sql3="delete from original where link_id in ("+linkids+")";
       // System.out.println("sql"+sql);
       // System.out.println("sql1"+sql1);
       // System.out.println("sql2"+sql2);
       // System.out.println("sql3"+sql3);
        jdbcTemplate.execute(sql1);
        jdbcTemplate.execute(sql2);
        jdbcTemplate.execute(sql3);
       // System.out.println("LinkService.reductionById"+oldList.get(0).toString());
                return oldList;
    }

    /**
     * 查看所有回收站的数据
     * @return
     */
    public List<Link> recover() {
        String sql ="select *,ST_AsText(wkt) as wkt2 from delete_link";
        List<Link> recoverLinks= jdbcTemplate.query(sql, new RowMapper<Link>(){
            @Override
            public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
                Link link = new Link();
                link.setId(rs.getString("id"));
                link.setLink_id(rs.getString("link_id"));
                link.setWkt(rs.getString("wkt2"));
                link.setWays_ids(rs.getString("ways_ids"));
                link.setName(rs.getString("name"));
                link.setIn_id(rs.getLong("in_id"));
                link.setOut_id(rs.getLong("out_Id"));
                link.setInsert_time(rs.getString("insert_time"));
                return link;
            }


        });

            return recoverLinks;

    }

    /**
     * 还原回收站数据 到原始数据表中
     * @param ids_array
     * @return
     */
    public List<Link> recoverReduction(String[] ids_array) {
        String linkids="";
        for(int i=0;i<ids_array.length;i++){
            if (i==0){
                linkids="'"+ids_array[i]+"'";
            }else{
                linkids=linkids+", '"+ids_array[i]+"'";
            }

        }
        String sql1 ="insert into  "+publicStaticParam.link_name+" select  id, ways_ids, wkt_geom, wkt, in_id, out_id, name, angle, type, length, up_ids, link_id, starting_point, end_point, up_intersection, end_intersection from "+publicStaticParam.deletelink+" where link_id in ("+linkids+")";
        String sql2="delete from "+publicStaticParam.deletelink+" where link_id in ("+linkids+")";
       // System.out.println("sql1 "+sql1);
        jdbcTemplate.execute(sql1);
        jdbcTemplate.execute(sql2);
        String sql3 ="select *  from  "+publicStaticParam.link_name+" where link_id in ("+linkids+")";

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
                return link;
            }


        });
        if (RecoverReductionLinks.size()!=0){
            return RecoverReductionLinks;
        }else{
            return null;
        }

    }

    /**
     * 将Link聚合成road
     * @param ids_array
     * @param link_name
     * @return
     */
        public List<Road> LinktoRoad(String[] ids_array, String link_name,String direction_name) {
            //String sql = "SELECT link_id, wkt,ways_ids,name,in_id,out_Id  FROM t_b_sectionv4 limit 10 ";
            String linkids="";
            for(int i=0;i<ids_array.length;i++){
                if (i==0){
                    linkids="'"+ids_array[i]+"'";
                }else{
                    linkids=linkids+", '"+ids_array[i]+"'";
                }

            }
//要加///一个List来判断是否最后都可以聚合起来  373198671-373198787 373198787-373198744 373198744-373198756

            String sql1 ="SELECT \"id\",link_id,type, ST_AsText(wkt) as wkt ,ways_ids,name,in_id,out_id  FROM t_b_sectionv4 where link_id in ("+linkids+")";
           // System.out.println("接受完 完整的参数为"+sql1);
            List<LinkComplete> links_all= jdbcTemplate.query(sql1, new RowMapper<LinkComplete>(){
                @Override
                public LinkComplete mapRow(ResultSet rs, int rowNum) throws SQLException {
                    LinkComplete link = new LinkComplete();
                    link.setId(rs.getString("id"));
                    link.setLink_id(rs.getString("link_id"));
                    link.setWkt(rs.getString("wkt"));
                    link.setWays_ids(rs.getString("ways_ids"));
                    link.setName(rs.getString("name"));
                    link.setIn_id(rs.getLong("in_id"));
                    link.setOut_id(rs.getLong("out_Id"));
                    link.setType(rs.getLong("type"));
                    return link;
                }

            });


            if(links_all.size()==0){
                return null;
            }

            List<LinkComplete> sort_List=new ArrayList<LinkComplete>();
            Long out_id=links_all.get(0).getOut_id();

            //将OUT_Id的顺序排起来
            for (int i = 0; i <links_all.size() ; i++) {
                if (i==0){
                    if(null==sort_List){
                        sort_List=new ArrayList<LinkComplete>();
                    }
                   // System.out.println("最开始的数据为"+links_all.get(i));
                    //  sort_List.add(links_all.get(i));
                    sort_List.add(links_all.get(i));
                }else {
                    //   Long out_ids=sort_List.get(i-1).getOut_id();
                    for (int j = 1; j <links_all.size() ; j++) {
                        if (out_id.equals(links_all.get(j).getIn_id())){
                            //System.out.println("lalala");
                            sort_List.add(links_all.get(j)); ;

                        }
                    }
                    out_id=links_all.get(i).getOut_id();
                }


            }
            //复原指针

            //将IN_Id的顺序 也要加入
            Long in_id =sort_List.get(0).getIn_id();
            for (int i = 0; i <links_all.size() ; i++) {

                // Long in_id=   sort_List.get(i-1).getIn_id();
                for (int j = 1; j <links_all.size() ; j++) {
                    if (in_id.equals(links_all.get(j).getOut_id())){
                        sort_List.add(0,links_all.get(j)); ;

                    }
                }
                in_id=sort_List.get(0).getIn_id();


            }
            //根据数据生成roadid 和 wkt地理信息
            String ways_ids="";
            String wkt="";
            for (int i = 0; i <links_all.size() ; i++) {
                if (i==0){
                    ways_ids=links_all.get(i).getLink_id();
                    wkt="ST_GeomFromText('"+links_all.get(i).getWkt()+"',4326)";
                }else{
                    ways_ids=ways_ids+"|"+links_all.get(i).getLink_id();
                    wkt=wkt+",ST_GeomFromText('"+links_all.get(i).getWkt()+"',4326)";
                }

            }
            wkt="ST_LineMerge(ST_Union(array["+wkt+"]))";
//road_id link_ids name type wkt direction id
            // String IntertOriginal="insert into original values('"+newLink_id+"','"+linkids.replace(",","|").replace("'","")+"','"+df.format(new Date())+"','Link')";
            //取出最大的ID
            List<Road> links_all_number= jdbcTemplate.query("select id::integer from dalian_road order by id desc limit 1", new RowMapper<Road>(){
                @Override
                public Road mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Road road = new Road();
                    //link.setId(rs.getString("id"));
                    road.setId(rs.getInt("id"));
                    return road;
                }

            });
            int id=1;
            if(links_all_number.size()!=0){
                 id =links_all_number.get(0).getId()+1;
            }




            String roadid=sort_List.get(0).getIn_id()+"-"+sort_List.get(sort_List.size()-1).getOut_id();
            String insertsql="insert into dalian_road values('"+roadid+"','"+ways_ids+"','"+link_name+"',+'"+sort_List.get(0).getType()+"',"+wkt+","+5+","+id+",'"+direction_name+"')";
           // System.out.println("insertsql:"+insertsql);
            jdbcTemplate.execute(insertsql);
           //// System.out.println(insertsql);
            //获取当前时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            df.setTimeZone(TimeZone.getTimeZone("GMT+8"));



                    for (int i = 0; i <links_all.size() ; i++) {
                        String linkid=  links_all.get(i).getLink_id();
                        String InsertRoadLink="insert into link_road values('"+roadid+"','"+linkid+"')";
                        jdbcTemplate.execute(InsertRoadLink);
                    }
                String sqlRoad="SELECT id,link_ids, ST_AsText(wkt) as wkt ,road_id,name,type,direction  FROM dalian_road where road_id='"+roadid+"'";
                List<Road> Road_list=jdbcTemplate.query(sqlRoad, new RowMapper<Road>(){
                    @Override
                    public Road mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Road road = new Road();
                        road.setId(rs.getInt("id"));
                        road.setWkt(rs.getString("wkt"));
                        road.setName(rs.getString("name"));
                        road.setRoad_id(rs.getString("road_id"));
                        //road.setLinksIds(rs.getString("link_ids").split("\\|"));
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
                //String InsertRoadLink="insert into link_road values('"+roadid+"','"++"')";
               // System.out.println("LinkService.LinktoRoad"+Road_list.get(0).toString());
                return  Road_list;



        }




    /**
     * 将Link聚合成road
     * @param ids_array
     * @param link_name
     * @return
     */
    public List<Road> LinktoRoad2(RoadMerge roadmerge) {
        roadmerge.setDroad_name(roadmerge.getDirection_name());
        System.out.println("roadmerge = [" + roadmerge + "]");
        //String sql = "SELECT link_id, wkt,ways_ids,name,in_id,out_Id  FROM t_b_sectionv4 limit 10 ";
        String linkids="";

        for(int i=0;i< roadmerge.getLink_ids().size();i++){
            if (i==0){
                linkids="'"+ roadmerge.getLink_ids().get(i)+"'";
            }else{
                linkids=linkids+", '"+roadmerge.getLink_ids().get(i)+"'";
            }

        }

//        "road_id": 231,
//        "road_name": "山东路",
//        "droad_name":"山东路东行",
//        "link_ids": ["1000343572-1000343834", "1000343572-1000343834", "1000343572-1000343834"]

//要加///一个List来判断是否最后都可以聚合起来  373198671-373198787 373198787-373198744 373198744-373198756
    //不存在就插入的逻辑
//        INSERT INTO test_postgre(id,name,InputTime,age)
//        VALUES('1','postgre','2018-01-10 22:00:00',24)
//        ON conflict(id)
//                DO UPDATE SET name = 'postgreOk', InputTime ='2018-02-22 12:00:00'

   //这步是在做 是否有重复名字的步骤 如果有id就不进去循环  如果没有ID则需要插入数据在主表中
        if(roadmerge.getRoad_id()==0){

            String querymaxid="select road_id::integer from "+publicStaticParam.road_main+" order by road_id::integer DESC";
            List<Integer> links_all12= jdbcTemplate.query(querymaxid, new RowMapper<Integer>(){
                @Override
                public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {

                    return rs.getInt("road_id");
                }

            });
            int maxid=1;
            if (links_all12.size()>0){
                maxid=links_all12.get(0)+1;
            }
            String querysql="select road_name from "+ publicStaticParam.road_main +" where road_name= '"+roadmerge.getRoad_name()+"'";
            System.out.println("querysql"+querysql);
            List<LinkComplete> links_all= jdbcTemplate.query(querysql, new RowMapper<LinkComplete>(){
                @Override
                public LinkComplete mapRow(ResultSet rs, int rowNum) throws SQLException {
                    LinkComplete link = new LinkComplete();
                    link.setName(rs.getString("road_name"));
                    return link;
                }

            });
            if (links_all.size()==0){
                String insertRoadWaysql="INSERT INTO "+publicStaticParam.road_main+" ( road_id, road_name, road_type) values" +
                        "('"+maxid+"', '"+roadmerge.getRoad_name()+"',1)";
                System.out.println("insertRoadWaysql"+insertRoadWaysql);
                jdbcTemplate.execute(insertRoadWaysql);

            }else{
                return  null;
            }
        }else{

            String querysql2="select road_name,road_id from "+ publicStaticParam.road_main +" where road_id= '"+roadmerge.getRoad_id()+"'";
            System.out.println("querysql"+querysql2);
            List<LinkComplete> links_all2= jdbcTemplate.query(querysql2, new RowMapper<LinkComplete>(){
                @Override
                public LinkComplete mapRow(ResultSet rs, int rowNum) throws SQLException {
                    LinkComplete link = new LinkComplete();
                    link.setName(rs.getString("road_name"));
                    link.setId(rs.getString("road_id"));
                    return link;
                }

            });
            roadmerge.setRoad_name(links_all2.get(0).getName());
        }

        String sql1 ="SELECT id,link_id,type, wkt ,ways_ids,name,in_id,out_id  FROM "+publicStaticParam.link_name+" where link_id in ("+linkids+")";
         System.out.println("接受完 完整的参数为"+sql1);
        List<LinkComplete> links_all= jdbcTemplate.query(sql1, new RowMapper<LinkComplete>(){
            @Override
            public LinkComplete mapRow(ResultSet rs, int rowNum) throws SQLException {
                LinkComplete link = new LinkComplete();
                link.setId(rs.getString("id"));
                link.setLink_id(rs.getString("link_id"));
                link.setWkt(rs.getString("wkt"));
                link.setWays_ids(rs.getString("ways_ids"));
                link.setName(rs.getString("name"));
                link.setIn_id(rs.getLong("in_id"));
                link.setOut_id(rs.getLong("out_Id"));
                link.setType(rs.getLong("type"));
                return link;
            }

        });

        if(links_all.size()==0){
            return null;
        }


        //根据数据生成roadid 和 wkt地理信息
        String link_ids="[";
        String wkt="";
        for (int i = 0; i <links_all.size() ; i++) {
            if (i==0){
                link_ids=link_ids+"\""+links_all.get(i).getLink_id()+"\"";
                wkt="ST_GeomFromText('"+links_all.get(i).getWkt()+"',4326)";
            }else{
                link_ids=link_ids+",\""+links_all.get(i).getLink_id()+"\"";
                wkt=wkt+",ST_GeomFromText('"+links_all.get(i).getWkt()+"',4326)";
            }
            System.out.println(link_ids);
        }
        wkt="ST_LineMerge(ST_Union(array["+wkt+"]))";
        link_ids=link_ids+"]";
        System.out.println(link_ids);
//road_id link_ids name type wkt direction id
        // String IntertOriginal="insert into original values('"+newLink_id+"','"+linkids.replace(",","|").replace("'","")+"','"+df.format(new Date())+"','Link')";
        //取出最大的ID
        List<Road> links_all_number= jdbcTemplate.query("select droad_id::integer from "+publicStaticParam.road_direction+" order by droad_id::integer desc limit 1", new RowMapper<Road>(){
            @Override
            public Road mapRow(ResultSet rs, int rowNum) throws SQLException {
                Road road = new Road();
                //link.setId(rs.getString("id"));
                road.setId(rs.getInt("droad_id"));
                return road;
            }

        });
        int id=1;
        if(links_all_number.size()!=0){
            id =links_all_number.get(0).getId()+1;
        }


        System.out.println("id:"+id +"|||"+wkt);

        //int roadid=links_all_number.get(0).getId()+1;
       // String insertsql="insert into dalian_road values('"+roadid+"','"+ways_ids+"','"+link_name+"',+'"+sort_List.get(0).getType()+"',"+wkt+","+5+","+id+",'"+direction_name+"')";
        // System.out.println("insertsql:"+insertsql);
      // jdbcTemplate.execute(insertsql);
        //// System.out.println(insertsql);
        //获取当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        String querysql2="select road_name,road_id from "+ publicStaticParam.road_main +" where road_name= '"+roadmerge.getRoad_name()+"'";
        System.out.println("querysql"+querysql2);
        List<LinkComplete> links_all2= jdbcTemplate.query(querysql2, new RowMapper<LinkComplete>(){
            @Override
            public LinkComplete mapRow(ResultSet rs, int rowNum) throws SQLException {
                LinkComplete link = new LinkComplete();
                link.setName(rs.getString("road_name"));
                link.setId(rs.getString("road_id"));
                return link;
            }

        });
        System.out.println(querysql2);
//st_length("+wkt+",false)
        String roadid="1";
        if(links_all2.size()!=0){
            roadid =links_all2.get(0).getId();
        }
      //  String  roadid=links_all2.get(0).getId();
//        String insert_directionSql="INSERT INTO "+publicStaticParam.road_direction+"(road_id, droad_id, link_ids, droad_name, droad_type, droad_angle, droad_length, wkt)" +
//                "VALUES ('"+roadid+"', '"+id+"', '"+link_ids+"', '"+roadmerge.getDroad_name()+"', "+links_all.get(0).getType()+", "+0+", st_length("+wkt+",false), '"+wkt+"');";

        String insert_directionSql2="INSERT INTO "+publicStaticParam.road_direction+"(road_id, droad_id, link_ids, droad_name, droad_type, droad_angle, droad_length, wkt)" +
                "VALUES ('"+roadid+"', '"+id+"', '"+link_ids+"', '"+roadmerge.getDroad_name()+"', "+links_all.get(0).getType()+", "+0+",st_length("+wkt+",false), "+wkt+")";


        System.out.println("insert_directionSql2"+insert_directionSql2);
        jdbcTemplate.execute(insert_directionSql2);

        for (int i = 0; i <links_all.size() ; i++) {
            String linkid=  links_all.get(i).getLink_id();
            String InsertRoadLink="insert into "+publicStaticParam.linkandRoad+" values('"+roadid+"','"+linkid+"')";
            jdbcTemplate.execute(InsertRoadLink);
        }
        String updatemain="UPDATE "+publicStaticParam.road_main+" SET  road_type="+links_all.get(0).getType()+
                " WHERE road_id='"+roadid+"'";
        //更改主表的数据
        jdbcTemplate.execute(updatemain);



        String selectangle="select ST_X(ST_StartPoint(wkt)) as x1,ST_Y(ST_StartPoint(wkt)) as y1,ST_X(ST_EndPoint(wkt)) as x2,ST_Y(ST_EndPoint(wkt))  as y2 from "+publicStaticParam.road_direction+" where droad_id='"+id+"'";

        List<LinkComplete> links_all3= jdbcTemplate.query(selectangle, new RowMapper<LinkComplete>(){
            @Override
            public LinkComplete mapRow(ResultSet rs, int rowNum) throws SQLException {
                LinkComplete link = new LinkComplete();
             Double angledouble=   publicStaticParam.getAngle1(rs.getDouble("x1"),rs.getDouble("y1"),rs.getDouble("x2"),rs.getDouble("y2"));
                 link.setAngle(angledouble.intValue());
                return link;
            }

        });
        String updateDirection="UPDATE "+publicStaticParam.road_direction+" SET  droad_angle="+links_all3.get(0).getAngle() +
                " WHERE droad_id='"+id+"'";
        //更改方向表的的数据
        jdbcTemplate.execute(updateDirection);


        String sqlRoad="SELECT road_id,link_ids, ST_AsText(wkt) as wkt ,road_id,droad_name,droad_type,droad_angle FROM "+publicStaticParam.road_direction+" where road_id='"+roadid+"'";
        List<Road> Road_list=jdbcTemplate.query(sqlRoad, new RowMapper<Road>(){
            @Override
            public Road mapRow(ResultSet rs, int rowNum) throws SQLException {
                Road road = new Road();
                road.setId(rs.getInt("road_id"));
                road.setWkt(rs.getString("wkt"));
                road.setName(rs.getString("droad_name"));
                road.setRoad_id(rs.getString("road_id"));
                road.setLinksIds(rs.getString("link_ids").replace("[","").replace("]","").split(","));
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
        //String InsertRoadLink="insert into link_road values('"+roadid+"','"++"')";
        // System.out.println("LinkService.LinktoRoad"+Road_list.get(0).toString());
        return  Road_list;








    }

    /**
     * 合并几个node节点成为一个路口
     * @param ids_array
     */
    public List<Intersection> intersectionToRoad(String[] ids_array) {
        String linkids="";
        for(int i=0;i<ids_array.length;i++){
            if (i==0){
                linkids=ids_array[i];
            }else{
                linkids=linkids+", "+ids_array[i];
            }

        }
        String sqlInitialization="select * from node_intersection where ";
        String sql="select * from "+publicStaticParam.node+" where id in ("+linkids+")";
        List<Node> Road_list=jdbcTemplate.query(sql, new RowMapper<Node>(){
            @Override
            public Node mapRow(ResultSet rs, int rowNum) throws SQLException {
                Node node = new Node();
                    node.setId(rs.getLong("id"));

                StringBuffer stringBuilder1=new StringBuffer(rs.getLong("lat")+"".trim());
                stringBuilder1.insert(2,".");
                StringBuffer stringBuilder2=new StringBuffer(rs.getLong("lon")+"".trim());
                stringBuilder2.insert(3,".");

                  node.setLat(Double.parseDouble(stringBuilder1.toString()));
                node.setLon(Double.parseDouble(stringBuilder2.toString()));
                return node;
            }

        });
        String nodes="";
        Double max_radius=0.0;
        Double max_lat=0.0;
        Double max_lon=0.0;
        for (int i = 0; i <Road_list.size() ; i++) {
            max_lat=max_lat+Road_list.get(i).getLat();
            max_lon=max_lon+Road_list.get(i).getLon();
            if (i==0){
                nodes=Road_list.get(i).getId()+"".trim();
            }else{
                nodes=nodes+"|"+Road_list.get(i).getId()+"".trim();
            }

           // System.out.println(Road_list.get(i).toString());
        }
        max_lat=max_lat/Road_list.size();
        max_lon=max_lon/Road_list.size();
       // System.out.println("center"+max_lat+","+max_lon);
        CalulateTwoLanLon lanlon = new CalulateTwoLanLon();
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(7);
        nf.setRoundingMode(RoundingMode.UP);
        max_lat=Double.parseDouble(nf.format(max_lat));
        max_lon=Double.parseDouble(nf.format(max_lon));
        Intersection intersection= new Intersection();
        intersection.setCenter_x(max_lat+"");
        intersection.setCenter_y(max_lon+"");
        //通过这个循环算出半径的最大值
        for (int i = 0; i <Road_list.size() ; i++) {
            max_radius=  (max_radius>lanlon.getDistance(max_lat,max_lon,Road_list.get(i).getLat(),Road_list.get(i).getLon()))?max_radius:lanlon.getDistance(max_lat,max_lon,Road_list.get(i).getLat(),Road_list.get(i).getLon());
           // lanlon.getDistance(max_lat,max_lon,Road_list.get(i).getLat(),Road_list.get(i).getLon());
        }
        List<Road> links_all_number= jdbcTemplate.query("select id::integer from "+publicStaticParam.intersection+" order by id desc limit 1", new RowMapper<Road>(){
            @Override
            public Road mapRow(ResultSet rs, int rowNum) throws SQLException {
                Road road = new Road();
                //link.setId(rs.getString("id"));
                road.setId(rs.getInt("id"));
                return road;
            }

        });
        int id=1;
        if(links_all_number.size()==0){
            id=1;
        }else{
            id =links_all_number.get(0).getId()+1;
        }
        String wktPoint="ST_GeomFromText('POINT("+max_lon+" "+max_lat+")',4326)";
        String insertIntersection="insert into "+publicStaticParam.intersection+" values('"+id+"',"+2+",'"+"无名路口"+"','"+max_lon+"','"+max_lat+"',"+1+",'"+"三岔路口"+"','"+linkids.replace(",","|")+"',"+wktPoint+",'"+max_radius+"',"+id+")";
       String insert2="INSERT INTO "+publicStaticParam.intersection+"(" +
               "intersection_id, link_ids, name, center_x, center_y, type_no, type_name, key_intersection, channelization, nodes, center, radius, id)" +
               "VALUES ('"+id+"', '[1,2]', '路口名称','"+max_lon+"','"+max_lat+"', "+1+",'三岔路口', "+0+",'路口渠化图','"+linkids.replace(",","|")+"', "+wktPoint+",'"+max_radius+"',"+id+");";
        System.out.println("insertIntersection"+insert2);
        jdbcTemplate.execute(insert2);
        for (int i = 0; i <ids_array.length ; i++) {
            String middle_intersection="insert into "+publicStaticParam.middle_intersection+" values('"+id+"','"+ids_array[i]+"')";
            jdbcTemplate.execute(middle_intersection);
        }
       // System.out.println("insertIntersection:"+insertIntersection);

        String sqlRoad="SELECT *  FROM "+publicStaticParam.intersection+" where id="+id;
        List<Intersection> intersections_list=jdbcTemplate.query(sqlRoad, new RowMapper<Intersection>(){
            @Override
            public Intersection mapRow(ResultSet rs, int rowNum) throws SQLException {
                Intersection section1 = new Intersection();
                section1.setId(rs.getInt("id"));
                section1.setIntersection_id(rs.getInt("id")+"");
                section1.setName(rs.getString("name"));
                section1.setCenter("["+rs.getString("center_x")+" "+rs.getString("center_y")+"]".trim());
               // System.out.println("center"+"["+rs.getString("center_x")+" "+rs.getString("center_y")+"]".trim());
                section1.setRadius(rs.getString("radius"));
                return section1;
            }

        });

        return intersections_list;

    }
//POINT(121.612574973185 38.9102133189689) 返回数据
    //
    public  List<LinkOrigin> selectForName(String name) {
        String QuerySql="select ST_ASText(ST_ClosestPoint(wkt, ST_Centroid(wkt))) as point,name from "+ publicStaticParam.link_name+"  where name like '%"+name+"%' limit 1";
       // System.out.println(QuerySql);
        List<LinkOrigin> intersections_list=jdbcTemplate.query(QuerySql, new RowMapper<LinkOrigin>(){
            @Override
            public LinkOrigin mapRow(ResultSet rs, int rowNum) throws SQLException {
                LinkOrigin section1 = new LinkOrigin();
                section1.setLat(rs.getString("point").split(" ")[1].replace(")",""));
                section1.setLng(rs.getString("point").split(" ")[0].replace("POINT(",""));
                section1.setName(rs.getString("name"));
                return section1;
            }

        });

            return  intersections_list;

    }
}
