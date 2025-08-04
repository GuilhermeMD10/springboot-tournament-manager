package pt.ul.fc.css.soccernow.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import pt.ul.fc.css.soccernow.model.enums.Turno;

public class JogoDto {
    private Long id;
    private LocalDate data; 
    private Turno horario;
    private String local;
    private ArbitroDto arbitroPrincipal; //ID do user
    private List<ArbitroDto> equipaArbitragem = new ArrayList<>(); //ID do user
    private EquipaTitularDto equipaTitularVisitada; //ID das equipas
    private EquipaTitularDto equipaTitularVisitante; //ID das equipas
    private ResultadoDto resultado; // id do resultado
    private String campeonato; //ID do campeonato

    public JogoDto(){}

    public JogoDto (ArbitroDto arbitroPrincipal, List<ArbitroDto> equipaArbitragem, EquipaTitularDto equipaTitularVisitante, EquipaTitularDto equipaTitularVisitada,
     ResultadoDto resultado,LocalDate data, Turno horario, String local, String campeonato){
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

    public ArbitroDto getArbitroPrincipal() {
        return arbitroPrincipal;
    }

    public void setArbitroPrincipal(ArbitroDto arbitroPrincipal) {
        this.arbitroPrincipal = arbitroPrincipal;
    }

    public List<ArbitroDto> getEquipaArbitragem() {
        return equipaArbitragem;
    }

    public void setEquipaArbitragem(List<ArbitroDto> equipaArbitragem) {
        this.equipaArbitragem = equipaArbitragem;
    }

    public EquipaTitularDto getEquipaTitularVisitada() {
        return equipaTitularVisitada;
    }

    public void setEquipaTitularVisitada(EquipaTitularDto equipaTitularVisitada) {
        this.equipaTitularVisitada = equipaTitularVisitada;
    }

    public EquipaTitularDto getEquipaTitularVisitante() {
        return equipaTitularVisitante;
    }

    public void setEquipaTitularVisitante(EquipaTitularDto equipaTitularVisitante) {
        this.equipaTitularVisitante = equipaTitularVisitante;
    }

    public ResultadoDto getResultado() {
        return resultado;
    }

    public void setResultado(ResultadoDto resultado) {
        this.resultado = resultado;
    }

    public String getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(String campeonato) {
        this.campeonato = campeonato;
    }
}
