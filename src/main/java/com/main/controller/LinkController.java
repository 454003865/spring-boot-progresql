package com.main.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.main.model.*;

import com.main.service.LinkService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/Link")
public class LinkController {
    private static final Logger logger = LoggerFactory.getLogger(LinkController.class);
    @Autowired
    private LinkService linkService;


    /**
     * 将数据合并的逻辑
     * @param ids
     * @return
     */
    @RequestMapping(value = "/getLinkids/id",method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> getLinkid(@RequestBody String ids){
       // System.out.println(ids);

        JSONObject outJson = JSONObject.parseObject(ids);
        JSONArray bounds12 = outJson.getJSONArray("ids");
        List<String> list2 = JSONObject.parseArray(bounds12.toJSONString(), String.class);

        String[] ids_array=list2.toArray(new String[list2.size()]);
       // System.out.println(ids_array);
        for (int i = 0; i < ids_array.length; i++) {
            ids_array[i]=ids_array[i].replace("[","").replace("]","").trim();
           // System.out.println(i+"|||"+ids_array[i]);
        }

        logger.info("从数据库读取Link集合");

        ResponseMessage responseMS= new ResponseMessage();
        List<Link> returnLink= linkService.getList(ids_array);
        if(returnLink !=null&&returnLink.size()>0){
            responseMS.setCode(200);
            responseMS.setData(returnLink);
            responseMS.setMsg("成功");

            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.OK);
        }else{

            responseMS.setCode(422);
            responseMS.setMsg("数据不能合并");
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.UNPROCESSABLE_ENTITY);

        }

    }

    /**
     * 接受bounds数据返回整体数据
     * @param bounds
     * @return
     *  @RequestMapping(value = "/getWays/bounds",method = RequestMethod.POST)
     *     @ResponseBody
     *     public List<Ways> getLinkOfBound(@RequestBody String bounds){
     */
    @RequestMapping(value = "/getLinkid/bounds",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseMessage> getLinkOfBound( @RequestBody String bounds){

       // System.out.println("LinkController.getLinkOfBound"+bounds);
        logger.info("接受到了LinkBounds请求");
        JSONObject outJson = JSONObject.parseObject(bounds);
        JSONArray bounds12 = outJson.getJSONArray("bounds");

        List<String> list2 = JSONObject.parseArray(bounds12.toJSONString(), String.class);

        String[] array=list2.toArray(new String[list2.size()]);

       // System.out.println(array);
        for (int i = 0; i < array.length; i++) {
            array[i]=array[i].replace("[","").replace("]","").trim();
           // System.out.println(i+"|||"+array[i]);
        }
        ResponseMessage responseMS= new ResponseMessage();


        try{
            List<Link_apart> ReturnLink_apart=linkService.getListOfBoundV2(array);
            if (array.length!=4){
                responseMS.setMsg("参数不满足四个");
                return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.UNPROCESSABLE_ENTITY);

            }
            else if(ReturnLink_apart!=null){
                responseMS.setCode(200);
                responseMS.setMsg("成功");
                responseMS.setData(ReturnLink_apart);
                return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.OK);

            }else{
                responseMS.setCode(404);
                responseMS.setMsg("报错");
                return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){

                responseMS.setCode(404);
                responseMS.setMsg("报错");
                responseMS.setData(e.getMessage());
                e.printStackTrace();
                return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.NOT_FOUND);


        }




    }

    /**
     * 修改整体LINK的名字
     * @param links
     * @return
     */
    @RequestMapping(value = "/updata",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseMessage> updataOfId( @RequestBody(required = false)  String links){
        //  // System.out.println("bounds"+bounds);
//        if (bounds.split(",").length != 4) {
//            return new ResponseEntity<>(ResponseVO.error("新增失败"), HttpStatus.BAD_REQUEST);
//        }
       // System.out.println("LinkController.updataOfId"+links);
        JSONObject jsonObject = JSONObject.parseObject(links);
        String Updataname = jsonObject.getString("name");
        JSONArray bounds12 = jsonObject.getJSONArray("link_ids");

        List<String> list2 = JSONObject.parseArray(bounds12.toJSONString(), String.class);
            Updata_LinkName updataname= new Updata_LinkName();
        updataname.setName(Updataname);
        updataname.setLink_id(list2);
       // System.out.println("LinkController.updataOfId"+updataname);

        List<Link> ReturnLink=linkService.updataOfId(updataname);
        ResponseMessage responseMS= new ResponseMessage();
        if (ReturnLink!=null){
            responseMS.setCode(200);
            responseMS.setMsg("修改成功");
            responseMS.setData(ReturnLink);
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.OK);
        }else{
            responseMS.setCode(404);
            responseMS.setMsg("修改失败");
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.NOT_FOUND);

        }


    }

    /**
     *删除Link数据
     * @param link_ids
     * @return
     */
    @RequestMapping(value = "/deleteLinkid/id",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseMessage> deleteOfId(@RequestBody  String link_ids){


       // System.out.println("link_ids");
        JSONObject outJson = JSONObject.parseObject(link_ids);
        JSONArray bounds12 = outJson.getJSONArray("link_ids");
        List<String> list2 = JSONObject.parseArray(bounds12.toJSONString(), String.class);

        String[] ids_array=list2.toArray(new String[list2.size()]);
       // System.out.println(ids_array);
        for (int i = 0; i < ids_array.length; i++) {
            ids_array[i]=ids_array[i].replace("[","").replace("]","").trim();
           // System.out.println(i+"|||"+ids_array[i]);
        }

        List<Link> deleteLink=   linkService.deleteOfId(ids_array);
        ResponseMessage responseMS= new ResponseMessage();
        if (deleteLink!=null){
            responseMS.setCode(200);
            responseMS.setMsg("删除成功");
            responseMS.setData(deleteLink);
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.OK);
        }else{
            responseMS.setCode(404);
            responseMS.setMsg("删除失败");
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.NOT_FOUND);

        }





    }

    /**
     * 返回回收站的数据
     * @return
     */
    @RequestMapping(value = "/recover",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ResponseMessage> recover( ){

        List<Link> recoverLink=   linkService.recover();
        ResponseMessage responseMS= new ResponseMessage();
            if(recoverLink.size()==0 ){
                responseMS.setCode(200);
                responseMS.setMsg("成功");
                responseMS.setData(recoverLink);
                return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.OK);
            }
        else if (recoverLink!=null){
            responseMS.setCode(200);
            responseMS.setMsg("成功");
            responseMS.setData(recoverLink);
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.OK);
        }else{
            responseMS.setCode(404);
            responseMS.setMsg("没有获取到回收站数据");
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.NOT_FOUND);

        }





    }

    /**
     * 还原回收站数据到 原始数据表中
     * @param link_id
     * @return
     */
    @RequestMapping(value = "/recover/reduction",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseMessage> recoverReduction(@RequestBody  String selectIdList ){
       // System.out.println("LinkController.recoverReduction"+selectIdList);
        String link_id=selectIdList;
        JSONObject outJson = JSONObject.parseObject(link_id);
        JSONArray bounds12 = outJson.getJSONArray("selectIdList");
        List<String> list2 = JSONObject.parseArray(bounds12.toJSONString(), String.class);
        String[] ids_array=list2.toArray(new String[list2.size()]);
        List<Link> recoverLink=   linkService.recoverReduction(ids_array);
        ResponseMessage responseMS= new ResponseMessage();
        if (recoverLink!=null){
            responseMS.setCode(200);
            responseMS.setMsg("成功");
            responseMS.setData(recoverLink);
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.OK);
        }else{
            responseMS.setCode(404);
            responseMS.setMsg("修改失败");
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.NOT_FOUND);

        }





    }

        //还原数据到原始表中 传过来的数据应该是 新的Link_id

    /**
     *
     * @param link_id
     * @return
     */
    @RequestMapping(value = "/reduction",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseMessage> reductionById(@RequestBody String link_id){
       // System.out.println("LinkController.reductionById 111"+link_id);
        JSONObject outJson = JSONObject.parseObject(link_id);
        String  Link_id1 = outJson.getString("link_id");

       // System.out.println("LinkController.reductionById 222"+Link_id1);
        String[] ids_array={Link_id1};
       // System.out.println("ids_array :"+ids_array[0]);
       // System.out.println(ids_array);
        for (int i = 0; i < ids_array.length; i++) {
            ids_array[i]=ids_array[i].replace("[","").replace("]","").trim();
           // System.out.println(i+"|||"+ids_array[i]);
        }
        ResponseMessage responseMS= new ResponseMessage();
       //// System.out.println(link_id);
       // System.out.println("---------------------------------------");
//        linkService.reductionById(ids_array);
        List<Original> reductionLink=linkService.reductionById(ids_array);
        if(reductionLink!=null){
            responseMS.setMsg("还原数据成功");
            responseMS.setCode(200);
            responseMS.setData(reductionLink);
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.OK);
        }else {
            responseMS.setMsg("还原数据失败");
            responseMS.setCode(404);

            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.NOT_FOUND);
        }
