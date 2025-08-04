    package pt.ul.fc.di.css.soccernowjavafx.controller;

    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.scene.control.*;
    import javafx.stage.Stage;
    import pt.ul.fc.di.css.soccernowjavafx.api.ApiClient;
    import pt.ul.fc.di.css.soccernowjavafx.dto.ArbitroDto;
    import pt.ul.fc.di.css.soccernowjavafx.dto.JogadorDto;
    import pt.ul.fc.di.css.soccernowjavafx.dto.UtilizadorDto;
    import pt.ul.fc.di.css.soccernowjavafx.model.*;

    public class CreateUtilizadorController implements ControllerWithModel{

        private DataModel model;

        private Stage stage;

        @FXML private TextField nomeField;
        @FXML private TextField emailField;
        @FXML private TextField nifField;

        @FXML private ComboBox<String> tipoUtilizadorCombo;
        @FXML private ComboBox<Posicao> posicaoCombo;
        @FXML private CheckBox certificadoCheckbox;

        @FXML
        public void initialize() {
            //tipoUtilizadorCombo.getItems().addAll("Nenhum", "Jogador", "Árbitro");
            //posicaoCombo.getItems().setAll(Posicao.values()); // Enum Posicao no combo

            // Esconder os campos específicos por padrão
            //posicaoCombo.setVisible(false);
            //certificadoCheckbox.setVisible(false);

            tipoUtilizadorCombo.setOnAction(e -> {
                //String tipo = tipoUtilizadorCombo.getValue();
                //posicaoCombo.setVisible("Jogador".equals(tipo));
                //certificadoCheckbox.setVisible("Árbitro".equals(tipo));
            });
        }

        @FXML
        private void handleCriarUtilizador() {

            
            String nome = nomeField.getText();
            String email = emailField.getText();
            String nif = nifField.getText();
            String tipo = tipoUtilizadorCombo.getValue();
            boolean success = true;
            if (nome.isEmpty() || email.isEmpty() || nif.isEmpty()) {
                mostrarAlerta("Erro", "Preencha todos os campos obrigatórios.");
                return;
            }

            nome.trim();
            email.trim();
            nif.trim();
            tipo.trim();


            if ("Jogador".equals(tipo)) {


                JogadorDto jogador = new JogadorDto(0L, nome, email, nif);
                try {
                    ApiClient.createJogador(jogador);
                } catch (Exception e) {
                    success = false;
                }
                if(success){
                    System.out.println("Jogador criado: " + jogador);
                    mostrarAlerta("Sucesso", "Jogador criado com sucesso!");
                } else{
                    System.out.println("Erro a criar jogador");
                    mostrarAlerta("ERROR", "Jogador não foi criado!");
                }
            } else if ("Arbitro".equals(tipo)) {
                ArbitroDto arbitro = new ArbitroDto(0L, nome, email, nif);
                try {
                    ApiClient.createArbitro(arbitro);
                } catch (Exception e) {
                    success = false;
                }
                if(success){
                    System.out.println("Arbitro criado: " + arbitro);
                    mostrarAlerta("Sucesso", "Arbitro criado com sucesso!");
                } else{
                    System.out.println("Erro a criar Arbitro");
                    mostrarAlerta("ERROR", "Arbitro não foi criado!");
                }                

            } else {
                UtilizadorDto utilizadorDto = new UtilizadorDto(0L, nome, email, nif);
                try {
                    ApiClient.createUtilizador(utilizadorDto);
                } catch (Exception e) {
                    success = false;
                }

                if(success){
                    System.out.println("Utilizador criado: " + utilizadorDto);
                    mostrarAlerta("Sucesso", "Utilizador criado com sucesso!");
                } else{
                    System.out.println("Erro a criar utilizador");
                    mostrarAlerta("ERROR", "Utilizador não foi criado!");
                }

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
            emailField.clear();
            nifField.clear();
            tipoUtilizadorCombo.getSelectionModel().clearSelection();
            //posicaoCombo.getSelectionModel().clearSelection();
            //certificadoCheckbox.setSelected(false);
            //posicaoCombo.setVisible(false);
            //certificadoCheckbox.setVisible(false);
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
