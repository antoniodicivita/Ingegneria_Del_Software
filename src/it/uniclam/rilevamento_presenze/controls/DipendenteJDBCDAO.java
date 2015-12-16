package it.uniclam.rilevamento_presenze.controls;

import it.uniclam.rilevamento_presenze.beanclass.Employee;
import it.uniclam.rilevamento_presenze.connections.Server;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 * La Classe DipendenteJDBCDAO:
 * 1. Svolge il ruolo di client per FXTableUser
 * 2. Implementa tutti i metodi utilizzati dalla classeFxTableUser
 * 3.Interagisce con le classi Server e FxTableUser
 */
public class DipendenteJDBCDAO {


    public DipendenteJDBCDAO() {
    }


    /**
     * Questo metodo si occupa di effettuare l'aggiunta di un dipendente alla tabella
     *
     * @param type_query
     * @param value
     */

    public void addList(String type_query, String value) {

        String req = type_query + "\n" + value + "\n";

        try {
            Socket s = new Socket(Server.HOST, Server.PORT);

            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(req);
            String line = in.readLine();

            System.out.println(line);
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Questo metodo si occupa di effettuare la rimozione di un dipendente alla tabella
     *
     * @param type_query
     * @param valueOne
     * @param valueTwo
     */

    public void removeList(String type_query, String valueOne, String valueTwo) {

        String req = type_query + "\n" + valueOne + "\n" + valueTwo + "\n";

        try {
            Socket s = new Socket(Server.HOST, Server.PORT);

            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(req);
            String line = in.readLine();

            System.out.println(line);
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Questo metodo si occupa di riempire la tabella dei dipendenti
     *
     * @param type_query
     * @return ObservableList<Employee> data
     */
    public ObservableList<Employee> selectList(String type_query) {


        List<Employee> list = new ArrayList<>();
        String req = type_query + "\n";

        try {
            Socket s = new Socket(Server.HOST, Server.PORT);

            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(req);

            String name = in.readLine();
            while (name.length() > 0) {

                name = in.readLine();
                String surname = in.readLine();
                String id = in.readLine();
                if (name != null && surname != null) {
                    list.add(new Employee(name, surname, id));
                }


                //  System.out.println("STAMPA: " + name + " e: " + surname);
            }


            s.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        ObservableList<Employee> data = FXCollections.observableList(list);
        return data;
    }


    /**
     * Questo metodo si occupa di riempire la tabella degli eventi in ordine cronologico
     *
     * @param varName
     * @param varSurname
     */

    public void gridView(String type_query, String varName, String varSurname) {
        JFrame frame = new JFrame("Dettagli Eventi");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        String columnNames[] = {"NOME", "COGNOME", "IN/OUT", "DATA", "ORA"};
        String req = type_query + "\n" + varName + "\n" + varSurname + "\n";


        try {
            Socket s = new Socket(Server.HOST, Server.PORT);

            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(req);

            int count = 0;

            String name = in.readLine();
            if (name.equals("OK")) {
                name = in.readLine();
                int countrow = Integer.parseInt(name);
                Object rowData[][] = new Object[countrow][];


                while (name.length() > 0 && count < countrow) {

                    name = in.readLine();
                    String surname = in.readLine();
                    String inOut = in.readLine();
                    String date = in.readLine();
                    String hour = in.readLine();


                    rowData[count] = new Object[]{name, surname, inOut, date, hour};

                    count++;
                    System.out.println(name + surname + inOut + date + hour);


                }
                s.close();

                JTable table = new JTable(rowData, columnNames);
                JScrollPane scrollPane = new JScrollPane(table);
                frame.add(scrollPane, BorderLayout.CENTER);
                frame.pack();
                frame.setVisible(true);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Questo metodo si occupa di riempire la tabella deelle informazioni dei dipendenti
     *
     * @param type_query
     * @param varName
     * @param varSurname
     */

    public void details(String type_query, String varName, String varSurname) {
        JFrame frame = new JFrame("Informazioni dipendenti");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        String columnNames[] = {"NOME", "COGNOME", "ORARI", "PAUSA PRANZO", "STRAORDINARIO"};
        String req = type_query + "\n" + varName + "\n" + varSurname + "\n";


        try {
            Socket s = new Socket(Server.HOST, Server.PORT);

            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(req);

            int count = 0;

            String name = in.readLine();
            if (name.equals("OK")) {
                name = in.readLine();
                int countrow = Integer.parseInt(name);
                Object rowData[][] = new Object[countrow][];


                while (name.length() > 0 && count < countrow) {


                    name = in.readLine();
                    String surname = in.readLine();
                    String workTime = in.readLine();
                    String breakLunch = in.readLine();
                    String extra = in.readLine();


                    rowData[count] = new Object[]{name, surname, workTime, breakLunch, extra};

                    count++;
                    // System.out.println(name + surname + INOUT + Data + Ora);


                }
                s.close();

                JTable table = new JTable(rowData, columnNames);
                JScrollPane scrollPane = new JScrollPane(table);
                frame.add(scrollPane, BorderLayout.CENTER);
                frame.pack();
                frame.setVisible(true);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     * Questo metodo si occupa di aggiornare i campi nella tabelle dei dipendenti
     *
     * @param type_query
     * @param name
     * @param surname
     * @param id
     */
    public void update(String type_query, String name, String surname, String id) {


        String req = type_query + "\n" + name + "\n" + surname + "\n" + id + "\n";

        try {
            Socket s = new Socket(Server.HOST, Server.PORT);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            // BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(req);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    //OK


    /**
     * Questo metodo si occupa della ricerca dei dipendenti
     *
     * @param type_query
     * @param keySearch
     * @param value
     */
    public void searchList(String type_query, String keySearch, ObservableList<Employee> value) {

        String req = type_query + "\n" + keySearch + "\n";

        ObservableList<Employee> data = value;
        try {
            Socket s = new Socket(Server.HOST, Server.PORT);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(req);

            String name = in.readLine();
            while (name.length() > 0) {

                name = in.readLine();
                String surname = in.readLine();
                String id = in.readLine();

                if (name != null && surname != null && id != null) {
                    data.addAll(new Employee(name, surname, id));
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}