package it.uniclam.rilevamento_presenze.connections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;


/**
 * Created by Antonio on 30/11/2015.
 */

/**
 * La classe Server:
 * 1. si occupa di interagire con il database per l'esecuzione delle query
 * 2. si occupa di rispondere alle richieste del client
 * 3. interagisce con le classi DipendenteJDBCDAO, EventJDBCDAO e Sensor
 */

public class Server {


    public static String HOST = "127.0.0.1";
    public static int PORT = 5555;
    public static String QUERY_RETURN_ID = "req_query_return_id";
    public static String QUERY_IN_OUT = "req_query_in_out";
    public static String QUERY_ORDERDATE = "req_query_orderdate";
    public static String QUERY_COUNT_ITEM = "req_query_count_item";
    public static String QUERY_ADD_LIST = "req_query_add_list";
    public static String QUERY_REMOVE_LIST = "req_query_remove_list";
    public static String QUERY_SELECT_ALL_LIST = "req_query_select_all_list";
    public static String QUERY_SEARCH_LIST = "req_search_list";
    public static String QUERY_UPDATE_LIST = "req_update_list";
    public static String QUERY_DETAILS = "req_details";
    public static String QUERY_CREATE_MONTH_REPORT = "req_create_month_report";
    public static String QUERY_CREATE_YEAR_REPORT = "req_create_year_report";
    public static String QUERY_UPDATE_EVENT = "req_update_event";
    public static String QUERY_DATE_SEARCH = "req_date_search";
    public static String QUERY_SELECT_ALL_EVENT = "req_select_all_event";
    private static Connection connection;
    private static PreparedStatement ptmt;

