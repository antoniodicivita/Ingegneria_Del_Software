package it.uniclam.rilevamento_presenze.utility;

import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;

/**
 * Created by Antonio on 01/12/2015.
 */


/**
 * La Classe Time
 * 1. Si occupa della creazione del formato desiderato per calendario e per il DatePicker
 * 2. Interagisce EventJDBCDAO e BadgeGUI
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


    /**
     * Questo metodo si occupa di settare il DatePicker nel formato desiderato
     *
     * @param checkInDatePicker
     * @param pattern
     */
    public void setCalendar(DatePicker checkInDatePicker, String pattern) {

        StringConverter converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };

        checkInDatePicker.setConverter(converter);
        checkInDatePicker.setPromptText(pattern.toLowerCase());
    }


}


