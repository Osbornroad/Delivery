package com.gmail.osbornroad.repository.jdbc;

import com.gmail.osbornroad.model.ReportItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class ReportRepository {

    @Autowired
    JdbcTemplate postgresJdbcTemplate;

    public List<ReportItem> getReport(String aPoint, Timestamp startDate, Timestamp endDate, String typeReport) {

        RowMapper<ReportItem> dailyMapper = (resultSet, i) -> {
            int year = resultSet.getInt(2);
            int month = resultSet.getInt(3);
            int day = resultSet.getInt(4);
            int integerDate = (year * 100 + month) * 100 + day;
            return new ReportItem(
                    resultSet.getString(1),
                    integerDate,
                    resultSet.getInt(5)
            );
        };

        RowMapper<ReportItem> monthlyMapper = (resultSet, i) -> {
            int year = resultSet.getInt(2);
            int month = resultSet.getInt(3);
            int integerDate = year * 100 + month;
            return new ReportItem(
                    resultSet.getString(1),
                    integerDate,
                    resultSet.getInt(4)
            );
        };

        List<ReportItem> reportItemList = new ArrayList<>();

        switch (typeReport) {
            case ("dailyReport"):
                reportItemList = postgresJdbcTemplate.query("SELECT kits.kitname, " +
                                "EXTRACT(YEAR FROM notes.apointdatetime), EXTRACT(MONTH FROM notes.apointdatetime), EXTRACT(DAY FROM notes.apointdatetime), " +
                                "COUNT(*) AS kitCount FROM notes JOIN kits ON notes.wib224 = kits.wib224 " +
                                "WHERE notes.apoint = ? AND notes.apointdatetime BETWEEN ? AND ? " +
                                "GROUP BY kits.kitname, EXTRACT(YEAR FROM notes.apointdatetime), EXTRACT(MONTH FROM notes.apointdatetime), EXTRACT(DAY FROM notes.apointdatetime)",
                        dailyMapper, aPoint, startDate, endDate);
                break;
            case ("monthlyReport"):
                reportItemList = postgresJdbcTemplate.query("SELECT kits.kitname, " +
                                "EXTRACT(YEAR FROM notes.apointdatetime), EXTRACT(MONTH FROM notes.apointdatetime), " +
                                "COUNT(*) AS kitCount FROM notes JOIN kits ON notes.wib224 = kits.wib224 " +
                                "WHERE notes.apoint = ? AND notes.apointdatetime BETWEEN ? AND ? " +
                                "GROUP BY kits.kitname, EXTRACT(YEAR FROM notes.apointdatetime), EXTRACT(MONTH FROM notes.apointdatetime)",
                        monthlyMapper, aPoint, startDate, endDate);
                break;
        }

        reportItemList.sort(Comparator.comparingInt(ReportItem::getIntegerDate));

        return reportItemList;
    }
}