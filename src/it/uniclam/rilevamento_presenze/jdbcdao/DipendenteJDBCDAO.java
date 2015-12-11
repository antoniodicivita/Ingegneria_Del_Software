package it.uniclam.rilevamento_presenze.jdbcdao;

import it.uniclam.rilevamento_presenze.connections.ConnectionDB;

//import it.uniclam.rilevamento_presenze.test.test.User;
import it.uniclam.rilevamento_presenze.connections.Server;
import it.uniclam.rilevamento_presenze.beanclass.Dipendente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

public class DipendenteJDBCDAO {
	Connection connection = null;
	PreparedStatement ptmt= null;
	ResultSet resultSet = null;

	public DipendenteJDBCDAO() {}

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



    public void removeList(String type_query,String valueONE,String valueTWO){

        String req= type_query +"\n" + valueONE + "\n"+ valueTWO + "\n";

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


    public ObservableList<Dipendente> SELECT_List(String type_query){



        List<Dipendente> list = new ArrayList<>();
        String req= type_query +"\n";

            try {
                Socket s = new Socket(Server.HOST, Server.PORT);

                PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out.println(req);
                //String line = in.readLine();

                String nome = in.readLine();
                while (nome.length() > 0) {
                   //System.out.println(line);


                    nome = in.readLine();
                    String cognome = in.readLine();
                    list.add(new Dipendente(nome, cognome,"10"));



                    System.out.println("STAMPA: "+nome+ " e: "+ cognome);
                }



                s.close();


            } catch (IOException e) {
                e.printStackTrace();
            }

        ObservableList<Dipendente> data = FXCollections.observableList(list);
        return data;
        }



    public void MySQL_GridView(String type_query,String nome, String cognome){
        JFrame frame = new JFrame("Dettagli Eventi");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        String columnNames[] = { "NOME", "COGNOME", "IN/OUT","DATA","ORA" };
        String req =  type_query +"\n" + nome+"\n" + cognome+"\n";


        try {
            Socket s  = new Socket(Server.HOST, Server.PORT);

            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(req);

            int count=0;

            String line=in.readLine();
            int countrow =Integer.parseInt(line);
            Object rowData[][] = new Object[countrow][];


            while(line.length()>0&&count<countrow){

                String Nome=in.readLine();
                String Cognome=in.readLine();
                String INOUT=in.readLine();
                String Data=in.readLine();
                String Ora=in.readLine();


                rowData[count] = new Object[]{Nome,Cognome,INOUT,Data,Ora};

                count++;
                System.out.println(Nome+Cognome+INOUT+Data+Ora);


            }
            s.close();

            JTable table = new JTable(rowData, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.pack();//frame.setSize(AUTO)
            frame.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }






    }



    public void updateList(String type_query,String nome,String cognome) {

        //String req = "UPDATE user SET Cognome='"+valueTWO+"' WHERE Nome='"+valueONE+"'AND Cognome'"+valueTWO+"'";
        String req = type_query +"\n" + nome+"\n" + cognome+"\n";

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


    public void searchList(String type_query,String chiavericerca,ObservableList<Dipendente> value){

        String req = type_query +"\n" + chiavericerca+"\n";

        ObservableList<Dipendente> data=value;
        try {
            Socket s = new Socket(Server.HOST, Server.PORT);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(req);

            String nome = in.readLine();
            while (nome.length() > 0) {

                nome = in.readLine();
                String cognome = in.readLine();

                data.addAll(new Dipendente(nome, cognome,""));

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int SELECT_NameSurname(String name,String surname,String id_user) {

        int state=0;
        try {
            String queryString = "SELECT * FROM user WHERE Cognome='"+surname+"'AND Nome='"+name+"' OR ID_User='"+id_user+"' ";
            connection = getConnection();
            //System.out.println("Prepare statemòent OK");
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery(queryString);
          /* if (res.next()!=false){
                System.out.println("ACCESSO VALIDO");
*/
            while (res.next()==true) {
                int id=res.getInt("ID_User");
                String nome = res.getString("Nome");
                String cognome = res.getString("Cognome");
                //String telefono=res.getString("Telefono");

                //  System.out.println(id + "\t" + nome + "\t" + cognome+ "\t" + telefono);
                state=id;}
            //if(res.next()==false) { System.out.println("ACCESSO NEGATO:Badge non valido");}

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {

                if (resultSet != null)
                    resultSet.close();
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return state;
    }

    public String genPDF(String type_query){

        String req = type_query +"\n";


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
return req;

    }


    public String SELECT_PDF() {

        int state=0;
        String stringa="";
        try {
            String queryString = "SELECT * FROM user  ";
            connection = getConnection();
            System.out.println("CO");
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery(queryString);
          /* if (res.next()!=false){
                System.out.println("ACCESSO VALIDO");
*/
            while (res.next()==true) {
                int id=res.getInt("ID_User");
                String nome = res.getString("Nome");
                String cognome = res.getString("Cognome");

stringa=id + "\t" + nome + "\t" + cognome;
                System.out.println(id + "\t" + nome + "\t" + cognome);
                state=id;}
            //if(res.next()==false) { System.out.println("ACCESSO NEGATO:Badge non valido");}

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {

                if (resultSet != null)
                    resultSet.close();
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return stringa;


}



}