package it.uniclam.rilevamento_presenze.beanclass;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Chriz 7X on 06/12/2015.
 */

/**
 * La classe Event:
 * 1. identifica l'oggetto "evento"
 * 2. Interagisce con la GUI FxTableEvent
 */

public class Event {

    //Tipo di stringa utilizzato dalla tabella FxTableEvent
    private SimpleStringProperty nome;
    private SimpleStringProperty data;
    private SimpleStringProperty cognome;
    private SimpleStringProperty type_id;
    private SimpleStringProperty event_id;

    public Event() {
    }

    /**
     * Costruttore personalizzato della classe evento
     *
     * @param s1
     * @param s2
     * @param s3
     * @param s4
     * @param s5
     */

    public Event(String s1, String s2,String s3,String s4,String s5) {

        cognome = new SimpleStringProperty(s1);
        nome = new SimpleStringProperty(s2);
        data = new SimpleStringProperty(s3);
        type_id = new SimpleStringProperty(s4);
        event_id=new SimpleStringProperty(s5);
    }

    public String getNome() {

        return nome.get();
    }
    public void setNome(String s) {

        nome.set(s);
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

    public String getCognome() {

        return cognome.get();
    }
    public void setCognome(String s) {

        cognome.set(s);
    }

    public String getEvent_id() {

        return event_id.get();
    }
    public void setEvent_id(String s) {

        event_id.set(s);
    }


    @Override
    public String toString() {

        return ("IL DIPENDENTE: " + cognome.get() + " " + nome.get() + "IN DATA" + data.get() + "HA EFFETTUATO UN' " + type_id.get());
    }
}
