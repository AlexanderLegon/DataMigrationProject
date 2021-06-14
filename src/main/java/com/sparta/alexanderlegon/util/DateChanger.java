package com.sparta.alexanderlegon.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateChanger {

    public static Long convert(String oldDate){
        long milliseconds = 0;
        String[] splitDate = oldDate.split("/");
        String newDate = (splitDate[2] + "-" + splitDate[1] + "-" + splitDate[0]);

        SimpleDateFormat f = new SimpleDateFormat("DD-MM-YYYY");

        try {
            java.util.Date d = f.parse(newDate);
            milliseconds = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }
}
