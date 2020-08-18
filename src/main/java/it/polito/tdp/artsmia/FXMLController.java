package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	
    	String role = this.boxRuolo.getValue();
    	
    	if (role == null) {
    		txtResult.appendText("Scegliere un ruolo!");
    		return;
    	}
    	
    	for (Adiacenza a : this.model.direttamenteConnessi(role)) {
    		txtResult.appendText(a.toString()+"\n");
    	}
    	

    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	
    	String id = this.txtArtista.getText();
    	Integer artistId;
    	
    	try {
    		artistId = Integer.parseInt(id);
    	} catch (IllegalArgumentException e) {
        	txtResult.appendText("Inserire l'id di un'artista!");
        	return;
        }
    	
    	if(this.model.idCorretto(artistId)==false) {
    		txtResult.appendText("Inserire l'id di un'artista valido!");
    	}
    	
    	txtResult.appendText("Il numero di esposizioni per cui il percorso è più lungo è: "+this.model.getCammino(artistId).size()+"\n");
    	
    	for(Artist a : this.model.getCammino(artistId)) {
    		txtResult.appendText(a.toString()+"\n");
    	}
    	
    	


    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	String role = this.boxRuolo.getValue();
    	
    	if (role == null) {
    		txtResult.appendText("Scegliere un ruolo!");
    		return;
    	}
    	
    	this.model.creaGrafo(role);
    	txtResult.appendText("Grafo creato!\n");
    	txtResult.appendText("# Vertici = "+this.model.getVertex().size()+" # Archi = "+this.model.getEdge().size());

    }

    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxRuolo.getItems().addAll(this.model.getRole());
	}
}

