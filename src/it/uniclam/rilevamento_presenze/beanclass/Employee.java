package it.uniclam.rilevamento_presenze.beanclass;

import javafx.beans.property.SimpleStringProperty;

/**
 * La classe Employee:
 * 1. identifica l'oggetto "dipendente"
 * 2. Interagisce con la GUI FxTableUser
 */

public class Employee {

    //Tipo di stringa utilizzato dalla tabella FxTableUser

    private SimpleStringProperty name;
    private SimpleStringProperty surname;
    private SimpleStringProperty id_employee;


    /**
     * Costruttore personalizzato della classe dipendente
     *
     * @param s1
     * @param s2
     * @param s3
     */
    public Employee(String s1, String s2, String s3) {

        name = new SimpleStringProperty(s1);
        surname = new SimpleStringProperty(s2);
        id_employee = new SimpleStringProperty(s3);
    }


    public String getName() {

        return name.get();
    }

    public void setName(String s) {

        name.set(s);
    }

    public String getSurname() {

        return surname.get();
    }

    public void setSurname(String s) {

        surname.set(s);
    }

    public String getId_employee() {
        return id_employee.get();
    }

    public void setId_employee(String s) {
        id_employee.set(s);
    }


    //Restituisce un record completo di employee
    @Override
    public String toString() {

        return (surname.get() + " " + name.get() + " " + id_employee.get());
    }


}