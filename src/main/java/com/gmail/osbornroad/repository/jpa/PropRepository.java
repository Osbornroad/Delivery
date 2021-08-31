package com.gmail.osbornroad.repository.jpa;

import com.gmail.osbornroad.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PropRepository  extends JpaRepository<Property, Integer> {
    Optional<Property> findByPropName(String propName);
}
