package pt.ul.fc.di.css.soccernowjavafx.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiClient;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiEquipa;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiEquipaTitular;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiJogo;
import pt.ul.fc.di.css.soccernowjavafx.dto.ArbitroDto;
import pt.ul.fc.di.css.soccernowjavafx.dto.EquipaDto;
import pt.ul.fc.di.css.soccernowjavafx.dto.EquipaTitularDto;
import pt.ul.fc.di.css.soccernowjavafx.dto.JogadorDto;
import pt.ul.fc.di.css.soccernowjavafx.dto.JogoDto;
import pt.ul.fc.di.css.soccernowjavafx.model.DataModel;
import pt.ul.fc.di.css.soccernowjavafx.model.Posicao;
import pt.ul.fc.di.css.soccernowjavafx.model.Turno;


public class CreateJogoController implements ControllerWithModel {

    private Stage stage;
    private DataModel model;

    @FXML private DatePicker dataPicker;
    @FXML private ComboBox<Turno> turnoComboBox;
    @FXML private TextField localField;
    @FXML private VBox arbitrosBox;
    private List<ArbitroDto> arbitrosSelecionados = new ArrayList<>();
    @FXML private Label mensagemLabel;

    @FXML private ComboBox<EquipaDto> visitadaComboBox;
    @FXML private ComboBox<EquipaDto> visitanteComboBox;

    @FXML private VBox jogadoresVisitadaBox;
    @FXML private VBox jogadoresVisitanteBox;

    private EquipaDto equipaVisitadaSelecionada;
    private EquipaDto equipaVisitanteSelecionada;

    private List<JogadorDto> titularesVisitada = new ArrayList<>();
    private List<JogadorDto> titularesVisitante = new ArrayList<>();



