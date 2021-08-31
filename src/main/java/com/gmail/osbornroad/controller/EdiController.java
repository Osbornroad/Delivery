package com.gmail.osbornroad.controller;

import com.gmail.osbornroad.model.UnitEDI;
import com.gmail.osbornroad.service.LoaderEDI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/edi")
public class EdiController {

    @Autowired
    LoaderEDI loaderEDI;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String showEdiPage(Model model) {
        return "edi";
    }

    @PostMapping(value = "/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String[][]> getReport(
            @RequestParam(value = "filePath") String filePath,
            @RequestParam(value = "ediDir") String ediDir
    ) {
        File file = null;
        if(!filePath.equals(""))
            file = new File(filePath);
        String[][] report = loaderEDI.getArrayOfEdi(file, ediDir);
        return ResponseEntity.ok(report);
    }

    @PostMapping(value = "/properties", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String[]> getProperties(
            @RequestParam(value = "filePath") String filePath,
            @RequestParam(value = "ediDir") String ediDir
    ) {
        File file = null;
        if(!filePath.equals(""))
            file = new File(filePath);
        String[] properties = loaderEDI.getEdiProperties(file, ediDir);
        return ResponseEntity.ok(properties);
    }
}
