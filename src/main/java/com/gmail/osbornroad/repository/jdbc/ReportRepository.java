package com.gmail.osbornroad.repository.jdbc;

import com.gmail.osbornroad.model.MonthlyReportItem;
import com.gmail.osbornroad.model.ReportA090;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

@Repository
public class ReportRepository {

    @Autowired
    JdbcTemplate postgresJdbcTemplate;

    public List<ReportA090> getReportA090(@Param("aPoint") String aPoint,
                                          @Param("startDate") Timestamp startDate,
                                          @Param("endDate") Timestamp endDate) {

        RowMapper mapper = new RowMapper<ReportA090>() {
            @Override
            public ReportA090 mapRow(ResultSet resultSet, int i) throws SQLException {
                ReportA090 reportA090 = new ReportA090(
                        resultSet.getString(1),
                        resultSet.getDate(2).toLocalDate(),
                        resultSet.getInt(3)
                );
                return reportA090;
            }
        };

        List<ReportA090> reportA090List = postgresJdbcTemplate.query("SELECT kits.kitname, notes.apointdatetime::date, " +
                        "COUNT(*) AS kitCount FROM notes JOIN kits ON notes.wib224 = kits.wib224 " +
                        "WHERE notes.apoint=? AND notes.apointdatetime BETWEEN ? AND ? GROUP BY kits.kitname, notes.apointdatetime::date " +
                        "ORDER BY notes.apointdatetime::date", mapper, aPoint, startDate, endDate
                );

        return reportA090List;
    }

    public List<MonthlyReportItem> getMonthlyReport(@Param("aPoint") String aPoint,
                                                        @Param("startDate") Timestamp startDate,
                                                        @Param("endDate") Timestamp endDate) {

        RowMapper mapper = new RowMapper<MonthlyReportItem>(){

            @Override
            public MonthlyReportItem mapRow(ResultSet resultSet, int i) throws SQLException {
                int year = resultSet.getInt(2);
                int month = resultSet.getInt(3);
                int yearMonth = year * 100 + month;
                MonthlyReportItem item = new MonthlyReportItem(
                        resultSet.getString(1),
                        yearMonth,
                        resultSet.getInt(4)
                );
                return item;
            }
        };

        List<MonthlyReportItem>monthlyReportItemList = postgresJdbcTemplate.query("SELECT kits.kitname, " +
                "EXTRACT(YEAR FROM notes.apointdatetime), EXTRACT(MONTH FROM notes.apointdatetime), " +
                "COUNT(*) AS kitCount FROM notes JOIN kits ON notes.wib224 = kits.wib224 " +
                "WHERE notes.apoint = ? AND notes.apointdatetime BETWEEN ? AND ? " +
                "GROUP BY kits.kitname, EXTRACT(YEAR FROM notes.apointdatetime), EXTRACT(MONTH FROM notes.apointdatetime)",
                mapper, aPoint, startDate, endDate);

        monthlyReportItemList.sort(new Comparator<MonthlyReportItem>() {
            @Override
            public int compare(MonthlyReportItem o1, MonthlyReportItem o2) {
                return o1.getYearMonth() - o2.getYearMonth();
            }
        });

        return monthlyReportItemList;
    }
}