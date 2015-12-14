package it.uniclam.rilevamento_presenze.gui;

import it.uniclam.rilevamento_presenze.beanclass.Dipendente;
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
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.awt.color.ColorSpace;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;


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

    public FxTableEvent(){
        this.dataInizialeTextBox = new DatePicker();
        this.dataFinaleTextBox = new DatePicker();
    }
    private TableView<Event> table;
	private ObservableList<Event> data;
	private Text actionStatus;
    public final String pattern = "yyyy-MM-dd";
    public String datainiziale = "";
    public String datafinale = "";
    public DatePicker dataInizialeTextBox;
    public DatePicker dataFinaleTextBox;
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



        TableColumn hourCol = new TableColumn("NOME");
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


        TableColumn typeCol = new TableColumn("IN/OUT");
        typeCol.setCellValueFactory(new PropertyValueFactory<Event, String>("type_id"));
        typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        typeCol.setOnEditCommit(new EventHandler<CellEditEvent<Event, String>>() {
            @Override
            public void handle(CellEditEvent<Event, String> t) {

                EventJDBCDAO lj = new EventJDBCDAO();
                        ((Event) t.getTableView().getItems().get(
                                        t.getTablePosition().getRow())
                        ).setEvent_id(t.getNewValue());

                //Aggiorno il valore nel database INIZIO:
                int ix = table.getSelectionModel().getFocusedIndex();
                Event event = (Event) table.getSelectionModel().getSelectedItem();
                System.out.println(ix);



                //String nome=table.getItems().get(ix).getNome().toString();
                String name_type=table.getItems().get(ix).getType_id().toString();
               // String name_type=table.getSelectionModel().ge;
                System.out.println(name_type);
                if(Objects.equals(name_type,"INGRESSO")){name_type="1";}
                else {name_type="2";}
                String id_event=table.getSelectionModel().getSelectedItem().getEvent_id();

                // System.out.println(autore+"tralalal");
                //update
                // lj.updateList(Server.QUERY_UPDATE_LIST, nome, cognome);
                //Aggiorno il valore nel database FINE:
                lj.update(Server.QUERY_UPDATE_LIST, name_type,id_event);
                System.out.println("Ho modificato il valore di COL 2");
            }
        });




         TableColumn userCol = new TableColumn("COGNOME");
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


                String nome = table.getItems().get(ix).getHour().toString();
                String cognome = table.getItems().get(ix).getData().toString();
                String user_id = table.getItems().get(ix).getUser_id().toString();
                String type_id = table.getItems().get(ix).getType_id().toString();
                // System.out.println(autore+"tralalal");
                //update
                //QUERYlj.updateList(Server.QUERY_UPDATE_LIST, nome, cognome);
                //Aggiorno il valore nel database FINE:

                System.out.println("Ho modificato il valore di autori");
            }
        });

        final TableColumn event_idCol = new TableColumn("ID");
        event_idCol.setCellValueFactory(new PropertyValueFactory<Dipendente, String>("event_id"));
        event_idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        event_idCol.setOnEditCommit(new EventHandler<CellEditEvent<Event, String>>() {
            @Override

            public void handle(CellEditEvent<Event, String> t) {

                EventJDBCDAO lj=new EventJDBCDAO();
                ((Event) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setEvent_id(t.getNewValue());

                //Modifica dei campi quando faccio invio (COLONNA 1)
                int ix = table.getSelectionModel().getSelectedIndex();
                Event event = (Event) table.getSelectionModel().getSelectedItem();
                System.out.println(ix);
                /*String nome=table.getItems().get(ix).getNome().toString();
                String cognome=table.getItems().get(ix).getCognome().toString();
                String id_employee=table.getItems().get(ix).getId_employee().toString();
                lj.update(Server.QUERY_UPDATE_LIST,cognome, nome, id_employee);
                System.out.println("Ho modificato il valore di COL 1");*/


            }
        });


//Dico le colonne quante sono
		table.getColumns().setAll(userCol, hourCol,dataCol, typeCol,event_idCol);
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


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        //DatePicker dataInizialeTextBox = new DatePicker();

        GridPane gridPane = new GridPane();

        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label dataIniziale = new Label("Data iniziale");
        gridPane.add(dataIniziale, 0, 0);

        GridPane.setHalignment(dataIniziale, HPos.CENTER);
        gridPane.add(dataInizialeTextBox, 0,1);

        setCalendar(dataInizialeTextBox);


//        DatePicker dataFinaleTextBox = new DatePicker();

        Label dataFinale = new Label("Data Finale");
        gridPane.add(dataFinale, 10,0);
        //gridPane2.setGridLinesVisible(true);
        GridPane.setHalignment(dataFinale, HPos.CENTER);
        gridPane.add(dataFinaleTextBox, 10, 1);

        setCalendar(dataFinaleTextBox);

        datainiziale = dataInizialeTextBox.getAccessibleText();
        datafinale = dataFinaleTextBox.toString();


        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        //Textbox

        TextboxSearch=new TextField();
        TextboxInsertPK=new TextField();
        TextboxInsertPK.setVisible(false);



		buttonHb = new HBox(10);
		buttonHb.setAlignment(Pos.CENTER);
		buttonHb.getChildren().addAll(addbtn, delbtn, detailsbtn);

        buttonHbONE = new HBox(10);
        buttonHbONE.setAlignment(Pos.CENTER);
       // buttonHbONE.getChildren().addAll(LabelSearch, TextboxSearch, srcbtn);
        buttonHbONE.getChildren().addAll(srcbtn);

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
		//vbox.getChildren().addAll(labelHb,buttonHbONE,table, buttonHb,buttonHbTWO,TextboxInsertPK,actionStatus,gridPane);
        vbox.getChildren().addAll(labelHb,gridPane,buttonHbONE,table, buttonHb,buttonHbTWO,TextboxInsertPK,actionStatus);
		// Scene
		Scene scene = new Scene(vbox, 500, 550); // w x h
        table.getStylesheets().add(getClass().getResource("table.css").toExternalForm());
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

    if (!table.getSelectionModel().isEmpty()){EventJDBCDAO.call(table, data); }
    else {EventJDBCDAO.call(table, data);}
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
            String queryString = "SELECT event.Data, Nome, Cognome, Name_Type,ID_Event FROM (event  JOIN user ON User_ID=ID_User) JOIN type ON Type_ID=ID_Type  ORDER BY STR_TO_DATE(Data, '%d%b%Y') DESC, User_ID";
            connection = getConnection();

            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery(queryString);

            while (res.next()==true) {

                String ora=res.getString("Nome");
                String data= res.getString("Data");
                String user_id=res.getString("Cognome");
                String type_id= res.getString("Name_Type");
                String event_id=res.getString("ID_Event");
                list.add(new Event(user_id,data,ora,type_id,event_id));

//cognome, data, nome, nametype

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
            datainiziale=dataInizialeTextBox.getValue().toString();
            datafinale = dataFinaleTextBox.getValue().toString();

            //data = EventJDBCDAO.searchDate(Server.QUERY_DATE_SEARCH, datainiziale, datafinale);
            //  EventJDBCDAO.call(table, data);
            data = lj.searchDate(Server.QUERY_DATE_SEARCH, datainiziale, datafinale);
            table.setItems(data);
            lj.call(table, data);
          //  lj.searchList(Server.QUERY_SEARCH_LIST,value,data);



/*************************/
        }//Fine Handle()
    }//Fine search listner button



    public class DetailshButtonListener implements EventHandler<ActionEvent> {
EventJDBCDAO lj=new EventJDBCDAO();
        @Override
        public void handle(ActionEvent e) {



//            String stringa = table.getItems().get(4).getUser_id().toString();

            //EventJDBCDAO.call(table, data);







/*************************/
        }//Fine Handle()
    }//Fine search listner button






    public void setCalendar(DatePicker checkInDatePicker){
        //DatePicker checkInDatePicker = new DatePicker();
                StringConverter converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };

        checkInDatePicker.setConverter(converter);
        checkInDatePicker.setPromptText(pattern.toLowerCase());
    }



}

