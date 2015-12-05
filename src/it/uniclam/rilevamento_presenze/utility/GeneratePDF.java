package it.uniclam.rilevamento_presenze.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.*;
import java.util.Date;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.simpleparser.TableWrapper;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import it.uniclam.rilevamento_presenze.connections.ConnectionDB;
import it.uniclam.rilevamento_presenze.connections.Server;
import it.uniclam.rilevamento_presenze.jdbcdao.DipendenteJDBCDAO;

import javax.swing.*;


public class GeneratePDF {


    public GeneratePDF() {}

    private static Connection getConnection() throws SQLException {
        Connection conn;
        conn = ConnectionDB.getInstance().getConnection();
        return conn;
    }

	public static void main(String[] args) {
        try {
			OutputStream file = new FileOutputStream(new File("C:\\Test.pdf"));

			Document document = new Document();
			PdfWriter.getInstance(document, file);

			document.open();
			document.add(new Paragraph("Report generato in automatico:"));
			document.add(new Paragraph(new Date().toString()));
            PdfPTable table = new PdfPTable(2);
            PdfPCell cell = new PdfPCell(new Paragraph("Nome                                                      Cognome"));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.GREEN);
            table.addCell(cell);

            /*******************************************/
            Connection connection = null;
            PreparedStatement ptmt= null;
            ResultSet resultSet = null;


            try {
                String queryString = "SELECT * FROM user  ";
                connection = getConnection();

                Statement st = connection.createStatement();
                ResultSet res = st.executeQuery(queryString);
          /* if (res.next()!=false){
                System.out.println("ACCESSO VALIDO");
*/
                while (res.next()==true) {

                    table.addCell(res.getString("Nome"));
                    table.addCell(res.getString("Cognome"));


                   }document.add(table);
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

        /**///////////////////////////////////*/

           // System.out.println(lj.genPDF(Server.QUERY_CREATE_PDF));


           // document.add(table);

            JOptionPane.showMessageDialog(null, "Report Salvato Con Successo!");
			document.close();
			file.close();

		} catch (Exception e) {
           // System.out.println("Impossibile accedere al file. Il file è utilizzato da un altro processo");
            JOptionPane.showMessageDialog(null, "Impossibile creare il file. Il documento è utilizzato da un altro processo!");
			//e.printStackTrace();
		}
	}
}