package com.gmail.osbornroad.controller;

import com.gmail.osbornroad.service.InitLoader;
import com.gmail.osbornroad.service.NoteService;
import com.gmail.osbornroad.service.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.net.spi.nameservice.NameService;

import java.time.LocalDateTime;

@Controller
public class NavbarController {

    @Autowired
    NoteService noteService;

    @GetMapping(value = "/navbar", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<LocalDateTime> getDateOfUpdating() {
        LocalDateTime dateTime = noteService.getLastSavedDate();
        return ResponseEntity.ok(dateTime);
    }
}
