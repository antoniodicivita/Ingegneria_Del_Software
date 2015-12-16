package it.uniclam.rilevamento_presenze.controls;

import it.uniclam.rilevamento_presenze.connections.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Antonio on 05/12/2015.
 */


/**
 * La Classe Sensor:
 * 1. Svolge il ruolo di client per BadgeGUI
 * 2. Implementa tutti i metodi utilizzati dalla classe BadgeGUI
 * 3.Interagisce con le classi Server e BadgeGUI
 */
public class Sensor {


    /**
     * Questo metodo si occupa di registrare gli ingressi e le uscite
     *
     * @param type_query
     * @param id
     * @param hour
     * @param date
     * @param type_event
     */

    public static void addEvent(String type_query, int id, String hour, String date, int type_event) {

        String req = type_query + "\n" + id + "\n" + hour + "\n" + date + "\n" + type_event + "\n";

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
     * Questo metodo si occupa di effettuare il conteggio degli eventi giornalieri di un dipendente
     *
     * @param type_query
     * @param id
     * @param date
     * @return int count
     */
    public static int countItem(String type_query, int id, String date) {

        int count = 0;

        String req = type_query + "\n" + id + "\n" + date + "\n";
        try {
            Socket s = new Socket(Server.HOST, Server.PORT);

            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(req);

            String line = in.readLine();

            if (line.equals("OK")) {
                line = in.readLine();
                count = Integer.parseInt(line);
                s.close();

            } else {
                System.out.println("Errore nel server!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return count;

    }


    /**
     * Questo metodo si occupa di estrarre l'id del dipendente che sta effettuando l'entata o l'uscita
     *
     * @param type_query
     * @param varName
     * @param varSurname
     * @param id_user
     * @return int id
     */
    public static int selectNameSurname(String type_query, String varName, String varSurname, String id_user) {


        String req = type_query + "\n" + varName + "\n" + varSurname + "\n" + id_user + "\n";

        int id = 0;


        try {
            Socket s = new Socket(Server.HOST, Server.PORT);

            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(req);


            String name = in.readLine();
            String surname = "";
            String idEmployee = "";
            if (name.equals("OK")) {
                while (name.length() > 0) {


                    name = in.readLine();
                    surname = in.readLine();
                    idEmployee = in.readLine();
                    if (idEmployee != null) {
                        id = Integer.parseInt(idEmployee);
                    }

                }
                s.close();

            } else {
                System.out.println("Errore nel server!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;


    }
}
