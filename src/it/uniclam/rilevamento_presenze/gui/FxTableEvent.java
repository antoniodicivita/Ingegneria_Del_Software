package it.uniclam.rilevamento_presenze.gui;

import it.uniclam.rilevamento_presenze.beanclass.Event;
import it.uniclam.rilevamento_presenze.connections.ConnectionDB;
import it.uniclam.rilevamento_presenze.connections.Server;
import it.uniclam.rilevamento_presenze.jdbcdao.EventJDBCDAO;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class FxTableEvent
		extends Application {

    /*CONNE DB*/


    public TextField TextboxSearch;
    public Label LabelSearch;
    public TextField TextboxInsertPK;

    public HBox buttonHb,buttonHbONE,buttonHbTWO;



    private Connection getConnection() throws SQLException {
        Connection conn;
        conn = ConnectionDB.getInstance().getConnection();
        return conn;
    }
    /*FINE Conn DB*/

    public FxTableEvent(){}
    private TableView<Event> table;
	private ObservableList<Event> data;
	private Text actionStatus;

	public static void main(String [] args) {

		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		primaryStage.setTitle("Pannello di Controllo:Responsabile Presenze");

		// Users label
		Label label = new Label("Tabella Dipendenti");
        LabelSearch=new Label("CERCA");
        LabelSearch.setTextFill(Color.YELLOWGREEN);
		label.setTextFill(Color.DARKBLUE);
		label.setFont(Font.font("Calibri", FontWeight.BOLD, 36));
		HBox labelHb = new HBox();
		labelHb.setAlignment(Pos.CENTER);
		labelHb.getChildren().add(label);

		// Table view, data, columns and properties

		table = new TableView<>();
		data = getInitialTableData();
		table.setItems(data);
		table.setEditable(true);
//Vedere QUI
		TableColumn hourCol = new TableColumn("Hour");
		hourCol.setCellValueFactory(new PropertyValueFactory<Event, String>("Hour"));
		hourCol.setCellFactory(TextFieldTableCell.forTableColumn());
		hourCol.setOnEditCommit(new EventHandler<CellEditEvent<Event, String>>() {
			@Override
			public void handle(CellEditEvent<Event, String> t) {

				((Event) t.getTableView().getItems().get(
					t.getTablePosition().getRow())
				).setHour(t.getNewValue());
			}
		});

		final TableColumn dataCol = new TableColumn("Data");
		dataCol.setCellValueFactory(new PropertyValueFactory<Event, String>("Data"));
		dataCol.setCellFactory(TextFieldTableCell.forTableColumn());
		dataCol.setOnEditCommit(new EventHandler<CellEditEvent<Event, String>>() {
			@Override
			public void handle(CellEditEvent<Event, String> t) {
	EventJDBCDAO lj=new EventJDBCDAO();
            ((Event) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
            ).setData(t.getNewValue());

                //Aggiorno il valore nel database INIZIO:
                int ix = table.getSelectionModel().getSelectedIndex();
                Event dipendente = (Event) table.getSelectionModel().getSelectedItem();
                System.out.println(ix);



                String nome=table.getItems().get(ix).getHour().toString();
                String cognome=table.getItems().get(ix).getData().toString();
               // System.out.println(autore+"tralalal");
                //update
                //QUERYlj.updateList(Server.QUERY_UPDATE_LIST, nome, cognome);
                //Aggiorno il valore nel database FINE:

                System.out.println("Ho modificato il valore di autori");
			}
		});

		table.getColumns().setAll(hourCol, dataCol);
		table.setPrefWidth(450);
		table.setPrefHeight(300);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		table.getSelectionModel().selectedIndexProperty().addListener(
			new RowSelectChangeListener());

		// Add and delete buttons
		Button addbtn = new Button("Aggiungi");
		addbtn.setOnAction(new AddButtonListener());
		Button delbtn = new Button("Elimina");
		delbtn.setOnAction(new DeleteButtonListener());
        Button srcbtn=new Button("Trova");
        srcbtn.setOnAction(new SearchButtonListener());
        Button okbtn=new Button("OK");
        okbtn.setOnAction(new OKButtonListener());
        Button detailsbtn=new Button("Dettagli");
        detailsbtn.setOnAction(new DetailshButtonListener());
        Button backbtn=new Button("Annulla");
        backbtn.setOnAction(new SearchButtonListener());

        //Textbox

        TextboxSearch=new TextField();
        TextboxInsertPK=new TextField();
        TextboxInsertPK.setVisible(false);



		buttonHb = new HBox(10);
		buttonHb.setAlignment(Pos.CENTER);
		buttonHb.getChildren().addAll(addbtn, delbtn,detailsbtn);

        buttonHbONE = new HBox(10);
        buttonHbONE.setAlignment(Pos.CENTER);
        buttonHbONE.getChildren().addAll(LabelSearch, TextboxSearch,srcbtn);


        buttonHbTWO = new HBox(10);
        buttonHbTWO.setAlignment(Pos.CENTER);
        buttonHbTWO.getChildren().addAll(okbtn,backbtn);
        buttonHbTWO.setVisible(false);

        // Status message text
        actionStatus = new Text();
        actionStatus.setFill(Color.FIREBRICK);

		// Vbox
		VBox vbox = new VBox(20);
		vbox.setPadding(new Insets(25, 25, 25, 25));;
		vbox.getChildren().addAll(labelHb,buttonHbONE,table, buttonHb,buttonHbTWO,TextboxInsertPK,actionStatus);

		// Scene
		Scene scene = new Scene(vbox, 500, 550); // w x h
		primaryStage.setScene(scene);
		primaryStage.show();

		// Select the first row
		table.getSelectionModel().select(0);
		Event dipendente = table.getSelectionModel().getSelectedItem();
		actionStatus.setText(dipendente.toString());
		
	} // start()
	
 	private class RowSelectChangeListener implements ChangeListener<Number> {

		@Override
		public void changed(ObservableValue<? extends Number> ov, 
				Number oldVal, Number newVal) {

			int ix = newVal.intValue();

			if ((ix < 0) || (ix >= data.size())) {
	
				return; // invalid data
			}

			Event dipendente = data.get(ix);


           actionStatus.setText(dipendente.toString());

            System.out.println(dipendente.toString()+"toSTRING");
		}
	}

	//(SELECT)Lettura di tutti i dipendenti dalla tabellaOK
	private ObservableList<Event> getInitialTableData() {
        Connection connection = null;
        PreparedStatement ptmt= null;
        ResultSet resultSet = null;
        EventJDBCDAO lj=new EventJDBCDAO();
        List list = new ArrayList();

        System.out.println("Sono passato da fuori");

        try {
            String queryString = "SELECT * FROM event  ";
            connection = getConnection();

            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery(queryString);

            while (res.next()==true) {

String ora=res.getString("Hour");
                String data= res.getString("Data");
                list.add(new Event(ora, data));

            }
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
        ObservableList data = FXCollections.observableList(list);

        return data;
    }
	
//Aggiunta di un Event alla tabella OK
    private class AddButtonListener implements EventHandler<ActionEvent> {
        //DynamicJDBCDAO dJD=new DynamicJDBCDAO();
		@Override
		public void handle(ActionEvent e) {

            System.out.println(data.size());/*dJD.addEXT("ris",""),dJD.addEXT("","Bgrown")*/
            buttonHb.setVisible(false);
            buttonHbTWO.setVisible(true);
            TextboxSearch.setVisible(false);
            TextboxInsertPK.setVisible(true);
        }
	}

    //Confirm Operation Button
    private class OKButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            EventJDBCDAO lj=new EventJDBCDAO();
            Event dipendente = dipendente =new Event("",TextboxInsertPK.getText());
            data.add(dipendente);

            int row = data.size() - 1;
            System.out.println(data.size());
            // Select the new row
            table.requestFocus();
            table.getSelectionModel().select(row);
            table.getFocusModel().focus(row);
            lj.addList(Server.QUERY_ADD_LIST,TextboxInsertPK.getText());

            //actionStatus.setText("Nuovo Event: Inserisci nome e cognome. Prmere <Enter>.");
            buttonHbTWO.setVisible(false);
            buttonHb.setVisible(true);
            TextboxInsertPK.setVisible(false);
            TextboxSearch.setVisible(true);


        }
    }

    //(DELETE)Cancellazione elementi dalla tabella e dal DB
	private class DeleteButtonListener implements EventHandler<ActionEvent> {
EventJDBCDAO lj=new EventJDBCDAO();
		@Override
		public void handle(ActionEvent e) {

			// Get selected row and delete
			int ix = table.getSelectionModel().getSelectedIndex();
			Event dipendente = (Event) table.getSelectionModel().getSelectedItem();
            System.out.println(table.getItems().get(ix).toString());

            //System.out.println(table.getItems().get(ix).getTitle().toString());//Titolo X selezionato OK
            //System.out.println(table.getItems().get(ix).getAuthor().toString());//Autore X selezionato OK
            String titolo=table.getItems().get(ix).getHour().toString();
            String autore=table.getItems().get(ix).getData().toString();
            data.remove(ix);//Elimino l'elemento dalla tabella e la aggiorno
           // lj.removeList(Server.QUERY_REMOVE_LIST,titolo,autore);
            actionStatus.setText("Cancellato: " + dipendente.toString());

			// Select a row

			if (table.getItems().size() == 0) {

				actionStatus.setText("Nessun record Trovato !");
				return;
			}

			if (ix != 0) {

				ix = ix -1;
			}

			table.requestFocus();
			table.getSelectionModel().select(ix);
			table.getFocusModel().focus(ix);
		}
	}

    //(SEARCH)Cancellazione elementi dalla tabella e dal DB OK
    private class SearchButtonListener implements EventHandler<ActionEvent> {

        EventJDBCDAO lj=new EventJDBCDAO();
        @Override
        public void handle(ActionEvent e) {


//Elimino tutti gli elementi dalla tabella ( NON dal database)
            /*************************/
            table.getItems().clear();
            String value=TextboxSearch.getText();

          //  lj.searchList(Server.QUERY_SEARCH_LIST,value,data);



/*************************/
        }//Fine Handle()
    }//Fine search listner button

    private class DetailshButtonListener implements EventHandler<ActionEvent> {
EventJDBCDAO lj=new EventJDBCDAO();
        @Override
        public void handle(ActionEvent e) {


// Get selected row and delete
            int ix = table.getSelectionModel().getSelectedIndex();
            Event dipendente = (Event) table.getSelectionModel().getSelectedItem();
            System.out.println(table.getItems().get(ix).toString());

            //System.out.println(table.getItems().get(ix).getTitle().toString());//Titolo X selezionato OK
            //System.out.println(table.getItems().get(ix).getAuthor().toString());//Autore X selezionato OK
         /*   String nome=table.getItems().get(ix).getNome().toString();
            String cognome=table.getItems().get(ix).getCognome().toString();
*/
         //  lj.MySQL_GridView(Server.QUERY_ORDERDATE,nome, cognome);


            actionStatus.setText("Event: " + dipendente.toString());

            // Select a row

            if (table.getItems().size() == 0) {

                actionStatus.setText("Nessun record Trovato !");
                return;
            }

            if (ix != 0) {

                ix =ix;
            }

            table.requestFocus();
            table.getSelectionModel().select(ix);
            table.getFocusModel().focus(ix);
        
/*************************/
        }//Fine Handle()
    }//Fine search listner button


}

