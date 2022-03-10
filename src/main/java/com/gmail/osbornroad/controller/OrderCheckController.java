package com.gmail.osbornroad.controller;

import com.gmail.osbornroad.model.OrderCheck;
import com.gmail.osbornroad.model.UnitEDI;
import com.gmail.osbornroad.service.LoaderEDI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orderCheck")
public class OrderCheckController {

    @Autowired
    LoaderEDI loaderEDI;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String showOrderCheckPage(Model model) {
//        String[][] orderCheck = loaderEDI.getOrderCheck();
        return "orderCheck";
    }

    @PostMapping(value = "/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<OrderCheck>> getOrderCheck(
            @RequestParam (value = "filePath", required = false)  String filePath,
            @RequestParam (value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam (value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
/*        if (startDate == null || "".equals(startDate)) {
            List<OrderCheck> list = new ArrayList<>();
            return ResponseEntity.ok(list);
        }*/

        List<OrderCheck> orderCheck = loaderEDI.getOrderCheckList(filePath, startDate, endDate);

        return ResponseEntity.ok(orderCheck);

    }
}
