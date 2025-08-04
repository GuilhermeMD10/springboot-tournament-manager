package pt.ul.fc.di.css.soccernowjavafx.dto;

import java.util.ArrayList;
import java.util.List;

import pt.ul.fc.di.css.soccernowjavafx.model.Campeonato_Estado;

public class CampeonatoDto {

    private Long id;

    private List<Long> equipas;

    private List<Long> classificacao;

    private List<Long> jogos;

    private String nome;

    private Campeonato_Estado estado;



    public Campeonato_Estado getEstado() {
        return estado;
    }
    public void setEstado(Campeonato_Estado estado) {
        this.estado = estado;
    }
    public CampeonatoDto(List<Long> equipas, String nome) {
        this.equipas = equipas;
        this.nome = nome;
        this.classificacao = new ArrayList<>();
        this.jogos = new ArrayList<>();
    }
    public CampeonatoDto() {
        this.nome = "";
        this.equipas = new ArrayList<>();
        this.classificacao = new ArrayList<>();
        this.jogos = new ArrayList<>();
    }
    
    public CampeonatoDto(String nome) {
        this.nome = nome;
        this.equipas = new ArrayList<>();
        this.classificacao = new ArrayList<>();
        this.jogos = new ArrayList<>();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getEquipas() {
        return equipas;
    }

    public void setEquipas(List<Long> equipas) {
        this.equipas = equipas;
    }

    public List<Long> getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(List<Long> classificacao) {
        this.classificacao = classificacao;
    }

    public List<Long> getJogos() {
        return jogos;
    }

    public void setJogos(List<Long> jogos) {
        this.jogos = jogos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
