package it.uniclam.rilevamento_presenze.gui;

import it.uniclam.rilevamento_presenze.connections.ConnectionDB;
//import it.uniclam.rilevamento_presenze.JDBCDataAccessObject.UserJDBCDAO;

//import it.uniclam.rilevamento_presenze.test.test.User;

import it.uniclam.rilevamento_presenze.connections.Server;
import it.uniclam.rilevamento_presenze.beanclass.Dipendente;
import it.uniclam.rilevamento_presenze.jdbcdao.DipendenteJDBCDAO;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TextField;
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

//import it.uniclam.rilevamento_presenze.JDBCDataAccessObject.DynamicJDBCDAO;

public class FxTableUser
		extends Application {

    /*CONNE DB*/


    public TextField TextboxSearch;
    public TextField TextboxInsertPK;

    public HBox buttonHb,buttonHbTWO;



    private Connection getConnection() throws SQLException {
        Connection conn;
        conn = ConnectionDB.getInstance().getConnection();
        return conn;
    }
    /*FINE Conn DB*/

    public FxTableUser(){}
    private TableView<Dipendente> table;
	private ObservableList<Dipendente> data;
	private Text actionStatus;

	public static void main(String [] args) {

		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		primaryStage.setTitle("Pannello di Controllo:Responsabile Presenze");

		// Users label
		Label label = new Label("Tabella Dipendenti");
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
		TableColumn titleCol = new TableColumn("Cognome");
		titleCol.setCellValueFactory(new PropertyValueFactory<Dipendente, String>("Cognome"));
		titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
		titleCol.setOnEditCommit(new EventHandler<CellEditEvent<Dipendente, String>>() {
			@Override
			public void handle(CellEditEvent<Dipendente, String> t) {

				((Dipendente) t.getTableView().getItems().get(
					t.getTablePosition().getRow())
				).setCognome(t.getNewValue());
			}
		});

		final TableColumn authorCol = new TableColumn("Nome");
		authorCol.setCellValueFactory(new PropertyValueFactory<Dipendente, String>("Nome"));
		authorCol.setCellFactory(TextFieldTableCell.forTableColumn());
		authorCol.setOnEditCommit(new EventHandler<CellEditEvent<Dipendente, String>>() {
			@Override
			public void handle(CellEditEvent<Dipendente, String> t) {
	DipendenteJDBCDAO lj=new DipendenteJDBCDAO();
            ((Dipendente) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
            ).setNome(t.getNewValue());

                //Aggiorno il valore nel database INIZIO:
                int ix = table.getSelectionModel().getSelectedIndex();
                Dipendente dipendente = (Dipendente) table.getSelectionModel().getSelectedItem();
                System.out.println(ix);



                String nome=table.getItems().get(ix).getNome().toString();
                String cognome=table.getItems().get(ix).getCognome().toString();
               // System.out.println(autore+"tralalal");
                //update
                lj.updateList(Server.QUERY_UPDATE_LIST, nome, cognome);
                //Aggiorno il valore nel database FINE:

                System.out.println("Ho modificato il valore di autori");
			}
		});

		table.getColumns().setAll(titleCol, authorCol);
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


		buttonHb = new HBox(10);
		buttonHb.setAlignment(Pos.CENTER);
		buttonHb.getChildren().addAll(addbtn, delbtn,srcbtn,detailsbtn);

        buttonHbTWO = new HBox(10);
        buttonHbTWO.setAlignment(Pos.CENTER);
        buttonHbTWO.getChildren().addAll(okbtn,backbtn);
        buttonHbTWO.setVisible(false);

        //Textbox

        TextboxSearch=new TextField();
TextboxInsertPK=new TextField();
        TextboxInsertPK.setVisible(false);
		// Status message text
		actionStatus = new Text();
		actionStatus.setFill(Color.FIREBRICK);

		// Vbox
		VBox vbox = new VBox(20);
		vbox.setPadding(new Insets(25, 25, 25, 25));;
		vbox.getChildren().addAll(labelHb, table, buttonHb,buttonHbTWO,TextboxSearch,TextboxInsertPK,actionStatus);

		// Scene
		Scene scene = new Scene(vbox, 500, 550); // w x h
		primaryStage.setScene(scene);
		primaryStage.show();

		// Select the first row
		table.getSelectionModel().select(0);
		Dipendente dipendente = table.getSelectionModel().getSelectedItem();
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

			Dipendente dipendente = data.get(ix);


           actionStatus.setText(dipendente.toString());

            System.out.println(dipendente.toString()+"toSTRING");
		}
	}

	//(SELECT)Lettura di tutti i dipendenti dalla tabellaOK
	private ObservableList<Dipendente> getInitialTableData() {
        DipendenteJDBCDAO lj=new DipendenteJDBCDAO();

        System.out.println("Sono passato da fuori");

        return lj.SELECT_List(Server.QUERY_SELECT_ALL_LIST);
	}
	
