package pt.ul.fc.di.css.soccernowjavafx.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiClient;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiEquipa;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiEquipaTitular;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiJogo;
import pt.ul.fc.di.css.soccernowjavafx.api.ApiResultado;
import pt.ul.fc.di.css.soccernowjavafx.dto.EquipaDto;
import pt.ul.fc.di.css.soccernowjavafx.dto.EquipaTitularDto;
import pt.ul.fc.di.css.soccernowjavafx.dto.JogadorDto;
import pt.ul.fc.di.css.soccernowjavafx.dto.JogoDto;
import pt.ul.fc.di.css.soccernowjavafx.dto.ResultadoDto;
import pt.ul.fc.di.css.soccernowjavafx.model.DataModel;

public class AddResultadoController  implements ControllerWithModel{

    private DataModel model;
    private Stage stage;
    @FXML private ComboBox<JogoDto> jogosComboBox;
    @FXML private ComboBox<EquipaTitularDto> vencedorComboBox;
    @FXML private VBox jogadoresVisitadaBox;
    @FXML private VBox jogadoresVisitanteBox;

    private Map<Long, JogadorControls> controlsVisitada = new HashMap<>();
    private Map<Long, JogadorControls> controlsVisitante = new HashMap<>();

    @FXML
    public void initialize() {
        try {
            List<JogoDto> jogosSemResultado = ApiJogo.getJogosSemResultado(); 
            jogosComboBox.setItems(FXCollections.observableArrayList(jogosSemResultado));

            jogosComboBox.setCellFactory(cb -> new ListCell<>() {
                @Override protected void updateItem(JogoDto jogo, boolean empty) {
                    super.updateItem(jogo, empty);
                    setText((jogo == null || empty) ? null : formatarJogo(jogo));
                }
            });

            jogosComboBox.setButtonCell(new ListCell<>() {
                @Override protected void updateItem(JogoDto jogo, boolean empty) {
                    super.updateItem(jogo, empty);
                    setText((jogo == null || empty) ? null : formatarJogo(jogo));
                }
            });

            jogosComboBox.setOnAction(e -> carregarJogadoresParaJogo());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void carregarJogadoresParaJogo() {
        try {
            jogadoresVisitadaBox.getChildren().clear();
            jogadoresVisitanteBox.getChildren().clear();
            controlsVisitada.clear();
            controlsVisitante.clear();
            vencedorComboBox.getItems().clear();

            JogoDto jogo = jogosComboBox.getValue();
            if (jogo == null) return;

            EquipaTitularDto visitada = ApiEquipaTitular.getEquipaTitularById(jogo.getEquipaTitularVisitada());
            EquipaTitularDto visitante = ApiEquipaTitular.getEquipaTitularById(jogo.getEquipaTitularVisitante());

            vencedorComboBox.getItems().addAll(visitada, visitante);

            preencherJogadores(visitada, jogadoresVisitadaBox, controlsVisitada);
            preencherJogadores(visitante, jogadoresVisitanteBox, controlsVisitante);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void preencherJogadores(EquipaTitularDto equipa, VBox container, Map<Long, JogadorControls> controlMap) throws Exception {
        for (Long jogadorId : equipa.getJogadores()) {
            JogadorDto jogador = ApiClient.getJogadorById(jogadorId);

            Label nome = new Label(jogador.getNome());

            Spinner<Integer> golos = new Spinner<>(0, 10, 0);
            CheckBox amarelo = new CheckBox("Amarelo");
            CheckBox vermelho = new CheckBox("Vermelho");

            HBox linha = new HBox(10, nome, new Label("Golos:"), golos, amarelo, vermelho);
            linha.setAlignment(Pos.CENTER_LEFT);
            container.getChildren().add(linha);

            controlMap.put(jogador.getId(), new JogadorControls(golos, amarelo, vermelho));
        }
    }

    @FXML
    private void handleSubmeterResultado() {
        try {
            JogoDto jogo = jogosComboBox.getValue();
            Long vencedorId = vencedorComboBox.getValue().getId();

            ResultadoDto resultado = new ResultadoDto();
            resultado.setVencedor(vencedorId);

            preencherListas(controlsVisitada, resultado.getGolosVisitado(),
                            resultado.getAmarelosVisitado(), resultado.getVermelhosVisitado());
            preencherListas(controlsVisitante, resultado.getGolosVisitante(),
                            resultado.getAmarelosVisitante(), resultado.getVermelhosVisitante());

            ApiResultado.submeterResultado(resultado, jogo.getId());



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void preencherListas(Map<Long, JogadorControls> mapa, List<Long> golos,
                                 List<Long> amarelos, List<Long> vermelhos) {
        for (Map.Entry<Long, JogadorControls> entry : mapa.entrySet()) {
            Long jogadorId = entry.getKey();
            JogadorControls jc = entry.getValue();

            for (int i = 0; i < jc.golos.getValue(); i++) golos.add(jogadorId);
            if (jc.amarelo.isSelected()) amarelos.add(jogadorId);
            if (jc.vermelho.isSelected()) vermelhos.add(jogadorId);
        }
    }

    private static class JogadorControls {
        Spinner<Integer> golos;
        CheckBox amarelo;
        CheckBox vermelho;

        JogadorControls(Spinner<Integer> golos, CheckBox amarelo, CheckBox vermelho) {
            this.golos = golos;
            this.amarelo = amarelo;
            this.vermelho = vermelho;
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
                return equipaVisitante.getNome() + " " + equipaVisitada.getNome() + " (" + jogo.getData() + ", local: " + jogo.getLocal() +  ")";
            }
            else{
                ResultadoDto resultado = ApiResultado.getResultadoById(jogo.getResultado());
                String data = jogo.getData().toString(); 
                return equipaVisitante.getNome() + " " + resultado + " " + equipaVisitada.getNome() + " (" + data + ")";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    
    }


    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
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
