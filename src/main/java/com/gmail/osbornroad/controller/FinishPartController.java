package com.gmail.osbornroad.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.osbornroad.model.FinishPart;
import com.gmail.osbornroad.model.Part;
import com.gmail.osbornroad.model.PartQty;
import com.gmail.osbornroad.model.PartQtyDTO;
import com.gmail.osbornroad.service.FinishPartService;
import com.gmail.osbornroad.service.PartQtyService;
import com.gmail.osbornroad.service.PartService;
import com.gmail.osbornroad.util.ValidationUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping(value = "/finishPart/{finishPartId}")
public class FinishPartController {

    @Autowired
    FinishPartService finishPartService;

    @Autowired
    PartService partService;

    @Autowired
    PartQtyService partQtyService;

    @Autowired
    ValidationUtil validationUtil;

//    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/ajax")
    public ResponseEntity<String> savePartQty(@Valid PartQtyDTO partQtyDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationUtil.getErrorResponse(bindingResult);
        }
        PartQty partQty = partQtyService.getPartQtyFromDto(partQtyDTO);
        PartQty savedPartQty = partQtyService.savePartQty(partQty);
        if(savedPartQty == null)
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        else
            return new ResponseEntity<>(HttpStatus.OK);
    }

//    @Secured("ROLE_ADMIN")
    @DeleteMapping(value = "/ajax/{partId}")
    @ResponseBody
    public ResponseEntity<String> deletePartQty(
            @PathVariable Integer finishPartId,
            @PathVariable Integer partId) {
        PartQty partQty = partQtyService.findByPartAndFinishPart(partId, finishPartId);
        partQtyService.deletePartQty(partQty);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/ajax/{stringPartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<PartQtyDTO> getPartQtyDTO(
            @PathVariable Integer finishPartId,
            @PathVariable String stringPartId) {
        PartQtyDTO dto;
        try {
            int partId = Integer.parseInt(stringPartId);
            PartQty partQty = partQtyService.findByPartAndFinishPart(partId, finishPartId);
            dto = partQtyService.getDtoFromPartQty(partQty);
        } catch (NumberFormatException e) {
            dto = new PartQtyDTO();
            dto.setFinishPartId(finishPartId);
            dto.setQty(1);
        }


        return ResponseEntity.ok(dto);

    }

    @GetMapping(value = "/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Set<PartQtyDTO> getPartQtyDTOSet(@PathVariable Integer finishPartId) {
        FinishPart finishPart = finishPartService.findFinishPartById(finishPartId);
        Set<PartQty> set = finishPart.getPartQtySet();
        Set<PartQtyDTO> dtoSet = new HashSet<>();
        for(PartQty partQty : set) {
            PartQtyDTO dto = partQtyService.getDtoFromPartQty(partQty);
            dtoSet.add(dto);
        }
        return dtoSet;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView showFinishPart(@PathVariable Integer finishPartId) {
        ModelAndView modelAndView = new ModelAndView("finishPart");
        ObjectMapper objectMapper = new ObjectMapper();
        List<Part> partList = partService.findAllParts();
        Collections.sort(partList, new Comparator<Part>() {
            @Override
            public int compare(Part o1, Part o2) {
                return o1.getPartNumber().compareTo(o2.getPartNumber());
            }
        });
        String jsonAllParts = null;
        try {
            jsonAllParts = objectMapper.writeValueAsString(partList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (finishPartId != null) {
                FinishPart finishPart = finishPartService.findFinishPartById(finishPartId);
                modelAndView.addObject("finishPartNumber", finishPart.getFinishPartNumber());
                modelAndView.addObject("finishPartId", finishPart.getId());
                modelAndView.addObject("allParts", jsonAllParts);
                modelAndView.addObject("partList", partList);
            }
        return modelAndView;
    }
}
