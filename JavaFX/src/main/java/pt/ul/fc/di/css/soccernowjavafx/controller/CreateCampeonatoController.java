package pt.ul.fc.di.css.soccernowjavafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiCampeonato;
import pt.ul.fc.di.css.soccernowjavafx.dto.CampeonatoDto;
import pt.ul.fc.di.css.soccernowjavafx.model.DataModel;

public class CreateCampeonatoController implements ControllerWithModel{
    
    private DataModel model;

    private Stage stage;

    @FXML private TextField nomeField;  
    
    @FXML
    private void handleCriarCampeonato() {
        String nome = nomeField.getText();
        if (nome.isEmpty()) {
            mostrarAlerta("Erro", "Preencha todos os campos obrigatórios.");
            return;
        }

        nome.trim();
        CampeonatoDto camp = new CampeonatoDto(nome);
        try{
            ApiCampeonato.createCampeonato(camp);

        }catch(Exception e){
        System.out.println("Erro a criar equipa"); 
        }
        limparCampos();
    }


    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void limparCampos() {
        nomeField.clear();

    }

    public void initModel(Stage stage, DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.stage = stage;
        this.model = model;
    }

    @FXML
    void back(ActionEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/soccernowjavafx/view/main_menu.fxml",
                "Main Menu", model);
    }

    @Override
    public Stage getStage() {
        return this.stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
