package it.uniclam.rilevamento_presenze.gui;

import it.uniclam.rilevamento_presenze.beanclass.Employee;
import it.uniclam.rilevamento_presenze.connections.Server;
import it.uniclam.rilevamento_presenze.controls.DipendenteJDBCDAO;
import it.uniclam.rilevamento_presenze.utility.GeneratePDF;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import java.io.IOException;


/**
 * La Classe FxTableUser
 * 1. Svolge il ruolo di interfaccia per la tabella dei dipendenti
 * 2. Permette al responsabile del serivizio presenze di visualizzare i dipendenti e i dettagli a loro associati
 * 3.Interagisce con le classi Employee e DipendenteJDBCDAO
 */


public class FxTableUser
		extends Application {




    public TextField TextboxSearch;
    public Label LabelSearch;
    public TextField TextboxInsertPK;

    public HBox buttonHb,buttonHbONE,buttonHbTWO;

    DipendenteJDBCDAO employeeObject = new DipendenteJDBCDAO();
    private TableView<Employee> table;
    private ObservableList<Employee> data;
    private Text actionStatus;
    public FxTableUser(){

    }

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






        //II COl
		TableColumn titleCol = new TableColumn("Cognome");
        titleCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("surname"));
        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        titleCol.setOnEditCommit(new EventHandler<CellEditEvent<Employee, String>>() {
            @Override
            public void handle(CellEditEvent<Employee, String> t) {

                t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setSurname(t.getNewValue());

                //Modifica dei campi quando faccio invio (COLONNA 1)
                int ix = table.getSelectionModel().getSelectedIndex();
                Employee employee = table.getSelectionModel().getSelectedItem();
                System.out.println(ix);
                String nome = table.getItems().get(ix).getName().toString();
                String cognome = table.getItems().get(ix).getSurname().toString();
                String id_employee=table.getItems().get(ix).getId_employee().toString();

                employeeObject.update(Server.QUERY_UPDATE_LIST, cognome, nome, id_employee);
                System.out.println("Ho modificato il valore di COL 1");


			}
		});

		TableColumn authorCol = new TableColumn("Nome");
        authorCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
        authorCol.setCellFactory(TextFieldTableCell.forTableColumn());
        authorCol.setOnEditCommit(new EventHandler<CellEditEvent<Employee, String>>() {
            @Override
            public void handle(CellEditEvent<Employee, String> t) {

                t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setName(t.getNewValue());

                //Aggiorno il valore nel database INIZIO:
                int ix = table.getSelectionModel().getSelectedIndex();
                Employee employee = table.getSelectionModel().getSelectedItem();
                System.out.println(ix);


                String nome = table.getItems().get(ix).getName().toString();
                String cognome = table.getItems().get(ix).getSurname().toString();
                String id_employee=table.getItems().get(ix).getId_employee().toString();

                //Aggiorno il valore nel database FINE:
                employeeObject.update(Server.QUERY_UPDATE_LIST, cognome, nome, id_employee);

			}
		});


        //II Final
        final TableColumn employeeCol = new TableColumn("ID");
        employeeCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("id_employee"));
        employeeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        employeeCol.setOnEditCommit(new EventHandler<CellEditEvent<Employee, String>>() {
            @Override

            public void handle(CellEditEvent<Employee, String> t) {


                t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setId_employee(t.getNewValue());

                //Modifica dei campi quando faccio invio (COLONNA 1)
                int ix = table.getSelectionModel().getSelectedIndex();
                Employee employee = table.getSelectionModel().getSelectedItem();
                System.out.println(ix);
                String nome = table.getItems().get(ix).getName().toString();
                String cognome = table.getItems().get(ix).getSurname().toString();
                String id_employee=table.getItems().get(ix).getId_employee().toString();
                employeeObject.update(Server.QUERY_UPDATE_LIST, cognome, nome, id_employee);



            }
        });




		table.getColumns().setAll(titleCol, authorCol,employeeCol);
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
        Button detailsbtn=new Button("Dettagli Eventi");
        detailsbtn.setOnAction(new DetailshButtonListener());
        Button backbtn=new Button("Annulla");
        backbtn.setOnAction(new BackButtonListener());
        Button discrepancybtn=new Button("Incongruenze");//InCONGRUENZE

        discrepancybtn.setOnAction(new discrepancyButtonListener());
        Button infobtn=new Button("Info Dipendenti");//InCONGRUENZE
        infobtn.setOnAction(new infoEmployeeButtonListener());
        Button pdfreportbtn=new Button("Genera Report");//InCONGRUENZE
        pdfreportbtn.setOnAction(new PdfReportButtonListener());



        //Textbox

        TextboxSearch=new TextField();
        TextboxInsertPK=new TextField();
        TextboxInsertPK.setVisible(false);



		buttonHb = new HBox(10);
		buttonHb.setAlignment(Pos.CENTER);
		buttonHb.getChildren().addAll(addbtn, delbtn,detailsbtn,discrepancybtn,infobtn,pdfreportbtn);
        buttonHbONE = new HBox(10);
        buttonHbONE.setAlignment(Pos.CENTER);
        buttonHbONE.getChildren().addAll(LabelSearch, TextboxSearch, srcbtn);


        buttonHbTWO = new HBox(10);
        buttonHbTWO.setAlignment(Pos.CENTER);
        buttonHbTWO.getChildren().addAll(okbtn, backbtn);
        buttonHbTWO.setVisible(false);

        // Status message text
        actionStatus = new Text();
        actionStatus.setFill(Color.FIREBRICK);

		// Vbox
		VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 25, 25, 25));
        vbox.getChildren().addAll(labelHb, buttonHbONE, table, buttonHb, buttonHbTWO, TextboxInsertPK, actionStatus);

		// Scene
		Scene scene = new Scene(vbox, 500, 550); // w x h
		primaryStage.show();
        table.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);

		// Select the first row
		table.getSelectionModel().select(0);
        Employee employee = table.getSelectionModel().getSelectedItem();
        actionStatus.setText(employee.toString());

    } // start()

    //(SELECT)Lettura di tutti i dipendenti dalla tabella
    private ObservableList<Employee> getInitialTableData() {

        return employeeObject.selectList(Server.QUERY_SELECT_ALL_LIST);
    }

 	private class RowSelectChangeListener implements ChangeListener<Number> {

		@Override
        public void changed(ObservableValue<? extends Number> ov,
                            Number oldVal, Number newVal) {

			int ix = newVal.intValue();

			if ((ix < 0) || (ix >= data.size())) {

				return; // invalid data
			}

            Employee employee = data.get(ix);


            actionStatus.setText(employee.toString());


		}
	}

    //Aggiunta di un Employee alla tabella OK
    private class AddButtonListener implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {

            System.out.println(data.size());
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

            DipendenteJDBCDAO lj = new DipendenteJDBCDAO();
            Employee employee = employee = new Employee("", TextboxInsertPK.getText(), "");
            data.add(employee);

            int row = data.size() - 1;
            System.out.println(data.size());
            // Select the new row
            table.requestFocus();
            table.getSelectionModel().select(row);
            table.getFocusModel().focus(row);
            lj.addList(Server.QUERY_ADD_LIST,TextboxInsertPK.getText());


            buttonHbTWO.setVisible(false);
            buttonHb.setVisible(true);
            TextboxInsertPK.setVisible(false);
            TextboxSearch.setVisible(true);


        }
    }

    //(DELETE)Cancellazione elementi dalla tabella e dal DB
	private class DeleteButtonListener implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {

			// Get selected row and delete
			int ix = table.getSelectionModel().getSelectedIndex();
            Employee employee = table.getSelectionModel().getSelectedItem();
            System.out.println(table.getItems().get(ix).toString());


            String name = table.getItems().get(ix).getName().toString();
            String surname = table.getItems().get(ix).getSurname().toString();
            data.remove(ix);//Elimino l'elemento dalla tabella e la aggiorno
            employeeObject.removeList(Server.QUERY_REMOVE_LIST, name, surname);


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

    //(SEARCH)Ricerca elementi nella tabella e dal DB OK
    private class SearchButtonListener implements EventHandler<ActionEvent> {


        @Override
        public void handle(ActionEvent e) {



            /*************************/
            table.getItems().clear();
            String value=TextboxSearch.getText();

            employeeObject.searchList(Server.QUERY_SEARCH_LIST, value, data);



/*************************/
        }//Fine Handle()
    }//Fine search listner button


    //Bottone degli eventi dettagliati relativi ai dipendenti
    private class DetailshButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {


//
            int ix = table.getSelectionModel().getSelectedIndex();
            Employee employee = table.getSelectionModel().getSelectedItem();
            System.out.println(table.getItems().get(ix).toString());

            String name = table.getItems().get(ix).getName().toString();
            String surname = table.getItems().get(ix).getSurname().toString();

            employeeObject.gridView(Server.QUERY_ORDERDATE, name, surname);


            actionStatus.setText("Employee: " + employee.toString());

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
        }
    }

