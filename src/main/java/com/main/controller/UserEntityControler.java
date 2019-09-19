package com.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.mapper.UserMapper;
import com.main.model.UserEntity;

@RestController
@RequestMapping("/user")
public class UserEntityControler {
	@Autowired
	private UserMapper userMapper;
	
	@RequestMapping("/getUsers")
	public List<UserEntity> getUsers() {
		List<UserEntity> users=userMapper.getAll();
		return users;
	}

//	public List<> getLink(){
//
//    }
    @RequestMapping("/getUser")
    public UserEntity getUser(Long id) {
    	UserEntity user=userMapper.getOne(id);
        return user;
    }
    
    @RequestMapping("/add")
    public void save(@RequestBody UserEntity user) {
       // System.out.println("-----------------------------------");
       // System.out.println(user.toString());
	    userMapper.insert(user);
    }
    
    @RequestMapping(value="update")
    public void update(@RequestBody UserEntity user) {

       // System.out.println(user.toString());
	    userMapper.update(user);
    }
    
    @RequestMapping(value="/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
    	userMapper.delete(id);
    }
}
