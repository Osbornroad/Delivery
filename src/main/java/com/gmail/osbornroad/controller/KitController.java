package com.gmail.osbornroad.controller;

import com.gmail.osbornroad.model.FinishPart;
import com.gmail.osbornroad.model.Kit;
import com.gmail.osbornroad.service.FinishPartService;
import com.gmail.osbornroad.service.KitService;
import com.gmail.osbornroad.service.NoteService;
import com.gmail.osbornroad.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/kits")
public class KitController {

    @Autowired
    KitService kitService;

    @Autowired
    ValidationUtil validationUtil;

    @Autowired
    FinishPartService finishPartService;

    @Autowired
    NoteService noteService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String showKitsList(Model model) {
        List<FinishPart> finishParts = finishPartService.findAllFinishParts();
        finishParts.sort(Comparator.comparing(FinishPart::getSortNum));
        model.addAttribute("finishParts", finishParts);
        List<String> series = noteService.getAllSeries();
        model.addAttribute("series", series);
        List<String> wib224 = noteService.getAllWib224();
        model.addAttribute("wib224", wib224);
        return "kits";
    }

    @GetMapping(value = "/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Collection<Kit> getAllKits() {
        Collection<Kit> kitCollection = kitService.findAllKits();
        return kitCollection;
    }

    @GetMapping(value = "/ajax/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Kit getKit(@PathVariable("id") String stringId/*, Model model*/) {
        Kit kit;
        Integer id;
        try {
            id = Integer.parseInt(stringId);
            kit = kitService.findKitById(id);
        } catch (NumberFormatException e) {
            kit = new Kit();
        }
        return kit;
    }

    @PostMapping(value = "/ajax")
    public ResponseEntity<String> saveKit(@Valid Kit kit, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationUtil.getErrorResponse(bindingResult);
        }
        Set<FinishPart> partSet = new HashSet<>();
        for (FinishPart part : kit.getFinishPartSet()) {
            FinishPart p = finishPartService.findFinishPartByNumber(part.getFinishPartNumber());
            partSet.add(p);
        }
        kit.setFinishPartSet(partSet);
        kitService.saveKit(kit);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/ajax/{id}")
    @ResponseBody
    public void deleteKit(@PathVariable Integer id) {
        Kit kit = kitService.findKitById(id);
        if (kit != null) {
            kitService.deleteKit(kit);
        }
    }
}

