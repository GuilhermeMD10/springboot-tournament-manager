package pt.ul.fc.css.soccernow.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

@Embeddable
public class Pontuacao {
    @ManyToOne
    private Equipa equipa;
    private Long pontos;

    public Pontuacao(){
        equipa = new Equipa();
        pontos = (long) 0;
    }
    public Equipa getEquipa() {
        return equipa;
    }
    public void setEquipa(Equipa equipa) {
        this.equipa = equipa;
    }
    public Long getPontos() {
        return pontos;
    }
    public void setPontos(Long pontos) {
        this.pontos = pontos;
    }
    public void addPontos(Long toAdd){
        this.pontos = this.pontos + toAdd;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.getEquipa().getNome()).append(":").append(this.getPontos());
        return sb.toString();
    }
}

