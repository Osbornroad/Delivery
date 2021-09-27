package com.gmail.osbornroad.repository.jpa;

import com.gmail.osbornroad.model.FinishPart;
import com.gmail.osbornroad.model.Part;
import com.gmail.osbornroad.model.PartQty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartQtyRepository  extends JpaRepository<PartQty, Integer> {
    Optional <PartQty> findByPartAndFinishPart(Part part, FinishPart finishPart);
}
