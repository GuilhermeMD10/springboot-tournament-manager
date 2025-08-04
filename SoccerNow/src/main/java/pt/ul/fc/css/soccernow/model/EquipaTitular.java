package pt.ul.fc.css.soccernow.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class EquipaTitular {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Equipa equipa;

    @ManyToMany
    private List<Jogador> jogadores = new ArrayList<>();

    @ManyToOne
    private Jogador guardaRedes;

    public EquipaTitular(){
    }

    public EquipaTitular(Equipa equipa, List<Jogador> jogadores, Jogador guardaRedes){
        this.equipa = equipa;
        this.jogadores = jogadores;
        this.guardaRedes = guardaRedes;
    }

    public Equipa getEquipa() {
        return equipa;
    }

    public void setEquipa(Equipa equipa) {
        this.equipa = equipa;
    }

    public Long getId() {
        return id;
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public void setJogadores(List<Jogador> jogadores) {
        this.jogadores = jogadores;
    }

    public Jogador getGuardaRedes() {
        return guardaRedes;
    }

    public void setGuardaRedes(Jogador guardaRedes) {
        this.guardaRedes = guardaRedes;
    }
}
