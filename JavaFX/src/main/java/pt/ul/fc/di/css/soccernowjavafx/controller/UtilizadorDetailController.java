package pt.ul.fc.di.css.soccernowjavafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiClient;
import pt.ul.fc.di.css.soccernowjavafx.dto.ArbitroDto;
import pt.ul.fc.di.css.soccernowjavafx.dto.ArbitroDtoUpdate;
import pt.ul.fc.di.css.soccernowjavafx.dto.JogadorDto;
import pt.ul.fc.di.css.soccernowjavafx.dto.JogadorDtoUpdate;
import pt.ul.fc.di.css.soccernowjavafx.dto.UtilizadorDto;
import pt.ul.fc.di.css.soccernowjavafx.model.DataModel;
import pt.ul.fc.di.css.soccernowjavafx.model.Posicao;

public class UtilizadorDetailController implements ControllerWithModel{

    

    private Stage stage;
    private DataModel model;

    private Long id;
    private String tipo;

    @FXML private TextField emailField, nifField;
    @FXML private ComboBox<String> tipoComboBox;

    @FXML private VBox utilizadorSection, jogadorSection, arbitroSection;
    @FXML private HBox acoesSection;

    @FXML private TextField nomeUtilizadorField, emailUtilizadorField, nifUtilizadorField, certificadoField, nomeJogadorField, emailJogadorField, nifJogadorField,nomeArbitroField, emailArbitroField, nifArbitroField;
    @FXML private TextField posicaoField;
    @FXML private ComboBox<Posicao> posicaoComboBox;
    @FXML private CheckBox certificadoCheckBox;




    @FXML private Label mensagemLabel;

    @FXML
    private void initialize() {
        // Opcional: define um valor padrão para o comboBox
        tipoComboBox.getItems().addAll("Jogador", "Arbitro", "Utilizador");
        tipoComboBox.getSelectionModel().selectFirst();
        posicaoComboBox.getItems().setAll(Posicao.values());

        // Esconder todas as secções no inicio
        ocultarSecoes();
    }

    private void ocultarSecoes() {
        utilizadorSection.setVisible(false);
        utilizadorSection.setManaged(false);

        jogadorSection.setVisible(false);
        jogadorSection.setManaged(false);

        arbitroSection.setVisible(false);
        arbitroSection.setManaged(false);

        acoesSection.setVisible(false);
        acoesSection.setManaged(false);
    }

    @FXML
    private void handleProcurarUtilizador() {
        String email = emailField.getText();
        String nif = nifField.getText();
        tipo = tipoComboBox.getValue();

        mensagemLabel.setText("");
        ocultarSecoes();

        try {
            switch (tipo) {
                case "Jogador":
                    JogadorDto jogador = ApiClient.getJogador(email, nif);

                    if (jogador == null) {
                        mensagemLabel.setText("Jogador não encontrado.");
                        mensagemLabel.setTextFill(Color.RED);
                        return;
                    }
                    // Preenche campos
                    //System.out.println(jogador.getId());
                    id = jogador.getId();
                    preencherCamposJogador(jogador);
                    break;

                case "Arbitro":
                    ArbitroDto arbitro = ApiClient.getArbitro(email, nif);
                    if (arbitro == null) {
                        mensagemLabel.setText("Arbitro não encontrado.");
                        mensagemLabel.setTextFill(Color.RED);
                        return;
                    }
                    id = arbitro.getId();
                    preencherCamposArbitro(arbitro);
                    break;

                case "Utilizador":
                    UtilizadorDto utilizador = ApiClient.getUtilizador(email, nif);
                    if (utilizador == null) {
                        mensagemLabel.setText("Utilizador não encontrado.");
                        mensagemLabel.setTextFill(Color.RED);
                        return;
                    }
                    id = utilizador.getId();
                    preencherCamposUtilizador(utilizador);
                    break;
            }
            acoesSection.setVisible(true);
            acoesSection.setManaged(true);
        } catch (Exception e) {
            mensagemLabel.setText("Erro na procura: " + e.getMessage());
            mensagemLabel.setTextFill(Color.RED);
        }
    }

    private void preencherCamposUtilizador(UtilizadorDto u) {
        utilizadorSection.setVisible(true);
        utilizadorSection.setManaged(true);

        nomeUtilizadorField.setText(u.getNome());
        emailUtilizadorField.setText(u.getEmail());
        nifUtilizadorField.setText(u.getNif());

        jogadorSection.setVisible(false);
        jogadorSection.setManaged(false);
        arbitroSection.setVisible(false);
        arbitroSection.setManaged(false);
    }

