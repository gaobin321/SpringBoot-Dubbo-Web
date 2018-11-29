package com.wenba.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wenba.api.UserServiceFacade;

import com.wenba.common.domain.User;
import com.wenba.common.vo.UserVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference(
            application = "${dubbo.application.id}",
            group = "${dubbo.consumer.group}"
    )
    private UserServiceFacade userServiceFacade;


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public User queryUser(UserVo userRequest){
        User remot = userServiceFacade.queryUserById(userRequest.getUserId());
        return remot;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addUser(UserVo userRequest) throws Exception {
        userServiceFacade.addUser(userRequest);
        return "success";
    }
}
