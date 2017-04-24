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
		
		makeFliColumn();
		makeDepTimeColumn();
		makeArrTimeColumn();
		makeDurColumn();
		makePriColumn();  
        table.setItems(data);
	}
	
	private void makeDepTimeColumn() {
        TableColumn<ArrayList<Flight>,String> depTimeColumn = new TableColumn<ArrayList<Flight>,String>("Departure Time (local)");
        depTimeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ArrayList<Flight>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<Flight>, String> param) {
            	Flight flight = param.getValue().get(0);
            	TimeZone.setDefault(TimeZone.getTimeZone("Etc/GMT+" + Math.abs(flight.dep.gmtOffset)));            	
            	return new SimpleStringProperty(flight.depDate.toString());
        	}
        });
        table.getColumns().add(depTimeColumn);
	}
	
	private void makeArrTimeColumn() {
        TableColumn<ArrayList<Flight>,String> arrTimeColumn = new TableColumn<ArrayList<Flight>,String>("Arrival Time (local)");
        arrTimeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ArrayList<Flight>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<Flight>, String> param) {
            	Flight flight = param.getValue().get(param.getValue().size() - 1);
            	TimeZone.setDefault(TimeZone.getTimeZone("Etc/GMT+" + Math.abs(flight.dep.gmtOffset)));            	
            	return new SimpleStringProperty(flight.arrDate.toString());            	
        	}
        });
        table.getColumns().add(arrTimeColumn);
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
            	int tot = param.getValue().stream().filter(f -> f.getDuration() > 10).mapToInt(f -> f.getDuration()).sum();
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
            	double tot = param.getValue().stream().filter(f -> f.getPrice() > 10).mapToDouble(f -> f.getPrice()).sum();
            	return  new SimpleStringProperty("$" +  new DecimalFormat("#.00").format(tot));
            }
        });
        table.getColumns().add(priColumn);  
	}
	
	@FXML
	public void selectitem() throws IOException{
		// Order which tells us if we're round trip or one way
		StateMachine sm = StateMachine.getInstance();
		int in =  table.getSelectionModel().getSelectedIndex();
		
		if(sm.order.roundtrip) {
			if(sm.order.secondRound) {
				System.out.println(1);
				sm.order.secondFlightPath = StateMachine.getInstance().flights.get(in);
				sm.switchState(StateMachine.state.confirm_order);
			} else {
				System.out.println(2);
				sm.order.firstFlightPath = StateMachine.getInstance().flights.get(in);
				sm.switchState(StateMachine.state.input_params_second);
			}
		} else {
			System.out.println(3);
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
