package com.main.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.main.model.ResponseMessage;
import com.main.model.Road;
import com.main.service.LinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/link")
public class LinkToPut {
    private static final Logger logger = LoggerFactory.getLogger(LinkToPut.class);
    @Autowired
    private LinkService linkService;

    /**
     *
     * @param link_ids
     * @return
     */
    @RequestMapping(value = "/generate/road",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseMessage> LinksToRoad(@RequestBody String link_ids){
        System.out.println("LinkController.reductionById 111"+link_ids);
        JSONObject outJson = JSONObject.parseObject(link_ids);
        JSONArray Link_id1 = outJson.getJSONArray("link_ids");
        String  link_name = outJson.getString("name");
        String  direction_name= outJson.getString("direction_name");
        //    String[] ids_array={Link_id1};
        List<String> list2 = JSONObject.parseArray(Link_id1.toJSONString(), String.class);
        String[] ids_array=list2.toArray(new String[list2.size()]);
        System.out.println(ids_array);
        for (int i = 0; i < ids_array.length; i++) {
            ids_array[i]=ids_array[i].replace("[","").replace("]","").trim();
            System.out.println(i+"|||"+ids_array[i]);
        }
        System.out.println("name:"+link_name);
        List<Road> roadlist= linkService.LinktoRoad(ids_array,link_name,direction_name);
        ResponseMessage responseMS= new ResponseMessage();
        if(roadlist!=null){
            responseMS.setMsg("合并数据成功");
            responseMS.setCode(200);
            responseMS.setData(roadlist);
            return new ResponseEntity<ResponseMessage>(responseMS, HttpStatus.OK);
        }else {
            responseMS.setMsg("合并数据失败");
            responseMS.setCode(404);

            return new ResponseEntity<ResponseMessage>(responseMS,HttpStatus.NOT_FOUND);
        }

    }
}
