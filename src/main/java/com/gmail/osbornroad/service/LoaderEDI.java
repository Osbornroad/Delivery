package com.gmail.osbornroad.service;

import com.gmail.osbornroad.model.*;
import com.gmail.osbornroad.repository.jpa.FinishPartRepository;
import com.gmail.osbornroad.repository.jpa.PropRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class LoaderEDI {

    @Autowired
    FinishPartRepository finishPartRepository;

    private int getMonth(String strMonth) {

        int intMonth = 0;
        switch (strMonth) {
            case ("авг"):
                intMonth = 8;
                break;
            case ("сен"):
                intMonth = 9;
                break;
            case ("окт"):
                intMonth = 10;
                break;
            case ("ноя"):
                intMonth = 11;
                break;
            case ("дек"):
                intMonth = 12;
                break;
            case ("янв"):
                intMonth = 1;
                break;
            case ("фев"):
                intMonth = 2;
                break;
            case ("март"):
                intMonth = 3;
                break;
        }

        return intMonth;
    }

    public String[][] getArrayOfEdi(File file, String ediDir) {

        int p = 55;

        if(file == null) {
            try {
                Property weeklyEdiDir = propRepository.findByPropName(ediDir).orElse(null);
                String propValue = weeklyEdiDir.getPropValue();
                Path path = findLastModifiedEdi(propValue);
                file = path.toFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<UnitEDI> unitEDIList = getEDIfromCSV(file);

        List<String> partNumbers = new ArrayList<>();

        List<LocalDate> dates = new ArrayList<>();

        //For all UnitEDI
        for(UnitEDI unitEDI : unitEDIList) {

            //Filling "dates" Arraylist
            LocalDate date = unitEDI.getDate();
            if(!dates.contains(date))
                dates.add(date);

            //Filling "partNumbers" ArrayList
            String partNumber = unitEDI.getPartNumber();
            if(!partNumbers.contains(partNumber))
                partNumbers.add(partNumber);
        }

        //Sorting dates
        dates.sort(LocalDate::compareTo);
        List<String> stringDates = new ArrayList<>();
        for(LocalDate date : dates) {
            stringDates.add(date.toString());
        }

        //Sorting partNumbers
        List<FinishPart> finishPartList = new ArrayList<>();
        for (String partNum : partNumbers) {
            FinishPart finishPart = finishPartRepository.findByFinishPartNumber(partNum).orElse(null);
            if(!finishPartList.contains(finishPart)) {
                finishPartList.add(finishPart);
            }
        }
        finishPartList.sort(Comparator.comparingInt(FinishPart::getSortNum));

        List<String> stringPartNumbers = new ArrayList<>();
        for(FinishPart part : finishPartList) {
            stringPartNumbers.add(part.getFinishPartNumber());
        }

        //Create 2-dimension array

        int rows = stringPartNumbers.size() + 3;
        int cols = stringDates.size() + 2;
        String[][] report = new String[rows][cols];
        report[0][0] = "Part number";
        report[rows - 2][0] = "Kits";
        report[rows - 1][0] = "Summary";
        report[0][cols - 1] = "Total";

        //Filling map for 1 st row with dates

        int length = report[0].length;
        Map<String, Integer> datesMap = new LinkedHashMap<>();
        for(int i = 1; i < length - 1; i++) {
            report[0][i] = stringDates.get(i - 1);
            datesMap.put(report[0][i], i);
        }

        //Filling map for 1st column with part numbers

        Map<String, Integer> numbersMap = new LinkedHashMap<>();
        for(int i = 1; i < report.length - 2; i++){
            report[i][0] = stringPartNumbers.get(i - 1);
            numbersMap.put(report[i][0], i);
        }

        //Create 2-dimension array with only results

        int[][] intReport = new int[rows][cols];
        for(UnitEDI unitEDI : unitEDIList) {
            int colNum = datesMap.get(unitEDI.getDate().toString());
            int rowNum = numbersMap.get(unitEDI.getPartNumber());

            String family = unitEDI.getPartNumber().substring(0, 5);

            intReport[rowNum][colNum] = intReport[rowNum][colNum] + unitEDI.getQuantity();

            if ("46220".equals(unitEDI.getPartNumber().substring(0, 5))) {
                intReport[rows - 2][colNum] = intReport[rows - 2][colNum] + unitEDI.getQuantity();
            }
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
    }

    public String[] getEdiProperties(File file, String ediDir) {

        int o = 78;

        if(file == null) {
            try {
                Property weeklyEdiDir = propRepository.findByPropName(ediDir).orElse(null);
                String propValue = weeklyEdiDir.getPropValue();
                Path path = findLastModifiedEdi(propValue);
                file = path.toFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*try {
                Files.walkFileTree(Paths.get(String.valueOf(propRepository.findByPropName("weeklyEdiDir"))), new EdiFileVisitor());
            } catch (IOException e) {
                e.printStackTrace();
            }
            filePath = LAST_EDI;*/
        }

        String[] properties = new String[3];
//        int counter = 0;
        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String[] lineInArray = reader.readNext();
            lineInArray = reader.readNext();
            if(lineInArray == null) {
                return null;
            }
            properties[0] = lineInArray[0]; //EDI number
            properties[1] = Objects.requireNonNull(getDate(lineInArray[1])).toString(); //EDI date

            properties[2] = ediDir.equals("weeklyEdiDir") ? "Weekly EDI forecast" : "Daily EDI forecast";

            /*while ((lineInArray = reader.readNext()) != null) {
                counter++;
                if (counter == 2)
                    break;
                propMap.put("ediNumber", li
            }*/
        }  catch (CsvValidationException | IOException e) {
                e.printStackTrace();
        }

        return properties;
    }

    private LocalDate getDate(String strDate) {
        int day, month, year;
        try {
            day = Integer.parseInt(strDate.substring(0, 2));
            month = getMonth(strDate.substring(3, 6));
            year = Integer.parseInt(strDate.substring(strDate.length() - 4));
        } catch (NumberFormatException e) {
            return null;
        }
        LocalDate date = LocalDate.of(year, month, day);
        return date;
    }

    @Autowired
    PropRepository propRepository;

//    private static String LAST_EDI = "";

    private Path findLastModifiedEdi(String sdir) throws IOException {
        Path dir = Paths.get(sdir);
        if (Files.isDirectory(dir)) {
            Optional<Path> opPath = Files.list(dir)
                    .filter(p -> !Files.isDirectory(p))
                    .sorted((p1, p2)-> Long.valueOf(p2.toFile().lastModified())
                            .compareTo(p1.toFile().lastModified()))
                    .findFirst();

            if (opPath.isPresent()){
                return opPath.get();
            }
        }

        return null;
    }

    public List<UnitEDI> getEDIfromCSV (File file) {


        List<UnitEDI> listEDI = new ArrayList();
        int counter = 0;
        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String[] lineInArray;
            while ((lineInArray = reader.readNext()) != null) {
                counter++;
                if (counter == 1)
                    continue;
                if (lineInArray.length < 19)
                    continue;
                if (lineInArray[17].equals(""))
                    continue;
                String partName = lineInArray[15].substring(0, 5) + "-" + lineInArray[15].substring(5);
                int quantity = 0;
//                int day, month, year;
                String strDate = lineInArray[18];
                try {
                    /*char[] charQtyArray = lineInArray[17].toCharArray();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (char c : charQtyArray) {
                        if(!(c == 160))
                            stringBuilder.append(c);
                    }
                    String strQty = String.valueOf(stringBuilder);*/
                    String strQty = lineInArray[17].replace("\u00a0","");
                    quantity = Integer.parseInt(strQty);
//                    day = Integer.parseInt(strDate.substring(0, 2));
//                    month = getMonth(strDate.substring(3, 6));
//                    year = Integer.parseInt(strDate.substring(strDate.length() - 4));
                } catch (NumberFormatException e) {
                    continue;
                }
//                LocalDate date = LocalDate.of(year, month, day);
                LocalDate date = getDate(strDate);
                if (date == null)
                    continue;
                UnitEDI unitEDI = new UnitEDI(partName, quantity, date);
                boolean isRecorded = false;
                for(UnitEDI unit : listEDI) {
                    if(unitEDI.equals(unit)) {
                        unit.setQuantity(unit.getQuantity() + unitEDI.getQuantity());
                        isRecorded = true;
                        break;
                    }
                }
                if(!isRecorded) {
                    listEDI.add(unitEDI);
                }
            }
        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
        }
        return listEDI;
    }

    /*public class EdiFileVisitor extends SimpleFileVisitor<Path>{

        private FileTime startDateTime = new FileTime()
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

            attrs
            return super.visitFile(file, attrs);
        }
    }*/
}
