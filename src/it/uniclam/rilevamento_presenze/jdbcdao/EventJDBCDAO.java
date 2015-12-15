package it.uniclam.rilevamento_presenze.jdbcdao;

import it.uniclam.rilevamento_presenze.beanclass.*;
import it.uniclam.rilevamento_presenze.connections.ConnectionDB;
import it.uniclam.rilevamento_presenze.connections.Server;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import it.uniclam.rilevamento_presenze.beanclass.Event;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Chriz 7X on 06/12/2015.
 */
public class EventJDBCDAO {Connection connection = null;
    PreparedStatement ptmt= null;
    ResultSet resultSet = null;

    public EventJDBCDAO() {}

    private Connection getConnection() throws SQLException {
        Connection conn;
        conn = ConnectionDB.getInstance().getConnection();
        return conn;
    }





    public void addList(String type_query,String valueONE){

        String req= type_query +"\n" + valueONE + "\n";

        try {
            Socket s = new Socket(Server.HOST, Server.PORT);

            PrintWriter out =new PrintWriter(s.getOutputStream(),true);
            BufferedReader in= new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(req);
            String line=in.readLine();

            System.out.println(line);
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }






    public void update(String type_query, String name_type, String id_event) {

        //String req = "UPDATE user SET Cognome='"+valueTWO+"' WHERE Nome='"+valueONE+"'AND Cognome'"+valueTWO+"'";
        String req = type_query +"\n" + name_type + "\n" + id_event + "\n";

        try {
            Socket s = new Socket(Server.HOST, Server.PORT);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(req);


        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    //OK




    public static  ObservableList<Event> searchDate(String type_query,String datainiziale, String datafinale){

        String req = type_query +"\n" + datainiziale+"\n" + datafinale +"\n";



        List list = new ArrayList();


        try {
            Socket s = new Socket(Server.HOST, Server.PORT);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(req);

            String nome=in.readLine();
            if(nome.equals("OK")) {

                while (nome.length() > 0 ) {

                    nome = in.readLine();
                    String Cognome = in.readLine();
                    String INOUT = in.readLine();
                    String Data = in.readLine();
                    String id_event = in.readLine();
                   // String Ora = in.readLine();

                    if(nome !=null && Cognome!=null && INOUT!=null && Data!=null&& id_event!=null){
                    list.add(new Event(Cognome,nome,Data,INOUT,id_event));
                        //System.out.println(Cognome+" " +nome+" " +Data+" " +INOUT+" " +id_event);
                    }

                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        ObservableList<Event> date_ordered = FXCollections.observableList(list);


        //return date_ordered;
        return  insertOnlyInconsistences(date_ordered);
    }







    public  static void call(TableView<Event> table, ObservableList<Event> data){

        int lenght = data.size();//Numero elementi nella tabella
        int i=0;


        while (i<lenght-1) {
            if (Objects.equals(table.getItems().get(i).getCognome().toString(), table.getItems().get(i + 1).getCognome().toString()) &&
                    Objects.equals(table.getItems().get(i).getData().toString(), table.getItems().get(i + 1).getData().toString()) &&
                    Objects.equals(table.getItems().get(i).getType_id().toString(), table.getItems().get(i + 1).getType_id().toString())) {
                table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                table.requestFocus();

                table.getSelectionModel().select(data.get(i));
                table.getSelectionModel().select(data.get(i + 1));


                table.getSelectionModel().focus(4);



                //table.getItems().
                //  System.out.println("I:"+i+" J:"+ j +"DataA: "+table.getItems().get(i).getCognome().toString() +"DataB: "+table.getItems().get(i).getData().toString()+" + "+ table.getItems().get(j).getCognome().toString()+" "+table.getItems().get(j).getData().toString());
            }
            i++;



        }



    }






    public  static ObservableList<Event> insertOnlyInconsistences(TableView<Event> table,ObservableList<Event> data){

        int lenght = table.getItems().size();//Numero elementi nella tabella
        int i=1;

        List list = new ArrayList();


        while (i<lenght) {

            if (Objects.equals(table.getItems().get(i).getCognome().toString(), table.getItems().get(i - 1).getCognome().toString()) &&
                    Objects.equals(table.getItems().get(i).getData().toString(), table.getItems().get(i - 1).getData().toString()) &&
                    Objects.equals(table.getItems().get(i).getType_id().toString(), table.getItems().get(i - 1).getType_id().toString())) {
               // data.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                //data.requestFocus();

                System.out.println(table.getItems().get(i).getEvent_id().toString() + "   " + table.getItems().get(i - 1).getEvent_id().toString());

/*
                    String nome = data.get(i).getNome().toString();
                    String Cognome = data.get(i).getCognome().toString();
                    String INOUT = data.get(i).getType_id().toString();
                    String Data = data.get(i).getData().toString();
                    String id_event = data.get(i).getEvent_id().toString();

*/
                    //list.add(table.getItems().get(i));
                if(!list.contains(table.getItems().get(i-1))){
                //if(table.getItems().get(i-1).getEvent_id()!=table.getItems().get(i).getEvent_id() ){
                    list.add(table.getItems().get(i-1));

                    //list.add(table.getItems().get(i+1));
                    list.add(table.getItems().get(i));}
//                System.out.println(list.get(i).toString());

                   // list.add(new Event(Cognome,nome,Data,INOUT,id_event));
                //System.out.println(Cognome+" " +nome+" " +Data+" " +INOUT+" " +id_event);
/*
                    String nome1 = data.get(i+1).getNome().toString();
                    String Cognome1 = data.get(i+1).getCognome().toString();
                    String INOUT1 = data.get(i+1).getType_id().toString();
                    String Data1 = data.get(i+1).getData().toString();
                    String id_event1 = data.get(i+1).getEvent_id().toString();
                    list.add(new Event(Cognome1,nome1,Data1,INOUT1,id_event1));
                    System.out.println(Cognome1+" " +nome1+" " +Data1+" " +INOUT1+" " +id_event1);

                //table.getSelectionModel().select(data.get(i));
                //table.getSelectionModel().select(data.get(i + 1));


                //table.getSelectionModel().focus(4);

              //  System.out.println(list.get(i).toString());

                //table.getItems().
                //  System.out.println("I:"+i+" J:"+ j +"DataA: "+table.getItems().get(i).getCognome().toString() +"DataB: "+table.getItems().get(i).getData().toString()+" + "+ table.getItems().get(j).getCognome().toString()+" "+table.getItems().get(j).getData().toString());
                */
            }
            i++;



        }


        return  FXCollections.observableList(list);

    }

    public  static ObservableList<Event> insertOnlyInconsistences(ObservableList<Event> data){

        int lenght = data.size();//Numero elementi nella tabella
        int i=1;

        List list = new ArrayList();


        while (i<lenght) {

            if (Objects.equals(data.get(i).getCognome().toString(), data.get(i - 1).getCognome().toString()) &&
                    Objects.equals(data.get(i).getData().toString(), data.get(i - 1).getData().toString()) &&
                    Objects.equals(data.get(i).getType_id().toString(), data.get(i - 1).getType_id().toString())) {

                System.out.println(data.get(i).getEvent_id().toString() + "   " + data.get(i - 1).getEvent_id().toString());

                if(!list.contains(data.get(i - 1))){

                    list.add(data.get(i - 1));

                    list.add(data.get(i));}
//
            }
            i++;



        }


        return  FXCollections.observableList(list);

    }


}