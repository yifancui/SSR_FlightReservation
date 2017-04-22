package GUI;

import java.io.IOException;

import Models.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class flightsDisplayController {
	
	
	@FXML ListView onelist;
		
	@FXML
	public void initialize(){
		System.out.println("found " + StateMachine.getInstance().flights.size() + " flights");
		ObservableList name = FXCollections.observableArrayList();		
		name.addAll(StateMachine.getInstance().flights);		
		onelist.setItems(name);
	}
	
	@FXML
	public void selectitem() throws IOException{
		// Order which tells us if we're round trip or one way
		StateMachine sm = StateMachine.getInstance();
		Order o = sm.order;
		
		if(!o.roundtrip || o.secondRound) { //to confirmation screen
			Object fly=onelist.getSelectionModel().getSelectedItem();
			sm.switchState(StateMachine.state.confirm_order);
		} else { //input some more parameters
			sm.switchState(StateMachine.state.input_params_second);
		}
	}
	@FXML
	public void cancelitem(){
		StateMachine sm = StateMachine.getInstance();
		sm.switchState(StateMachine.state.finish);
	}
}
