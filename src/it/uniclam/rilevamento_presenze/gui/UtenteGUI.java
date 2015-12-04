package it.uniclam.rilevamento_presenze.gui;



//import it.uniclam.rilevamento_presenze.JDBCDataAccessObject.EventJDBCDAO;
//import it.uniclam.rilevamento_presenze.JDBCDataAccessObject.UtenteJDBCDAO;
//import it.uniclam.rilevamento_presenze.beanclass.UtenteBean;
import it.uniclam.rilevamento_presenze.connections.Server;
import it.uniclam.rilevamento_presenze.utility.Time;
import it.uniclam.rilevamento_presenze.jdbcdao.DipendenteJDBCDAO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Chriz 7X on 16/11/2015.
 */
public class UtenteGUI extends JFrame implements ActionListener {

    boolean state_TrueFalse;
    int Return_ID_User;
    int Count;

    JPanel panel;
    JTextField NewMsg;
    //Obj Button IN
    JTextField TextBoxBADGE_IN;
    String String_BADGE_IN="BADGE_IN SEND";
    JLabel LabelBADGE_IN;
    JButton ButtonBADGE_IN;

    //Obj Button IN
    JTextField TextBoxBADGE_OUT;
    String String_BADGE_OUT="BADGE_OUT SEND";
    JLabel LabelBADGE_OUT;
    JButton ButtonBADGE_OUT;

    //Obj Cognome
    JTextField TextBoxCognome;
    String String_Cognome="C1";
    JLabel LabelCognome;
    JButton ButtonCognome;

    //Obj Nome
    JTextField TextBoxNome;
    String String_Nome="N1";
    JLabel LabelNome;
    JButton ButtonNome;

    //Obj CodiceUtente.
    JTextField TextBoxCodice;
    String String_Codice="3T1";
    JLabel LabelCodice;
    JButton ButtonCodice;



    public UtenteGUI() throws UnknownHostException, IOException{
        TextBoxBADGE_IN=new JTextField();
        LabelBADGE_IN=new JLabel();
        ButtonBADGE_IN=new JButton();
        TextBoxBADGE_OUT=new JTextField();
        LabelBADGE_OUT=new JLabel();
        ButtonBADGE_OUT=new JButton();
        panel = new JPanel();
        NewMsg = new JTextField();
        TextBoxCognome=new JTextField();
        LabelCognome=new JLabel();
        ButtonCognome=new JButton();

        TextBoxNome=new JTextField();
        LabelNome=new JLabel();
        ButtonNome=new JButton();

        TextBoxCodice=new JTextField();
        LabelCodice=new JLabel();
        ButtonCodice=new JButton();



        //1°FRAME
        this.setSize(250,250 );
        this.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        panel.setLayout(null);
        this.add(panel);
        //OBJ BUTTON BADGE_OUT
        ButtonBADGE_IN.setBounds(20,125,100, 30);
        panel.add(ButtonBADGE_IN);
        ButtonBADGE_IN.setText("↑ IN ↑");
        //ButtonBADGE_IN.setEnabled(false);
        ButtonBADGE_IN.addActionListener(this);

        //OBJ BUTTON BADGE_OUT
        ButtonBADGE_OUT.setBounds(125,125,100, 30);
        panel.add(ButtonBADGE_OUT);
        ButtonBADGE_OUT.setText("↓ OUT ↓");
        //ButtonBADGE_OUT.setEnabled(false);
        ButtonBADGE_OUT.addActionListener(this);

        /*OBJ's COGNOME*/
        TextBoxCognome.setBounds(70, 15, 100, 30);//(x,y,larghezza,altezza)
        panel.add(TextBoxCognome);
        TextBoxCognome.setText("CognomeText");
        LabelCognome.setBounds(5,15,100,30);//(x,y,larghezza,altezza)
        panel.add(LabelCognome);
        LabelCognome.setText("COGNOME:");

        /*OBJ's NOME*/
        TextBoxNome.setBounds(70, 45, 100, 30);//(x,y,larghezza,altezza)
        panel.add(TextBoxNome);
        TextBoxNome.setText("Nometext");
        LabelNome.setBounds(5,45,100,30);//(x,y,larghezza,altezza)
        panel.add(LabelNome);
        LabelNome.setText("NOME:");

        /*OBJ's CODICE*/

        TextBoxCodice.setBounds(70, 75, 100, 30);//(x,y,larghezza,altezza)
        panel.add(TextBoxCodice);
        TextBoxCodice.setText("CodiceID");
        LabelCodice.setBounds(5,75,100,30);//(x,y,larghezza,altezza)
        panel.add(LabelCodice);
        LabelCodice.setText("CODICE:");



        this.setTitle("Rilevazione Presenze");
        

    }



