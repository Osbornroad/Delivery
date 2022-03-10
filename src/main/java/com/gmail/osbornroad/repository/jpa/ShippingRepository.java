package com.gmail.osbornroad.repository.jpa;

import com.gmail.osbornroad.model.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShippingRepository extends JpaRepository<Shipping, Integer> {

    Shipping findTopByOrderByIdDesc();


}
