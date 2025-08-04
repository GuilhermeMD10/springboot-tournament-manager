package pt.ul.fc.di.css.soccernowjavafx.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiEquipa;
import pt.ul.fc.di.css.soccernowjavafx.dto.EquipaDto;
import pt.ul.fc.di.css.soccernowjavafx.model.*;

public class CreateEquipaController implements ControllerWithModel{

    private DataModel model;

    private Stage stage;

    @FXML private TextField nomeField;  

    @FXML
    private void handleCriarEquipa() {

        
        String nome = nomeField.getText();
        if (nome.isEmpty()) {
            mostrarAlerta("Erro", "Preencha todos os campos obrigatórios.");
            return;
        }

        nome.trim();
        EquipaDto equipa = new EquipaDto(nome);
        try{
                    ApiEquipa.createEquipa(equipa);

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
