package com.career.careeroauth2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


/**
 * 测试登陆
 * */
@RestController
public class LoginController {

    /**
     * 登录地址
     * @return
     */
    @GetMapping("/auth/login")
    public ModelAndView require() {
        return new ModelAndView("ftl/login");
    }

}

