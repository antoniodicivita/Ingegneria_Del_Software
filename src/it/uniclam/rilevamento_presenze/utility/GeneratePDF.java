package it.uniclam.rilevamento_presenze.utility;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import it.uniclam.rilevamento_presenze.connections.Server;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * La Classe GeneratePDF
 * 1. Si occupa della creazione del report in formato pdf
 * 2. Interagisce con FxTableUser e Server
 */

public class GeneratePDF {


    public GeneratePDF() {
    }


    public static void main(String[] args) {
        try {

            //Creazione documento
            OutputStream file = new FileOutputStream(new File("C:\\Users\\Antonio Di Civita\\Documents\\Test.pdf"));
			Document document = new Document();
			PdfWriter.getInstance(document, file);

			document.open();
			document.add(new Paragraph("Report generato in automatico:"));
			document.add(new Paragraph(new Date().toString()));
            PdfPTable table = new PdfPTable(5);
            PdfPCell cell = new PdfPCell(new Paragraph("Nome          Cognome            Evento             Data           Ora"));

            cell.setColspan(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.GREEN);
            table.addCell(cell);

            /*******************************************/


        /**///////////////////////////////////*/


            //Riempimento del documento
            GregorianCalendar gcalendar = new GregorianCalendar();
            int month = gcalendar.get(Calendar.MONTH) + 1;
            int year = gcalendar.get(Calendar.YEAR);

            createReport(Server.QUERY_CREATE_MONTH_REPORT, month, year, table);
            document.add(table);

            JOptionPane.showMessageDialog(null, "Report Salvato Con Successo!");
			document.close();
			file.close();

		} catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Impossibile creare il file. Il documento è utilizzato da un altro processo!");

        }
    }


    /**
     * Questo metodo si occupa di creare la tabella da inserire nel documento per il report
     *
     * @param type_query
     * @param month
     * @param year
     * @param table
     */
    public static void createReport(String type_query, int month, int year, PdfPTable table) {


        String req = type_query + "\n" + month + "\n" + year + "\n";
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
                    String hour = in.readLine();


                    table.addCell(name);
                    table.addCell(surname);
                    table.addCell(inOut);
                    table.addCell(date);
                    table.addCell(hour);
            }
        }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}