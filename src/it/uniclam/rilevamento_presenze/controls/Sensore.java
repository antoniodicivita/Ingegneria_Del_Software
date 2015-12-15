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
public class Sensore {



    public static void addEvent(String type_query, int id,  String hour, String date, int type_event){

        String req= type_query +"\n" + id + "\n" + hour + "\n"+ date+ "\n"+ type_event + "\n";

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

    public static int countItem(String type_query, int id, String date){

        int count = 0;

        String req = type_query +"\n" + id + "\n" + date+ "\n";
        try {
            Socket s = new Socket(Server.HOST, Server.PORT);

            PrintWriter out =new PrintWriter(s.getOutputStream(),true);
            BufferedReader in= new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(req);
            String count2="";
            String line=in.readLine();

            if(line.equals("OK")) {
                line=in.readLine();
                count=Integer.parseInt(line);
                s.close();

            }else{
                System.out.println("Errore nel server!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return count;

    }


    public void orderByDate(String type_query, int id){

        String req = type_query +"\n" + id + "\n";


        try {
            Socket s = new Socket(Server.HOST, Server.PORT);

            PrintWriter out =new PrintWriter(s.getOutputStream(),true);
            BufferedReader in= new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(req);

            String line=in.readLine();

            while(line.length()>0){
                System.out.println(line);
                line=in.readLine();
            }


            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static int SELECT_NameSurname(String type_query,String name,String surname,String id_user){



        String req = type_query +"\n" + name + "\n" + surname + "\n" + id_user + "\n";

        int id = 0;


        try {
            Socket s = new Socket(Server.HOST, Server.PORT);

            PrintWriter out =new PrintWriter(s.getOutputStream(),true);
            BufferedReader in= new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(req);

            //String line=in.readLine();
            //String id_dipendente =in.readLine();
            String nome = in.readLine();
            String cognome = "";
            String id_dipendente = "";
            if(nome.equals("OK")) {
               while (nome.length()>0) {


                       nome = in.readLine();
                       cognome = in.readLine();
                       id_dipendente = in.readLine();
                   if(id_dipendente!=null){
                       id=Integer.parseInt(id_dipendente);
                   }
                   //s.close();
               }
                s.close();
                //id=Integer.parseInt(id_dipendente);
            }else{
                System.out.println("Errore nel server!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;
        /*
        int state=0;

        String queryString = "SELECT * FROM user WHERE Cognome='"+surname+"'AND Nome='"+name+"' OR ID_User='"+id_user+"' ";

        try {
            connection = getConnection();

            try {


                Statement st = connection.createStatement();
                ResultSet res = st.executeQuery(queryString);
                while (res.next()==true){
                    int id=res.getInt("ID_User");
                    String nome = res.getString("Nome");
                    String cognome = res.getString("Cognome");
                    state=id;
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }

            //String telefono=res.getString("Telefono");


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return state;*/
    }
}
