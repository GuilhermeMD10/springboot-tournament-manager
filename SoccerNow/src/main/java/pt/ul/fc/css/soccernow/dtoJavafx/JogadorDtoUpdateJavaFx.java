package pt.ul.fc.css.soccernow.dtoJavafx;

import pt.ul.fc.css.soccernow.model.enums.Posicao;

public class JogadorDtoUpdateJavaFx  extends UtilizadorDtoJavaFx{
    private Posicao prefPos;

    public JogadorDtoUpdateJavaFx() {
        super();
    }

    public Posicao getPrefPos() {
        return prefPos;
    }

    public void setPrefPos(Posicao prefPos) {
        this.prefPos = prefPos;
    }

    
}
