package pt.ul.fc.css.soccernow.dto;

import java.util.ArrayList;
import java.util.List;

import pt.ul.fc.css.soccernow.model.enums.Posicao;

public class JogadorDto extends UtilizadorDto{
    private Posicao prefPos;
    private int golos;
    private int amarelos;
    private int vermelhos;
    private int partidasJogadas;
    private List<EquipaDto> equipas;

    public JogadorDto(){super();}

    public JogadorDto(String nome, Posicao prefPos) {
        super();
        this.setNome(nome);
        this.prefPos = prefPos;
        this.golos = 0;
        this.amarelos = 0;
        this.vermelhos = 0;
        this.partidasJogadas = 0;
        this.equipas = new ArrayList<>();
    }

    public JogadorDto(String nome) {
        super();
        this.setNome(nome);
        this.prefPos = null;
        this.golos = 0;
        this.amarelos = 0;
        this.vermelhos = 0;
        this.partidasJogadas = 0;
        this.equipas = new ArrayList<>();
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

    public List<EquipaDto> getEquipas() {
        return equipas;
    }

    public void setEquipas(List<EquipaDto> equipas) {
        this.equipas = equipas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JogadorDto that = (JogadorDto) o;

        return this.getNif() != null ? this.getNif().equals(that.getNif()) : that.getNif() == null;
    }

    @Override
    public int hashCode() {
        return this.getNif() != null ? this.getNif().hashCode() : 0;
    }



}
