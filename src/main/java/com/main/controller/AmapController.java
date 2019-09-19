package com.main.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.main.model.*;
import com.main.service.AmapService;
import com.main.service.LinkService;
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
@RequestMapping("/amap")
public class AmapController {
    private static final Logger logger = LoggerFactory.getLogger(AmapController.class);

    @Autowired
    private AmapService amapService;

    @RequestMapping(value = "/binding", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> binding(@RequestBody String data) {

        AmapBind amapbind = JSONObject.parseObject(data,AmapBind.class);
       // AmapBind studentFromJson = JSONObject.parseJsonToObj(studentJson, Student.class);
       // System.out.println("AmapController.getRoad"+amapbind.toString());
        ResponseMessage responseMS= new ResponseMessage();
        try {
            List<Amapbound> amapbounds= amapService.binding(amapbind);
            responseMS.setCode(200);
            responseMS.setData(amapbounds);
            responseMS.setMsg("成功");
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.OK);
        }catch (Exception e){
            responseMS.setCode(404);

            responseMS.setMsg("失败");
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.NOT_FOUND);
        }


       // ResponseEntity<ResponseMessage> =




    }

    @RequestMapping(value = "/associated/index", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> Amapbounds(@RequestBody String bounds) {

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
      //  List<AmapPointSimple> pointSimples  = amapService.Amapindex(array);

        try {
            List<AmapPointSimple> pointSimples  = amapService.Amapindex(array);

            if (pointSimples.size()==0){
                responseMS.setCode(200);
                responseMS.setData(pointSimples);
                responseMS.setMsg("范围内没有数据");
            }else{
                responseMS.setCode(200);
                responseMS.setData(pointSimples);
                responseMS.setMsg("成功");
            }

            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.OK);
        }catch (Exception e){
            responseMS.setCode(404);

            responseMS.setMsg("失败");
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/associated/update", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> Amapupdata(@RequestBody String updatedata) {
        AmapUpdate amapupdate = JSONObject.parseObject(updatedata, AmapUpdate.class);



        ResponseMessage responseMS= new ResponseMessage();
        //  List<AmapPointSimple> pointSimples  = amapService.Amapindex(array);

        try {
           amapService.Amapupdate(amapupdate);


                responseMS.setCode(200);

                responseMS.setMsg("修改成功");

            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.OK);
        }catch (Exception e){
            responseMS.setCode(404);

            responseMS.setMsg("失败");
            e.printStackTrace();
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> AmapDelete(@RequestBody String ids) {



        JSONObject outJson = JSONObject.parseObject(ids);
        JSONArray bounds12 = outJson.getJSONArray("ids");
        List<String> list2 = JSONObject.parseArray(bounds12.toJSONString(), String.class);

        String[] ids_array=list2.toArray(new String[list2.size()]);
       // System.out.println(ids_array);
        for (int i = 0; i < ids_array.length; i++) {
            ids_array[i]=ids_array[i].replace("[","").replace("]","").trim();
           // System.out.println(i+"|||"+ids_array[i]);
        }

        ResponseMessage responseMS= new ResponseMessage();


        try {
            //amapService.AmapDelete(amapupdate);
            amapService.Amapdelete(ids_array);

            responseMS.setCode(200);

            responseMS.setMsg("删除成功");

            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.OK);
        }catch (Exception e){
            responseMS.setCode(404);

            responseMS.setMsg("删除失败");
            e.printStackTrace();
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.NOT_FOUND);
        }

    }



}
