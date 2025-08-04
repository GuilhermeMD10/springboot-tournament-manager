package pt.ul.fc.di.css.soccernowjavafx.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import pt.ul.fc.di.css.soccernowjavafx.model.Turno;


public class JogoDto {
    private Long id;
    private LocalDate data; 
    private Turno horario;
    private String local;
    private Long arbitroPrincipal; //ID do user
    private List<Long> equipaArbitragem = new ArrayList<>(); //ID do user
    private Long equipaTitularVisitada; //ID das equipas
    private Long equipaTitularVisitante; //ID das equipas
    private Long resultado; // id do resultado
    private Long campeonato; //ID do campeonato

    public JogoDto(){}

    public JogoDto (Long arbitroPrincipal, List<Long> equipaArbitragem, Long equipaTitularVisitante, Long equipaTitularVisitada,
     Long resultado,LocalDate data, Turno horario, String local, Long campeonato){
        this.arbitroPrincipal = arbitroPrincipal;
        this.equipaArbitragem = equipaArbitragem;
        this.equipaTitularVisitante = equipaTitularVisitante;
        this.equipaTitularVisitada = equipaTitularVisitada;
        this.resultado = resultado;
        this.data = data;
        this.horario = horario;
        this.local = local;
        this.campeonato = campeonato;
     }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Turno getHorario() {
        return horario;
    }

    public void setHorario(Turno horario) {
        this.horario = horario;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Long getArbitroPrincipal() {
        return arbitroPrincipal;
    }

    public void setArbitroPrincipal(Long arbitroPrincipal) {
        this.arbitroPrincipal = arbitroPrincipal;
    }

    public List<Long> getEquipaArbitragem() {
        return equipaArbitragem;
    }

    public void setEquipaArbitragem(List<Long> equipaArbitragem) {
        this.equipaArbitragem = equipaArbitragem;
    }

    public Long getEquipaTitularVisitada() {
        return equipaTitularVisitada;
    }

    public void setEquipaTitularVisitada(Long equipaTitularVisitada) {
        this.equipaTitularVisitada = equipaTitularVisitada;
    }

    public Long getEquipaTitularVisitante() {
        return equipaTitularVisitante;
    }

    public void setEquipaTitularVisitante(Long equipaTitularVisitante) {
        this.equipaTitularVisitante = equipaTitularVisitante;
    }

    public Long getResultado() {
        return resultado;
    }

    public void setResultado(Long resultado) {
        this.resultado = resultado;
    }

    public Long getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(Long campeonato) {
        this.campeonato = campeonato;
    }
}
