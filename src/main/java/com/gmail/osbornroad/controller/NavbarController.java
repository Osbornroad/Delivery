package com.gmail.osbornroad.controller;

import com.gmail.osbornroad.service.InitLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
public class NavbarController {

    @Autowired
    InitLoader initLoader;

    @GetMapping(value = "/navbar", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<LocalDateTime> getDateOfUpdating() {
        LocalDateTime dateTime = initLoader.getBaseUpdated();
        return ResponseEntity.ok(dateTime);
    }
}
