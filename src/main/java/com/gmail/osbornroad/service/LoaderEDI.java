package com.gmail.osbornroad.service;

import com.gmail.osbornroad.model.*;
import com.gmail.osbornroad.repository.jpa.FinishPartRepository;
import com.gmail.osbornroad.repository.jpa.PropRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.*;
import java.time.DayOfWeek;
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
            case ("мар"):
                intMonth = 3;
                break;
            case ("апр"):
                intMonth = 4;
                break;
            case ("май"):
                intMonth = 5;
                break;
            case ("июн"):
                intMonth = 6;
                break;
            case ("июл"):
                intMonth = 7;
                break;
        }

        return intMonth;
    }

    public List<UnitEDI> getEdiFilteredByDate (LocalDate start, LocalDate finish, String ediDir) {

        File file = getFile(null, ediDir);
        List<UnitEDI> unitEDIList = getEDIfromCSV(file);
        List<UnitEDI> filteredUnitEDIList = new ArrayList<>();
        LocalDate startOfEDI = finish;
        LocalDate finishOfEDI = start;

        for (UnitEDI unitEDI : unitEDIList) {
            if (unitEDI.getDate().isEqual(start) || unitEDI.getDate().isEqual(finish) || (unitEDI.getDate().isAfter(start) && unitEDI.getDate().isBefore(finish))) {
                boolean isAdded = false;
                for (UnitEDI savedUnitEDI : filteredUnitEDIList) {
                    if(savedUnitEDI.getPartNumber().equals(unitEDI.getPartNumber())) {
                        savedUnitEDI.setQuantity(savedUnitEDI.getQuantity() + unitEDI.getQuantity());
                        isAdded = true;
                    }
                }
                if(!isAdded) {
                    filteredUnitEDIList.add(unitEDI);
                }
                if(unitEDI.getDate().isBefore(startOfEDI))
                    startOfEDI = unitEDI.getDate();
                if(unitEDI.getDate().isAfter(finishOfEDI))
                    finishOfEDI = unitEDI.getDate();
            }
        }
        //Go to end of week
        finishOfEDI = finishOfEDI.plusDays(6);

        //Get month forecast based on weekly EDI
        float multiplier = (float)getWorkingDaysInMonth(start) / (float)getWorkingDaysBetweenDates(startOfEDI, finishOfEDI);

        for (UnitEDI unitEDI : filteredUnitEDIList) {
            int qty = unitEDI.getQuantity();
            unitEDI.setQuantity((int)(qty * multiplier));
        }

        return filteredUnitEDIList;
    }

//    String PATH_TO_ORDER = "C:\\Shared\\10. IT\\03.Order\\Order.xlsx";

    public List<OrderCheck> getOrderCheckList(String fileOrderPath, LocalDate startDate, LocalDate endDate) {

        String ediDir = "weeklyEdiDir";
        List<UnitEDI> unitEDIList = getEdiFilteredByDate(startDate, endDate, ediDir);
        Map<Part, Double>  necessaryOrder = getNecessaryPartsOrder(unitEDIList);
        Map<Part, Integer> necessaryOrderBySnp = getNecessaryPartsOrderBySnp(necessaryOrder);
        Map<String, Integer> factOrder =
                orderLoading((fileOrderPath == null || fileOrderPath.equals("")) ? propService.getPropValueByName("orderDir") : fileOrderPath);

        List<OrderCheck> orderCheckList = new ArrayList<>();
        for (Map.Entry<Part, Integer> entry : necessaryOrderBySnp.entrySet()) {
            OrderCheck orderCheck = new OrderCheck(entry.getKey().getPartNumber(), entry.getValue(), 0, 0);
            orderCheckList.add(orderCheck);
        }
        for (Map.Entry<String, Integer> factEntry : factOrder.entrySet()) {
            boolean isRecorded = false;
            for (OrderCheck orderCheck : orderCheckList) {
                if (isPartNumbersEqual(orderCheck.getPartNumber(), factEntry.getKey())) {
                    orderCheck.setOrdered(factEntry.getValue());
                    orderCheck.setDiff(orderCheck.getOrdered() - orderCheck.getNecessaryAmount());
                    isRecorded = true;
                    break;
                }
            }
            if (!isRecorded) {
                OrderCheck orderCheck = new OrderCheck(factEntry.getKey(), 0, 0, 0);
                orderCheckList.add(orderCheck);
            }
        }

        return orderCheckList;
    }

