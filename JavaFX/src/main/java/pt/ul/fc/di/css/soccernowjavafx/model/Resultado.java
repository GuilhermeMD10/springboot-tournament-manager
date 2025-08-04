package pt.ul.fc.di.css.soccernowjavafx.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Resultado {

    private final LongProperty id = new SimpleLongProperty();
    private final ObjectProperty<EquipaTitular> vencedor = new SimpleObjectProperty<>();

    private final ListProperty<Jogador> golosVisitado = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<Jogador> golosVisitante = new SimpleListProperty<>(FXCollections.observableArrayList());

    private final ListProperty<Jogador> amarelosVisitado = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<Jogador> amarelosVisitante = new SimpleListProperty<>(FXCollections.observableArrayList());

    private final ListProperty<Jogador> vermelhosVisitado = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<Jogador> vermelhosVisitante = new SimpleListProperty<>(FXCollections.observableArrayList());

    public Resultado() {}

    public Resultado(EquipaTitular vencedor,
                     ObservableList<Jogador> golosVisitado, ObservableList<Jogador> golosVisitante,
                     ObservableList<Jogador> amarelosVisitado, ObservableList<Jogador> amarelosVisitante,
                     ObservableList<Jogador> vermelhosVisitado, ObservableList<Jogador> vermelhosVisitante) {

        this.vencedor.set(vencedor);
        this.golosVisitado.set(golosVisitado);
        this.golosVisitante.set(golosVisitante);
        this.amarelosVisitado.set(amarelosVisitado);
        this.amarelosVisitante.set(amarelosVisitante);
        this.vermelhosVisitado.set(vermelhosVisitado);
        this.vermelhosVisitante.set(vermelhosVisitante);
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

    public EquipaTitular getVencedor() {
        return vencedor.get();
    }

    public void setVencedor(EquipaTitular vencedor) {
        this.vencedor.set(vencedor);
    }

    public ObjectProperty<EquipaTitular> vencedorProperty() {
        return vencedor;
    }

    public ObservableList<Jogador> getGolosVisitado() {
        return golosVisitado.get();
    }

    public void setGolosVisitado(ObservableList<Jogador> jogadores) {
        this.golosVisitado.set(jogadores);
    }

    public ListProperty<Jogador> golosVisitadoProperty() {
        return golosVisitado;
    }

    public ObservableList<Jogador> getGolosVisitante() {
        return golosVisitante.get();
    }

    public void setGolosVisitante(ObservableList<Jogador> jogadores) {
        this.golosVisitante.set(jogadores);
    }

    public ListProperty<Jogador> golosVisitanteProperty() {
        return golosVisitante;
    }

    public ObservableList<Jogador> getAmarelosVisitado() {
        return amarelosVisitado.get();
    }

    public void setAmarelosVisitado(ObservableList<Jogador> jogadores) {
        this.amarelosVisitado.set(jogadores);
    }

    public ListProperty<Jogador> amarelosVisitadoProperty() {
        return amarelosVisitado;
    }

    public ObservableList<Jogador> getAmarelosVisitante() {
        return amarelosVisitante.get();
    }

    public void setAmarelosVisitante(ObservableList<Jogador> jogadores) {
        this.amarelosVisitante.set(jogadores);
    }

    public ListProperty<Jogador> amarelosVisitanteProperty() {
        return amarelosVisitante;
    }

    public ObservableList<Jogador> getVermelhosVisitado() {
        return vermelhosVisitado.get();
    }

    public void setVermelhosVisitado(ObservableList<Jogador> jogadores) {
        this.vermelhosVisitado.set(jogadores);
    }

    public ListProperty<Jogador> vermelhosVisitadoProperty() {
        return vermelhosVisitado;
    }

    public ObservableList<Jogador> getVermelhosVisitante() {
        return vermelhosVisitante.get();
    }

    public void setVermelhosVisitante(ObservableList<Jogador> jogadores) {
        this.vermelhosVisitante.set(jogadores);
    }

    public ListProperty<Jogador> vermelhosVisitanteProperty() {
        return vermelhosVisitante;
    }
}
