package com.gmail.osbornroad.repository.jpa;

import com.gmail.osbornroad.model.Kit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KitRepository extends JpaRepository<Kit, Integer> {
/*
    @Query("SELECT k FROM Kit k WHERE k.wib224 = :wib224")
    Kit findByWib224(@Param("wib224") String wib224);*/

    List<Kit> findByCurrentIsTrue();

    List<Kit> findAllByKitNameIn(List<String> kits);

    @Query("SELECT k.kitName FROM Kit k ORDER BY k.sortNum ASC")
    List<String> getAllKitNames();

    @Query("SELECT k.wib224 FROM Kit k")
    List <String> getUsedWib224();

    Kit findKitByKitName(String kitName);


}