/*    public String[][] getOrderCheck(String fileOrderPath, LocalDate startDate, LocalDate endDate) {

        String ediDir = "weeklyEdiDir";
        List<UnitEDI> unitEDIList = getEdiFilteredByDate(startDate, endDate, ediDir);
        Map<String, Double>  necessaryOrder = getNecessaryPartsOrder(unitEDIList);
        Map<String, Integer> necessaryOrderBySnp = getNecessaryPartsOrderBySnp(necessaryOrder);
        Map<String, Integer> factOrder =
                orderLoading((fileOrderPath == null || fileOrderPath.equals("")) ? propService.getPropValueByName("orderDir") : fileOrderPath);

        boolean isNecMapSmaller = necessaryOrderBySnp.size() < factOrder.size();
        int rows = isNecMapSmaller ? factOrder.size() : necessaryOrderBySnp.size();
        String[][] orderCheck = new String[rows + 1][4];
        orderCheck[0][0] = "Part number";
        orderCheck[0][1] = "Necessary amount";
        orderCheck[0][2] = "Ordered";
        orderCheck[0][3] = "Difference";
        if (isNecMapSmaller) {
            int counter = 1;
            for (Map.Entry<String, Integer> entry : necessaryOrderBySnp.entrySet()) {
                orderCheck[counter][0] = entry.getKey();
                orderCheck[counter][1] = entry.getValue().toString();
                orderCheck[counter][2] = "n/a";
                orderCheck[counter][3] = "n/a";
                counter++;
            }
            int factCounter = necessaryOrderBySnp.size() + 1;
            Map<String, Integer> error = new HashMap<>();
            for (Map.Entry<String, Integer> entry : factOrder.entrySet()) {
                String factPartNumber = entry.getKey();
                *//*if (factPartNumber.length() == 10) {
                    factPartNumber = factPartNumber.substring(0, 5) + "-" + factPartNumber.substring(5);
                }
                if (factPartNumber.substring(0, 5).equals("17507")) {
                    factPartNumber = factPartNumber.substring(0, 5) + "-"
                            + factPartNumber.substring(5, 10) + "-" + factPartNumber.substring(10);
                }*//*
                boolean isRecorded = false;
                for(int i = 1; i < orderCheck.length; i++) {
                    if (orderCheck[i][0] == null)
                        continue;
//                    if (orderCheck[i][0].equals(factPartNumber)) {
                    if (isPartNumbersEqual(orderCheck[i][0], factPartNumber)) {
                        int nesQty = Integer.parseInt(orderCheck[i][1]);
                        orderCheck[i][2] = entry.getValue().toString();
                        orderCheck[i][3] = String.valueOf(entry.getValue() - nesQty);
                        isRecorded = true;
                        break;
                    }
                }
                try {
                    if (!isRecorded) {
                        orderCheck[factCounter][0] = entry.getKey();
                        orderCheck[factCounter][1] = "n/a";
                        orderCheck[factCounter][2] = entry.getValue().toString();
                        orderCheck[factCounter][3] = "n/a";
                        factCounter++;
                    }
                } catch (IndexOutOfBoundsException e) {
                    error.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return orderCheck;
    }*/

    private boolean isPartNumbersEqual(String basePartNumber, String orderPartNumber) {
        if (basePartNumber == null || orderPartNumber == null)
            return false;
        basePartNumber = basePartNumber.replaceAll("\\s+","");
        orderPartNumber = orderPartNumber.replaceAll("\\s+","");
        basePartNumber = basePartNumber.replaceAll("-","");
        orderPartNumber = orderPartNumber.replaceAll("-","");
        return basePartNumber.equals(orderPartNumber);
    }

