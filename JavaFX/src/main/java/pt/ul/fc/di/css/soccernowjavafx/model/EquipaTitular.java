package pt.ul.fc.di.css.soccernowjavafx.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EquipaTitular {

    private final LongProperty id = new SimpleLongProperty();
    private final ObjectProperty<Equipa> equipa = new SimpleObjectProperty<>();
    private final ListProperty<Jogador> jogadores = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<Jogador> guardaRedes = new SimpleObjectProperty<>();

    public EquipaTitular() {}

    public EquipaTitular(Equipa equipa, ObservableList<Jogador> jogadores, Jogador guardaRedes) {
        this.equipa.set(equipa);
        this.jogadores.set(jogadores);
        this.guardaRedes.set(guardaRedes);
    }

    public long getId() {
        return id.get();
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public LongProperty idProperty() {
        return id;
    }

    public Equipa getEquipa() {
        return equipa.get();
    }

    public void setEquipa(Equipa equipa) {
        this.equipa.set(equipa);
    }

    public ObjectProperty<Equipa> equipaProperty() {
        return equipa;
    }

    public ObservableList<Jogador> getJogadores() {
        return jogadores.get();
    }

    public void setJogadores(ObservableList<Jogador> jogadores) {
        this.jogadores.set(jogadores);
    }

    public ListProperty<Jogador> jogadoresProperty() {
        return jogadores;
    }

    public Jogador getGuardaRedes() {
        return guardaRedes.get();
    }

    public void setGuardaRedes(Jogador guardaRedes) {
        this.guardaRedes.set(guardaRedes);
    }

    public ObjectProperty<Jogador> guardaRedesProperty() {
        return guardaRedes;
    }
}
