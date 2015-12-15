package it.uniclam.rilevamento_presenze.connections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.GregorianCalendar;

//import it.uniclam.rilevamento_presenze.JDBCDataAccessObject.EventJDBCDAO;
/**
 * Created by Antonio on 30/11/2015.
 */
public class Server {


    private static Connection connection;
    private static PreparedStatement ptmt;

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

                        String nome = in.readLine().replace("name", "");
                        String cognome = in.readLine().replace("surname", "");
                        String id = in.readLine().replace("id_user", "");


                        String query = "SELECT Nome, Cognome, ID_User FROM user WHERE Cognome='"+cognome+"'AND Nome='"+nome+"' OR ID_User='"+id+"' ";
                        String count = retId(query);

                        outchannel.println(count);
                        s.close();

                    }

                    else if (command.equals(QUERY_ORDERDATE)){


                        String nome = in.readLine().replace("nome", "");
                        String cognome = in.readLine().replace("cognome", "");

                        String query = "SELECT  Nome,Cognome, Name_Type, Data,Hour FROM (type JOIN event ON Type_ID=ID_Type) JOIN user ON User_ID=ID_User WHERE Nome='"+nome+"' AND Cognome='"+cognome+"' ORDER BY (str_to_date(Data, '%d%b%Y')) DESC  , (str_to_date(Hour, '%H:%i:%s')) DESC";

                        String out = orderByDate(query);


                        outchannel.println(out);
                        s.close();
                    }

                    else if (command.equals(QUERY_DATE_SEARCH)){


                        String datainiziale = in.readLine();
                        String datafinale = in.readLine();

                        String query = "SELECT Cognome, Nome, Data, Name_Type, ID_Event FROM (event JOIN type ON Type_ID = ID_Type) JOIN user ON User_ID=ID_User WHERE (str_to_date(Data, '%d%b%Y')) >= '" + datainiziale + "' AND (str_to_date(Data, '%d%b%Y')) <= '" + datafinale+"' ORDER BY  (str_to_date(Data, '%d%b%Y')) DESC, User_ID";
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


                        String chiavericerca = in.readLine().replace("chiavericerca", "");
                        String query = "SELECT * FROM user WHERE Cognome LIKE  '" + chiavericerca + "%'OR Nome LIKE '" + chiavericerca + "%'";
                        String out = searchList(query);
                        outchannel.println(out);


                        s.close();
                    }




                    else if(command.equals(QUERY_UPDATE_LIST)){

                        String nome = in.readLine().replace("valueONE", "");
                        String cognome = in.readLine().replace("valueTWO", "");
                        String id = in.readLine().replace("id", "");
                        String query = "UPDATE user SET Cognome='"+cognome+"', Nome='"+nome+"' WHERE ID_User='"+id+"'";
                        String out = standardQueryExecutor(query);
                        outchannel.println(out);
                        s.close();
                    }

                    else if(command.equals(QUERY_UPDATE_EVENT)){

                       // String event = in.readLine().replace("event", "");

                        String id_type = in.readLine();
                        String id_event = in.readLine();
                        String query = "UPDATE event SET Type_ID ='"+id_type+"' WHERE ID_Event='"+id_event+"'";
                        //String query = "UPDATE event SET Type_ID ='2' WHERE ID_Event='238'";
                        String out = standardQueryExecutor(query);
                        outchannel.println(out);
                        s.close();
                    }

                    else if(command.equals(QUERY_CREATE_MONTH_REPORT)){

                        String mese = in.readLine();
                        String anno = in.readLine();
                        String query = "SELECT Nome,Cognome,Name_Type,Data,Hour FROM (type JOIN event ON Type_ID=ID_Type) JOIN user ON User_ID=ID_User  WHERE YEAR(str_to_date(Data,'%d%b%Y')) = '" + anno +"' AND MONTH(str_to_date(Data,'%d%b%Y')) = '"+ mese+"' ORDER BY (str_to_date(Data, '%d%b%Y')) DESC" ;
                        String out = createReport(query);
                        outchannel.println(out);
                        s.close();
                    }

                    else if(command.equals(QUERY_CREATE_YEAR_REPORT)){

                        String mese = in.readLine();
                        String anno = in.readLine();
                        String query = "SELECT Nome,Cognome,Name_Type,Data,Hour FROM (type JOIN event ON Type_ID=ID_Type) JOIN user ON User_ID=ID_User  WHERE YEAR(str_to_date(Data,'%d%b%Y')) = '" + anno +"'ORDER BY (str_to_date(Data, '%d%b%Y')) DESC" ;
                        String out = createReport(query);
                        outchannel.println(out);
                        s.close();
                    }

                    else if(command.equals(QUERY_DETAILS)){


                        String nome = in.readLine().replace("nome", "");
                        String cognome = in.readLine().replace("cognome", "");

                        String query = "SELECT Nome, Cognome, Orari, Pausa_pranzo, Straordinario FROM user WHERE Nome='"+ nome + "' AND Cognome='" + cognome+"'";

                        String out = details(query);


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


    /*
    public static void addList(String query){
        try {

            connection = ConnectionDB.getInstance().getConnection();

            ptmt = connection.prepareStatement(query);

            ptmt.executeUpdate(query);


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    public static void removeList(String query){
        try {

            connection = ConnectionDB.getInstance().getConnection();

            ptmt = connection.prepareStatement(query);

            ptmt.executeUpdate(query);


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static void updateList(String query){
        try {

            connection = ConnectionDB.getInstance().getConnection();

            ptmt = connection.prepareStatement(query);

            ptmt.executeUpdate(query);


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }




    public static void addEvent(String query) {

        try {

            connection = ConnectionDB.getInstance().getConnection();

            ptmt = connection.prepareStatement(query);

            ptmt.executeUpdate(query);


        } catch (SQLException e) {
            e.printStackTrace();
        }




    }


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

                //out += res.getString("Hour")+ "\n";


            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;

    }


    public static String standardQueryExecutor(String query){

        String out = "Error!\n";
        try {

            connection = ConnectionDB.getInstance().getConnection();

            ptmt = connection.prepareStatement(query);

            ptmt.executeUpdate(query);
            //ptmt.executeQuery(query);
            out = "OK\n";



        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;
    }



}




