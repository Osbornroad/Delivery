package com.gmail.osbornroad.service;

import com.gmail.osbornroad.model.*;
import com.gmail.osbornroad.repository.jpa.PartQtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PartQtyService {

    @Autowired
    PartQtyRepository partQtyRepository;

    @Autowired
    PartService partService;

    @Autowired
    FinishPartService finishPartService;

    public PartQty findByPartAndFinishPart(Integer partId, Integer finishPartId) {
        Part part = partService.findPartById(partId);
        FinishPart finishPart = finishPartService.findFinishPartById(finishPartId);
        Optional<PartQty> optional = partQtyRepository.findByPartAndFinishPart(part, finishPart);
        return optional.orElse(null);
    }

    public PartQtyDTO getDtoFromPartQty(PartQty partQty) {
        int partId = partQty.getPart().getId();
        int finishPartId = partQty.getFinishPart().getId();
        String partNumber = partQty.getPart().getPartNumber();
        double qty = partQty.getQty();
        PartQtyDTO dto = new PartQtyDTO(partId, finishPartId, partNumber, qty);
        return dto;
    }

    public PartQty getPartQtyFromDto(PartQtyDTO dto) {
        int partId = dto.getPartId();
        int finishPartId = dto.getFinishPartId();
        PartQty partQty = findByPartAndFinishPart(partId, finishPartId);
        if (partQty == null) {
            PartQtyKey key = new PartQtyKey(partId, finishPartId);
            Part part = partService.findPartById(partId);
            FinishPart finishPart = finishPartService.findFinishPartById(finishPartId);
            partQty = new PartQty(key, part, finishPart);
        }
        partQty.setQty(dto.getQty());
        return partQty;
    }

    public PartQty savePartQty(PartQty partQty) {
        try
        {
            return partQtyRepository.save(partQty);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public void deletePartQty(PartQty partQty) {
        partQtyRepository.delete(partQty);
    }
}
