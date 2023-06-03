package com.sorawingwind.storage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WebController {

    @RequestMapping({"/web","/web/login","/web/report","/web/order","/web/inStorage","/web/outStorage","/web/company","/web/dict"})
    public String index(HttpServletRequest request){
        return "forward:/web/index.html";
    }
}
