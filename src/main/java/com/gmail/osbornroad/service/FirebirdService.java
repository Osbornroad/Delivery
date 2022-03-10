package com.gmail.osbornroad.service;

import com.gmail.osbornroad.model.Match224;
import com.gmail.osbornroad.model.Note;
import com.gmail.osbornroad.model.Shipping;
import com.gmail.osbornroad.repository.firebird.FirebirdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FirebirdService {

    @Autowired
    FirebirdRepository firebirdRepository;

    @Autowired
    Match224Service match224Service;

    private static final Logger logger = LoggerFactory.getLogger(FirebirdService.class);

    public Shipping getNextUnsavedShipping(int lastSavedId) {
        return firebirdRepository.getNextUnsavedShipping(lastSavedId);
    }


        public Integer getLastFieldKey(){
        Integer lastFieldKey = firebirdRepository.getLastFieldKey();
//        logger.info("getLastFieldKey() returns {}", lastFieldKey);
        return lastFieldKey;
    }

    public Note getNoteFromFireBird(int id) {
        Note note = firebirdRepository.getNoteFromFireBird(id);
        if(null == note){
//            logger.info("getNoteFromFireBird({}) returns {}", id, null);
            return null;
        }
        if (note.getFieldKey() < 101986) {
            String concat = note.getSeries() + note.getWib225() + note.getWib224();
            concat = concat.trim();
            Match224 wib224 = match224Service.findByConcat(concat);
            if(null != wib224) {
                note.setWib224(wib224.getWib224());
            }
        }
//        logger.info("getNoteFromFireBird({}) returns {}", id, note);
        return note;
    }

}
