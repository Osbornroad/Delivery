package com.gmail.osbornroad.repository.jpa;

import com.gmail.osbornroad.model.FinishPart;
import com.gmail.osbornroad.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartRepository extends JpaRepository<Part, Integer> {
    Optional<Part> findByPartNumber(String partNumber);

}
