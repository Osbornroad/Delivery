package com.gmail.osbornroad.service;

import com.gmail.osbornroad.model.FinishPart;
import com.gmail.osbornroad.repository.jpa.FinishPartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FinishPartService {

    @Autowired
    FinishPartRepository finishPartRepository;

    public FinishPart findFinishPartById(Integer id) {
        Optional<FinishPart> optional = finishPartRepository.findById(id);
        return optional.orElse(null);
    }

    @Transactional(readOnly = true)
    public List<FinishPart> findAllFinishParts() {
        List<FinishPart> finishPartList = new ArrayList<>();
        Iterable<FinishPart> iterable = finishPartRepository.findAll();
        iterable.forEach(finishPartList::add);
        return finishPartList;
    }

    public FinishPart findFinishPartByNumber(String partNumber) {
        Optional<FinishPart> optional = finishPartRepository.findByFinishPartNumber(partNumber);
        return optional.orElse(null);
    }

    public FinishPart saveFinishPart(FinishPart finishPart) {
        try
            {
                return finishPartRepository.save(finishPart);
            }
        catch (Exception e)
        {
            return null;
        }
    }

    public void deleteFinishPart(FinishPart finishPart) {
        finishPartRepository.delete(finishPart);
    }

}
