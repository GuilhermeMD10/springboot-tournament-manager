package pt.ul.fc.di.css.soccernowjavafx.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiCampeonato;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiClient;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiEquipa;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiEquipaTitular;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiJogo;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiResultado;
import pt.ul.fc.di.css.soccernowjavafx.dto.CampeonatoDto;
import pt.ul.fc.di.css.soccernowjavafx.dto.EquipaDto;
import pt.ul.fc.di.css.soccernowjavafx.dto.EquipaTitularDto;
import pt.ul.fc.di.css.soccernowjavafx.dto.JogoDto;
import pt.ul.fc.di.css.soccernowjavafx.dto.ResultadoDto;
import pt.ul.fc.di.css.soccernowjavafx.model.Campeonato_Estado;
import pt.ul.fc.di.css.soccernowjavafx.model.DataModel;

public class CampeonatoController implements ControllerWithModel{

    private DataModel model;

    private Stage stage;

    @FXML private TextField nomeField;  

    @FXML private VBox listaCampeonatosBox;

    private CampeonatoDto campSelecionado;
    
    @FXML private VBox editarCampeonatoBox;

    @FXML private TextField novoNomeField;
    @FXML private FlowPane equipasCampeonatoPane;

    @FXML private Label mensagemLabel;

    @FXML private ComboBox<EquipaDto> equipasDisponiveisComboBox;

    @FXML private VBox jogosCampeonatoBox;

    @FXML private ComboBox<JogoDto> jogosDisponiveisComboBox;




    public void preencherListaCampeonatos() {
        listaCampeonatosBox.getChildren().clear();

        List<CampeonatoDto> camps = ApiCampeonato.getTodosCampeonatos();
        for (CampeonatoDto camp : camps) {
            System.out.println(camp);
            Button campButton = new Button(camp.getNome());
            campButton.setMaxWidth(Double.MAX_VALUE);
            campButton.setOnAction(e -> mostrarDetalhesParaEditar(camp));
            listaCampeonatosBox.getChildren().add(campButton);
        }
    }

    private void carregarEquipasCampeonato() {
        equipasCampeonatoPane.getChildren().clear();

        for (Long equipaId : campSelecionado.getEquipas()) {
            EquipaDto equipa;
            try {
                equipa = ApiEquipa.getEquipaById(equipaId);
                Button btn = new Button(equipa.getNome());
                btn.setOnAction(e -> handleRemoverEquipa(equipa));
                equipasCampeonatoPane.getChildren().add(btn);
            } catch (Exception e) {
                System.out.println("Erro a receber jogador pelo id");
            }
        }
    }

    @FXML
    private void handleAdicionarEquipa() {
        if (campSelecionado.getEstado() != Campeonato_Estado.POR_COMECAR) {
            mensagemLabel.setText("Equipa só pode ser adicionada se o campeonato estiver no estado POR_COMECAR.");
            return;
        }
        EquipaDto equipaSelecionada = equipasDisponiveisComboBox.getValue();
        if (equipaSelecionada != null) {
            try {
                ApiCampeonato.adicionarEquipa(equipaSelecionada.getId(), campSelecionado.getId());
                campSelecionado.getEquipas().add(equipaSelecionada.getId());

                mensagemLabel.setText("Equipa adicionada com sucesso!");
                carregarEquipasDisponiveis();
                carregarEquipasCampeonato();
                carregarJogosDisponiveis();
            } catch (Exception e) {
                System.out.println("Erro ao adicionar equipa ao campeonato.");
                e.printStackTrace();
                mensagemLabel.setText("Erro ao adicionar equipa.");
            }
        } else {
            mensagemLabel.setText("Selecione uma equipa para adicionar.");
        }
    }


