package com.gmail.osbornroad.controller;

import com.gmail.osbornroad.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/forecast")
public class ForecastController {

    @Autowired
    NoteService noteService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String showForecastPage(Model model) {
        return "forecast";
    }

    @GetMapping(value = "/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Map<String, String>> getForecast() {
        List<Map<String, String>> forecast = noteService.getForecast();
        return forecast;
    }

}
