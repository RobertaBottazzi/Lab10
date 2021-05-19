/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.rivers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.rivers.model.Flow;
import it.polito.tdp.rivers.model.Model;
import it.polito.tdp.rivers.model.River;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxRiver"
    private ComboBox<River> boxRiver; // Value injected by FXMLLoader

    @FXML // fx:id="txtStartDate"
    private TextField txtStartDate; // Value injected by FXMLLoader

    @FXML // fx:id="txtEndDate"
    private TextField txtEndDate; // Value injected by FXMLLoader

    @FXML // fx:id="txtNumMeasurements"
    private TextField txtNumMeasurements; // Value injected by FXMLLoader

    @FXML // fx:id="txtFMed"
    private TextField txtFMed; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    

    @FXML
    void doFiume(ActionEvent event) {
    	List<Flow> flows= model.getFlowsPerRiver(this.boxRiver.getValue());
    	this.txtStartDate.setText(flows.get(0).getDay().toString());
    	this.txtEndDate.setText(flows.get(flows.size()-1).getDay().toString());
    	this.txtNumMeasurements.setText(String.valueOf(flows.size()));
    	this.txtFMed.setText(String.valueOf(model.getMedia(flows)));
    }

    @FXML
    void doSimula(ActionEvent event) {
    	if(this.boxRiver.getValue()==null)
    		this.txtResult.setText("Selezionare un fiume please, thank you :)");
    	try {
    	if(this.txtK.getText().isEmpty() || Double.parseDouble(this.txtK.getText())<=0.0)
    		this.txtResult.setText("Selezionare un fattore di scala ammissibile please, thank you :)");
    	} catch (NumberFormatException e) {
    		this.txtResult.setText("Selezionare un fattore di scala ammissibile please, thank you :)");
    	}
    	this.model.init(model.getFlowsPerRiver(this.boxRiver.getValue()), Double.parseDouble(this.txtK.getText()));
    	this.model.run();
    	this.txtResult.setText("GIORNI DI DISSERVIZIO: "+this.model.getGiorniSenzaIrrigazione()+"\n"+"IL FLUSSO MEDIO PER IL FIUME "+this.boxRiver.getValue().getName()+" è: "+this.model.getCMedia(model.getFlowsPerRiver(this.boxRiver.getValue())));    		
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxRiver != null : "fx:id=\"boxRiver\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtStartDate != null : "fx:id=\"txtStartDate\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtEndDate != null : "fx:id=\"txtEndDate\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNumMeasurements != null : "fx:id=\"txtNumMeasurements\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtFMed != null : "fx:id=\"txtFMed\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxRiver.getItems().addAll(model.getRivers());
    }
}
