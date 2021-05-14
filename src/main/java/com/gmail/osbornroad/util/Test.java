package com.gmail.osbornroad.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Test {
    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.of(2000,01,01, 00, 00);
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        System.out.println(timestamp.getTime());
    }
}
