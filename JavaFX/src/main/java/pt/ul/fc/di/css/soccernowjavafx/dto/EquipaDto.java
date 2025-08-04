package pt.ul.fc.di.css.soccernowjavafx.dto;

import java.util.ArrayList;
import java.util.List;


public class EquipaDto {

    private Long id;
    private String nome;

    private List<Long> plantel;

    //ids dos campeonatos em que participou
    private List<Long> conquistas;

    public EquipaDto(){
        this.nome = "";
        this.plantel = new ArrayList<>();
        this.conquistas = new ArrayList<>();
    };

    public EquipaDto(String nome){
        this.nome = nome;
        this.plantel = new ArrayList<>();
        this.conquistas = new ArrayList<>();
    };
    
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Long> getPlantel() {
        return plantel;
    }

    public void setPlantel(List<Long> plantel) {
        this.plantel = plantel;
    }
    
    public List<Long> getConquistas() {
        return conquistas;
    }

    public void setConquistas(List<Long> conquistas) {
        this.conquistas = conquistas;
    }

    public void setId(Long id2) {
        id = id2;
    }

    @Override
    public String toString() {
        return nome != null ? nome : "Equipa sem nome";
    }
}


