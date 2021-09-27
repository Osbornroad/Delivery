package com.gmail.osbornroad.service;

import com.gmail.osbornroad.model.FinishPart;
import com.gmail.osbornroad.model.Note;
import com.gmail.osbornroad.model.Part;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Service
//@EnableScheduling
public class InitLoader {

/*
    @Autowired
    private FirebirdService firebirdService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private Match224ServiceImpl match224ServiceImpl;

    private static final Logger logger = LoggerFactory.getLogger(InitLoader.class);
    private static final Logger warnLogger = LoggerFactory.getLogger("warning");

    LocalDateTime defaultDate= LocalDateTime.of(2000, 1, 1, 0, 0);
*/

/*    private void wib224MatchingLoading(){
        Map<String, String> map = getMapFromExcel("/xlsx/Wib_codes.xlsx", 0,1);
        match224ServiceImpl.saveMapMatch224(map);
    }*/

    /*private Map<String, String> getMapFromExcel(String location, int firstCol, int secondCol){
        Map<String, String> map = new HashMap<>();
        Workbook workbook;
        try(InputStream inputStream = getClass().getResourceAsStream(location))
        {
            workbook = new XSSFWorkbook(inputStream);
        }  catch (IOException e) {
            e.printStackTrace();
            map.put("error", e.getMessage());
            return map;
        }
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if(row.getRowNum() == 0) {
                continue;
            }
            Cell cell = row.getCell(secondCol);
            if(cell == null)
                continue;
            map.put(
                    row.getCell(firstCol).getRichStringCellValue().getString(),
                    cell.getRichStringCellValue().getString()
            );
        }
        return map;
    }*/

    @Autowired
    PartService partService;

