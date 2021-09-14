package com.gmail.osbornroad.service;


import com.gmail.osbornroad.model.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableScheduling
public class Scheduler {

    private static final Logger logger = LoggerFactory.getLogger(InitLoader.class);
    private static final Logger warnLogger = LoggerFactory.getLogger("warning");

    @Autowired
    private FirebirdService firebirdService;

    @Autowired
    private NoteService noteService;

    public String getElapsedTime(long start, long finish){
        long duration = finish - start;
        long hours = duration/3600000;
        long minutes = (duration%3600000)/60000;
        long sec = (duration%60000)/1000;
        return String.format("%d hours, %d minutes, %d seconds", hours, minutes, sec);
    }

    private static final int GAP = 50000;

/*    private static LocalDateTime BASE_UPDATED = null;

    public LocalDateTime getBaseUpdated() {
        return BASE_UPDATED;
    }*/

    public void notesTableFillingFromFireBird() {
        long start = System.currentTimeMillis();
        Integer lastFKPostgres = noteService.getLastSavedFieldKey();
        if (lastFKPostgres == 0){
            lastFKPostgres = 3463;
        }
        Integer lastFKFireBird = firebirdService.getLastFieldKey();
        if(lastFKFireBird > lastFKPostgres + GAP)
        {
            lastFKFireBird = lastFKPostgres + GAP;
        }

        int counter = 0;

        List<Integer> unfoundedFK = new ArrayList<>();

        for(int i = lastFKPostgres + 1; i <= lastFKFireBird; i++){
            Note note = firebirdService.getNoteFromFireBird(i);
            if(null != note){
                noteService.saveNote(note);
                counter++;
            } else {
                warnLogger.warn("firebirdService.getNoteFromFireBird({}) returned NULL", i);
                unfoundedFK.add(i);
            }
        }
        long finish = System.currentTimeMillis();
        String response = String.format("notesTableFillingFromFireBird() saved %d Notes for %s from %s until %s\n" +
                        "firebirdService.getNoteFromFireBird({}) returned NULL for following fieldKeys: \n" +
                        unfoundedFK.toString(),
                counter, getElapsedTime(start,finish),lastFKPostgres + 1, noteService.getLastSavedFieldKey());
        logger.info(response);
/*        if (counter != 0)
            BASE_UPDATED = LocalDateTime.now();*/
    }

//        @Scheduled(fixedDelay = 60000)
    public void scheduler() {
        notesTableFillingFromFireBird();
    }
}
