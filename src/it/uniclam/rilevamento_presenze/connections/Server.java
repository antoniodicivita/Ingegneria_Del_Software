package it.uniclam.rilevamento_presenze.connections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

//import it.uniclam.rilevamento_presenze.JDBCDataAccessObject.EventJDBCDAO;
/**
 * Created by Antonio on 30/11/2015.
 */
public class Server {


    private static Connection connection;
    private static PreparedStatement ptmt;

    public static String HOST = "127.0.0.1";
    public static int PORT = 5555;
    public static String QUERY_IN_OUT = "req_query_in_out";
    public static String QUERY_ORDERDATE = "req_query_orderdate";
    public static String QUERY_COUNT_ITEM = "req_query_count_item";
    public static String QUERY_ADD_LIST = "req_query_add_list";
    public static String QUERY_REMOVE_LIST = "req_query_remove_list";
    public static String QUERY_SELECT_ALL_LIST = "req_query_select_all_list";
    public static String QUERY_SEARCH_LIST = "req_search_list";
    public static String QUERY_UPDATE_LIST = "req_update_list";
    public static String QUERY_CREATE_PDF = "req_create_pdf";



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

                        addEvent(query);
                        outchannel.println("Evento registrato correttamente");

                        s.close();

                    } else if (command.equals(QUERY_COUNT_ITEM)) {

                        String id = in.readLine().replace("Return_ID_User", "");
                        String date = in.readLine().replace("date", "");

                        String query = "SELECT COUNT(ID_Event)FROM event WHERE User_ID ='" + id + "' AND Data ='" + date + "'";
                        int count = countItem(query);

                        outchannel.println(count);
                        s.close();

                    }

                    else if (command.equals(QUERY_ORDERDATE)){


                        String nome = in.readLine().replace("nome", "");
                        String cognome = in.readLine().replace("cognome", "");

                        String query = "SELECT DISTINCT Nome,Cognome, Name_Type, Data, Hour FROM (type JOIN event ON Type_ID=ID_Type) JOIN user ON User_ID=ID_User WHERE Nome='"+nome+"' AND Cognome='"+cognome+"' ORDER BY (str_to_date(Data, '%d%b%Y')) DESC  , (str_to_date(Hour, '%H:%i:%s')) DESC";

                        String out = orderByDate(query);


                        outchannel.println(out);
                        s.close();
                    }

                    else if(command.equals(QUERY_ADD_LIST)){

                        String valueOne = in.readLine().replace("valueONE", "");
                        String query = "INSERT INTO user(Cognome,Nome) VALUES ('"+valueOne+"','')";

                        addList(query);
                        outchannel.println("fatto");
                        s.close();
                    }

                    else if(command.equals(QUERY_REMOVE_LIST)){

                        String valueOne = in.readLine().replace("valueONE", "");
                        String valueTwo = in.readLine().replace("valueTWO", "");
                        String query = "DELETE FROM user WHERE user.Nome='"+valueOne+"' AND user.Cognome='"+valueTwo+"'";
                        removeList(query);
                        outchannel.println("fatto");
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
                        String query = "UPDATE user SET Cognome='"+nome+"' AND Nome='"+cognome+"' WHERE Nome='"+nome+"'AND Cognome'"+cognome+"'";
                        updateList(query);
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

    public static String searchList(String query) {

        String out="";
        try {



            connection = ConnectionDB.getInstance().getConnection();

            ptmt = connection.prepareStatement(query);


            ResultSet res= ptmt.executeQuery(query);
            out += "OK"+ "\n";
            while (res.next()) {


                out += res.getString("Nome")+"\n";
                out += res.getString("Cognome")+"\n";


            }



        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;
    }
    public static String selectAllList(String query){

        String out = "";
        try {

            connection = ConnectionDB.getInstance().getConnection();

            ptmt = connection.prepareStatement(query);

           // ptmt.executeUpdate(query);

            ResultSet res= ptmt.executeQuery(query);


            out += "OK"+ "\n";
            while (res.next()) {


                out += res.getString("Nome")+"\n";
                out += res.getString("Cognome")+"\n";


            }




        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;
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

    public static int countItem(String query) {

        int value = 0;
        try {

            connection = ConnectionDB.getInstance().getConnection();

            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery(query);


            while (res.next() == true) {
                value = res.getInt("COUNT(ID_Event)");
            }


        } catch (SQLException e) {
            e.printStackTrace();

        }

        return value;
    }

    public static String orderByDate(String query){

        String out="";
        try {
            connection = ConnectionDB.getInstance().getConnection();

            Statement st = connection.createStatement();
            ResultSet res= st.executeQuery(query);

            int rowcount = 0;
            if (res.last()) {
                rowcount = res.getRow();
                res.beforeFirst();
            }

            out += rowcount+"\n";
            while (res.next()==true) {


                out += res.getString("Nome")+ "\n";
                out += res.getString("Cognome")+ "\n";
                out += res.getString("Name_Type")+ "\n";
                out += res.getString("Data")+ "\n";
                out += res.getString("Hour")+ "\n";


                //out += "\n";

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;

    }

    public static String generatePDF(String query){

        String out = "";
        try {

            connection = ConnectionDB.getInstance().getConnection();

            ptmt = connection.prepareStatement(query);

            // ptmt.executeUpdate(query);

            ResultSet res= ptmt.executeQuery(query);


            out += "OK"+ "\n";
            while (res.next()) {


                out += res.getString("Nome")+"\n";
                out += res.getString("Cognome")+"\n";


            }




        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;
    }


}

