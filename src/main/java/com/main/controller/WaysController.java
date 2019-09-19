package com.main.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.main.model.Link;
import com.main.model.ResponseMessage;
import com.main.model.Updata_LinkName;
import com.main.model.Ways;
import com.main.service.WaysService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/Ways")
public class WaysController {
    private static final Logger logger = LoggerFactory.getLogger(WaysController.class);
    @Autowired
    private WaysService wayservice;

    /**
     * 接受Link范围内的数据
     *
     * @param bounds
     * @return
     */
    @RequestMapping(value = "/getWays/bounds", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseMessage> getLinkOfBound(@RequestBody String bounds) {

        ResponseMessage responseMS = new ResponseMessage();
        JSONObject outJson = JSONObject.parseObject(bounds);
        JSONArray bounds12 = outJson.getJSONArray("bounds");

        List<String> list2 = JSONObject.parseArray(bounds12.toJSONString(), String.class);

        String[] array = list2.toArray(new String[list2.size()]);
       // System.out.println(array);
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].replace("[", "").replace("]", "").trim();
           // System.out.println(i + "|||" + array[i]);
        }
        List<Ways> BoundsWays = wayservice.getListOfBound(array);

        if (BoundsWays != null) {
            responseMS.setCode(200);
            responseMS.setMsg("查询成功");
            responseMS.setData(BoundsWays);
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.OK);
        } else {
            responseMS.setCode(404);
            responseMS.setMsg("查询失败");
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.NOT_FOUND);
        }

    }

    /**
     * 合并Way数据到Link里
     *
     * @param waysids
     * @return
     */
    @RequestMapping("/getWayids/id")
    @ResponseBody
    public ResponseEntity<ResponseMessage> LinkMerge(@RequestBody String waysids) {

        ResponseMessage responseMS = new ResponseMessage();
        JSONObject outJson = JSONObject.parseObject(waysids);
        JSONArray bounds12 = outJson.getJSONArray("waysids");
        List<String> list2 = JSONObject.parseArray(bounds12.toJSONString(), String.class);

        String[] ids_array = list2.toArray(new String[list2.size()]);
        //System.out.println(ids_array);
        for (int i = 0; i < ids_array.length; i++) {
           // System.out.println(ids_array[i]);
            ids_array[i] = ids_array[i].replace("[", "").replace("]", "").trim();

        }
        logger.info("从数据库读取Ways集合");

        List<Link> BoundsWays = wayservice.LinkMerge(ids_array);
       // System.out.println("WaysController.LinkMerge 222"+BoundsWays.get(0).toString());
        if (BoundsWays != null) {
            responseMS.setCode(200);
            responseMS.setMsg("合并成功");
            responseMS.setData(BoundsWays);
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.OK);
        } else {
            responseMS.setCode(404);
            responseMS.setMsg("合并失败");
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/updata")
    @ResponseBody
    public ResponseEntity<ResponseMessage> UpdataWays(@RequestBody String waysids) {
        //  // System.out.println("bounds"+bounds);
//        if (bounds.split(",").length != 4) {
//            return new ResponseEntity<>(ResponseVO.error("新增失败"), HttpStatus.BAD_REQUEST);
//        }
       // System.out.println("LinkController.updataOfId" + waysids);
        JSONObject jsonObject = JSONObject.parseObject(waysids);
        String Updataname = jsonObject.getString("osm_name");
        JSONArray bounds12 = jsonObject.getJSONArray("ways_ids");

        List<String> list2 = JSONObject.parseArray(bounds12.toJSONString(), String.class);
        Updata_LinkName updataname = new Updata_LinkName();
        updataname.setName(Updataname);
        updataname.setLink_id(list2);
        ResponseMessage responseMS = new ResponseMessage();
       List<Ways> listWays =  wayservice.UpdataWays(updataname);
        if (listWays != null) {
            responseMS.setCode(200);
            responseMS.setMsg("修改成功");
            responseMS.setData(listWays);
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.OK);

        } else {
            responseMS.setCode(404);
            responseMS.setMsg("修改失败");
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.NOT_FOUND);
        }



    }
}