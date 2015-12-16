package it.uniclam.rilevamento_presenze.gui;

import it.uniclam.rilevamento_presenze.beanclass.Event;
import it.uniclam.rilevamento_presenze.connections.Server;
import it.uniclam.rilevamento_presenze.controls.EventJDBCDAO;
import it.uniclam.rilevamento_presenze.utility.Time;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

import java.util.Objects;

/**
 * La Classe FxTableEvent
 * 1. Svolge il ruolo di interfaccia per la tabella degli eventi
 * 2. Permette al responsabile del serivizio presenze di controllare gli eventi dei dipendenti e le eventuali incongruenze
 * 3.Interagisce con le classi Event e EventJDBCDAO
 */

public class FxTableEvent extends Application {


    public final String pattern = "yyyy-MM-dd";
    public TextField TextboxSearch;
    public Label LabelSearch;
    public TextField TextboxInsertPK;
    public HBox buttonHb,buttonHbONE,buttonHbTWO;
    public String datainiziale = "";
    public String datafinale = "";
    public DatePicker dataInizialeTextBox;
    public DatePicker dataFinaleTextBox;
    EventJDBCDAO eventObject = new EventJDBCDAO();
    private Stage primaryStage;
    private BorderPane rootLayout;
    private TableView<Event> table;
    private ObservableList<Event> data;
    private Text actionStatus;


    public FxTableEvent(){
        this.dataInizialeTextBox = new DatePicker();
        this.dataFinaleTextBox = new DatePicker();
    }

	public static void main(String [] args) {

		Application.launch(args);
	}



	@Override
	public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
		primaryStage.setTitle("Pannello di Controllo:Responsabile Presenze");

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
        data = eventObject.getInitialTableData(Server.QUERY_SELECT_ALL_EVENT);
        table.setItems(data);
        table.setEditable(true);



        TableColumn dataCol = new TableColumn("Data");
        dataCol.setCellValueFactory(new PropertyValueFactory<Event, String>("data"));
        dataCol.setCellFactory(TextFieldTableCell.forTableColumn());
        dataCol.setOnEditCommit(new EventHandler<CellEditEvent<Event, String>>() {
            @Override
            public void handle(CellEditEvent<Event, String> t) {

                t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setData(t.getNewValue());
            }
        });



        TableColumn hourCol = new TableColumn("NOME");
		hourCol.setCellValueFactory(new PropertyValueFactory<Event, String>("nome"));
		hourCol.setCellFactory(TextFieldTableCell.forTableColumn());
		hourCol.setOnEditCommit(new EventHandler<CellEditEvent<Event, String>>() {
            @Override
            public void handle(CellEditEvent<Event, String> t) {

                t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setNome(t.getNewValue());
            }
        });


        TableColumn typeCol = new TableColumn("IN/OUT");

        typeCol.setCellValueFactory(new PropertyValueFactory<Event, String>("type_id"));
        typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        typeCol.setOnEditCommit(new EventHandler<CellEditEvent<Event, String>>() {
            @Override
            public void handle(CellEditEvent<Event, String> t) {


                t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setType_id(t.getNewValue());

                //Aggiorno il valore nel database INIZIO:
                int ix = table.getSelectionModel().getFocusedIndex();

               // Event event = (Event) table.getSelectionModel().getSelectedItem();


                String id_event = table.getItems().get(ix).getEvent_id().toString();
                String id_type = table.getItems().get(ix).getType_id().toString();

                if(Objects.equals(id_type,"Entrata")){
                    id_type="1";

                    eventObject.update(Server.QUERY_UPDATE_EVENT, id_type, id_event);

                }
                else if (Objects.equals(id_type,"Uscita")){
                    id_type="2";
                    eventObject.update(Server.QUERY_UPDATE_EVENT, id_type, id_event);
                }



                System.out.println("Ho modificato il valore di COL 2");
            }
        });




         TableColumn userCol = new TableColumn("COGNOME");
        userCol.setCellValueFactory(new PropertyValueFactory<Event, String>("cognome"));
        userCol.setCellFactory(TextFieldTableCell.forTableColumn());
        userCol.setOnEditCommit(new EventHandler<CellEditEvent<Event, String>>() {
            @Override
            public void handle(CellEditEvent<Event, String> t) {

                t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setCognome((t.getNewValue()));

                //Aggiorno il valore nel database INIZIO:
                int ix = table.getSelectionModel().getSelectedIndex();
                Event dipendente = table.getSelectionModel().getSelectedItem();
                System.out.println(ix);


                String nome = table.getItems().get(ix).getNome().toString();
                String cognome = table.getItems().get(ix).getData().toString();
                String user_id = table.getItems().get(ix).getCognome().toString();
                String type_id = table.getItems().get(ix).getType_id().toString();
                // System.out.println(autore+"tralalal");
                //update
                //QUERYlj.updateList(Server.QUERY_UPDATE_LIST, nome, cognome);
                //Aggiorno il valore nel database FINE:

                System.out.println("Ho modificato il valore di autori");
            }
        });