    public void partLoading() {
        Workbook workbook;
        try(InputStream inputStream = InitLoader.class.getResourceAsStream("/xlsx/partnumbers.xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Sheet sheet = workbook.getSheetAt(0);
        int order = 0;
        for(Row row : sheet) {
            if (order == 0) {
                order += 100;
                continue;
            }
            Cell partNumberCell = row.getCell(0);
            String partNumber = partNumberCell.getRichStringCellValue().getString();
            Cell partTypeCell = row.getCell(1);
            String partType = partTypeCell.getRichStringCellValue().getString();
            Cell snpCell = row.getCell(2);
            int snp = (int)snpCell.getNumericCellValue();
            Part part = new Part(partNumber, order, partType, snp);
            partService.savePart(part);

            order += 100;
        }
    }

   /* @Autowired
    FinishPartService finishPartService;*/

    /*public void finishPartLoading() {
        Workbook workbook;
        try(InputStream inputStream = InitLoader.class.getResourceAsStream("/xlsx/Wib_codes.xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Sheet sheet = workbook.getSheetAt(1);
        for(Row row : sheet) {
            Cell partNumberCell = row.getCell(0);
            String partNumber = partNumberCell.getRichStringCellValue().getString();
            Cell sortNumCell = row.getCell(1);
            Integer sortNum = (int)sortNumCell.getNumericCellValue();
            FinishPart finishPart = new FinishPart(partNumber, sortNum);
            finishPartService.saveFinishPart(finishPart);
        }
    }*/

//    private final String FIRST_SEQUENCE= "201505250190";

    //Load data from Excel
    /*public String nissanDataLoading() {
        long start = System.currentTimeMillis();
        Map<String, String> map = getMapFromExcel("/xlsx/Engine_codes.xlsx", 2, 3);
        if(map.isEmpty()){
            return "Loading of Engine_codes.xlsx retuned empty or null map";
        }
        if(map.get("error") != null) {
            return map.get("error");
        }
        Workbook workbook;
        try(InputStream inputStream = getClass().getResourceAsStream("/xlsx/Nissan.xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        Sheet sheet = workbook.getSheetAt(0);
        int counter = 0;
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            String series = getStringFromCell(row.getCell(0));
            String engine = getStringFromCell(row.getCell(6));
            String concat = series + engine;
            String wib224 = map.get(concat);
            if(null == wib224 || wib224.length() == 0){
                continue;
            }
            Integer fieldKey = 0;
            String aPoint = "A090";
            String sequence = 20 + getStringFromCell(row.getCell(7));
            if(sequence.equals(FIRST_SEQUENCE)){
                break;
            }
            String modelVariant = getStringFromCell(row.getCell(2));
            Integer number = Integer.parseInt(getStringFromCell(row.getCell(1)));
            LocalDateTime planned = defaultDate;
            String wib225 = "00";
            String aPointDTString = getStringFromCell(row.getCell(4));

            int year = Integer.parseInt(aPointDTString.substring(0, 4));
            int month = Integer.parseInt(aPointDTString.substring(4, 6));
            int day = Integer.parseInt(aPointDTString.substring(6, 8));
            int hour = Integer.parseInt(aPointDTString.substring(8, 10));
            int minute = Integer.parseInt(aPointDTString.substring(10, 12));
            LocalDateTime aPointDateTime = LocalDateTime.of(year, month, day, hour, minute);

            Note note = new Note();
            note.setFieldKey(fieldKey);
            note.setAPoint(aPoint);
            note.setSequence(sequence);
            note.setModelVariant(modelVariant);
            note.setSeries(series);
            note.setNumber(number);
            note.setPlanned(planned);
            note.setWib225(wib225);
            note.setWib224(wib224);
            note.setAPointDateTime(aPointDateTime);
            noteService.saveNote(note);
            counter++;
        }
        long finish = System.currentTimeMillis();
        String response = String.format("nissanDataLoading() saved %d Notes for %s", counter, getElapsedTime(start,finish));
        return response;
    }*/

    /*private String getStringFromCell(Cell cell){
        String cellData = "";
        switch (cell.getCellType()){
            case STRING: cellData = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                DataFormatter formatter = new DataFormatter();
                cellData = formatter.formatCellValue(cell);
                break;
        }
        return cellData;
    }*/

    /*public String getElapsedTime(long start, long finish){
        long duration = finish - start;
        long hours = duration/3600000;
        long minutes = (duration%3600000)/60000;
        long sec = (duration%60000)/1000;
        String stringTime = String.format("%d hours, %d minutes, %d seconds", hours, minutes, sec);
        return stringTime;
    }

    private static final int GAP = 50000;

    private static LocalDateTime BASE_UPDATED = null;

    public LocalDateTime getBaseUpdated() {
        return BASE_UPDATED;
    }

    public String notesTableFillingFromFireBird() {
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
        if (counter != 0)
            BASE_UPDATED = LocalDateTime.now();
        return response;
    }

//    @Scheduled(fixedDelay = 60000)
    public void scheduler() {
        notesTableFillingFromFireBird();
    }*/


    /*private static final Path rootFtp = Paths.get("C:\\Shared\\10. IT\\ARCHIVE");

    public class MyFileVisitor extends SimpleFileVisitor<Path> {

        Map<String, Integer> mapChecked = new LinkedHashMap<>();

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

            String nameArchive = file.getFileName().toString();
            Integer filesInside = 0;

            ZipFile zipFile = new ZipFile(file.toFile());

            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while(entries.hasMoreElements()){
                String str = "";
                ZipEntry entry = entries.nextElement();
                try (InputStream stream = zipFile.getInputStream(entry);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                    while ((str = reader.readLine()) != null) {

                        String aPoint = str.substring(0, 4);
                        String sequence = str.substring(4, 16);
                        String modelVariant = str.substring(16, 34);
                        String series = str.substring(34, 38);
                        Integer number = 0;
                        try {
                            number = Integer.parseInt(str.substring(38, 44));
                        } catch (NumberFormatException e) {
                            continue;
                        }
                        LocalDateTime planned;
                        String plannedStr = str.substring(56, 68);
                        try {
                            planned = LocalDateTime.of(
                                    Integer.parseInt(plannedStr.substring(0, 4)),
                                    Integer.parseInt(plannedStr.substring(4, 6)),
                                    Integer.parseInt(plannedStr.substring(6, 8)),
                                    Integer.parseInt(plannedStr.substring(8, 10)),
                                    Integer.parseInt(plannedStr.substring(10, 12)));
                        } catch (NumberFormatException | DateTimeException e) {
                            e.printStackTrace();
                            continue;
                        }

                        String wib225;
                        String wib224;
                        LocalDateTime aPointDateTime;

                        if (str.length() == 84) {
                            // apoint A080
                            // sequence 2020 12 03 0165
                            // variant FDRNLPWJ11EQA-DA-B
                            // series J117
                            // number 120760
                            // --- 2020 12 04 0087
                            // planned 2020 12 04 11 30
                            // achieved 2020 12 03 15 31
                            // 225 MX
                            // 224 F2

                            //0123 4            16                 34   38     44           56           68           80 82
                            //A080 202012030165 FDRNLPWJ11EQA-DA-B J117 120760 202012040087 202012041130 202012031531 MX F2

                            String aPointDateTimeStr = str.substring(68, 80);
                            try {
                                aPointDateTime = LocalDateTime.of(
                                        Integer.parseInt(aPointDateTimeStr.substring(0, 4)),
                                        Integer.parseInt(aPointDateTimeStr.substring(4, 6)),
                                        Integer.parseInt(aPointDateTimeStr.substring(6, 8)),
                                        Integer.parseInt(aPointDateTimeStr.substring(8, 10)),
                                        Integer.parseInt(aPointDateTimeStr.substring(10, 12)));
                            } catch (NumberFormatException | DateTimeException e) {
                                e.printStackTrace();
                                continue;
                            }

                            wib225 = str.substring(80, 82);
                            wib224 = str.substring(82, 84);
                        } else if (str.length() == 72) {
                            //0    4            16                 34   38     44           56           68 70
                            //A080 201502270175 TDRNLRWT32E8A-B--- T326 004019 201503020088 201503021757 2E CM

                            String aPointDateTimeStr = str.substring(56, 68);
                            try {
                                aPointDateTime = LocalDateTime.of(
                                        Integer.parseInt(aPointDateTimeStr.substring(0, 4)),
                                        Integer.parseInt(aPointDateTimeStr.substring(4, 6)),
                                        Integer.parseInt(aPointDateTimeStr.substring(6, 8)),
                                        Integer.parseInt(aPointDateTimeStr.substring(8, 10)),
                                        Integer.parseInt(aPointDateTimeStr.substring(10, 12)));
                            } catch (NumberFormatException | DateTimeException e) {
                                e.printStackTrace();
                                continue;
                            }

                            wib225 = str.substring(68, 70);
                            wib224 = str.substring(70, 72);
                        } else
                            continue;
                        Note note = new Note(0, aPoint, sequence, modelVariant, series, number, planned, wib225, wib224, aPointDateTime);
                        if (number == 999999) {
                            System.out.println(999999);

                        }
                        noteService.saveNote(note);
                    }
                }
                filesInside++;

            }

            System.out.println("Hello");

            mapChecked.put(nameArchive, filesInside);

            return FileVisitResult.CONTINUE;
        }
    }

    public void walkFTP() {
        FileVisitor visitor = new MyFileVisitor();
        try {
            Files.walkFileTree(rootFtp, visitor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
