package pt.ul.fc.css.soccernow.dto;

import java.util.ArrayList;
import java.util.List;

public class ResultadoDto {
    private Long id;
    private Long vencedor; //ID da equipa
    private List<Long> golosVisitado = new ArrayList<>(); //Longs sao os ID's dos jogadores
    private List<Long> golosVisitante = new ArrayList<>();
    private List<Long> amarelosVisitado = new ArrayList<>();
    private List<Long> amarelosVisitante = new ArrayList<>();
    private List<Long> vermelhosVisitado = new ArrayList<>();
    private List<Long> vermelhosVisitante = new ArrayList<>();

    public ResultadoDto(){}

    public ResultadoDto(Long vencedor,List<Long> golosVisitado, List<Long> golosVisitante,
                 List<Long> amarelosVisitado, List<Long> amarelosVisitante,
                 List<Long> vermelhosVisitado,List<Long> vermelhosVisitante) {
        this.vencedor = vencedor;
        this.golosVisitado = golosVisitado;
        this.golosVisitante = golosVisitante;
        this.amarelosVisitado = amarelosVisitado;
        this.amarelosVisitante = amarelosVisitante;
        this.vermelhosVisitado = vermelhosVisitado;
        this.vermelhosVisitante = vermelhosVisitante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVencedor() {
        return vencedor;
    }

    public void setVencedor(Long vencedor) {
        this.vencedor = vencedor;
    }

    public List<Long> getGolosVisitado() {
        return golosVisitado;
    }

    public void setGolosVisitado(List<Long> golosVisitado) {
        this.golosVisitado = golosVisitado;
    }

    public List<Long> getGolosVisitante() {
        return golosVisitante;
    }

    public void setGolosVisitante(List<Long> golosVisitante) {
        this.golosVisitante = golosVisitante;
    }

    public List<Long> getAmarelosVisitado() {
        return amarelosVisitado;
    }

    public void setAmarelosVisitado(List<Long> amarelosVisitado) {
        this.amarelosVisitado = amarelosVisitado;
    }

    public List<Long> getAmarelosVisitante() {
        return amarelosVisitante;
    }

    public void setAmarelosVisitante(List<Long> amarelosVisitante) {
        this.amarelosVisitante = amarelosVisitante;
    }

    public List<Long> getVermelhosVisitado() {
        return vermelhosVisitado;
    }

    public void setVermelhosVisitado(List<Long> vermelhosVisitado) {
        this.vermelhosVisitado = vermelhosVisitado;
    }

    public List<Long> getVermelhosVisitante() {
        return vermelhosVisitante;
    }

    public void setVermelhosVisitante(List<Long> vermelhosVisitante) {
        this.vermelhosVisitante = vermelhosVisitante;
    }




}
