package com.gmail.osbornroad.repository.jpa;

import com.gmail.osbornroad.model.Match224;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface Match224Repository extends JpaRepository<Match224, Integer> {

    Match224 findByConcat(@Param("concat") String concat);
}
