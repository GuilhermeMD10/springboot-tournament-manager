package pt.ul.fc.css.soccernow.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
//import pt.ul.fc.css.soccernow.model.enums.Modalidade;
import pt.ul.fc.css.soccernow.model.enums.Campeonato_Estado;

@Entity
public class Campeonato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private Modalidade modalidade;

    //juntar variavel STATUS
    //de enum : POR_COMEÇAR, EM_PROGRESSO, TERMINADO 
    
    @ManyToMany
    @JoinTable(
        name = "campeonato_equipa",
        joinColumns = @JoinColumn(name = "campeonato_id"),
        inverseJoinColumns = @JoinColumn(name = "equipa_id")
    )
    private List<Equipa> equipas;

    @ElementCollection
    private List<Pontuacao> classificacao;

    @OneToMany(mappedBy = "campeonato")
    private List<Jogo> jogos;

    //campeonato tem nome
    private String nome;

    @Enumerated(EnumType.STRING)
    private Campeonato_Estado estado;

    public Campeonato(){
        estado = Campeonato_Estado.POR_COMECAR;
        this.equipas = new ArrayList<>();
        this.classificacao = new ArrayList<>();
        this.jogos = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }
    /* 
    public Modalidade getModalidade() {
        return modalidade;
    }

    public void setModalidade(Modalidade modalidade) {
        this.modalidade = modalidade;
    }
    */
    public List<Equipa> getEquipas() {
        return equipas;
    }

    public void setEquipas(List<Equipa> equipas) {
        this.equipas = equipas;
    }

    public List<Pontuacao> getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(List<Pontuacao> classificacao) {
        this.classificacao = classificacao;
    }

    public List<Jogo> getJogos() {
        return jogos;
    }

    public void setJogos(List<Jogo> jogos) {
        this.jogos = jogos;
    }

    public String getNome(){
        return nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }  

    public void addEquipa(Equipa equipa){
        Pontuacao pontuacao = new Pontuacao();
        pontuacao.setEquipa(equipa);
        equipas.add(equipa);
        classificacao.add(pontuacao);
    }

    public void iniciarCampeonato(){
        this.estado = Campeonato_Estado.EM_CURSO;
    }

    public void terminarCampeonato(){
        this.estado = Campeonato_Estado.TERMINADO;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Campeonato_Estado getEstado() {
        return estado;
    }

    public void setEstado(Campeonato_Estado estado) {
        this.estado = estado;
    }

    
}
