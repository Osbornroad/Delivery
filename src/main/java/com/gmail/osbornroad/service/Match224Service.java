package com.gmail.osbornroad.service;


import com.gmail.osbornroad.model.Match224;
import com.gmail.osbornroad.repository.jpa.Match224Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service()
@Transactional
public class Match224Service {

    @Autowired
    private Match224Repository match224Repository;

    private static final Logger logger = LoggerFactory.getLogger(Match224Service.class);

    public Match224 findByConcat(String concat){
        Match224 match224 = match224Repository.findByConcat(concat);
        logger.info("findByConcat({}) returns {}", concat, match224);
        return match224;
    }

/*    public void saveMapMatch224(Map<String, String> map){
        for (Map.Entry<String, String> entry : map.entrySet()){
            Match224 match224 = new Match224(entry.getKey(), entry.getValue());
            saveMatch224(match224);
        }
        logger.info("saveMapMatch224()");
    }

    public Match224 saveMatch224(Match224 match224){
        Match224 savedMatch224 = match224Repository.save(match224);
        logger.info("saveMatch224({}) returns {}", match224, savedMatch224);
        return savedMatch224;
    }*/
}
