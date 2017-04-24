package GUI.controllers;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import GUI.StateMachine;
import Models.Flight;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.util.Callback;

public class flightsDisplayController {
	
	
	//@FXML ListView onelist;
		
	@FXML TableView<ArrayList<Flight>> table;
	
	@FXML RadioButton priceRadio;
	@FXML RadioButton durationRadio;
	@FXML RadioButton departureRadio;
	
	ObservableList<ArrayList<Flight>> data;
	
	@FXML
	public void initialize(){		
		data = FXCollections.observableArrayList(StateMachine.getInstance().flights);
		
		makeFliColumn();
		makeDurColumn();
		makePriColumn();  
        table.setItems(data);
	}
	
	private void makeFliColumn() {
		TableColumn<ArrayList<Flight>,String> fliColumn=new TableColumn<ArrayList<Flight>,String>("Flights & Connections");
        fliColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ArrayList<Flight>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<Flight>, String> param) {
            	ArrayList<Flight> path = param.getValue();
            	String s = "";
            	for(int i = 0; i < path.size(); i++) {
            		s = s + path.get(i).dep.code + " --> ";
            		if(i == path.size() - 1) {
            			s = s + path.get(i).arr.code;
            		}
            	}	
                return new SimpleStringProperty(s);
            }
        });
        table.getColumns().add(fliColumn);
	}
	
	private void makeDurColumn() {
        TableColumn<ArrayList<Flight>,String> durColumn=new TableColumn<ArrayList<Flight>,String>("Total Duration");
        durColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ArrayList<Flight>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<Flight>, String> param) {
            	int tot = 0;
            	for (Flight f : param.getValue()) {
            		tot += f.duration;
            	}
            	return new SimpleStringProperty(tot + " minutes");
        	}
        });
        table.getColumns().add(durColumn);
	}
	
	private void makePriColumn() {
        TableColumn<ArrayList<Flight>,String> priColumn=new TableColumn<ArrayList<Flight>,String>("Total Price");
        priColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ArrayList<Flight>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<Flight>, String> param) {
            	double tot = 0.0;
            	for (Flight f : param.getValue()) {
            		if(StateMachine.getInstance().order.firstClass) {
            			tot += f.firstPrice;
            		} else {
            			tot += f.coachPrice;
            		}
            	}
            	DecimalFormat df = new DecimalFormat("#.00");
            	return  new SimpleStringProperty("$" + df.format(tot));
            }
        });
        table.getColumns().add(priColumn);
	}
	
	@FXML
	public void selectitem() throws IOException{
		// Order which tells us if we're round trip or one way
		StateMachine sm = StateMachine.getInstance();
		int in =  table.getSelectionModel().getSelectedIndex();
		sm.order.firstFlightPath = StateMachine.getInstance().flights.get(in);
		System.out.println(sm.order.firstFlightPath);
		sm.switchState(StateMachine.state.confirm_order);
	}
	@FXML
	public void cancelitem(){
		StateMachine sm = StateMachine.getInstance();
		sm.switchState(StateMachine.state.finish);
	}
	
	@FXML
	public void sortList(ActionEvent event) {
		if(this.priceRadio.isSelected()) { //price
			System.out.println("sort by price pl0x");
		} else if(this.durationRadio.isSelected()) { //duration
			System.out.println("sort by duration pl0x");
		} else { //departure time
			System.out.println("sort by departure time pl0x");
		}
	}
}
