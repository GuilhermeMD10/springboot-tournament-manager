package pt.ul.fc.css.soccernow.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Equipa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    //fazer FetchType.LAZY
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "equipas")
    private List<Jogador> plantel;

    
    @ManyToMany
    @JoinTable(
        name = "equipa_conquistas",
        joinColumns = @JoinColumn(name = "equipa_id"),
        inverseJoinColumns = @JoinColumn(name = "campeonato_id")
    )
    private List<Campeonato> conquistas;

    public Equipa(){
        this.nome = "";
        this.plantel = new ArrayList<>();
        this.conquistas = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Jogador> getPlantel() {
        return plantel;
    }

    public void setPlantel(List<Jogador> plantel) {
        this.plantel = plantel;
    }

    public List<Campeonato> getConquistas() {
        return conquistas;
    }

    public void setConquistas(List<Campeonato> conquistas) {
        this.conquistas = conquistas;
    }

    public void addConquista(Campeonato conquista){
        this.conquistas.add(conquista);
    }

    public void removeConquista(Campeonato conquista){
        this.conquistas.remove(conquista);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipa equipa = (Equipa) o;
        return Objects.equals(id, equipa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
