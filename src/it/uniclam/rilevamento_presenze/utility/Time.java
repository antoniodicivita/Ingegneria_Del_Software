package it.uniclam.rilevamento_presenze.utility;

import java.util.GregorianCalendar;

/**
 * Created by Antonio on 01/12/2015.
 */
public class Time {



    String months[] =null;

    private String hour = "";
    private String date = "";

    public Time(){

        GregorianCalendar gcalendar = new GregorianCalendar();

        this.months = new String[]{
                "Jan", "Feb", "Mar", "Apr",
                "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec"};
        this.setHour(gcalendar.get(java.util.Calendar.HOUR_OF_DAY) + ":" + gcalendar.get(java.util.Calendar.MINUTE) + ":" + gcalendar.get(java.util.Calendar.SECOND));
        this.setDate(gcalendar.get(java.util.Calendar.DATE) + " " + months[gcalendar.get(java.util.Calendar.MONTH)] + " " + gcalendar.get(java.util.Calendar.YEAR));
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
