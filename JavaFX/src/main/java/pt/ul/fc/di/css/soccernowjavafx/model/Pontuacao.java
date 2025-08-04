package pt.ul.fc.di.css.soccernowjavafx.model;

import javafx.beans.property.*;

public class Pontuacao {

    private final ObjectProperty<Equipa> equipa = new SimpleObjectProperty<Equipa>();
    private final LongProperty pontos = new SimpleLongProperty(0);

    public Pontuacao() {
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

    public long getPontos() {
        return pontos.get();
    }

    public void setPontos(long pontos) {
        this.pontos.set(pontos);
    }

    public LongProperty pontosProperty() {
        return pontos;
    }

    public void addPontos(long toAdd) {
        this.pontos.set(this.pontos.get() + toAdd);
    }
}
