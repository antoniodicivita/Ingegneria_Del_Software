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
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.color.ColorSpace;
import java.io.IOException;
import java.sql.*;
import java.util.*;


public class FxTableEvent
		extends Application {

    /*CONNE DB*/

    private Stage primaryStage;
    private BorderPane rootLayout;
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

    public void initRootLayout() {
        // Load root layout from fxml file.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FxTableEvent.class.getResource("TESTaddress/view/RootLayout.fxml"));
        // rootLayout = (BorderPane) loader.load();

        // Show the scene containing the root layout.
       /* Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();*/
    }

	@Override
	public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
		primaryStage.setTitle("Pannello di Controllo:Responsabile Presenze");
       // initRootLayout();
		// Users label
		Label label = new Label("INCONGRUENZE");
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
		TableColumn hourCol = new TableColumn("Ora");
		hourCol.setCellValueFactory(new PropertyValueFactory<Event, String>("hour"));
		hourCol.setCellFactory(TextFieldTableCell.forTableColumn());
		hourCol.setOnEditCommit(new EventHandler<CellEditEvent<Event, String>>() {
			@Override
			public void handle(CellEditEvent<Event, String> t) {

				((Event) t.getTableView().getItems().get(
					t.getTablePosition().getRow())
				).setHour(t.getNewValue());
			}
		});

        TableColumn dataCol = new TableColumn("Data");
        dataCol.setCellValueFactory(new PropertyValueFactory<Event, String>("data"));
        dataCol.setCellFactory(TextFieldTableCell.forTableColumn());
        dataCol.setOnEditCommit(new EventHandler<CellEditEvent<Event, String>>() {
            @Override
            public void handle(CellEditEvent<Event, String> t) {

                ((Event) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setData(t.getNewValue());
            }
        });

        TableColumn typeCol = new TableColumn("IN/OUT");
        typeCol.setCellValueFactory(new PropertyValueFactory<Event, String>("type_id"));
        typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        typeCol.setOnEditCommit(new EventHandler<CellEditEvent<Event, String>>() {
            @Override
            public void handle(CellEditEvent<Event, String> t) {

                ((Event) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setType_id(t.getNewValue());
            }
        });




        final TableColumn userCol = new TableColumn("ID");
        userCol.setCellValueFactory(new PropertyValueFactory<Event, String>("user_id"));
        userCol.setCellFactory(TextFieldTableCell.forTableColumn());
        userCol.setOnEditCommit(new EventHandler<CellEditEvent<Event, String>>() {
            @Override
            public void handle(CellEditEvent<Event, String> t) {

                ((Event) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setUser_id((t.getNewValue()));

                //Aggiorno il valore nel database INIZIO:
                int ix = table.getSelectionModel().getSelectedIndex();
                Event dipendente = (Event) table.getSelectionModel().getSelectedItem();
                System.out.println(ix);



                String nome=table.getItems().get(ix).getHour().toString();
                String cognome=table.getItems().get(ix).getData().toString();
                        String user_id=table.getItems().get(ix).getUser_id().toString();
                String type_id=table.getItems().get(ix).getType_id().toString();
               // System.out.println(autore+"tralalal");
                //update
                //QUERYlj.updateList(Server.QUERY_UPDATE_LIST, nome, cognome);
                //Aggiorno il valore nel database FINE:

                System.out.println("Ho modificato il valore di autori");
			}
		});
//Dico le colonne quante sono
		table.getColumns().setAll(userCol,dataCol,hourCol,typeCol);
		table.setPrefWidth(450);
		table.setPrefHeight(300);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		table.getSelectionModel().selectedIndexProperty().addListener(
			new RowSelectChangeListener());


        /********************************/



        /********************************/

		// Add and delete buttons
		Button addbtn = new Button("Aggiungi");
		addbtn.setOnAction(new AddButtonListener());
		Button delbtn = new Button("Elimina");
		delbtn.setOnAction(new DeleteButtonListener());
        Button srcbtn=new Button("Trova");
        srcbtn.setOnAction(new SearchButtonListener());
        Button okbtn=new Button("OK");
        okbtn.setOnAction(new OKButtonListener());
        Button detailsbtn=new Button("Vedi Incongruenza/e");
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

		// Seleziona la prima riga
		table.getSelectionModel().select(0);

		Event dipendente = table.getSelectionModel().getSelectedItem();
		actionStatus.setText(dipendente.toString());
		
	} // start()
	
 	private class RowSelectChangeListener implements ChangeListener<Number> {

		@Override
		public void changed(ObservableValue<? extends Number> ov, 
				Number oldVal, Number newVal) {
            table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            table.requestFocus();
			int ix = newVal.intValue();

			if ((ix < 0) || (ix >= data.size())) {return;}// invalid data

if (ix < data.size()) {

    if (!table.getSelectionModel().isEmpty()){

        call();
        //table.getSelectionModel().clearSelection(0);NI


    }
    else {call();}
}

            /****/
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
            String queryString = "SELECT event.Data, Nome, Cognome, Name_Type FROM (event  JOIN user ON User_ID=ID_User) JOIN type ON Type_ID=ID_Type  ORDER BY STR_TO_DATE(Data, '%d%b%Y') DESC, User_ID";
            connection = getConnection();

            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery(queryString);

            while (res.next()==true) {

                String ora=res.getString("Nome");
                String data= res.getString("Data");
               String user_id=res.getString("Cognome");
                //Integer user_id=res.getInt(4);
                String type_id= res.getString("Name_Type");
                list.add(new Event(user_id,data,ora,type_id));



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
           // Event dipendente = dipendente =new Event("",TextboxInsertPK.getText(),1);
           // data.add(dipendente);

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

    public class DetailshButtonListener implements EventHandler<ActionEvent> {
EventJDBCDAO lj=new EventJDBCDAO();
        @Override
        public void handle(ActionEvent e) {
            String stringa = table.getItems().get(4).getUser_id().toString();

call();
/*************************/
        }//Fine Handle()
    }//Fine search listner button




    public void call(){

        int lenght = table.getItems().size();//Numero elementi nella tabella
        int i=0;
        while (i<lenght-1) {
            if (Objects.equals(table.getItems().get(i).getUser_id().toString(), table.getItems().get(i+1).getUser_id().toString()) &&
                    Objects.equals(table.getItems().get(i).getData().toString(), table.getItems().get(i+1).getData().toString()) &&
                    Objects.equals(table.getItems().get(i).getType_id().toString(), table.getItems().get(i+1).getType_id().toString())) {
                table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                table.requestFocus();

                table.getSelectionModel().select(data.get(i));
                table.getSelectionModel().select(data.get(i+1));

                table.getSelectionModel().focus(4);
                //table.getItems().
                //  System.out.println("I:"+i+" J:"+ j +"DataA: "+table.getItems().get(i).getUser_id().toString() +"DataB: "+table.getItems().get(i).getData().toString()+" + "+ table.getItems().get(j).getUser_id().toString()+" "+table.getItems().get(j).getData().toString());
            }
            i++;
        }

    }



}

