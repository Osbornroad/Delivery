package com.gmail.osbornroad.service;

import com.gmail.osbornroad.model.FinishPart;
import com.gmail.osbornroad.model.Kit;
import com.gmail.osbornroad.model.ReportItem;
import com.gmail.osbornroad.repository.jdbc.ReportRepository;
import com.gmail.osbornroad.repository.jpa.KitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class ReportService {

    @Autowired
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
    }

    public String[][] getReport(String aPoint, List<String> kits, LocalDate startDate, LocalDate endDate, String typeReport) {

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
    }
}