//    private String getStringWithoutSpacesAndDashes(String )

    private Map<String, Integer> orderLoading(String stringPath) {
        File file = null;
        try {
            file = Objects.requireNonNull(findLastModifiedFile(stringPath)).toFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Workbook workbook;
        Map<String, Integer> map = new HashMap<>();
        try(InputStream inputStream = new FileInputStream(file)) {
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Sheet sheet = workbook.getSheetAt(0);
        for(Row row : sheet) {
            Cell partNumberCell = row.getCell(0);
            String partNumber = partNumberCell.getRichStringCellValue().getString();

            Cell qtyCell = row.getCell(1);
            int qty = (int)qtyCell.getNumericCellValue();

            map.put(partNumber, qty);
        }
        return map;
    }

    @Autowired
    PartService partService;

    public Map<Part, Integer> getNecessaryPartsOrderBySnp(Map<Part, Double> map) {
        Map<Part, Integer> mapBySnp = new HashMap<>();
        for (Map.Entry<Part, Double> entry : map.entrySet()) {
            Part part = entry.getKey();
//            Part part = partService.findPartByNumber(entry.getKey());
            int boxes = (int)(double) entry.getValue() / part.getSnp() + 1;
            int qty = boxes * part.getSnp();
            mapBySnp.put(entry.getKey(), qty);
        }
        return mapBySnp;
    }

    public Map<Part, Double> getNecessaryPartsOrder(List<UnitEDI> unitEDIList ) {
        Map<Part, Double> partMap = new HashMap<>();
        for (UnitEDI unitEDI : unitEDIList) {
            FinishPart finishPart = finishPartRepository.findByFinishPartNumber(unitEDI.getPartNumber()).orElse(null);
            if(finishPart == null)
                continue;
            int finishPartQty = unitEDI.getQuantity();
            Set<PartQty> partQtySet = finishPart.getPartQtySet();
            for(PartQty partQty : partQtySet) {
                Part part = partQty.getPart();
                double qty = partQty.getQty() * finishPartQty;
                if(partMap.containsKey(part)) {
                    partMap.put(part, partMap.get(part) + qty);
                } else {
                    partMap.put(part, qty);
                }
                /*for(Map.Entry<String, Integer> entry : partMap.entrySet()) {

                }*/
            }
        }
        return partMap;
    }

/*    public Map<String, Double> getNecessaryPartsOrder(List<UnitEDI> unitEDIList ) {
        Map<String, Double> partMap = new HashMap<>();
        for (UnitEDI unitEDI : unitEDIList) {
            FinishPart finishPart = finishPartRepository.findByFinishPartNumber(unitEDI.getPartNumber()).orElse(null);
            if(finishPart == null)
                continue;
            int finishPartQty = unitEDI.getQuantity();
            Set<PartQty> partQtySet = finishPart.getPartQtySet();
            for(PartQty partQty : partQtySet) {
                String partNumber = partQty.getPart().getPartNumber();
                double qty = partQty.getQty() * finishPartQty;
                if(partMap.containsKey(partNumber)) {
                    partMap.put(partNumber, partMap.get(partNumber) + qty);
                } else {
                    partMap.put(partNumber, qty);
                }
                *//*for(Map.Entry<String, Integer> entry : partMap.entrySet()) {

                }*//*
            }
        }
        return partMap;
    }*/

    public int getWorkingDaysInMonth(LocalDate date) {
        LocalDate startOfMonth = date.withDayOfMonth(1);
        LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());
        int workingDays = getWorkingDaysBetweenDates(startOfMonth, endOfMonth);
        return workingDays;
    }

    public int getWorkingDaysBetweenDates(LocalDate start, LocalDate finish) {
        int counter = 0;
        if(start.isAfter(finish))
            return 0;
        while(true) {
            if (!(start.getDayOfWeek() == DayOfWeek.SATURDAY || start.getDayOfWeek() == DayOfWeek.SUNDAY))
                counter++;
            if (start.isEqual(finish))
                break;
            start = start.plusDays(1);
        }
        return counter;
    }

    public String[][] getArrayOfEdi(File file, String ediDir) {

        int p = 55;

        file = getFile(file, ediDir);

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

    @Autowired
    PropService propService;

    private File getFile(File file, String propName) {
        if(file == null) {
            try {
                String propValue = propService.getPropValueByName(propName);
                Path path = findLastModifiedFile(propValue);
                file = path.toFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public String[] getEdiProperties(File file, String ediDir) {

        int o = 78;

        file = getFile(file, ediDir);

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

    private Path findLastModifiedFile(String sdir) throws IOException {
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
