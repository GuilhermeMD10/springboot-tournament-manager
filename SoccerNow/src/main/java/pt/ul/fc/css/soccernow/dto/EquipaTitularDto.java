package pt.ul.fc.css.soccernow.dto;

import java.util.ArrayList;
import java.util.List;

public class EquipaTitularDto {
    private Long id;
    private EquipaDto equipa; //Maybe String com o nome da equipa?
    private List<JogadorDto> jogadores = new ArrayList<>();
    private JogadorDto guardaRedes;

    public EquipaTitularDto(){}

    public EquipaTitularDto(EquipaDto equipa, List<JogadorDto> jogadores, JogadorDto guardaRedes){
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

    public EquipaDto getEquipa() {
        return equipa;
    }

    public void setEquipa(EquipaDto equipa) {
        this.equipa = equipa;
    }

    public List<JogadorDto> getJogadores() {
        return jogadores;
    }

    public void setJogadores(List<JogadorDto> jogadores) {
        this.jogadores = jogadores;
    }

    public JogadorDto getGuardaRedes() {
        return guardaRedes;
    }

    public void setGuardaRedes(JogadorDto guardaRedes) {
        this.guardaRedes = guardaRedes;
    }
}
