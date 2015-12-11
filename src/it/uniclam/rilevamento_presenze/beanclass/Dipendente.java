package it.uniclam.rilevamento_presenze.beanclass;

import javafx.beans.property.SimpleStringProperty;

public class Dipendente {

    private SimpleStringProperty Nome;//Hour;title
	private SimpleStringProperty Cognome;//Data;//author
    private SimpleStringProperty id_employee;//Dipendente

    public Dipendente() {
    }

	public Dipendente(String s1, String s2,String s3) {

        Nome = new SimpleStringProperty(s1);
        Cognome = new SimpleStringProperty(s2);
        id_employee=new SimpleStringProperty(s3);
    }

    public String getNome() {
	
        return Nome.get();
    }
    public void setNome(String s) {
	
        Nome.set(s);
    }
	
    public String getCognome() {
	
        return Cognome.get();
    }
    public void setCognome(String s) {
	
        Cognome.set(s);
    }

    public String getId_employee() {
        return id_employee.get();
    }

    public void setId_employee(String s) {
        id_employee.set(s);
    }
	
    @Override
    public String toString() {
	
        return (Cognome.get() + " " + Nome.get()+" "+id_employee.get());
    }


}