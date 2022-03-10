package com.gmail.osbornroad.repository.firebird;

import com.gmail.osbornroad.model.Note;
import com.gmail.osbornroad.model.Shipping;
import com.gmail.osbornroad.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public class FirebirdRepository{

    @Autowired
    private JdbcTemplate fireBirdJdbcTemplate;


//    @Autowired
    NoteService noteService = new NoteService();

    Timestamp defaultDate = Timestamp.valueOf(LocalDateTime.of(2000,01,01, 00, 00));

    public Integer getLastFieldKey(){

        Integer lastFieldKey = fireBirdJdbcTemplate.queryForObject("SELECT MAX(FIELD_KEY) FROM BD_DELIVERY", new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                Integer lfk = resultSet.getInt("MAX");
                return lfk;
            }
        });
        return lastFieldKey;
    }


    public Shipping getNextUnsavedShipping(int lastSavedId) {
        int nextId = lastSavedId + 1;
        try {
            return fireBirdJdbcTemplate.queryForObject("SELECT FIELD_KEY, FK_DELIVERY, DATE_TIME FROM BD_SHIPPING WHERE FIELD_KEY = " + nextId, new RowMapper<Shipping>() {
                @Override
                public Shipping mapRow(ResultSet resultSet, int i) throws SQLException {
                    Shipping shipping = new Shipping();

                    int fkDelivery = resultSet.getInt("FK_DELIVERY");
                    int noteId = noteService.getNoteId(fkDelivery);
                    shipping.setNoteId(noteId);

                    LocalDateTime dateTime = resultSet.getTimestamp("DATE_TIME").toLocalDateTime();
                    shipping.setDateTime(dateTime);

                    Integer fkShipping = resultSet.getInt("FIELD_KEY");
                    shipping.setFkShipping(fkShipping);

                    return shipping;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

//    select field_key, fk_delivery, date_time from bd_shipping
//    where field_key > 48 and field_key < 100;

    public Note getNoteFromFireBird(int id) {
        try {
            return fireBirdJdbcTemplate.queryForObject("SELECT FIELD_KEY, APOINT, SEQUESNCE_DATE, SEQUESNCE_NUM, MODELVARIANT," +
                    " SERIES, NUMBER, PLANNED, TCWI225, TCWI129, APOINT_DT FROM BD_DELIVERY WHERE FIELD_KEY = " + id, new RowMapper<Note>() {
                @Override
                public Note mapRow(ResultSet resultSet, int i) throws SQLException {

                    Note note = new Note();

                    //fieldKey
                    note.setFieldKey(resultSet.getInt("FIELD_KEY"));

                    //aPoint
                    String aPoint = Optional.of(resultSet.getString("APOINT")).orElse("A000");
                    note.setAPoint(aPoint);

                    //sequence
                    Timestamp sequesnceDateTimestamp = resultSet.getTimestamp("SEQUESNCE_DATE");
                    String sequence = "000000000000";
                    if (sequesnceDateTimestamp != null) {
                        LocalDateTime sequenceDate = sequesnceDateTimestamp.toLocalDateTime();

                        String year = String.valueOf(sequenceDate.getYear());
                        String month = String.valueOf(sequenceDate.getMonth().getValue());
                        if (month.length() == 1) {
                            month = 0 + month;
                        }
                        String day = String.valueOf(sequenceDate.getDayOfMonth());
                        if (day.length() == 1) {
                            day = 0 + day;
                        }
                        String sequenceNum = Optional.of(resultSet.getString("SEQUESNCE_NUM")).orElse("0000");
                        sequence = year + month + day + sequenceNum;
                    }
                    note.setSequence(sequence);

                    //modelVariant
                    String modelVariant = Optional.of(resultSet.getString("MODELVARIANT")).orElse("000000000000000000");
                    note.setModelVariant(modelVariant);

                    //series
                    String series = Optional.of(resultSet.getString("SERIES")).orElse("0000");
                    note.setSeries(series);

                    //number
                    Integer number = Optional.of(resultSet.getInt("NUMBER")).orElse(0);
                    note.setNumber(number);

                    //planned
                    Timestamp plannedTimestamp = resultSet.getTimestamp("PLANNED");
                    plannedTimestamp = plannedTimestamp == null ? defaultDate : plannedTimestamp;
                    LocalDateTime planned = plannedTimestamp.toLocalDateTime();
                    note.setPlanned(planned);

                    //wib225
                    String wib225 = Optional.of(resultSet.getString("TCWI225")).orElse("00");
                    note.setWib225(wib225);

                    //wib224
                    String wib129 = Optional.of(resultSet.getString("TCWI129")).orElse("00");
                    note.setWib224(wib129);

                    //aPointDateTime
                    Timestamp aPointDTTimestamp = resultSet.getTimestamp("APOINT_DT");
                    aPointDTTimestamp = aPointDTTimestamp == null ? defaultDate : aPointDTTimestamp;
                    LocalDateTime aPointDateTime = aPointDTTimestamp.toLocalDateTime();
                    note.setAPointDateTime(aPointDateTime);

                    return note;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
