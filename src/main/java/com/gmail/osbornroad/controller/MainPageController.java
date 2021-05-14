package com.gmail.osbornroad.controller;


import com.gmail.osbornroad.service.InitLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = {"/"/*, "Delivery"*/})
public class MainPageController {

    @Autowired
    InitLoader initLoader;

    private static final Logger logger = LoggerFactory.getLogger(MainPageController.class);
    private static final Logger warnLogger = LoggerFactory.getLogger("warning");

    @GetMapping()
    public String showMainPage() {
        return "main";
    }

    @GetMapping("/wib224")
    public String wib224MatchingLoading(){
        initLoader.wib224MatchingLoading();
        return "main";
    }

    @GetMapping("/finishPartsLoading")
    public String finishPartsLoading() {
        initLoader.finishPartLoading();
        return "main";
    }

    @GetMapping("/nissan")
    public String nissanDataLoading(Model model){
        String response = initLoader.nissanDataLoading();
//        String response = "nissanDataLoading() unavailable";
        model.addAttribute("responseNissan", response);
        return "main";
    }

    @GetMapping("/firebird")
    public String firebirdDataLoading(Model model){
//        warnLogger.warn("firebirdDataLoading started with warn");
//        logger.info("firebirdDataLoading started with info");
        String response = initLoader.notesTableFillingFromFireBird();
        model.addAttribute("responseFirebird", response);
        return "main";
    }

    @GetMapping("/walkFTP")
    public String archiveCheck() {
        initLoader.walkFTP();
        return "main";
    }

 }
