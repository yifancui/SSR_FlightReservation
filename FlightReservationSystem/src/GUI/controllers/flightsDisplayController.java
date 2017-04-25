package GUI.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Collectors;

import GUI.StateMachine;
import Models.Airport;
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
		table.getColumns().addAll(guiHelpers.getColumns());
        table.setItems(data);
	}
	
	@FXML
	public void selectitem() throws IOException{
		// Order which tells us if we're round trip or one way
		StateMachine sm = StateMachine.getInstance();
		int in =  table.getSelectionModel().getSelectedIndex();
		
		if(sm.order.roundtrip) {
			if(sm.order.secondRound) {
				sm.order.secondFlightPath = StateMachine.getInstance().flights.get(in);
				sm.switchState(StateMachine.state.confirm_order);
			} else {
				sm.order.firstFlightPath = StateMachine.getInstance().flights.get(in);
				sm.switchState(StateMachine.state.input_params_second);
			}
		} else {
			sm.order.firstFlightPath = StateMachine.getInstance().flights.get(in);
			sm.switchState(StateMachine.state.confirm_order);
		}
	}
	@FXML
	public void cancelitem(){
		StateMachine sm = StateMachine.getInstance();
		sm.switchState(StateMachine.state.finish);
	}
	
	@FXML
	public void sortList(ActionEvent event) {
		if(this.priceRadio.isSelected()) { //price
			Collections.sort(data, new Comparator<ArrayList<Flight>> () {
			    @Override
			    public int compare(ArrayList<Flight> a, ArrayList<Flight> b) {
			    	double totA = a.stream().filter(f -> f.getPrice() > 10).mapToDouble(f -> f.getPrice()).sum();
			    	double totB = b.stream().filter(f -> f.getPrice() > 10).mapToDouble(f -> f.getPrice()).sum();
			    	return Double.compare(totA, totB);
			    }
			});			
		} else if(this.durationRadio.isSelected()) { //duration
			Collections.sort(data, new Comparator<ArrayList<Flight>> () {
			    @Override
			    public int compare(ArrayList<Flight> a, ArrayList<Flight> b) {
			    	int totA = a.stream().filter(f -> f.getDuration() > 10).mapToInt(f -> f.getDuration()).sum();
			    	int totB = b.stream().filter(f -> f.getDuration() > 10).mapToInt(f -> f.getDuration()).sum();
			    	return Integer.compare(totA, totB);
			    }
			});					
		} else { //departure time
			Collections.sort(data, new Comparator<ArrayList<Flight>> () {
			    @Override
			    public int compare(ArrayList<Flight> a, ArrayList<Flight> b) {
			    	return Long.compare(a.get(0).getDate().getTime(), b.get(0).getDate().getTime());
			    }
			});	
		}
	}
}