//Aggiunta di un Dipendente alla tabella OK
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

            DipendenteJDBCDAO lj=new DipendenteJDBCDAO();
            Dipendente dipendente = dipendente =new Dipendente("",TextboxInsertPK.getText());
            data.add(dipendente);

            int row = data.size() - 1;
            System.out.println(data.size());
            // Select the new row
            table.requestFocus();
            table.getSelectionModel().select(row);
            table.getFocusModel().focus(row);
            lj.addList(Server.QUERY_ADD_LIST,TextboxInsertPK.getText());

            //actionStatus.setText("Nuovo Dipendente: Inserisci nome e cognome. Prmere <Enter>.");
            buttonHbTWO.setVisible(false);
            buttonHb.setVisible(true);
            TextboxInsertPK.setVisible(false);
            TextboxSearch.setVisible(true);


        }
    }

    //(DELETE)Cancellazione elementi dalla tabella e dal DB
	private class DeleteButtonListener implements EventHandler<ActionEvent> {
DipendenteJDBCDAO lj=new DipendenteJDBCDAO();
		@Override
		public void handle(ActionEvent e) {

			// Get selected row and delete
			int ix = table.getSelectionModel().getSelectedIndex();
			Dipendente dipendente = (Dipendente) table.getSelectionModel().getSelectedItem();
            System.out.println(table.getItems().get(ix).toString());

            //System.out.println(table.getItems().get(ix).getTitle().toString());//Titolo X selezionato OK
            //System.out.println(table.getItems().get(ix).getAuthor().toString());//Autore X selezionato OK
            String titolo=table.getItems().get(ix).getNome().toString();
            String autore=table.getItems().get(ix).getCognome().toString();
            data.remove(ix);//Elimino l'elemento dalla tabella e la aggiorno
            lj.removeList(Server.QUERY_REMOVE_LIST,titolo,autore);
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

        DipendenteJDBCDAO lj=new DipendenteJDBCDAO();
        @Override
        public void handle(ActionEvent e) {


//Elimino tutti gli elementi dalla tabella ( NON dal database)
            /*************************/
            table.getItems().clear();
            String value=TextboxSearch.getText();

            lj.searchList(Server.QUERY_SEARCH_LIST,value,data);



/*************************/
        }//Fine Handle()
    }//Fine search listner button

    private class DetailshButtonListener implements EventHandler<ActionEvent> {
DipendenteJDBCDAO lj=new DipendenteJDBCDAO();
        @Override
        public void handle(ActionEvent e) {


// Get selected row and delete
            int ix = table.getSelectionModel().getSelectedIndex();
            Dipendente dipendente = (Dipendente) table.getSelectionModel().getSelectedItem();
            System.out.println(table.getItems().get(ix).toString());

            //System.out.println(table.getItems().get(ix).getTitle().toString());//Titolo X selezionato OK
            //System.out.println(table.getItems().get(ix).getAuthor().toString());//Autore X selezionato OK
            String nome=table.getItems().get(ix).getNome().toString();
            String cognome=table.getItems().get(ix).getCognome().toString();

           lj.MySQL_GridView(Server.QUERY_ORDERDATE,nome, cognome);


            actionStatus.setText("Dipendente: " + dipendente.toString());

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



  /*  private ObservableList<User> RefreshData() {

        List<User> list = new ArrayList<>();
        /*
        try {
            String queryString = "SELECT * FROM libri";
            connection = getConnection();

            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery(queryString);
            System.out.println("Prepare sctatement OK");
            while (res.next()) {

                String title = res.getString("title");
                String author = res.getString("author");

                list.add(new User(title, author));

                System.out.println(title + "\t" + author +" Refresh dei dati");
            }
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
        ObservableList<User> data = FXCollections.observableList(list);
 return data;
    }*/






}

