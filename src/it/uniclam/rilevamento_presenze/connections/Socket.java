package it.uniclam.rilevamento_presenze.connections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;

/**
 * Created by Chriz 7X on 04/12/2015.
 */
public class Socket {
   /* public static void main(String[] args){
        try {
            ServerSocket ss = new ServerSocket(5556);

            while (true) {
                java.net.Socket s = ss.accept();

                PrintWriter outchannel = new PrintWriter(s.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

                String command = in.readLine();

                if(command.equals(Server.QUERY_RETURN_ID)) {

                    String nome = in.readLine();
                    String cognome = in.readLine();
                    String id = in.readLine();


                    String query = "SELECT * FROM user WHERE Cognome='"+cognome+"'AND Nome='"+nome+"' OR ID_User='"+id+"' ";
                    String count = Server.returnId(query);

                    outchannel.println(count);
                    s.close();

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }*/
}

