package com.gmail.osbornroad.repository.jpa;

import com.gmail.osbornroad.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {

    LocalDateTime TODAY = LocalDateTime.now();

    String A090 = "A090";

    @Query("SELECT n FROM Note n WHERE n.wib224 IN (:wib224List) AND n.APoint = :aPoint AND n.planned > :today")
    List<Note> getForecastA001(@Param("wib224List") List<String> wib224List,
                               @Param("aPoint") String aPoint,
                               @Param("today") LocalDateTime today);

    Note findTopByOrderByIdDesc();


    @Query("SELECT n FROM Note n WHERE n.APoint IN (:aPoints) AND  n.APointDateTime BETWEEN :startDate AND :endDate ORDER BY n.APointDateTime DESC")
    List<Note> getBetweenDates(@Param("aPoints") List<String> aPoints,
                               @Param("startDate") LocalDateTime startDate,
                               @Param("endDate") LocalDateTime endDate);


//    @Query("SELECT MAX(n.sequence) FROM Note n GROUP BY n.series")
    @Query("SELECT DISTINCT n.series FROM Note n ORDER BY n.series ASC")
    List<String> getAllSeries();

    @Query("SELECT DISTINCT n.wib224 FROM Note n ORDER BY n.wib224 ASC")
    List<String> getAllWib224();

    /*    @Query("SELECT n FROM Note n WHERE n.APoint =:aPoint AND n.wib224 IN (:wib224List) AND  n.APointDateTime BETWEEN :startDate AND :endDate ORDER BY n.APointDateTime DESC")
    List<Note> getReportA090(@Param("aPoint") String aPoint,
                             @Param("wib224List") List<String> wib224List,
                             @Param("startDate") LocalDateTime startDate,
                             @Param("endDate") LocalDateTime endDate);*/
}