    private void preencherCamposJogador(JogadorDto j) {
        nomeJogadorField.setText(j.getNome());
        emailJogadorField.setText(j.getEmail());
        nifJogadorField.setText(j.getNif());
        posicaoComboBox.setValue(j.getPrefPos());

        

        jogadorSection.setVisible(true);
        jogadorSection.setManaged(true);

        //posicaoField.setText(j.getPrefPos());
        utilizadorSection.setVisible(false);
        utilizadorSection.setManaged(false);

        arbitroSection.setVisible(false);
        arbitroSection.setManaged(false);
    }

    private void preencherCamposArbitro(ArbitroDto a) {
        nomeArbitroField.setText(a.getNome());
        emailArbitroField.setText(a.getEmail());
        nifArbitroField.setText(a.getNif());
        certificadoCheckBox.setSelected(a.isCertificado());
        
        arbitroSection.setVisible(true);
        arbitroSection.setManaged(true);
        utilizadorSection.setVisible(false);
        utilizadorSection.setManaged(false);

        jogadorSection.setVisible(false);
        jogadorSection.setManaged(false);
    }

    @FXML
    private void handleAlterarUtilizador(ActionEvent event) {
        try {
            switch (this.tipo) {
                case "Jogador":
                    JogadorDtoUpdate jogadorUpdate = new JogadorDtoUpdate();
                    jogadorUpdate.setId(this.id);

                    jogadorUpdate.setNome(nomeJogadorField.getText());
                    jogadorUpdate.setEmail(emailJogadorField.getText());
                    jogadorUpdate.setNif(nifJogadorField.getText());
                    jogadorUpdate.setPrefPos(posicaoComboBox.getValue());
                    
                    JogadorDto jogadorAtualizado = ApiClient.updateJogador(jogadorUpdate);
                    mensagemLabel.setText("Jogador Atualizado Com Sucesso!.");
                    mensagemLabel.setTextFill(Color.GREEN);
                    ocultarSecoes();
                    System.out.println("Jogador atualizado: " + jogadorAtualizado.getNome());
                    break;

                case "Arbitro":
                    ArbitroDtoUpdate arbitroUpdate = new ArbitroDtoUpdate();
                    arbitroUpdate.setId(this.id);
                    arbitroUpdate.setNome(nomeArbitroField.getText());
                    arbitroUpdate.setEmail(emailArbitroField.getText());
                    arbitroUpdate.setNif(nifArbitroField.getText());
                    arbitroUpdate.setCertificado(certificadoCheckBox.isSelected());
                    mensagemLabel.setText("Arbitro Atualizado Com Sucesso!.");
                    mensagemLabel.setTextFill(Color.GREEN);
                    ocultarSecoes();
                    ArbitroDto arbitroAtualizado = ApiClient.updateArbitro(arbitroUpdate);
                    System.out.println("Arbitro atualizado: " + arbitroAtualizado.getNome());
                    break;

                case "Utilizador":
                    UtilizadorDto utilizadorUpdate = new UtilizadorDto();
                    utilizadorUpdate.setId(this.id);
                    utilizadorUpdate.setNome(nomeUtilizadorField.getText());
                    utilizadorUpdate.setEmail(emailUtilizadorField.getText());
                    utilizadorUpdate.setNif(nifUtilizadorField.getText());
                    mensagemLabel.setText("Utilizador Atualizado Com Sucesso!.");
                    mensagemLabel.setTextFill(Color.GREEN);
                    ocultarSecoes();
                    UtilizadorDto utilizadorAtualizado = ApiClient.updateUtilizador(utilizadorUpdate);
                    System.out.println("Utilizador atualizado: " + utilizadorAtualizado.getNome());
                    break;

                default:
                    System.err.println("Tipo desconhecido para update: " + tipo);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Aqui podes mostrar um alerta na UI para o erro
        }
    }


    @FXML
    private void handleApagarUtilizador(ActionEvent event) {
        try {
            ApiClient.deleteRequest(this.tipo, this.id);
        } catch (Exception e) {
            System.out.println("Error a apagar");
            mensagemLabel.setTextFill(Color.RED);
            e.printStackTrace();
        }

        mensagemLabel.setText(this.tipo + " Apagado Com Sucesso!.");
        mensagemLabel.setTextFill(Color.GREEN);
        ocultarSecoes();
    }

    @Override
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
