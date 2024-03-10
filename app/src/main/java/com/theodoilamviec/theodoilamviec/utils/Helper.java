package com.theodoilamviec.theodoilamviec.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {

    
    public static String get_formatted_date(Long date_time) {
        SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM yy hh:mm");
        return newFormat.format(new Date(date_time));
    }





}
