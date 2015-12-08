package it.uniclam.rilevamento_presenze.beanclass;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Chriz 7X on 06/12/2015.
 */
public class Event {
    private SimpleStringProperty hour;//NOme=HOUR
    private SimpleStringProperty data;//COgnome=Data
    private SimpleStringProperty user_id;
    private SimpleStringProperty type_id;

    public Event() {
    }

    public Event(String s1, String s2,String s3,String s4) {

        hour = new SimpleStringProperty(s3);
        data = new SimpleStringProperty(s2);
        user_id = new SimpleStringProperty(s1);
        type_id = new SimpleStringProperty(s4);
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

    public String getType_id() {

        return type_id.get();
    }
    public void setType_id(String s) {

        type_id.set(s);
    }

    public String getUser_id() {

        return user_id.get();
    }
    public void setUser_id(String s) {

        user_id.set(s);
    }

    @Override
    public String toString() {

        return (user_id.get()+" "+data.get() + " " + hour.get()+" "+type_id.get());
    }
}
