package com.gmail.osbornroad.controller;


import com.gmail.osbornroad.repository.jpa.KitRepository;
import com.gmail.osbornroad.service.KitService;
import com.gmail.osbornroad.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/monthlyReport")
public class MonthlyReportController {

    @Autowired
    NoteService noteService;

    @Autowired
    KitRepository kitRepository;

    @Autowired
    KitService kitService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String showReportPage(Model model) {
        List<String> kits = kitRepository.getAllKitNames();
        model.addAttribute("kits", kits);
        return "monthlyReport";
    }

    @PostMapping(value = "/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String[][]> getReport(
            @RequestParam(value = "existProjects") boolean existProjects,
            @RequestParam(value = "aPoint") String aPoint,
            @RequestParam(value = "kits", required = false) List<String> kits,
            @RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        if(existProjects || kits.isEmpty() || kits == null)
            kits = kitService.getCurrentKitNameList();
        if(startDate.isAfter(endDate))
            return ResponseEntity.badRequest().body(null);
        String[][] report = noteService.getMonthlyReport(aPoint, kits, startDate, endDate);
        return ResponseEntity.ok(report);
    }
}
