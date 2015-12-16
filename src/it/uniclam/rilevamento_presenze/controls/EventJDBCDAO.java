package it.uniclam.rilevamento_presenze.controls;


import it.uniclam.rilevamento_presenze.beanclass.Event;
import it.uniclam.rilevamento_presenze.connections.Server;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Chriz 7X on 06/12/2015.
 */


/**
 * La Classe EventJDBCDAO:
 * 1. Svolge il ruolo di client per FXTableEvent
 * 2. Implementa tutti i metodi utilizzati dalla classeFxTableEvent
 * 3.Interagisce con le classi Server e FxTableEvent
 */
public class EventJDBCDAO {

    public EventJDBCDAO() {
    }

    /**
     * Questo metodo si occupa di effettuare la ricerca degli eventi per data
     *
     * @param type_query
     * @param initialDate
     * @param finalDate
     * @return ObservableList<Event> data
     */

    public static ObservableList<Event> searchDate(String type_query, String initialDate, String finalDate) {

        String req = type_query + "\n" + initialDate + "\n" + finalDate + "\n";



        List list = new ArrayList();


        try {
            Socket s = new Socket(Server.HOST, Server.PORT);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(req);

            String name = in.readLine();
            if (name.equals("OK")) {

                while (name.length() > 0) {

                    name = in.readLine();
                    String surname = in.readLine();
                    String inOut = in.readLine();
                    String date = in.readLine();
                    String id_event = in.readLine();


                    if (name != null && surname != null && inOut != null && date != null && id_event != null) {
                        list.add(new Event(surname, name, date, inOut, id_event));

                    }

                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        ObservableList<Event> date_ordered = FXCollections.observableList(list);



        return  insertOnlyInconsistences(date_ordered);
    }

    /**
     * Questo metodo si occupa di evidenziare le incongruenze (doppie entrate/uscite) nella tebella degli eventi
     *
     * @param table
     * @param data
     */

    public static void inconsistenceSelector(TableView<Event> table, ObservableList<Event> data) {

        int lenght = data.size();
        int i=0;


        while (i<lenght-1) {
            if (Objects.equals(table.getItems().get(i).getCognome().toString(), table.getItems().get(i + 1).getCognome().toString()) &&
                    Objects.equals(table.getItems().get(i).getData().toString(), table.getItems().get(i + 1).getData().toString()) &&
                    Objects.equals(table.getItems().get(i).getType_id().toString(), table.getItems().get(i + 1).getType_id().toString())) {
                table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                table.requestFocus();

                table.getSelectionModel().select(data.get(i));
                table.getSelectionModel().select(data.get(i + 1));


                table.getSelectionModel().focus(0);



            }
            i++;



        }


    }

    /**
     * Questo metodo si occupa di inserire solo le incongruenze nell'apposita tabella
     *
     * @param data
     * @return ObservableList<Event>
     */

    public static ObservableList<Event> insertOnlyInconsistences(ObservableList<Event> data) {

        int lenght = data.size();
        int i = 1;

        List list = new ArrayList();


        while (i < lenght) {

            if (Objects.equals(data.get(i).getCognome().toString(), data.get(i - 1).getCognome().toString()) &&
                    Objects.equals(data.get(i).getData().toString(), data.get(i - 1).getData().toString()) &&
                    Objects.equals(data.get(i).getType_id().toString(), data.get(i - 1).getType_id().toString())) {

                System.out.println(data.get(i).getEvent_id().toString() + "   " + data.get(i - 1).getEvent_id().toString());

                if (!list.contains(data.get(i - 1))) {

                    list.add(data.get(i - 1));

                    list.add(data.get(i));
                }
//
            }
            i++;


        }


        return FXCollections.observableList(list);

    }

    /**
     * Questo metodo si occupa di effettuare l'update dei campi nella tabella Eventi
     * @param type_query
     * @param name_type
     * @param id_event
     */

    public void update(String type_query, String name_type, String id_event) {


        String req = type_query + "\n" + name_type + "\n" + id_event + "\n";

        try {
            Socket s = new Socket(Server.HOST, Server.PORT);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);

            out.println(req);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Questo metodo si occupa di riempire la tabella degli eventi
     *
     * @param type_query
     * @return ObservableList<Event> data
     */
    public ObservableList<Event> getInitialTableData(String type_query) {

        String req = type_query +"\n";
        List list = new ArrayList();


        try {
            Socket s = new Socket(Server.HOST, Server.PORT);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(req);

            String name = in.readLine();
            if (name.equals("OK")) {

                while (name.length() > 0) {

                    name = in.readLine();
                    String surname = in.readLine();
                    String inOut = in.readLine();
                    String date = in.readLine();
                    String id_event = in.readLine();


                    if (name != null && surname != null && inOut != null && date != null && id_event != null) {
                        list.add(new Event(surname, name, date, inOut, id_event));

                    }

                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObservableList<Event> data = FXCollections.observableList(list);

        return data;

    }


}