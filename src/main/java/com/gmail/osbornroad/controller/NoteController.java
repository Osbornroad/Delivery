package com.gmail.osbornroad.controller;

import com.gmail.osbornroad.model.Note;
import com.gmail.osbornroad.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping(value = "/notes")
public class NoteController {

    @Autowired
    NoteService noteService;

    private final LocalDateTime START = LocalDateTime.now().minusWeeks(1);
    private final LocalDateTime END = LocalDateTime.now();

    @PostMapping(value = "/ajax/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Collection<Note>> getFilteredNotes(
            @RequestParam(value = "A001", required = false) String a001,
            @RequestParam(value = "A080", required = false) String a080,
            @RequestParam(value = "A090", required = false) String a090,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<String> aPoints = new ArrayList<>();
        if(null != a001)
            aPoints.add(a001);
        if(null != a080)
            aPoints.add(a080);
        if(null != a090)
            aPoints.add(a090);
        Collection<Note> noteList = noteService.getBetweenDates(aPoints, startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
        if (noteList.size() > 10000) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("reason","Requested data are too large. Use filter for reduce less than 10000 notes.").body(null);
        }
        return ResponseEntity.ok(noteList);
    }

    @GetMapping(value = "/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Collection<Note> getAllNotes() {
        List<String> aPoints = new ArrayList<>();
//        Collection<Note> noteList = noteService.findAllNotes();
        Collection<Note> noteList = noteService.getBetweenDates(aPoints, START, END);
//        Collection<Note> noteList = Collections.emptyList();
        return noteList;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String showNotesList() {
        return "notes";
    }


}
