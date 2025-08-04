package pt.ul.fc.css.soccernow.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import pt.ul.fc.css.soccernow.model.enums.Posicao;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;


@Entity
public class Jogador extends Utilizador{
    
    @Enumerated(EnumType.STRING)
    private Posicao prefPos;

    @Column(nullable = false)
    private int golos;

    @Column(nullable = false)
    private int amarelos;

    @Column(nullable = false)
    private int vermelhos;

    @Column(nullable = false)
    private int partidasJogadas;

    @ManyToMany
    @JsonBackReference
    @JoinTable(
        name = "jogador_equipas",
        joinColumns = @JoinColumn(name = "jogador_id"),
        inverseJoinColumns = @JoinColumn(name = "equipa_id")
    )
    private List<Equipa> equipas;
    
    public Jogador(){
        super();
    }

    

    public Posicao getPrefPos() {
        return prefPos;
    }

    public void setPrefPos(Posicao prefPos) {
        this.prefPos = prefPos;
    }

    public int getGolos() {
        return golos;
    }

    public void setGolos(int golos) {
        this.golos = golos;
    }

    public int getAmarelos() {
        return amarelos;
    }

    public void setAmarelos(int amarelos) {
        this.amarelos = amarelos;
    }

    public int getVermelhos() {
        return vermelhos;
    }

    public void setVermelhos(int vermelhos) {
        this.vermelhos = vermelhos;
    }

    public int getPartidasJogadas() {
        return partidasJogadas;
    }

    public void setPartidasJogadas(int partidasJogadas) {
        this.partidasJogadas = partidasJogadas;
    }



    public List<Equipa> getEquipas() {
        return equipas;
    }

    

    public void setEquipas(List<Equipa> equipas) {
        this.equipas = equipas;
    }
}
