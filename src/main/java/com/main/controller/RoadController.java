package com.main.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.main.model.*;
import com.main.service.RoadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/road")
public class RoadController {
    private static final Logger logger = LoggerFactory.getLogger(RoadController.class);
    @Autowired
    RoadService roadService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> getRoad(@RequestBody String bounds) {
       // System.out.println("LinkController.getLinkOfBound" + bounds);
        logger.info("接受到了LinkBounds请求");
        JSONObject outJson = JSONObject.parseObject(bounds);
        JSONArray bounds12 = outJson.getJSONArray("bounds");

        List<String> list2 = JSONObject.parseArray(bounds12.toJSONString(), String.class);

        String[] array = list2.toArray(new String[list2.size()]);

       // System.out.println(array);
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].replace("[", "").replace("]", "").trim();
           // System.out.println(i + "|||" + array[i]);
        }
      //  loadService.getRounds(array);

        ResponseMessage responseMS= new ResponseMessage();
        List<Road> ReturnRoad_apart=roadService.getRounds(array);
        if (array.length!=4){
            responseMS.setMsg("参数不满足四个");
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.UNPROCESSABLE_ENTITY);

        }else if(ReturnRoad_apart.size()==0){
            responseMS.setCode(200);
            responseMS.setMsg("范围内road数据为空");
            responseMS.setData(ReturnRoad_apart);
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.OK);
        }
        else if (ReturnRoad_apart!=null){
            responseMS.setCode(200);
            responseMS.setMsg("成功");
            responseMS.setData(ReturnRoad_apart);
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.OK);

        }
        else{
            responseMS.setCode(404);
            responseMS.setMsg("参数不对");
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.NOT_FOUND);
        }



    }

    /**
     * 修改整体Road的名字
     * @param links
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseMessage> updataOfId( @RequestBody(required = false)  String roads){
        //  // System.out.println("bounds"+bounds);
//        if (bounds.split(",").length != 4) {
//            return new ResponseEntity<>(ResponseVO.error("新增失败"), HttpStatus.BAD_REQUEST);
//        }
       // System.out.println("RoadController.updataOfId:"+roads);
        JSONObject jsonObject = JSONObject.parseObject(roads);
        String Updataname = jsonObject.getString("name");
        JSONArray bounds12 = jsonObject.getJSONArray("road_ids");
        String direction_name = jsonObject.getString("direction_name");
        List<String> list2 = JSONObject.parseArray(bounds12.toJSONString(), String.class);


        List<Road> ReturnRoad=roadService.updataOfId(list2,Updataname,direction_name);
        ResponseMessage responseMS= new ResponseMessage();
        if (ReturnRoad!=null&&ReturnRoad.size()==list2.size()){
            responseMS.setCode(200);
            responseMS.setMsg("修改成功");
            responseMS.setData(ReturnRoad);
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.OK);
        }else if(ReturnRoad.size()!=list2.size()){
            responseMS.setCode(200);
            responseMS.setMsg("修改参数有误并未全部修改成功");
            responseMS.setData(ReturnRoad);
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.OK);
        }else{
            responseMS.setCode(404);
            responseMS.setMsg("修改失败");
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.NOT_FOUND);

        }


    }


    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseMessage> deleteOfId( @RequestBody(required = false)  String road_ids){
        //  // System.out.println("bounds"+bounds);
//        if (bounds.split(",").length != 4) {
//            return new ResponseEntity<>(ResponseVO.error("新增失败"), HttpStatus.BAD_REQUEST);
//        }
       // System.out.println("RoadController.updataOfId:"+road_ids);
        JSONObject jsonObject = JSONObject.parseObject(road_ids);
        JSONArray bounds12 = jsonObject.getJSONArray("road_ids");
        List<String> list2 = JSONObject.parseArray(bounds12.toJSONString(), String.class);
        ResponseMessage responseMS= new ResponseMessage();
        try {
            roadService.deleteOfId(list2);
            responseMS.setCode(200);
            responseMS.setMsg("删除成功");
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.OK);
        }catch (Exception e){
            responseMS.setCode(404);
            responseMS.setMsg("删除失败");
            responseMS.setData(e);
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.NOT_FOUND);
        }






    }

    @RequestMapping(value = "/generate/link",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseMessage> RoadandLinkConmerge( @RequestBody(required = false)  String data){

        RoadAndLinkConmerge amapupdate = JSONObject.parseObject(data, RoadAndLinkConmerge.class);
        System.out.println(amapupdate.toString());
        ResponseMessage responseMS= new ResponseMessage();
        try{
            roadService.conmergeLink(amapupdate);
            responseMS.setCode(200);
            responseMS.setMsg("合并成功");
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            responseMS.setCode(404);
            responseMS.setMsg("合并失败");
            responseMS.setData(e.getMessage());
            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.NOT_FOUND);
        }

    }
}