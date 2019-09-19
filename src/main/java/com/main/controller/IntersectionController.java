package com.main.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.main.model.*;
import com.main.service.IntersectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/intersection")
public class IntersectionController {
    private static final Logger logger = LoggerFactory.getLogger(IntersectionController.class);
    @Autowired
    IntersectionService intersectionService;
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> getIntersection(@RequestBody String bounds) {
       // System.out.println("LinkController.getLinkOfBound" + bounds);
        //logger.info("接受到了LinkBounds请求");
        JSONObject outJson = JSONObject.parseObject(bounds);
        JSONArray bounds12 = outJson.getJSONArray("bounds");

        List<String> list2 = JSONObject.parseArray(bounds12.toJSONString(), String.class);

        String[] array = list2.toArray(new String[list2.size()]);

       // System.out.println(array);
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].replace("[", "").replace("]", "").trim();
           // System.out.println(i + "|||" + array[i]);
        }
        ResponseMessage responseMS= new ResponseMessage();
        if(array.length!=4){
            responseMS.setCode(422);
            responseMS.setMsg("传入经纬度参数有误");
            //  responseMS.setData(IntersectionList);
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.UNPROCESSABLE_ENTITY);

        }
        List<Intersectionv2> IntersectionList=  intersectionService.getIntersection(array);
        if(IntersectionList.size()==0){
            responseMS.setCode(200);
            responseMS.setMsg("范围内无路口数据");
            responseMS.setData(IntersectionList);
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.OK);

        }else if(IntersectionList.size()!=0){

            responseMS.setCode(200);
            responseMS.setMsg("查询成功");
            responseMS.setData(IntersectionList);
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.OK);
        }else{
            responseMS.setCode(404);
            responseMS.setMsg("查询失败");
            responseMS.setData(IntersectionList);
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.NOT_FOUND);
        }

    }
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> deleteIntersection(@RequestBody String IntersectionIds){
       // System.out.println("link_ids");
        JSONObject outJson = JSONObject.parseObject(IntersectionIds);
        JSONArray bounds12 = outJson.getJSONArray("intersection_id");
        List<String> list2 = JSONObject.parseArray(bounds12.toJSONString(), String.class);

        String[] ids_array=list2.toArray(new String[list2.size()]);
       // System.out.println(ids_array);
        for (int i = 0; i < ids_array.length; i++) {
            ids_array[i]=ids_array[i].replace("[","").replace("]","").trim();
           // System.out.println(i+"|||"+ids_array[i]);
        }
        ResponseMessage responseMS= new ResponseMessage();

        try {

            intersectionService.deleteIntersection(ids_array);
            responseMS.setCode(200);
            responseMS.setMsg("删除成功");
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.OK);

        }catch(Exception e){
            responseMS.setCode(404);
            responseMS.setMsg("删除失败");
            responseMS.setData(e.getMessage());
          e.printStackTrace();
             return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.NOT_FOUND);

        }



    }

    @RequestMapping(value = "/getData", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> nodeBound(@RequestBody String bounds) {
        ResponseMessage responseMS= new ResponseMessage();
       // System.out.println("LinkController.getLinkOfBound" + bounds);
        //logger.info("接受到了LinkBounds请求");
        JSONObject outJson = JSONObject.parseObject(bounds);
        JSONArray bounds12 = outJson.getJSONArray("bounds");

        List<String> list2 = JSONObject.parseArray(bounds12.toJSONString(), String.class);

        String[] array = list2.toArray(new String[list2.size()]);

       // System.out.println(array);
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].replace("[", "").replace("]", "").trim();
           // System.out.println(i + "|||" + array[i]);
        }


        if(array.length!=4){
            responseMS.setCode(422);
            responseMS.setMsg("传入经纬度参数有误");
            //  responseMS.setData(IntersectionList);
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.UNPROCESSABLE_ENTITY);

        }


        List<NodeforAlive> IntersectionList=  intersectionService.getnodeBound(array);



        if(IntersectionList.size()==0){
            responseMS.setCode(200);
            responseMS.setMsg("范围内无路口数据");
            responseMS.setData(IntersectionList);
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.OK);

        }else if(IntersectionList.size()!=0){

            responseMS.setCode(200);
            responseMS.setMsg("查询成功");
            responseMS.setData(IntersectionList);
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.OK);
        }else{
            responseMS.setCode(404);
            responseMS.setMsg("查询失败");
            responseMS.setData(IntersectionList);
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.NOT_FOUND);
        }

    }
    @RequestMapping(value = "/getTarget", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> Gettarget(@RequestBody String bounds) {
        ResponseMessage responseMS= new ResponseMessage();
       // System.out.println("LinkController.getLinkOfBound" + bounds);
        //logger.info("接受到了LinkBounds请求");
        JSONObject outJson = JSONObject.parseObject(bounds);
        JSONArray bounds12 = outJson.getJSONArray("bounds");

        List<String> list2 = JSONObject.parseArray(bounds12.toJSONString(), String.class);

        String[] array = list2.toArray(new String[list2.size()]);

       // System.out.println(array);
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].replace("[", "").replace("]", "").trim();
           // System.out.println(i + "|||" + array[i]);
        }


        if(array.length!=4){
            responseMS.setCode(422);
            responseMS.setMsg("传入经纬度参数有误");
            //  responseMS.setData(IntersectionList);
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.UNPROCESSABLE_ENTITY);

        }


        List<target> IntersectionList=  intersectionService.getTargetBound(array);



        if(IntersectionList.size()==0){
            responseMS.setCode(200);
            responseMS.setMsg("范围内无路口数据");
            responseMS.setData(IntersectionList);
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.OK);

        }else if(IntersectionList.size()!=0){

            responseMS.setCode(200);
            responseMS.setMsg("查询成功");
            responseMS.setData(IntersectionList);
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.OK);
        }else{
            responseMS.setCode(404);
            responseMS.setMsg("查询失败");
            responseMS.setData(IntersectionList);
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.NOT_FOUND);
        }

    }
}
