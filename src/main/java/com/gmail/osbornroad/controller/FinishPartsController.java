package com.gmail.osbornroad.controller;

import com.gmail.osbornroad.model.FinishPart;
import com.gmail.osbornroad.service.FinishPartService;
import com.gmail.osbornroad.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping(value = "/finishParts")
public class FinishPartsController {

    @Autowired
    FinishPartService finishPartService;

    @Autowired
    ValidationUtil validationUtil;

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/ajax")
    public ResponseEntity<String> saveFinishPart(@Valid FinishPart finishPart, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationUtil.getErrorResponse(bindingResult);
        }
        if (finishPart.getId() != null) {
            if (finishPart.getKitSet() == null || finishPart.getKitSet().size() == 0) {
                FinishPart savedFinishPart = finishPartService.findFinishPartById(finishPart.getId());
                if (savedFinishPart.getKitSet() != null || savedFinishPart.getKitSet().size() > 0) {
                    finishPart.setKitSet(savedFinishPart.getKitSet());
                }
            }
        }
        FinishPart savedFP = finishPartService.saveFinishPart(finishPart);
        if (savedFP == null) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping(value = "/ajax/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteFinishPart(@PathVariable Integer id) {
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
/*        FinishPart finishPart = finishPartService.findFinishPartById(id);
        if (finishPart != null) {
            finishPartService.deleteFinishPart(finishPart);
        }*/
    }

    @GetMapping(value = "/ajax/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public FinishPart getFinishPart(@PathVariable("id") String stringId/*, Model model*/) {
        FinishPart finishPart;
        Integer id;
        try {
            id = Integer.parseInt(stringId);
            finishPart = finishPartService.findFinishPartById(id);
        } catch (NumberFormatException e) {
            finishPart = new FinishPart();
        }
//        List<String> shortOpNames = new ArrayList<>();

//        model.addAttribute("shortOpNames", shortOpNames);
        return finishPart;
    }

    @GetMapping(value = "/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Collection<FinishPart> getAllFinishParts() {
        Collection<FinishPart> finishPartList = finishPartService.findAllFinishParts();
        return finishPartList;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String showFinishPartsList() {
        return "finishParts";
    }
}
