package com.gmail.osbornroad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
//@RequestMapping("/")
public class Root {

//    @RequestMapping
    public ModelAndView getIndex() {
        ModelAndView modelAndView  = new ModelAndView("main");
        return modelAndView;
    }
}
