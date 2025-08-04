package pt.ul.fc.di.css.soccernowjavafx.model;

import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Jogador extends Utilizador {

    private final ObjectProperty<Posicao> prefPos = new SimpleObjectProperty<>();
    private final IntegerProperty golos = new SimpleIntegerProperty();
    private final IntegerProperty amarelos = new SimpleIntegerProperty();
    private final IntegerProperty vermelhos = new SimpleIntegerProperty();
    private final IntegerProperty partidasJogadas = new SimpleIntegerProperty();

    private final ObservableList<Equipa> equipas = FXCollections.observableArrayList();

    public Jogador(Long id, String nome, String email, String nif) {
        super(id, nome, email, nif);
    }

    public ObjectProperty<Posicao> prefPosProperty() {
        return prefPos;
    }

    public Posicao getPrefPos() {
        return prefPos.get();
    }

    public void setPrefPos(Posicao prefPos) {
        this.prefPos.set(prefPos);
    }

    public IntegerProperty golosProperty() {
        return golos;
    }

    public int getGolos() {
        return golos.get();
    }

    public void setGolos(int golos) {
        this.golos.set(golos);
    }

    public IntegerProperty amarelosProperty() {
        return amarelos;
    }

    public int getAmarelos() {
        return amarelos.get();
    }

    public void setAmarelos(int amarelos) {
        this.amarelos.set(amarelos);
    }

    public IntegerProperty vermelhosProperty() {
        return vermelhos;
    }

    public int getVermelhos() {
        return vermelhos.get();
    }

    public void setVermelhos(int vermelhos) {
        this.vermelhos.set(vermelhos);
    }

    public IntegerProperty partidasJogadasProperty() {
        return partidasJogadas;
    }

    public int getPartidasJogadas() {
        return partidasJogadas.get();
    }

    public void setPartidasJogadas(int partidasJogadas) {
        this.partidasJogadas.set(partidasJogadas);
    }

    public ObservableList<Equipa> getEquipas() {
        return equipas;
    }

    public void setEquipas(List<Equipa> equipas) {
        this.equipas.setAll(equipas);
    }

    @Override
    public String toString() {
        return "Jogador{" +
            "id=" + getId() +
            ", nome='" + getNome() + '\'' +
            ", email='" + getEmail() + '\'' +
            ", nif='" + getNif() + '\'' +
            ", prefPos=" + prefPos +
            ", golos=" + golos +
            ", amarelos=" + amarelos +
            ", vermelhos=" + vermelhos +
            ", partidasJogadas=" + partidasJogadas +
            ", numeroEquipas=" + (equipas != null ? equipas.size() : 0) +
            '}';
}

}

