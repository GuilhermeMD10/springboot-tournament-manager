package pt.ul.fc.di.css.soccernowjavafx.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Campeonato {

    private final Long id;

    private final StringProperty nome = new SimpleStringProperty();
    private final ObjectProperty<Campeonato_Estado> estado = new SimpleObjectProperty<>(Campeonato_Estado.POR_COMECAR);

    private final ObservableList<Equipa> equipas = FXCollections.observableArrayList();
    private final ObservableList<Pontuacao> classificacao = FXCollections.observableArrayList();
    private final ObservableList<Jogo> jogos = FXCollections.observableArrayList();

    public Campeonato(Long id) {
        this.id = id;
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

    public Campeonato_Estado getEstado() {
        return estado.get();
    }

    public void setEstado(Campeonato_Estado estado) {
        this.estado.set(estado);
    }

    public ObjectProperty<Campeonato_Estado> estadoProperty() {
        return estado;
    }

    public ObservableList<Equipa> getEquipas() {
        return equipas;
    }

    public ObservableList<Pontuacao> getClassificacao() {
        return classificacao;
    }

    public ObservableList<Jogo> getJogos() {
        return jogos;
    }
}

