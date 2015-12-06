package it.uniclam.rilevamento_presenze.beanclass;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Chriz 7X on 06/12/2015.
 */
public class Event {
    private SimpleStringProperty hour;//NOme=HOUR
    private SimpleStringProperty data;//COgnome=Data
    private SimpleIntegerProperty user_id;
    private SimpleIntegerProperty type_id;

    public Event() {
    }

    public Event(String s1, String s2/*,Integer s3,Integer s4*/) {

        hour = new SimpleStringProperty(s1);
        data = new SimpleStringProperty(s2);
        /*user_id = new SimpleIntegerProperty(s3);
        type_id = new SimpleIntegerProperty(s4);*/
    }

    public String getHour() {

        return hour.get();
    }
    public void setHour(String s) {

        hour.set(s);
    }

    public String getData() {

        return data.get();
    }
    public void setData(String s) {

        data.set(s);
    }

    public Integer getType_id() {

        return type_id.get();
    }
    public void setType_id(Integer s) {

        type_id.set(s);
    }

    public Integer getUser_id() {

        return user_id.get();
    }
    public void setUser_id(Integer s) {

        user_id.set(s);
    }

    @Override
    public String toString() {

        return (data.get() + " " + hour.get()/*+type_id.get()+" "+user_id*/);
    }
}
