package pt.ul.fc.di.css.soccernowjavafx.dto;

import pt.ul.fc.di.css.soccernowjavafx.model.Posicao;

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