    @Override
    public void actionPerformed(ActionEvent e) {



        DipendenteJDBCDAO amicoDB=new DipendenteJDBCDAO();
        //UtenteBean amicoClass=new UtenteBean();
        //EventJDBCDAO eventDB=new EventJDBCDAO();

        Time time = new Time();

        int type_event=0;
        if (e.getSource()==ButtonBADGE_IN){

            type_event=1;

            Return_ID_User=amicoDB.SELECT_NameSurname(TextBoxNome.getText(), TextBoxCognome.getText(), TextBoxCodice.getText());


                if (Return_ID_User != 0) {

                    int countevent_IN_OUT = countItem(Server.QUERY_COUNT_ITEM, Return_ID_User, time.getDate());


                    if (countevent_IN_OUT < 2) {

                        addEvent(Server.QUERY_IN_OUT, time.getHour(), time.getDate(), type_event);

                        orderByDate(Server.QUERY_ORDERDATE, Return_ID_User);

                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Dipendente NON inserito",
                                "Numero di INGRESSI superato",
                                JOptionPane.WARNING_MESSAGE);
                    }
                    TextBoxNome.setText(" ");
                    TextBoxCognome.setText(" ");
                    TextBoxCodice.setText(" ");


                } else {
                    //System.out.println("False");
                    //System.out.println(Return_ID_User);
                }

        }

        else if (e.getSource()==ButtonBADGE_OUT){

            type_event = 2;

            if (Return_ID_User!=0) {

                int countevent_IN_OUT = countItem(Server.QUERY_COUNT_ITEM, Return_ID_User, time.getDate());


                if (countevent_IN_OUT < 2) {

                    addEvent(Server.QUERY_IN_OUT, time.getHour(), time.getDate(), type_event);

                    orderByDate(Server.QUERY_ORDERDATE, Return_ID_User);

                } else {
                    System.out.println("Dipendente non INSERITO nel DB");
                    JOptionPane.showMessageDialog(this,
                            "Dipendente NON inserito",
                            "Numero di INGRESSI/USCITE superato",
                            JOptionPane.WARNING_MESSAGE);}
                    TextBoxNome.setText(" ");
                    TextBoxCognome.setText(" ");
                    TextBoxCodice.setText(" ");



            }else {
                System.out.println("Dipendente non INSERITO nel DB");}
        }

    }


    public static void main(String[] args) throws UnknownHostException,
            IOException {
        UtenteGUI user=new UtenteGUI();
        FxTableUser.main(args);
        user.setVisible(true);
//Orologio.main(args);

        user.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       /* AmicoJDBCDAO amicoDB=new AmicoJDBCDAO();
        AmicoBean amicoClass=new AmicoBean();
        Scanner sc = new Scanner(System.in);

        System.out.print("Premi invio per continuare. ");
        sc.nextLine();
        amicoClass.setCognome(user.TextBoxCognome.getText());
        amicoClass.setNome(user.TextBoxNome.getText());
        amicoClass.setCodice(user.TextBoxCodice.getText());
        //EMAIL mancante

        amicoDB.add(amicoClass);

        amicoDB.findAll();*/





    }

    public void addEvent(String type_query, String hour, String date, int type_event){

        String req= type_query +"\n" + Return_ID_User + "\n" + hour + "\n"+ date+ "\n"+ type_event + "\n";

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

    public int countItem(String type_query, int id, String date){

        int count = 0;

        String req = type_query +"\n" + id + "\n" + date+ "\n";
        try {
            Socket s = new Socket(Server.HOST, Server.PORT);

            PrintWriter out =new PrintWriter(s.getOutputStream(),true);
            BufferedReader in= new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(req);
            String line=in.readLine();

            count = Integer.parseInt(line);


            s.close();


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

}