    public void initialize() {
        turnoComboBox.setItems(FXCollections.observableArrayList(Turno.values()));
        turnoComboBox.getSelectionModel().selectFirst();

        try {
            List<ArbitroDto> arbitros = ApiClient.getArbitrosDisponiveis();
            arbitrosSelecionados.clear();
            arbitrosBox.getChildren().clear();

            for (ArbitroDto arbitro : arbitros) {
                String texto = arbitro.getNome();
                if (arbitro.isCertificado()) {
                    texto += " (certificado)";
                }
                CheckBox cb = new CheckBox(texto);
                cb.setUserData(arbitro);
                cb.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        arbitrosSelecionados.add(arbitro);
                    } else {
                        arbitrosSelecionados.remove(arbitro);
                    }
                });
                arbitrosBox.getChildren().add(cb);
            }

        } catch (Exception e) {
            mensagemLabel.setText("Erro ao carregar árbitros.");
            e.printStackTrace();
        }
        try {
            List<EquipaDto> equipas = ApiEquipa.getTodasEquipas();
            ObservableList<EquipaDto> obsEquipas = FXCollections.observableArrayList(equipas);

            visitadaComboBox.setItems(obsEquipas);
            visitanteComboBox.setItems(obsEquipas);

            // Factories separadas para cada ComboBox
            Callback<ListView<EquipaDto>, ListCell<EquipaDto>> factoryVisitada = cb -> new ListCell<>() {
                @Override
                protected void updateItem(EquipaDto item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNome());
                }
            };

            Callback<ListView<EquipaDto>, ListCell<EquipaDto>> factoryVisitante = cb -> new ListCell<>() {
                @Override
                protected void updateItem(EquipaDto item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNome());
                }
            };

            // Aplicar factories
            visitadaComboBox.setCellFactory(factoryVisitada);
            visitanteComboBox.setCellFactory(factoryVisitante);

            // ButtonCells independentes
            visitadaComboBox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(EquipaDto item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNome());
                }
            });

            visitanteComboBox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(EquipaDto item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNome());
                }
            });

            visitadaComboBox.setOnAction(e -> {
                equipaVisitadaSelecionada = visitadaComboBox.getValue();
                if (equipaVisitadaSelecionada != null) {
                    mostrarJogadoresParaSelecao(equipaVisitadaSelecionada, jogadoresVisitadaBox, titularesVisitada);
                }
            });

            visitanteComboBox.setOnAction(e -> {
                equipaVisitanteSelecionada = visitanteComboBox.getValue();
                if (equipaVisitanteSelecionada != null) {
                    mostrarJogadoresParaSelecao(equipaVisitanteSelecionada, jogadoresVisitanteBox, titularesVisitante);
                }
            });


        } catch (Exception e) {
            mensagemLabel.setText("Erro ao carregar equipas.");
            e.printStackTrace();
        }
    }


    private String formatarPosicao(Posicao pos) {
        return switch (pos) {
            case GUARDA_REDES -> "Guarda-Redes";
            case FIXO -> "Fixo";
            case ALA -> "Ala";
            case PIVO -> "Pivô";
        };
    }

    @FXML
    private void confirmarTitularesVisitada() {
        titularesVisitada.clear();
        for (var node : jogadoresVisitadaBox.getChildren()) {
            if (node instanceof CheckBox cb && cb.isSelected()) {
                titularesVisitada.add((JogadorDto) cb.getUserData());
            }
        }
        if (validarTitulares(titularesVisitada)) {
            mensagemLabel.setText("Titulares da equipa visitada confirmados.");
        } else {
            mensagemLabel.setText("Selecione pelo menos 5 jogadores, incluindo 1 guarda-redes.");
        }
    }

    private void mostrarJogadoresParaSelecao(EquipaDto equipa, VBox container, List<JogadorDto> listaTitulares) {
        container.getChildren().clear();
        listaTitulares.clear();

        for (Long jogadorId : equipa.getPlantel()) {
            try {
                JogadorDto jogador = ApiClient.getJogadorById(jogadorId);
                CheckBox cb;
                if(jogador.getPrefPos() == null){
                    cb = new CheckBox(jogador.getNome() + " (" + "Sem Posicao" + ")");
                }
                else{
                    cb = new CheckBox(jogador.getNome() + " (" + formatarPosicao(jogador.getPrefPos()) + ")");
                }
                cb.setUserData(jogador);
                container.getChildren().add(cb);
            } catch (Exception e) {
                System.out.println("Erro a carregar jogador: " + jogadorId);
            }
        }
    }

    @FXML
    private void confirmarTitularesVisitante() {
        titularesVisitante.clear();
        for (var node : jogadoresVisitanteBox.getChildren()) {
            if (node instanceof CheckBox cb && cb.isSelected()) {
                titularesVisitante.add((JogadorDto) cb.getUserData());
            }
        }
        if (validarTitulares(titularesVisitante)) {
            mensagemLabel.setText("Titulares da equipa visitante confirmados.");
        } else {
            mensagemLabel.setText("Selecione pelo menos 5 jogadores, incluindo 1 guarda-redes.");
        }
    }


    @FXML
    private void handleSelecionarVisitada() {
        equipaVisitadaSelecionada = visitadaComboBox.getValue();
        atualizarComboVisitante();
        carregarJogadores(equipaVisitadaSelecionada, jogadoresVisitadaBox, true);
    }

    @FXML
    private void handleSelecionarVisitante() {
        equipaVisitanteSelecionada = visitanteComboBox.getValue();
        atualizarComboVisitada();
        carregarJogadores(equipaVisitanteSelecionada, jogadoresVisitanteBox, false);
    }

    private void atualizarComboVisitante() {
        EquipaDto selecionada = equipaVisitadaSelecionada;
        visitanteComboBox.setItems(
            FXCollections.observableArrayList(
                ApiEquipa.getTodasEquipas().stream()
                    .filter(e -> !e.equals(selecionada))
                    .toList()
            )
        );
    }

    private void atualizarComboVisitada() {
        EquipaDto selecionada = equipaVisitanteSelecionada;
        visitadaComboBox.setItems(
            FXCollections.observableArrayList(
                ApiEquipa.getTodasEquipas().stream()
                    .filter(e -> !e.equals(selecionada))
                    .toList()
            )
        );
    }

    private void carregarJogadores(EquipaDto equipa, VBox container, boolean isVisitada) {
        container.getChildren().clear();

        if (equipa == null) return;

        for (Long id : equipa.getPlantel()) {
            try {
                JogadorDto jogador = ApiClient.getJogadorById(id);
                CheckBox cb = new CheckBox(jogador.getNome());
                cb.setUserData(jogador);

                container.getChildren().add(cb);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleConfirmarTitularesVisitada() {
        titularesVisitada = new ArrayList<>( // ← cria nova lista mutável
            jogadoresVisitadaBox.getChildren().stream()
                .filter(n -> n instanceof CheckBox && ((CheckBox) n).isSelected())
                .map(n -> (JogadorDto) ((CheckBox) n).getUserData())
                .toList()
        );

        if (!validarTitulares(titularesVisitada)) {
            mensagemLabel.setText("Visitada: selecione pelo menos 5 jogadores e 1 guarda-redes.");
            return;
        }

        mensagemLabel.setText("Titulares da equipa visitada confirmados.");
    }

    @FXML
    private void handleConfirmarTitularesVisitante() {
        titularesVisitante = new ArrayList<>(
            jogadoresVisitanteBox.getChildren().stream()
                .filter(n -> n instanceof CheckBox && ((CheckBox) n).isSelected())
                .map(n -> (JogadorDto) ((CheckBox) n).getUserData())
                .toList()
        );


        if (!validarTitulares(titularesVisitante)) {
            mensagemLabel.setText("Visitante: selecione pelo menos 5 jogadores e 1 guarda-redes.");
            return;
        }

        mensagemLabel.setText("Titulares da equipa visitante confirmados.");
    }

    private boolean validarTitulares(List<JogadorDto> titulares) {
        if (titulares.size() < 5) {
            mensagemLabel.setText("Deve selecionar pelo menos 5 jogadores.");
            return false;
        }

        boolean temGuardaRedes = titulares.stream()
            .anyMatch(jogador -> jogador.getPrefPos() == Posicao.GUARDA_REDES);

        if (!temGuardaRedes) {
            mensagemLabel.setText("A equipa deve ter pelo menos um guarda-redes.");
            return false;
        }

        mensagemLabel.setText("");
        return true;
    }




    @FXML
    private void handleCriarJogo() {
        LocalDate data = dataPicker.getValue();
        Turno turno = turnoComboBox.getValue();
        String local = localField.getText();

        List<ArbitroDto> selecionados = new ArrayList<>(arbitrosSelecionados);

        if (data == null || turno == null || local.isBlank() || selecionados.isEmpty()) {
            mensagemLabel.setText("Preencha todos os campos e selecione árbitros.");
            limparCamposGerais();
            return;
        }
        if(titularesVisitada.equals(titularesVisitante)){
            mensagemLabel.setText("Não é permitido criação de jogos entre a mesma equipa");
            limparEquipas();
            return;
        }
        if(!validarTitulares(titularesVisitada) || !validarTitulares(titularesVisitante)){
            limparTitulares();
            return;
        }
        Long guardaRedesVisitadaId = titularesVisitada.stream()
            .filter(j -> j.getPrefPos() == Posicao.GUARDA_REDES)
            .map(JogadorDto::getId)
            .findFirst()
            .orElse(null);

        EquipaTitularDto equipaTitularVisitada = new EquipaTitularDto(
            equipaVisitadaSelecionada.getId(),
            titularesVisitada.stream().map(JogadorDto::getId).toList(),
            guardaRedesVisitadaId
        );

        Long guardaRedesVisitanteId = titularesVisitante.stream()
            .filter(j -> j.getPrefPos() == Posicao.GUARDA_REDES)
            .map(JogadorDto::getId)
            .findFirst()
            .orElse(null);

        EquipaTitularDto equipaTitularVisitante = new EquipaTitularDto(
            equipaVisitanteSelecionada.getId(),
            titularesVisitante.stream().map(JogadorDto::getId).toList(),
            guardaRedesVisitanteId
        );

        Long arbitroPrincipal = selecionados.get(0).getId();
        List<Long> equipaArbitragem = selecionados.stream()
                                                  .map(ArbitroDto::getId)
                                                  .toList();

        try{
            EquipaTitularDto equipaTVisitada = ApiEquipaTitular.createEquipaTitular(equipaTitularVisitada);
            EquipaTitularDto equipaTVisitante = ApiEquipaTitular.createEquipaTitular(equipaTitularVisitante);
            JogoDto jogo = new JogoDto();
            jogo.setArbitroPrincipal(arbitroPrincipal);
            jogo.setData(data);
            jogo.setEquipaArbitragem(equipaArbitragem);
            jogo.setHorario(turno);
            jogo.setLocal(local);
            jogo.setEquipaTitularVisitada(equipaTVisitada.getId());
            jogo.setEquipaTitularVisitante(equipaTVisitante.getId());
            jogo.setCampeonato(0L);
            ApiJogo.createJogo(jogo);
            mensagemLabel.setText("Jogo criado com sucesso!");
        }catch(Exception e ){
            mensagemLabel.setText("Criação das Equipas Titulares falhou, criação de jogo falhou");
            return;
        }
    }
    private void limparCamposGerais() {
        dataPicker.setValue(null);
        turnoComboBox.setValue(null);
        localField.clear();

        limparEquipas();
    }

    private void limparEquipas() {
        visitadaComboBox.setValue(null);
        visitanteComboBox.setValue(null);

        equipaVisitadaSelecionada = null;
        equipaVisitanteSelecionada = null;

        limparTitulares();
    }

    private void limparTitulares() {
        jogadoresVisitadaBox.getChildren().clear();
        jogadoresVisitanteBox.getChildren().clear();

        titularesVisitada.clear();
        titularesVisitante.clear();
    }


    @Override
    public void initModel(Stage stage, DataModel model) {
        this.stage = stage;
        this.model = model;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void back() {
        Util.switchScene(stage, "/pt/ul/fc/di/css/soccernowjavafx/view/main_menu.fxml", "Main Menu", model);
    }

}
