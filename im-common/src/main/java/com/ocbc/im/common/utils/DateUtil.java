package com.ocbc.im.common.utils;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.ocbc.im.common.constant.Constants.DATE_FORMAT;

public class DateUtil {
    public static LocalDateTime parse(String dataStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return  LocalDate.parse(dataStr, formatter).atStartOfDay();
    }
}
