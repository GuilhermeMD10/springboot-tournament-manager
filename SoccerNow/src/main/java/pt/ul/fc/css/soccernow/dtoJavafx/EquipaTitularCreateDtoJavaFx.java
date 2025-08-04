package pt.ul.fc.css.soccernow.dtoJavafx;

import java.util.ArrayList;
import java.util.List;

public class EquipaTitularCreateDtoJavaFx {
    private Long equipa; //Maybe String com o nome da equipa?
    private List<Long> jogadores = new ArrayList<>();
    private Long guardaRedes;

    public EquipaTitularCreateDtoJavaFx(){}

    public EquipaTitularCreateDtoJavaFx(Long equipa, List<Long> jogadores, Long guardaRedes){
        this.equipa = equipa;
        this.jogadores = jogadores;
        this.guardaRedes = guardaRedes;
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
