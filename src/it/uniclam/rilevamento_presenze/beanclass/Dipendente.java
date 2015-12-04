package it.uniclam.rilevamento_presenze;

import javafx.beans.property.SimpleStringProperty;

public class Dipendente {

    private SimpleStringProperty Nome;//Hour;title
	private SimpleStringProperty Cognome;//Data;//author

    public Dipendente() {
    }

	public Dipendente(String s1, String s2) {

        Nome = new SimpleStringProperty(s1);
        Cognome = new SimpleStringProperty(s2);
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
	
    @Override
    public String toString() {
	
        return (Cognome.get() + " " + Nome.get());
    }
}