package pt.ul.fc.di.css.soccernowjavafx.model;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Equipa {

    private final long id;
    private final StringProperty nome = new SimpleStringProperty();

    private final ObservableList<Jogador> plantel = FXCollections.observableArrayList();
    private final ObservableList<Campeonato> conquistas = FXCollections.observableArrayList();

    public Equipa(long id) {
        this.id = id;
        this.nome.set("");
    }

    public Equipa(long id, String nome) {
        this.id = id;
        this.nome.set(nome);
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public ObservableList<Jogador> getPlantel() {
        return plantel;
    }

    public void setPlantel(ObservableList<Jogador> plantel) {
        this.plantel.setAll(plantel);
    }

    public void setPlantel(List<Jogador> plantel) {
        this.plantel.setAll(plantel);
    }

    public ObservableList<Campeonato> getConquistas() {
        return conquistas;
    }

    public void setConquistas(ObservableList<Campeonato> conquistas) {
        this.conquistas.setAll(conquistas);
    }

    public void setConquistas(List<Campeonato> conquistas) {
        this.conquistas.setAll(conquistas);
    }

    @Override
    public String toString() {
        return "Equipa{id=" + getId() + ", nome=" + getNome() + ", plantelSize=" + plantel.size() + ", conquistasSize=" + conquistas.size() + "}";
    }
}