//Incongruenze
    public class discrepancyButtonListener implements EventHandler<ActionEvent> {


    @Override
        public void handle(ActionEvent e) {

            try {

                Runtime.getRuntime().exec
                        ("\"C:\\Program Files\\Java\\jdk1.8.0_60\\bin\\java\" -Didea.launcher.port=7534 \"-Didea.launcher.bin.path=C:\\Program Files (x86)\\JetBrains\\IntelliJ IDEA 14.0.3\\bin\" -Dfile.encoding=UTF-8 -classpath \"C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\charsets.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\deploy.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\javaws.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\jce.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\jfr.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\jfxswt.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\jsse.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\management-agent.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\plugin.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\resources.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\rt.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\ext\\access-bridge-64.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\ext\\cldrdata.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\ext\\dnsns.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\ext\\jaccess.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\ext\\jfxrt.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\ext\\localedata.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\ext\\nashorn.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\ext\\sunec.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\ext\\sunjce_provider.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\ext\\sunmscapi.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\ext\\sunpkcs11.jar;C:\\Program Files\\Java\\jdk1.8.0_60\\jre\\lib\\ext\\zipfs.jar;C:\\Users\\Antonio Di Civita\\Desktop\\java\\Ingegneria Del Software\\out\\production\\Ingegneria Del Software;C:\\Users\\Antonio Di Civita\\Desktop\\java\\Ingegneria Del Software\\mysql-connector-java-5.1.37-bin.jar;C:\\Users\\Antonio Di Civita\\Desktop\\java\\Ingegneria Del Software\\itextpdf-5.2.1.jar;C:\\Program Files (x86)\\JetBrains\\IntelliJ IDEA 14.0.3\\lib\\idea_rt.jar\" com.intellij.rt.execution.application.AppMain it.uniclam.rilevamento_presenze.gui.FxTableEvent");

            } catch (IOException e1) {
                e1.printStackTrace();
            }
/*************************/
    }


}

    //Informazioni sui dipendenti
    private class infoEmployeeButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {



            int ix = table.getSelectionModel().getSelectedIndex();
            Employee employee = table.getSelectionModel().getSelectedItem();
            System.out.println(table.getItems().get(ix).toString());


            String name = table.getItems().get(ix).getName().toString();
            String surname = table.getItems().get(ix).getSurname().toString();

            employeeObject.details(Server.QUERY_DETAILS, name, surname);


            actionStatus.setText("Employee: " + employee.toString());

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
        }
    }


    //Generatore di report
    private class PdfReportButtonListener implements EventHandler<ActionEvent> {
        GeneratePDF generatePDF = new GeneratePDF();
        @Override
        public void handle(ActionEvent e) {

            GeneratePDF.main(null);


/*************************/
        }
    }

    //Bottone di ritorno
    private class BackButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
        buttonHb.setVisible(true);
        buttonHbONE.setVisible(false);
            buttonHbTWO.setVisible(false);
            TextboxInsertPK.setVisible(false);

        }
    }



}