    private void carregarJogosDisponiveis() throws Exception {
        List<JogoDto> todosJogos = ApiJogo.getTodosJogos(); 
        List<JogoDto> jogosNoCampeonato = ApiJogo.getJogosCampeonato(campSelecionado.getId());
        Set<Long> equipasNoCampeonato = new HashSet<>(campSelecionado.getEquipas());
        Set<Long> idsJogosNoCampeonato = jogosNoCampeonato.stream()
            .map(JogoDto::getId)
            .collect(Collectors.toSet());

        List<JogoDto> jogosDisponiveis = new ArrayList<>();
        for (JogoDto jogo : todosJogos) {
            boolean valido = true;
                if (idsJogosNoCampeonato.contains(jogo.getId())) {
                    System.out.println("Jogo já está no campeonato");
                    valido = false;
                }
                if(jogo.getCampeonato() != 0){
                    System.out.println("Jogo já está associado a outro campeonato");
                    valido = false;
                }
            EquipaTitularDto visitada = ApiEquipaTitular.getEquipaTitularById(jogo.getEquipaTitularVisitada());
            EquipaTitularDto visitante = ApiEquipaTitular.getEquipaTitularById(jogo.getEquipaTitularVisitante());

            if (!equipasNoCampeonato.contains(visitada.getEquipa()) || !equipasNoCampeonato.contains(visitante.getEquipa())) {
                System.out.println("Equipa nao estah no campeonato");
                valido = false;
            }

            boolean certificado = jogo.getEquipaArbitragem().stream().anyMatch(id -> {
                try {
                    return ApiClient.getArbitroById(id).isCertificado();
                } catch (Exception e) {
                    return false;
                }
            });

            if (!certificado) {
                System.out.println("Arbitro nao certificado");
                valido = false;
            }

            if (valido){
                System.out.println("Jogo valido para adicionar");
                jogosDisponiveis.add(jogo);
            } 
        }

        // Preencher o ComboBox
        jogosDisponiveisComboBox.setItems(FXCollections.observableArrayList(jogosDisponiveis));
        jogosDisponiveisComboBox.getSelectionModel().clearSelection();

        jogosDisponiveisComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(JogoDto jogo, boolean empty) {
                super.updateItem(jogo, empty);
                if (empty || jogo == null) {
                    setText(null);
                } else {
                    setText(formatarJogo(jogo));
                }
            }
        });

        jogosDisponiveisComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(JogoDto jogo, boolean empty) {
                super.updateItem(jogo, empty);
                setText((empty || jogo == null) ? null : formatarJogo(jogo));
            }
        });
    }



    @FXML
    private void handleAdicionarJogo() {
        if (campSelecionado.getEstado() != Campeonato_Estado.POR_COMECAR) {
            mensagemLabel.setText("Jogo so pode ser adicionado se o campeonato estiver no estado POR_COMECAR.");
            return;
        }
        JogoDto jogoSelecionado = jogosDisponiveisComboBox.getValue();
        if (jogoSelecionado != null) {
            try {
                ApiCampeonato.adicionarJogoAoCampeonato(jogoSelecionado.getId(), campSelecionado.getId());
                mensagemLabel.setText("Jogo adicionado com sucesso!");
                carregarJogosDoCampeonato();
                carregarJogosDisponiveis();
            } catch (Exception e) {
                mensagemLabel.setText("Erro ao adicionar jogo.");
                e.printStackTrace();
            }
        } else {
            mensagemLabel.setText("Selecione um jogo para adicionar.");
        }
    }


    private void carregarJogosDoCampeonato() {
        jogosCampeonatoBox.getChildren().clear();

        List<JogoDto> jogos = ApiJogo.getJogosCampeonato(campSelecionado.getId()); 

        if (jogos.isEmpty()) {
            Label vazio = new Label("Nenhum jogo neste campeonato.");
            jogosCampeonatoBox.getChildren().add(vazio);
        } else {
            for (JogoDto jogo : jogos) {
                VBox jogoBox = new VBox();
                Label jogoLabel = new Label(formatarJogo(jogo));
                Button removerBtn = new Button("Remover");
                removerBtn.setOnAction(e -> handleRemoverJogo(jogo));
                jogoBox.getChildren().addAll(jogoLabel, removerBtn);
                jogosCampeonatoBox.getChildren().add(jogoBox);
            }
        }
    }

    private void handleRemoverJogo(JogoDto jogo) {
        if (campSelecionado == null || jogo == null) return;

        if (campSelecionado.getEstado() != Campeonato_Estado.POR_COMECAR) {
            mensagemLabel.setText("Jogo só pode ser removido se o campeonato estiver no estado POR_COMECAR.");
            return;
        }

        try {
            ApiJogo.removerJogo(jogo.getId()); 
            mensagemLabel.setText("Jogo removido com sucesso!");
            carregarJogosDoCampeonato();
            carregarJogosDisponiveis();
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao remover jogo.");
            e.printStackTrace();
        }
    }

    
    @FXML
    private void handleApagarCampeonato() {
        if (campSelecionado == null) return;

        try {
            ApiCampeonato.apagarCampeonato(campSelecionado.getId());
            mensagemLabel.setText("Campeonato apagado com sucesso!");
            campSelecionado = null;
            preencherListaCampeonatos();
            editarCampeonatoBox.setVisible(false);
            editarCampeonatoBox.setManaged(false);
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao apagar campeonato.");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleIniciarCampeonato() {
        if (campSelecionado == null) return;
        if(campSelecionado.getEstado() != Campeonato_Estado.POR_COMECAR) {
            mensagemLabel.setText("Campeonato não pode ser iniciado neste estado.");
            return;
        }
        if (campSelecionado.getEquipas().size() < 8) {
            mensagemLabel.setText("Campeonato deve ter pelo menos 8 equipas.");
            return;
        }
        try {
            ApiCampeonato.iniciarCampeonato(campSelecionado.getId());
            campSelecionado.setEstado(Campeonato_Estado.EM_CURSO);
            mensagemLabel.setText("Campeonato iniciado com sucesso!");
            preencherListaCampeonatos();
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao iniciar campeonato.");
            e.printStackTrace();
        }
    }
    @FXML
    private void handleTerminarCampeonato() {
        if (campSelecionado == null) return;
        if(campSelecionado.getEstado() != Campeonato_Estado.EM_CURSO) {
            mensagemLabel.setText("Campeonato não pode ser terminado neste estado.");
            return;
        }
        if (campSelecionado.getJogos().isEmpty()) {
            mensagemLabel.setText("Campeonato deve ter pelo menos 1 jogo.");
            return;
        }
        try {
            ApiCampeonato.terminarCampeonato(campSelecionado.getId());
            campSelecionado.setEstado(Campeonato_Estado.TERMINADO);
            mensagemLabel.setText("Campeonato terminado com sucesso!");
            preencherListaCampeonatos();
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao terminar campeonato.");
            e.printStackTrace();        
        }   
    }   

    private String formatarJogo(JogoDto jogo) {
        //sacar o nome de cada equipa titular e colocar no string
        
        try {
            EquipaTitularDto equipaTVisitante = ApiEquipaTitular.getEquipaTitularById(jogo.getEquipaTitularVisitante());
            EquipaDto equipaVisitante = ApiEquipa.getEquipaById(equipaTVisitante.getId());
        
            EquipaTitularDto equipaTVisitada = ApiEquipaTitular.getEquipaTitularById(jogo.getEquipaTitularVisitada());
            EquipaDto equipaVisitada = ApiEquipa.getEquipaById(equipaTVisitada.getId());

            //resultado pode ser null ou sej ainda nao aconteceu
            if(jogo.getResultado() == null){
                //jogo ainda nao foi realizado
                return equipaVisitante.getNome() + " " + equipaVisitada.getNome() + " (" + jogo.getData() + ", " + jogo.getLocal() +  ")";
            }
            else{
                ResultadoDto resultado = ApiResultado.getResultadoById(jogo.getResultado());
                String data = jogo.getData().toString(); // ou formatado com DateTimeFormatter
                return equipaVisitante.getNome() + " " + resultado + " " + equipaVisitada.getNome() + " (" + data + ")";
            }
        } catch (Exception e) {
            mensagemLabel.setText("Erros a aceder à informacao dos jogos");
            e.printStackTrace();
            return "";
        }
    
    }


    private void mostrarDetalhesParaEditar(CampeonatoDto campDto) {
        this.campSelecionado = campDto;
        editarCampeonatoBox.setVisible(true);
        editarCampeonatoBox.setManaged(true);
        novoNomeField.setText(campSelecionado.getNome());

        carregarEquipasDisponiveis();
        carregarEquipasCampeonato();
        carregarJogosDoCampeonato();
        try{
            carregarJogosDisponiveis();
        }catch(Exception e){
            mensagemLabel.setText("Erro a carregar jogos disponiveis");
        }
        
        mensagemLabel.setText("");
    }




    private void handleRemoverEquipa(EquipaDto equipa) {
        if (campSelecionado == null || equipa == null) return;

        if (campSelecionado.getEstado() != Campeonato_Estado.POR_COMECAR) {
            mensagemLabel.setText("Equipa só pode ser removida se o campeonato estiver no estado POR_COMECAR.");
            return;
        }
        try {
            ApiCampeonato.removerEquipa(equipa.getId(), campSelecionado.getId());
            campSelecionado.getEquipas().remove(equipa.getId());
            mensagemLabel.setText("Jogador removido com sucesso!");

            carregarEquipasDisponiveis();
            carregarEquipasCampeonato();
        } catch (Exception e) {
            System.out.println("Erro a Remover Jogador ah equipa");
            e.printStackTrace();
            return;
        }

    }


    private void carregarEquipasDisponiveis() {
        List<EquipaDto> todasEquipas;
        try {
            todasEquipas = ApiEquipa.getTodasEquipas();
            List<Long> idsPlantel = campSelecionado.getEquipas();
            List<EquipaDto> equipasSelecionadas = todasEquipas.stream()
                .filter(j -> !idsPlantel.contains(j.getId()))
                .collect(Collectors.toList());

            equipasDisponiveisComboBox.setItems(FXCollections.observableArrayList(equipasSelecionadas));
            equipasDisponiveisComboBox.getSelectionModel().clearSelection();

                equipasDisponiveisComboBox.setCellFactory(lv -> new ListCell<>() {
        @Override
        protected void updateItem(EquipaDto equipa, boolean empty) {
                super.updateItem(equipa, empty);
                setText((empty || equipa == null) ? null : equipa.getNome());
            }
        });

        equipasDisponiveisComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(EquipaDto equipa, boolean empty) {
                super.updateItem(equipa, empty);
                setText((empty || equipa == null) ? null : equipa.getNome());
            }
        });

        } catch (Exception e) {
            System.out.println("Erro a receber todos os jogadores");
            return;
        }
    }

    @FXML
    private void handleGuardarAlteracoes() {
        if (campSelecionado == null) return;

        String novoNome = novoNomeField.getText().trim();
        if (!novoNome.isEmpty() && !novoNome.equals(campSelecionado.getNome())) {
            try {
                ApiCampeonato.atualizarNomeEquipa(campSelecionado.getId(), novoNome);
                campSelecionado.setNome(novoNome);
                mensagemLabel.setText("Nome atualizado com sucesso!");
                preencherListaCampeonatos(); // Atualiza lista com nomes novos
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
        nomeField.clear();

    }

    public void initModel(Stage stage, DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.stage = stage;
        this.model = model;
        editarCampeonatoBox.setVisible(false);
        editarCampeonatoBox.setManaged(false);
        preencherListaCampeonatos();
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
