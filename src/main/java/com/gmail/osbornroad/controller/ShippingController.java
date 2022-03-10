package com.gmail.osbornroad.controller;

import com.gmail.osbornroad.model.Shipping;
import com.gmail.osbornroad.repository.jpa.ShippingRepository;
import com.gmail.osbornroad.service.InitLoader;
import com.gmail.osbornroad.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/shipping")
public class ShippingController {

    @Autowired
    InitLoader initLoader;

    @Autowired
    ShippingService shippingService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String loadNissanShippingBase() {
        initLoader.nissanBaseShippingLoading();
        return "main";
    }

    @GetMapping(value = "/sanoh", produces = MediaType.APPLICATION_JSON_VALUE)
    public String loadSanohShippingBase() {
        initLoader.sanohBaseShippingLoading();
//        Shipping shipping = shippingService.getLastSavedShipping();
        return "main";
    }
}
