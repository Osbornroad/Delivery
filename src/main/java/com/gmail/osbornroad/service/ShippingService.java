package com.gmail.osbornroad.service;

import com.gmail.osbornroad.model.Note;
import com.gmail.osbornroad.model.Shipping;
import com.gmail.osbornroad.repository.jpa.ShippingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ShippingService {

    private static final Logger logger = LoggerFactory.getLogger(NoteService.class);
    private static final Logger warnLogger = LoggerFactory.getLogger("warning");

    @Autowired
    ShippingRepository shippingRepository;

    @Autowired
    NoteService noteService;

    public void saveNextShipping() {
//        int noteId = getLastSavedShipping().getNoteId();
//        Note noteOfLastSavedShipping = noteService.findNoteById(noteId);

        int fk_shipping = getLastSavedShipping().getFkShipping();
    }

    public Shipping getLastSavedShipping() {
        Shipping shipping = shippingRepository.findTopByOrderByIdDesc();
        return shipping;
    }



    public Shipping saveShipping (Shipping shipping) {
        Shipping savedShipping = null;
        try {
            savedShipping = shippingRepository.save(shipping);
            logger.info("saveNote({}) returns {}", shipping.getNoteId(), savedShipping);
        } catch (DataIntegrityViolationException e)
        {
            warnLogger.warn("Note " + shipping + " already exists" + e.getMessage());
        } catch (Exception e) {
            warnLogger.warn(e.getMessage());
        }
        return savedShipping;
    }
}
