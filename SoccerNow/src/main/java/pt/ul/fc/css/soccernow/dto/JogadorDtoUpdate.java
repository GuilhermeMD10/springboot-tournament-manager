package pt.ul.fc.css.soccernow.dto;

import pt.ul.fc.css.soccernow.model.enums.Posicao;

public class JogadorDtoUpdate  extends UtilizadorDto{
    private Posicao prefPos;

    public JogadorDtoUpdate() {
        super();
    }

    public Posicao getPrefPos() {
        return prefPos;
    }

    public void setPrefPos(Posicao prefPos) {
        this.prefPos = prefPos;
    }

    
}
