package com.edu.platu.ru.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class ProjectUtils {

    public static Date getDate(final String part) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = dateFormat.parse(part);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getTime(Date date){
        SimpleDateFormat format = new SimpleDateFormat(
                "HH:mm:ss");
        return format.format(date);
    }

    public static boolean isBadRespCode(final String element) {
        return element.split(" ")[8].startsWith("5");
    }

    public static boolean isBadRespTime(final String element, final int respTimeLimit) {
        AtomicInteger respTime = new AtomicInteger(Integer.parseInt(element.split(" ")[10].split("\\.")[0]));
        return respTime.get() >= respTimeLimit;
    }

    public static boolean isBadRespCodeOrRespTime(final String element, final int respTimeLimit) {
        return isBadRespCode(element) || isBadRespTime(element, respTimeLimit);
    }
}
