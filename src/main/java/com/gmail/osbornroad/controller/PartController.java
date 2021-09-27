package com.gmail.osbornroad.controller;

import com.gmail.osbornroad.model.Part;
import com.gmail.osbornroad.service.InitLoader;
import com.gmail.osbornroad.service.PartService;
import com.gmail.osbornroad.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping(value = "/parts")
public class PartController {


    @Autowired
    PartService partService;

    @Autowired
    ValidationUtil validationUtil;

    @PostMapping(value = "/ajax")
    public ResponseEntity<String> savePart(@Valid Part part, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationUtil.getErrorResponse(bindingResult);
        }
        if (part.getId() != null) {
            if (part.getPartQtySet() == null || part.getPartQtySet().size() == 0) {
                Part savedPart = partService.findPartById(part.getId());
                if (savedPart.getPartQtySet() != null || savedPart.getPartQtySet().size() > 0) {
                    part.setPartQtySet(savedPart.getPartQtySet());
                }
            }
        }
        Part savedP = partService.savePart(part);
        if (savedP == null) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/ajax/{id}")
    @ResponseBody
    public ResponseEntity<String> deletePart(@PathVariable Integer id) {
//        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        Part part = partService.findPartById(id);
        if (part != null) {
            partService.deletePart(part);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping(value = "/ajax/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Part getPart(@PathVariable("id") String stringId/*, Model model*/) {
        Part part;
        Integer id;
        try {
            id = Integer.parseInt(stringId);
            part = partService.findPartById(id);
        } catch (NumberFormatException e) {
            part = new Part();
        }
//        List<String> shortOpNames = new ArrayList<>();

//        model.addAttribute("shortOpNames", shortOpNames);
        return part;
    }

    @GetMapping(value = "/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Collection<Part> getAllParts() {
        Collection<Part> partList = partService.findAllParts();
        return partList;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String showPartsList() {
        return "parts";
    }

/*    @Autowired
    InitLoader initLoader;

    @GetMapping(value = "/load", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void loadParts() {
        initLoader.partLoading();
    }*/
}