//        ResponseMessage responseMS= new ResponseMessage();
//        if (
//                linkService.getListOfBoundV2(array)!=null
//        ){
//            responseMS.setCode(200);
//            responseMS.setMsg("成功");
//            responseMS.setData(linkService.getListOfBoundV2(array));
//
//            return responseMS;
//        }
//        else{
//            responseMS.setCode(404);
//            responseMS.setMsg("参数不对");
//            return responseMS;
//        }



    }



    /**
     *
     * @param link_ids
     * @return
     * 合并link到道路级别
     */
    @RequestMapping(value = "/generate/road",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseMessage> LinksToRoad(@RequestBody String link_ids){

        JSONObject outJson = JSONObject.parseObject(link_ids);
        JSONArray Link_id1 = outJson.getJSONArray("link_ids");
        String  link_name = outJson.getString("name");
        String  direction_name = outJson.getString("direction_name");
       // List<String> list2 = JSONObject.parseArray(link_ids, String.class);
        RoadMerge roadMerge = JSONObject.parseObject(link_ids,RoadMerge.class);
        System.out.println(roadMerge);



        List<Road> roadlist= linkService.LinktoRoad2(roadMerge);
        ResponseMessage responseMS= new ResponseMessage();
        if(roadlist!=null){
            responseMS.setMsg("合并数据成功");
            responseMS.setCode(200);
            responseMS.setData(roadlist);
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.OK);
        }else if(roadlist==null){
            responseMS.setMsg("合并数据失败或者主路名字重复");
            responseMS.setCode(422);

            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.UNPROCESSABLE_ENTITY);
       }else{
            responseMS.setMsg("合并数据失败");
            responseMS.setCode(404);

            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.NOT_FOUND);
        }

    }

    /**
     *将node 节点合并成路口
     * @param link_ids
     * @return
     */
    @RequestMapping(value = "/generate/intersection",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseMessage> IntersectionToRoad(@RequestBody String intersection){
       // System.out.println("LinkController.reductionById 111"+intersection);
        JSONObject outJson = JSONObject.parseObject(intersection);
        JSONArray Link_id1 = outJson.getJSONArray("associatedIds");

        //    String[] ids_array={Link_id1};
        List<String> list2 = JSONObject.parseArray(Link_id1.toJSONString(), String.class);
        String[] ids_array=list2.toArray(new String[list2.size()]);
       // System.out.println(ids_array);
        for (int i = 0; i < ids_array.length; i++) {
            ids_array[i]=ids_array[i].replace("[","").replace("]","").trim();
           // System.out.println(i+"|||"+ids_array[i]);
        }
        List<Intersection> inter_last=linkService.intersectionToRoad(ids_array);


        ResponseMessage responseMS= new ResponseMessage();

            if(inter_last.size()>0){
                responseMS.setCode(200);
                responseMS.setMsg("成功");
                responseMS.setData(inter_last);
                return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.OK);

            }else{
                responseMS.setCode(404);
                responseMS.setMsg("失败");
                responseMS.setData(inter_last);
                return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.NOT_FOUND);

            }

    }


    /**
     *
     * @param link_id
     * @return
     */
    @RequestMapping(value = "/select/name",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseMessage> selectForName(@RequestBody String name){

        JSONObject outJson = JSONObject.parseObject(name);
        String  name1 = outJson.getString("name");
       // System.out.println(name1);
        List<LinkOrigin> reductionLink=linkService.selectForName(name1);
       // System.out.println("数量为"+reductionLink.size());
        for (int i = 0; i < reductionLink.size(); i++) {
           // System.out.println("查找数据整体"+reductionLink.get(i).toString());
        }

        ResponseMessage responseMS= new ResponseMessage();
        if(reductionLink!=null&&reductionLink.size()>0){
            responseMS.setMsg("搜索数据成功");
            responseMS.setCode(200);
            responseMS.setData(reductionLink.get(0));
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.OK);
        }else if(reductionLink.size()==0){
            responseMS.setMsg("搜索数据成功 返回数据为空");
            responseMS.setCode(422);
            //responseMS.setData(reductionLink.get(0));
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            responseMS.setMsg("搜索数据失败");
            responseMS.setCode(404);

            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.NOT_FOUND);
        }


    }
}
