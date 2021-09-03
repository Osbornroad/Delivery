package com.gmail.osbornroad.controller;


import com.gmail.osbornroad.service.InitLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = {"/"})
public class MainPageController {

/*    @Autowired
    InitLoader initLoader;*/

    private static final Logger logger = LoggerFactory.getLogger(MainPageController.class);
    private static final Logger warnLogger = LoggerFactory.getLogger("warning");

    @GetMapping()
    public String showMainPage() {
        return "main";
    }

/*    @GetMapping("/wib224")
    public String wib224MatchingLoading(){
        initLoader.wib224MatchingLoading();
        return "main";
    }*/

/*    @GetMapping("/finishPartsLoading")
    public String finishPartsLoading() {
        initLoader.finishPartLoading();
        return "main";
    }*/

/*    @GetMapping("/walkFTP")
    public String archiveCheck() {
        initLoader.walkFTP();
        return "main";
    }*/

 }
