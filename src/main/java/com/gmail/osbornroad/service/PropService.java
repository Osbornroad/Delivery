package com.gmail.osbornroad.service;

import com.gmail.osbornroad.model.Property;
import com.gmail.osbornroad.repository.jpa.PropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PropService {

    @Autowired
    PropRepository propRepository;

    public Property findByPropName(String propName) {
        Optional<Property> optional = propRepository.findByPropName(propName);
        return optional.orElse(null);
    }

    public String getPropValueByName(String propName) {
        String propValue = findByPropName(propName).getPropValue();
        return propValue;
    }
}
