package pt.ul.fc.css.soccernow.dtoJavafx;

public class ArbitroDtoJavaFx extends UtilizadorDtoJavaFx{

    private boolean certificado;

    private int partidasOficiadas;

    private int cartoesMostrados;

    public ArbitroDtoJavaFx(){super();}

    public ArbitroDtoJavaFx(boolean certificado){
        this.setCertificado(certificado);
    }

    public boolean isCertificado() {
        return certificado;
    }

    public void setCertificado(boolean isCertificado) {
        this.certificado = isCertificado;
    }

    public int getPartidasOficiadas() {
        return partidasOficiadas;
    }

    public void setPartidasOficiadas(int partidasOficiadas) {
        this.partidasOficiadas = partidasOficiadas;
    }

    public int getCartoesMostrados() {
        return cartoesMostrados;
    }

    public void setCartoesMostrados(int cartoesMostrados) {
        this.cartoesMostrados = cartoesMostrados;
    };
    
    
}
