package pt.ul.fc.di.css.soccernowjavafx.dto;

import java.util.ArrayList;
import java.util.List;

public class EquipaTitularDto {
    private Long id;
    private Long equipa;
    private List<Long> jogadores = new ArrayList<>();
    private Long guardaRedes;

    public EquipaTitularDto(){}

    public EquipaTitularDto(Long equipa, List<Long> jogadores, Long guardaRedes){
        this.equipa = equipa;
        this.jogadores = jogadores;
        this.guardaRedes = guardaRedes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEquipa() {
        return equipa;
    }

    public void setEquipa(Long equipa) {
        this.equipa = equipa;
    }

    public List<Long> getJogadores() {
        return jogadores;
    }

    public void setJogadores(List<Long> jogadores) {
        this.jogadores = jogadores;
    }

    public Long getGuardaRedes() {
        return guardaRedes;
    }

    public void setGuardaRedes(Long guardaRedes) {
        this.guardaRedes = guardaRedes;
    }
}
