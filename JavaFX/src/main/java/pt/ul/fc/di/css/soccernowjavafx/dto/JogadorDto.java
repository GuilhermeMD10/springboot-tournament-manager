package pt.ul.fc.di.css.soccernowjavafx.dto;

import java.util.ArrayList;
import java.util.List;

import pt.ul.fc.di.css.soccernowjavafx.model.Posicao;



public class JogadorDto extends UtilizadorDto{
    private Posicao prefPos;
    private int golos;
    private int amarelos;
    private int vermelhos;
    private int partidasJogadas;
    private List<EquipaDto> equipas;

    public JogadorDto(){super();}


    public JogadorDto(Long id, String nome, String email, String nif) {
        super(id,nome, email, nif);
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
    public String toString() {
        return "JogadorDto [prefPos=" + prefPos + ", golos=" + golos + ", amarelos=" + amarelos + ", vermelhos="
                + vermelhos + ", partidasJogadas=" + partidasJogadas + ", equipas=" + equipas + ", getNome()="
                + getNome() + ", getEmail()=" + getEmail() + ", getNif()=" + getNif() + "]";
    }

    
}

