package pt.ul.fc.di.css.soccernowjavafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiClient;
import pt.ul.fc.di.css.soccernowjavafx.model.DataModel;

public class InitController {

    private DataModel model;

    private Stage stage;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button logInButton;

    @FXML
    private Label mensagemLabel;

    @FXML
    private void handleLogIn() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email == null || email.isEmpty() ) {
            mensagemLabel.setText("Preencha todos os campos.");
            mensagemLabel.setTextFill(Color.RED);
            return;
        }

        boolean autenticado = ApiClient.autenticarUtilizador(email);

        if (autenticado) {
            mensagemLabel.setText("Login com sucesso!");
            mensagemLabel.setTextFill(Color.GREEN);
            Util.switchScene(stage, "/pt/ul/fc/di/css/soccernowjavafx/view/main_menu.fxml",
                    "Main Menu", model);
        } else {
            mensagemLabel.setText("Credenciais inválidas.");
            mensagemLabel.setTextFill(Color.RED);
        }
    }



    public void initModel(Stage stage, DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.stage = stage;
        this.model = model;
    }
}
