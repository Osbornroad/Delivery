package com.gmail.osbornroad.service;

import com.gmail.osbornroad.model.Part;
import com.gmail.osbornroad.repository.jpa.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PartService {

    @Autowired
    PartRepository partRepository;

    public Part findPartById(Integer id) {
        Optional<Part> optional = partRepository.findById(id);
        return optional.orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Part> findAllParts() {
        List<Part> partList = new ArrayList<>();
        Iterable<Part> iterable = partRepository.findAll();
        iterable.forEach(partList::add);
        return partList;
    }


    public Part findPartByNumber(String partNumber) {
        Optional<Part> optional = partRepository.findByPartNumber(partNumber);
        return optional.orElse(null);
    }

    public Part savePart(Part part) {
        try
        {
            return partRepository.save(part);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public void deletePart(Part part) {
        partRepository.delete(part);
    }
}
