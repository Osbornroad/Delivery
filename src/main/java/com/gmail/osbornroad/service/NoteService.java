package com.gmail.osbornroad.service;


import com.gmail.osbornroad.model.*;
import com.gmail.osbornroad.repository.jdbc.ReportRepository;
import com.gmail.osbornroad.repository.jpa.KitRepository;
import com.gmail.osbornroad.repository.jpa.NoteRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class NoteService {

    @Autowired
    NoteRepository noteRepository;

    private static final Logger logger = LoggerFactory.getLogger(NoteService.class);
    private static final Logger warnLogger = LoggerFactory.getLogger("warning");


    public Note findNoteById(Integer id) {
        Optional<Note> optional = noteRepository.findById(id);
        logger.info("findNoteById({}) returns {}", id, optional);
        return optional.orElse(null);
    }

    @Transactional(readOnly = true)
    public Collection<Note> getBetweenDates(List<String> aPoints, LocalDateTime startDate, LocalDateTime endDate) {
        return noteRepository.getBetweenDates(aPoints, startDate, endDate);
    }

    public Note saveNote(Note note) {
        Note savedNote = null;
        try {
            savedNote = noteRepository.save(note);
            logger.info("saveNote({}) returns {}", note.getSeries() + note.getNumber(), savedNote);
        } catch (DataIntegrityViolationException e)
        {
            warnLogger.warn("Note " + note + " already exists" + e.getMessage());
        } catch (Exception e) {
            warnLogger.warn(e.getMessage());
        }
        return savedNote;
    }

    public Note getLastSavedNote(){
        Optional<Note> optional = Optional.ofNullable(noteRepository.findTopByOrderByIdDesc());
        return optional.orElse(null);
    }

    public LocalDateTime getLastSavedDate() {
        Note lastNote = getLastSavedNote();
        LocalDateTime lastDate = LocalDateTime.MIN;
        if(null != lastNote) {
            LocalDateTime date = lastNote.getAPointDateTime();
            if(null != date){
                lastDate = date;
            }
        }
        return lastDate;
    }

    public Integer getLastSavedFieldKey(){
        Integer lastFieldKey = 0;
        Note lastNote = getLastSavedNote();
        if(null != lastNote) {
            Integer fieldKey = lastNote.getFieldKey();
            if(null != fieldKey){
                lastFieldKey = fieldKey;
            }
        }
        return lastFieldKey;
    }

    public List<String> getAllSeries() {
        return noteRepository.getAllSeries();
    }

    public List<String> getAllWib224() {
        return noteRepository.getAllWib224();
    }

    /*public void checkGaps() {
        Note noteA080 = null;
        Note noteA090 = null;
        for (int i = 127500; i < 811000; i++) {
            Note nextNote = findNoteById(i);
            if (nextNote.getAPoint().equals("A080")) {
                if (noteA080 == null) {
                    noteA080 = nextNote;
                    continue;
                } else {
                    int prevNum = Integer.parseInt(noteA080.getSequence().substring(8));
                    int nextNum = Integer.parseInt(nextNote.getSequence().substring(8));
                    if (nextNum == 1) {
                        noteA080 = nextNote;
                        continue;
                    }
                    if (nextNum - prevNum != 1) {
                        warnLogger.warn("There is a gap before " + nextNote);
                    }
                    noteA080 = nextNote;
                }
            }
            if (nextNote.getAPoint().equals("A090")) {
                if (noteA090 == null) {
                    noteA090 = nextNote;
                    continue;
                } else {
                    int prevNum = Integer.parseInt(noteA090.getSequence().substring(8));
                    int nextNum = Integer.parseInt(nextNote.getSequence().substring(8));
                    if (nextNum == 1) {
                        noteA090 = nextNote;
                        continue;
                    }
                    if (nextNum - prevNum != 1) {
                        warnLogger.warn("There is a gap before " + nextNote);
                    }
                    noteA090 = nextNote;
                }
            }
        }
    }*/

    private static final String APOINT = "A001";

    @Autowired
    KitService kitService;

    public List<Map<String, String>> getForecast() {
        List<Note> notes = noteRepository.getForecastA001(kitService.getCurrentWib224List(), APOINT, LocalDate.now().atStartOfDay());
        List<Kit> existKits = kitService.findAllKits();
        List<Kit> kits = new ArrayList<>();
        List<LocalDate> dates = new ArrayList<>();

        //For all Notes
        for(Note note : notes) {
            //Filling "dates" Arraylist
            LocalDate date = note.getPlanned().toLocalDate();
            if(!dates.contains(date))
                dates.add(date);
            //Filling "kits" ArrayList
            for(Kit existKit : existKits) {
                if(existKit.getWib224().equals(note.getWib224())) {
                    if(!kits.contains(existKit)) {
                        kits.add(existKit);
                    }
                    break;
                }
            }
        }
        //Sorting
        dates.sort(LocalDate::compareTo);
        kits.sort(Comparator.comparingInt(Kit::getSortNum));
        //Temporary Map
        Map<Kit, Map<LocalDate, Integer>> interMap = new LinkedHashMap<>();
        //Creating empty map date-quantity and adding to temp map for creating table structure
        for(Kit kit : kits) {
            Map<LocalDate, Integer> map = new LinkedHashMap<>();
            for(LocalDate date : dates) {
                map.put(date, 0);
            }
            interMap.put(kit, map);
        }
        //Creating map wib-kit for quick find kit by map
        Map<String, Kit> kitHashMap = new HashMap<>();
        for(Kit kit : kitService.findAllKits()) {
            kitHashMap.put(kit.getWib224(), kit);
        }
        //Filling temp map
        for(Note note : notes) {
            String wib224 = note.getWib224();
            Kit kit = kitHashMap.get(wib224);
            if(interMap.containsKey(kit)) {
                Map<LocalDate, Integer> itemMap = interMap.get(kit);
            /*if(itemMap == null)
                continue;*/
                LocalDate itemDate = note.getPlanned().toLocalDate();
                if(itemMap.containsKey(itemDate)) {
                    itemMap.put(itemDate, itemMap.get(itemDate) + 1);
                }
            }
        }

        List<Map<String, String>> forecast = new ArrayList<>();

        Map<String, Integer> mapSum = new LinkedHashMap<>();
        mapSum.put("Kit", 0);

        //Temp map to result map
        for(Map.Entry<Kit, Map<LocalDate, Integer>> mapEntry : interMap.entrySet()) {
            Map<String, String> forecastItem = new LinkedHashMap<>();
            forecastItem.put("Kit", mapEntry.getKey().getKitName());
            for (Map.Entry<LocalDate, Integer> forecastDate : mapEntry.getValue().entrySet()) {
                String forecastItemKey = forecastDate.getKey().getDayOfMonth() + "." + forecastDate.getKey().getMonthValue();
                Integer forecastItemValue = forecastDate.getValue();
                forecastItem.put(forecastItemKey, forecastItemValue.toString());

                if(mapSum.containsKey(forecastItemKey)) {
                    mapSum.put(forecastItemKey, mapSum.get(forecastItemKey) + forecastItemValue);
                } else {
                    mapSum.put(forecastItemKey, forecastItemValue);
                }

            }
            forecast.add(forecastItem);
        }

        Map<String, String> sum = new LinkedHashMap<>();

        for (Map.Entry<String, Integer> entry : mapSum.entrySet()) {
            if(entry.getKey().equals("Kit")) {
                sum.put("Kit", "Total");
                continue;
            }
            sum.put(entry.getKey(), entry.getValue().toString());
        }

        forecast.add(sum);

        return forecast;
    }

/*    @Autowired
    ReportRepository reportRepository;

    @Autowired
    KitRepository kitRepository;

    private List<String> getPartNumbers(List<String> kits) {

        List<Kit> kitList = kitRepository.findAllByKitNameIn(kits);

        List<FinishPart> finishParts = new ArrayList<>();
        for(Kit kit : kitList) {
            for(FinishPart part : kit.getFinishPartSet()) {
                if(!finishParts.contains(part))
                    finishParts.add(part);
            }
        }
        finishParts.sort(Comparator.comparingInt(FinishPart::getSortNum));
        List<String> partNumbers = new ArrayList<>();
        for(FinishPart part : finishParts) {
            partNumbers.add(part.getFinishPartNumber());
        }

        return partNumbers;
    }*/

/*    public String[][] getReport(String aPoint, List<String> kits, LocalDate startDate, LocalDate endDate, String typeReport) {

        //Getting report from repository
        List<ReportItem> unfilteredReport = reportRepository.getReport(aPoint,
                Timestamp.valueOf(startDate.atStartOfDay()),
                Timestamp.valueOf(endDate.plusDays(1).atStartOfDay()), typeReport);

        //Filtering report by given kits
        List<ReportItem> reportItems = new ArrayList<>();
        for(ReportItem item : unfilteredReport) {
            if(kits.contains(item.getKitName()))
                reportItems.add(item);
        }

        //Getting part numbers based on kit names
        List<String> partNumbers = getPartNumbers(kits);

        //Getting list of unrepeatable dates from report
        List<Integer> dateUnits = new ArrayList<>();
        for(ReportItem item : reportItems) {
            int integerDate = item.getIntegerDate();
            if(!dateUnits.contains(integerDate))
                dateUnits.add(integerDate);
        }
        dateUnits.sort(Comparator.comparingInt(o -> o));
        List<String> stringDateUnits = new ArrayList<>();
        for(Integer dateUnit : dateUnits) {
            stringDateUnits.add(dateUnit.toString());
        }

        //Create 2-dimension array

        int rows = partNumbers.size() + 3;
        int cols = dateUnits.size() + 2;
        String[][] report = new String[rows][cols];
        report[0][0] = "Part number";
        report[rows - 2][0] = "Kits";
        report[rows - 1][0] = "Summary";
        report[0][cols - 1] = "Total";

        //Filling map for 1 st row with dateUnits

        int length = report[0].length;
        Map<String, Integer> dateUnitsMap = new LinkedHashMap<>();
        for(int i = 1; i < length - 1; i++) {
            report[0][i] = stringDateUnits.get(i - 1);
            dateUnitsMap.put(report[0][i], i);
        }

        //Filling map for 1st column with part numbers

        Map<String, Integer> numbersMap = new LinkedHashMap<>();
        for(int i = 1; i < report.length - 2; i++){
            report[i][0] = partNumbers.get(i - 1);
            numbersMap.put(report[i][0], i);
        }

        //Create 2-dimension array with only results

        int[][] intReport = new int[rows][cols];

        for(ReportItem item : reportItems) {
            int colNum = dateUnitsMap.get(item.getIntegerDate().toString());
            Set<FinishPart> finishPartSet = kitRepository.findKitByKitName(item.getKitName()).getFinishPartSet();
            for(FinishPart finishPart : finishPartSet) {
                int rowNum = numbersMap.get(finishPart.getFinishPartNumber());
                intReport[rowNum][colNum] = intReport[rowNum][colNum] + item.getQuantity();
            }
            intReport[rows - 2][colNum] = intReport[rows - 2][colNum] + item.getQuantity();
        }

        //Row's sum counting (Total)
        for(int i = 1; i < intReport.length - 1; i++) {
            int sum = 0;
            for(int j = 1; j < intReport[i].length - 1; j++) {
                sum += intReport[i][j];
            }
            intReport[i][intReport[i].length - 1] = sum;
        }

        //Col's sum counting (Summary)
        for(int j = 1; j < intReport[0].length; j++){
            int sum = 0;
            for(int i = 1; i < intReport.length - 2; i++) {
                sum += intReport[i][j];
            }
            intReport[intReport.length - 1][j] = sum;
        }

        //Migration from intReport to report
        for(int i = 1; i < intReport.length; i++) {
            for(int j = 1; j < intReport[i].length; j++) {
                report[i][j] = String.valueOf(intReport[i][j]);
            }
        }

        return report;
    }*/

}