        final TableColumn event_idCol = new TableColumn("ID");
        event_idCol.setCellValueFactory(new PropertyValueFactory<Event, String>("event_id"));
        event_idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        event_idCol.setOnEditCommit(new EventHandler<CellEditEvent<Event, String>>() {
            @Override

            public void handle(CellEditEvent<Event, String> t) {


                t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setEvent_id(t.getNewValue());

                //Modifica dei campi quando faccio invio (COLONNA 1)

                Event event = table.getSelectionModel().getSelectedItem();


            }
        });



		table.getColumns().setAll(userCol, hourCol,dataCol, typeCol,event_idCol);
		table.setPrefWidth(450);
		table.setPrefHeight(300);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		table.getSelectionModel().selectedIndexProperty().addListener(
			new RowSelectChangeListener());


		// Aggiunge
		Button backbtn = new Button("Indietro");
		backbtn.setOnAction(new BackButtonListener());
		Button exitbtn = new Button("Esci");
		exitbtn.setOnAction(new ExitButtonListener());
        Button srcbtn=new Button("Trova");
        srcbtn.setOnAction(new SearchButtonListener());



//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        //TextBox di ricerca per data con calendario integrato

        GridPane gridPane = new GridPane();

        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label dataIniziale = new Label("Data iniziale");
        gridPane.add(dataIniziale, 0, 0);

        GridPane.setHalignment(dataIniziale, HPos.CENTER);
        gridPane.add(dataInizialeTextBox, 0,1);


        Time time = new Time();

        time.setCalendar(dataInizialeTextBox, pattern);


        Label dataFinale = new Label("Data Finale");
        gridPane.add(dataFinale, 4,0);

        GridPane.setHalignment(dataFinale, HPos.CENTER);
        gridPane.add(dataFinaleTextBox, 4, 1);
        gridPane.add(srcbtn, 5, 1);

        time.setCalendar(dataFinaleTextBox, pattern);

        datainiziale = dataInizialeTextBox.getAccessibleText();
        datafinale = dataFinaleTextBox.toString();


        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        //Textbox

        TextboxSearch=new TextField();
        TextboxInsertPK=new TextField();
        TextboxInsertPK.setVisible(false);



		buttonHb = new HBox(10);
		buttonHb.setAlignment(Pos.CENTER);
		buttonHb.getChildren().addAll(backbtn, exitbtn);

        buttonHbONE = new HBox(10);
        buttonHbONE.setAlignment(Pos.CENTER);
        buttonHbONE.getChildren().addAll(gridPane);

        buttonHbTWO = new HBox(10);
        buttonHbTWO.setAlignment(Pos.CENTER);
        buttonHbTWO.getChildren().addAll(backbtn);


        actionStatus = new Text();
        actionStatus.setFill(Color.FIREBRICK);

		// Vbox
		VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 25, 25, 25));
        vbox.getChildren().addAll(labelHb,buttonHbONE,table,buttonHb,buttonHbTWO,TextboxInsertPK,actionStatus);

		// Scene
		Scene scene = new Scene(vbox, 500, 550); // w x h
        table.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
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

    if (!table.getSelectionModel().isEmpty()) {
        EventJDBCDAO.inconsistenceSelector(table, data);
    } else {
        EventJDBCDAO.inconsistenceSelector(table, data);
    }
}


            /****/
		}
	}


    // Chiusura del programma
    private class ExitButtonListener implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
        System.exit(0);

		}
	}

    //(SEARCH)Ricerca elementi dalla tabella
    private class SearchButtonListener implements EventHandler<ActionEvent> {


        @Override
        public void handle(ActionEvent e) {


            /*************************/
            table.getItems().clear();

            datainiziale=dataInizialeTextBox.getValue().toString();
            datafinale = dataFinaleTextBox.getValue().toString();

            data = EventJDBCDAO.searchDate(Server.QUERY_DATE_SEARCH, datainiziale, datafinale);

            table.setItems(data);



/*************************/
        }//Fine Handle()
    }//Fine search listner button






    private class BackButtonListener implements EventHandler<ActionEvent> {


        @Override
        public void handle(ActionEvent e) {

            ObservableList<Event> return_table_data = eventObject.getInitialTableData(Server.QUERY_SELECT_ALL_EVENT);
           table.setItems(return_table_data);
            EventJDBCDAO.inconsistenceSelector(table, return_table_data);



/*************************/
        }
    }


}



