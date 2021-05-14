package com.gmail.osbornroad.repository.jpa;

import com.gmail.osbornroad.model.FinishPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FinishPartRepository extends JpaRepository<FinishPart, Integer>{
    Optional<FinishPart> findByFinishPartNumber(String partNumber);
}