    public static void main(String[] args) {

        try {
            ServerSocket ss = new ServerSocket(PORT);
            try {
                while (true) {
                    Socket s = ss.accept();

                    PrintWriter outchannel = new PrintWriter(s.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

                    String command = in.readLine();

                    if (command.equals(QUERY_IN_OUT)) {
                        String id = in.readLine().replace("Return_ID_User", "");
                        String hour = in.readLine().replace("hour", "");
                        String date = in.readLine().replace("date", "");
                        String type_event = in.readLine().replace("type_event", " ");

                        String query = "INSERT INTO event(Hour, Data,User_ID,Type_ID) VALUES('" + hour + "','" + date + "','" + id + "','" + type_event + "')";

                        standardQueryExecutor(query);
                        outchannel.println("Evento registrato correttamente");

                        s.close();

                    } else if (command.equals(QUERY_COUNT_ITEM)) {

                        String id = in.readLine().replace("Return_ID_User", "");
                        String date = in.readLine().replace("date", "");

                        String query = "SELECT COUNT(ID_Event)FROM event WHERE User_ID ='" + id + "' AND Data ='" + date + "'";
                        String count = countItem(query);

                        outchannel.println(count);
                        s.close();

                    }

                    else if (command.equals(QUERY_RETURN_ID)) {

                        String name = in.readLine().replace("name", "");
                        String surname = in.readLine().replace("surname", "");
                        String id = in.readLine().replace("id_user", "");


                        String query = "SELECT Nome, Cognome, ID_User FROM user WHERE Cognome='" + surname + "'AND Nome='" + name + "' OR ID_User='" + id + "' ";
                        String count = retId(query);

                        outchannel.println(count);
                        s.close();

                    }

                    else if (command.equals(QUERY_ORDERDATE)){


                        String name = in.readLine().replace("nome", "");
                        String surname = in.readLine().replace("cognome", "");

                        String query = "SELECT  Nome,Cognome, Name_Type, Data,Hour FROM (type JOIN event ON Type_ID=ID_Type) JOIN user ON User_ID=ID_User WHERE Nome='" + name + "' AND Cognome='" + surname + "' ORDER BY (str_to_date(Data, '%d%b%Y')) DESC  , (str_to_date(Hour, '%H:%i:%s')) DESC";

                        String out = orderByDate(query);


                        outchannel.println(out);
                        s.close();
                    }

                    else if (command.equals(QUERY_DATE_SEARCH)){


                        String initialdate = in.readLine();
                        String finaldate = in.readLine();

                        String query = "SELECT Cognome, Nome, Data, Name_Type, ID_Event FROM (event JOIN type ON Type_ID = ID_Type) JOIN user ON User_ID=ID_User WHERE (str_to_date(Data, '%d%b%Y')) >= '" + initialdate + "' AND (str_to_date(Data, '%d%b%Y')) <= '" + finaldate + "' ORDER BY  (str_to_date(Data, '%d%b%Y')) DESC, User_ID";
                        String out = searchDate(query);


                        outchannel.println(out);
                        s.close();
                    }


                    else if(command.equals(QUERY_ADD_LIST)){

                        String valueOne = in.readLine().replace("valueONE", "");
                        String query = "INSERT INTO user(Cognome,Nome) VALUES ('"+valueOne+"','')";

                        String out = standardQueryExecutor(query);
                        outchannel.println(out);
                        s.close();
                    }

                    else if(command.equals(QUERY_REMOVE_LIST)){

                        String valueOne = in.readLine().replace("valueONE", "");
                        String valueTwo = in.readLine().replace("valueTWO", "");
                        String query = "DELETE FROM user WHERE user.Nome='"+valueOne+"' AND user.Cognome='"+valueTwo+"'";
                        String out = standardQueryExecutor(query);
                        outchannel.println(out);
                        s.close();
                    }

                    else if(command.equals(QUERY_SELECT_ALL_LIST)){

                        String query = "SELECT * FROM user";

                        String out = selectAllList(query);
                        outchannel.println(out);
                        s.close();
                    }
                    else if(command.equals(QUERY_SEARCH_LIST)){


                        String keySearch = in.readLine().replace("chiavericerca", "");
                        String query = "SELECT * FROM user WHERE Cognome LIKE  '" + keySearch + "%'OR Nome LIKE '" + keySearch + "%'";
                        String out = searchList(query);
                        outchannel.println(out);


                        s.close();
                    }




                    else if(command.equals(QUERY_UPDATE_LIST)){

                        String name = in.readLine().replace("valueONE", "");
                        String surname = in.readLine().replace("valueTWO", "");
                        String id = in.readLine().replace("id", "");
                        String query = "UPDATE user SET Cognome='" + surname + "', Nome='" + name + "' WHERE ID_User='" + id + "'";
                        String out = standardQueryExecutor(query);
                        outchannel.println(out);
                        s.close();
                    }

                    else if(command.equals(QUERY_UPDATE_EVENT)){

                        String id_type = in.readLine();
                        String id_event = in.readLine();
                        String query = "UPDATE event SET Type_ID ='"+id_type+"' WHERE ID_Event='"+id_event+"'";

                        String out = standardQueryExecutor(query);
                        outchannel.println(out);
                        s.close();
                    }

                    else if(command.equals(QUERY_CREATE_MONTH_REPORT)){

                        String month = in.readLine();
                        String year = in.readLine();
                        String query = "SELECT Nome,Cognome,Name_Type,Data,Hour FROM (type JOIN event ON Type_ID=ID_Type) JOIN user ON User_ID=ID_User  WHERE YEAR(str_to_date(Data,'%d%b%Y')) = '" + year + "' AND MONTH(str_to_date(Data,'%d%b%Y')) = '" + month + "' ORDER BY (str_to_date(Data, '%d%b%Y')) DESC";
                        String out = createReport(query);
                        outchannel.println(out);
                        s.close();
                    }

                    else if(command.equals(QUERY_CREATE_YEAR_REPORT)){


                        String year = in.readLine();
                        String query = "SELECT Nome,Cognome,Name_Type,Data,Hour FROM (type JOIN event ON Type_ID=ID_Type) JOIN user ON User_ID=ID_User  WHERE YEAR(str_to_date(Data,'%d%b%Y')) = '" + year + "'ORDER BY (str_to_date(Data, '%d%b%Y')) DESC";
                        String out = createReport(query);
                        outchannel.println(out);
                        s.close();
                    }

                    else if(command.equals(QUERY_DETAILS)){


                        String name = in.readLine().replace("nome", "");
                        String surname = in.readLine().replace("cognome", "");

                        String query = "SELECT Nome, Cognome, Orari, Pausa_pranzo, Straordinario FROM user WHERE Nome='" + name + "' AND Cognome='" + surname + "'";

                        String out = details(query);


                        outchannel.println(out);
                        s.close();
                    } else if (command.equals(QUERY_SELECT_ALL_EVENT)) {


                        String query = "SELECT event.Data, Nome, Cognome, Name_Type,ID_Event FROM (event  JOIN user ON User_ID=ID_User) JOIN type ON Type_ID=ID_Type  ORDER BY STR_TO_DATE(Data, '%d%b%Y') DESC, User_ID";
                        String out = searchDate(query);


                        outchannel.println(out);
                        s.close();
                    }


                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    //Inizio implementazione metodi di esecuzione query

    /**
     * Questo metodo si occupa di eseguire la query di selezione delle informazioni dei dipendenti
     *
     * @param query
     * @return String out che contiene il risultato della query
     */
    public static String details(String query){

        String out="Error\n";
        try {
            connection = ConnectionDB.getInstance().getConnection();

            Statement st = connection.createStatement();
            ResultSet res= st.executeQuery(query);

            int rowcount = 0;
            if (res.last()) {
                rowcount = res.getRow();
                res.beforeFirst();
            }

            out = "OK\n";
            out += rowcount+"\n";
            while (res.next()==true) {


                out += res.getString("Nome")+ "\n";
                out += res.getString("Cognome")+ "\n";
                out += res.getString("Orari")+ "\n";
                out += res.getString("Pausa_pranzo")+ "\n";
                out += res.getString("Straordinario")+ "\n";



            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;

    }


    /**
     * Questo metodo si occupa di eseguire la query di selezione delle informazioni utili alla creazione del report
     *
     * @param query
     * @return String out che contiene il risultato della query
     */
    public static String createReport(String query){

        String out="Error!\n";
        try {
            connection = ConnectionDB.getInstance().getConnection();

            Statement st = connection.createStatement();
            ResultSet res= st.executeQuery(query);


            out ="OK\n";
            while (res.next()==true) {


                out += res.getString("Nome")+ "\n";
                out += res.getString("Cognome")+ "\n";
                out += res.getString("Name_Type")+ "\n";
                out += res.getString("Data")+ "\n";
                out += res.getString("Hour")+ "\n";

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;

    }


    /**
     * Questo metodo si occupa di eseguire la query di selezione dei dipendenti
     *
     * @param query
     * @return String out che contiene il risultato della query
     */


    public static String searchList(String query) {

        String out="Error!\n";
        try {


            connection = ConnectionDB.getInstance().getConnection();

            ptmt = connection.prepareStatement(query);


            ResultSet res= ptmt.executeQuery(query);

            out = "OK\n";
            while (res.next()) {


                out += res.getString("Nome")+"\n";
                out += res.getString("Cognome")+"\n";
                out += res.getString("ID_User")+"\n";


            }



        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;
    }

    /**
     * Questo metodo si occupa di eseguire la query di selezione dei dipendenti
     *
     * @param query
     * @return String out che contiene il risultato della query
     */

    public static String selectAllList(String query){

        String out="Error!\n";
        try {

            connection = ConnectionDB.getInstance().getConnection();

            ptmt = connection.prepareStatement(query);


            ResultSet res= ptmt.executeQuery(query);



            out = "OK\n";

            int rowcount = 0;
            if (res.last()) {
                rowcount = res.getRow();
                res.beforeFirst();
            }
            int v=rowcount;
            while (res.next()) {


                out += res.getString("Nome")+"\n";
                out += res.getString("Cognome")+"\n";
                out += res.getString("ID_User") + "\n";


            }




        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;
    }


    /**
     * Questo metodo si occupa di eseguire la query per il conteggio del numero di eventi giornalieri
     *
     * @param query
     * @return String out che contiene il risultato della query
     */
    public static String countItem(String query) {


        String out = "Error!\n";
        try {

            connection = ConnectionDB.getInstance().getConnection();

            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery(query);


            out = "OK\n";
            while (res.next() == true) {
                out += res.getString("COUNT(ID_Event)")+"\n";

            }


        } catch (SQLException e) {
            e.printStackTrace();

        }

        return out;
    }


    /**
     * Questo metodo si occupa di eseguire la query di selezione dei dipendenti
     *
     * @param query
     * @return String out che contiene il risultato della query
     */
    public static String retId(String query){

        String out = "Error!\n";
        try {

            connection = ConnectionDB.getInstance().getConnection();

            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery(query);

            out = "OK\n";
            while (res.next()) {

                out += res.getString("Nome")+ "\n";
                out += res.getString("Cognome")+"\n";
                out += res.getString("ID_User")+"\n";

            }
        }catch (SQLException e) {
            e.printStackTrace();

        }
        return out;
    }


    /**
     * Questo metodo si occupa di eseguire la query di ordinamento cronologico degli eventi
     *
     * @param query
     * @return String out che contiene il risultato della query
     */
    public static String orderByDate(String query){

        String out="Error!\n";
        try {
            connection = ConnectionDB.getInstance().getConnection();

            Statement st = connection.createStatement();
            ResultSet res= st.executeQuery(query);

            int rowcount = 0;
            if (res.last()) {
                rowcount = res.getRow();
                res.beforeFirst();
            }

            out = "OK\n";
            out += rowcount+"\n";
            while (res.next()==true) {

                out += res.getString("Nome")+ "\n";
                out += res.getString("Cognome")+ "\n";
                out += res.getString("Name_Type")+ "\n";
                out += res.getString("Data")+ "\n";
                out += res.getString("Hour")+ "\n";


            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;

    }


    /**
     * Questo metodo si occupa di eseguire la query di ricerca per intervallo di data
     *
     * @param query
     * @return String out che contiene il risultato della query
     */
    public static String searchDate(String query){

        String out="Error!\n";
        try {
            connection = ConnectionDB.getInstance().getConnection();

            Statement st = connection.createStatement();
            ResultSet res= st.executeQuery(query);



            out = "OK\n";

            while (res.next()==true) {


                out += res.getString("Nome")+ "\n";
                out += res.getString("Cognome")+ "\n";
                out += res.getString("Name_Type")+ "\n";
                out += res.getString("Data")+ "\n";

                out += res.getString("ID_Event")+ "\n";



            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;

    }


    /**
     * Questo metodo si occupa di eseguire le query di aggiunta e rimozione dei dipendenti dal db
     *
     * @param query
     * @return
     * String out che contiene il risultato della query
     */
    public static String standardQueryExecutor(String query){

        String out = "Error!\n";
        try {

            connection = ConnectionDB.getInstance().getConnection();

            ptmt = connection.prepareStatement(query);

            ptmt.executeUpdate(query);

            out = "OK\n";



        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;
    }



}




