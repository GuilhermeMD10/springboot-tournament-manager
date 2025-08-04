package pt.ul.fc.di.css.soccernowjavafx.controller;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiClient;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiEquipa;
import pt.ul.fc.di.css.soccernowjavafx.dto.EquipaDto;
import pt.ul.fc.di.css.soccernowjavafx.dto.JogadorDto;
import pt.ul.fc.di.css.soccernowjavafx.model.*;

public class EquipaDetailController implements ControllerWithModel{

    private DataModel model;

    private Stage stage;

    @FXML private VBox listaEquipasBox;
    @FXML private VBox editarEquipaBox;

    @FXML private TextField novoNomeField;
    @FXML private ComboBox<JogadorDto> jogadoresDisponiveisComboBox;
    @FXML private FlowPane jogadoresEquipaPane;
    @FXML private Label mensagemLabel;

    private EquipaDto equipaSelecionada;

    
    public void preencherListaEquipas() {
        listaEquipasBox.getChildren().clear();

        List<EquipaDto> equipas = ApiEquipa.getTodasEquipas();
        for (EquipaDto equipa : equipas) {
            Button equipaButton = new Button(equipa.getNome());
            equipaButton.setMaxWidth(Double.MAX_VALUE);
            equipaButton.setOnAction(e -> mostrarDetalhesParaEditar(equipa));
            listaEquipasBox.getChildren().add(equipaButton);
        }
    }
//agr deste lado, 
    @FXML
    private void handleApagarEquipa() {
        if (equipaSelecionada == null) {
            mensagemLabel.setText("Nenhuma equipa selecionada para apagar.");
            return;
        }
        try {
            ApiEquipa.apagarEquipa(equipaSelecionada.getId());
            mensagemLabel.setText("Equipa apagada com sucesso!");
            equipaSelecionada = null;
            preencherListaEquipas();
            editarEquipaBox.setVisible(false);
            editarEquipaBox.setManaged(false);
        } catch (Exception e) {
            System.out.println("Erro ao apagar a equipa");
            e.printStackTrace();
            mensagemLabel.setText("Erro ao apagar a equipa.");
        }
    }



    
    private void mostrarDetalhesParaEditar(EquipaDto equipa) {
        this.equipaSelecionada = equipa;
        editarEquipaBox.setVisible(true);
        editarEquipaBox.setManaged(true);
        novoNomeField.setText(equipa.getNome());

        carregarJogadoresDisponiveis();
        carregarJogadoresDaEquipa();
        mensagemLabel.setText("");
    }

    private void carregarJogadoresDisponiveis() {
        List<JogadorDto> todosJogadores;
        try {
            todosJogadores = ApiClient.getTodosJogadores();
            List<Long> idsPlantel = equipaSelecionada.getPlantel();
            List<JogadorDto> jogadoresDisponiveis = todosJogadores.stream()
                .filter(j -> !idsPlantel.contains(j.getId()))
                .collect(Collectors.toList());

            jogadoresDisponiveisComboBox.setItems(FXCollections.observableArrayList(jogadoresDisponiveis));
            jogadoresDisponiveisComboBox.getSelectionModel().clearSelection();

                jogadoresDisponiveisComboBox.setCellFactory(lv -> new ListCell<>() {
        @Override
        protected void updateItem(JogadorDto jogador, boolean empty) {
                super.updateItem(jogador, empty);
                setText((empty || jogador == null) ? null : jogador.getNome());
            }
        });

        jogadoresDisponiveisComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(JogadorDto jogador, boolean empty) {
                super.updateItem(jogador, empty);
                setText((empty || jogador == null) ? null : jogador.getNome());
            }
        });

        } catch (Exception e) {
            System.out.println("Erro a receber todos os jogadores");
            return;
        }
    }

    private void carregarJogadoresDaEquipa() {
        jogadoresEquipaPane.getChildren().clear();

        for (Long jogadorId : equipaSelecionada.getPlantel()) {
            JogadorDto jogador;
            try {
                jogador = ApiClient.getJogadorById(jogadorId);
                Button btn = new Button(jogador.getNome());
                btn.setOnAction(e -> handleRemoverJogador(jogador));
                jogadoresEquipaPane.getChildren().add(btn);
            } catch (Exception e) {
                System.out.println("Erro a receber jogador pelo id");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleAdicionarJogador() {
        JogadorDto selecionado = jogadoresDisponiveisComboBox.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mensagemLabel.setText("Por favor, selecione um jogador para adicionar.");
            return;
        }
        try {
            ApiEquipa.adicionarJogador(selecionado.getId(), equipaSelecionada.getId());
            equipaSelecionada.getPlantel().add(selecionado.getId());
            mensagemLabel.setText("Jogador adicionado com sucesso!");

            carregarJogadoresDisponiveis();
            carregarJogadoresDaEquipa();
        } catch (Exception e) {
            System.out.println("Erro a Adicionar Jogador ah equipa");
            e.printStackTrace();
            return;
        }
    }

    private void handleRemoverJogador(JogadorDto jogador) {
        try {
            ApiEquipa.removerJogador(jogador.getId(), equipaSelecionada.getId());
            equipaSelecionada.getPlantel().remove(jogador.getId());
            mensagemLabel.setText("Jogador removido com sucesso!");

            carregarJogadoresDisponiveis();
            carregarJogadoresDaEquipa();
        } catch (Exception e) {
            System.out.println("Erro a Remover Jogador ah equipa");
            e.printStackTrace();
            return;
        }

    }

    @FXML
    private void handleGuardarAlteracoes() {
        if (equipaSelecionada == null) return;

        String novoNome = novoNomeField.getText().trim();
        if (!novoNome.isEmpty() && !novoNome.equals(equipaSelecionada.getNome())) {
            try {
                ApiEquipa.atualizarNomeEquipa(equipaSelecionada.getId(), novoNome);
                equipaSelecionada.setNome(novoNome);
                mensagemLabel.setText("Nome atualizado com sucesso!");
                preencherListaEquipas(); // Atualiza lista com nomes novos
                limparCampos();
            } catch (Exception e) {
                System.out.println("Erro a atualizar a equipa");
                e.printStackTrace();
            }

        } else {
            mensagemLabel.setText("Nenhuma alteração no nome para guardar.");
        }
    }



    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void limparCampos() {
        novoNomeField.clear();

    }

    public void initModel(Stage stage, DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.stage = stage;
        this.model = model;
        editarEquipaBox.setVisible(false);
        editarEquipaBox.setManaged(false);
        preencherListaEquipas();
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
