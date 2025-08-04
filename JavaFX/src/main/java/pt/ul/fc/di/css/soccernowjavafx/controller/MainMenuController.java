package pt.ul.fc.di.css.soccernowjavafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import pt.ul.fc.di.css.soccernowjavafx.model.DataModel;

public class MainMenuController implements ControllerWithModel{

    private Stage stage;
    private DataModel model;

    @FXML
    private Button createUtilizador;

    @FXML
    private void createUtilizador(ActionEvent event) {
        // Aqui podes abrir um novo FXML, chamar um método do modelo, etc.
        System.out.println("Botão 'Create Utilizador' clicado.");
        Util.switchScene(stage, "/pt/ul/fc/di/css/soccernowjavafx/view/register_user.fxml",
        "Create Utilizador", model);
    }

    @FXML
    private void findUtilizador(ActionEvent event) {
        // Aqui podes abrir um novo FXML, chamar um método do modelo, etc.
        System.out.println("Botão 'Find Utilizador' clicado.");
        Util.switchScene(stage, "/pt/ul/fc/di/css/soccernowjavafx/view/utilizador_detail.fxml",
        "Find Utilizador", model);
    }

    @FXML
    private void createEquipa(ActionEvent event) {
        // Aqui podes abrir um novo FXML, chamar um método do modelo, etc.
        System.out.println("Botão 'Create Equipa' clicado.");
        Util.switchScene(stage, "/pt/ul/fc/di/css/soccernowjavafx/view/create_equipa.fxml",
        "Create Equipa", model);
    }

    @FXML
    private void findEquipa(ActionEvent event) {
        // Aqui podes abrir um novo FXML, chamar um método do modelo, etc.
        System.out.println("Botão 'Find Equipa' clicado.");
        Util.switchScene(stage, "/pt/ul/fc/di/css/soccernowjavafx/view/equipa_list.fxml",
        "Find Equipa", model);
    }

    @FXML
    private void createCampeonato(ActionEvent event) {
        // Aqui podes abrir um novo FXML, chamar um método do modelo, etc.
        System.out.println("Botão 'Create Campeonato' clicado.");
        Util.switchScene(stage, "/pt/ul/fc/di/css/soccernowjavafx/view/create_campeonato.fxml",
        "Create Campeonato", model);
    }

    @FXML
    private void updateCampeonato(ActionEvent event) {
        // Aqui podes abrir um novo FXML, chamar um método do modelo, etc.
        System.out.println("Botão 'Update Campeonato' clicado.");
        Util.switchScene(stage, "/pt/ul/fc/di/css/soccernowjavafx/view/update_campeonato.fxml",
        "Update Campeonato", model);
    }   

    @FXML
    private void createJogo(ActionEvent event) {
        System.out.println("Botão 'Create Jogo' clicado.");
        Util.switchScene(stage, "/pt/ul/fc/di/css/soccernowjavafx/view/create_jogo.fxml",
        "Create Jogo", model);
    }
    @FXML
    private void addResultado(ActionEvent event) {
        System.out.println("Botão 'Add Resultado' clicado.");
        Util.switchScene(stage, "/pt/ul/fc/di/css/soccernowjavafx/view/add_resultado.fxml",
        "Add Resultado", model);
    }

    public void initModel(Stage stage, DataModel model) {
        this.stage = stage;
        this.model = model;
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
