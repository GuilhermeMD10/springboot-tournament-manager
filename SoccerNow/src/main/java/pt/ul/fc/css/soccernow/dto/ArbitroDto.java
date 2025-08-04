package pt.ul.fc.css.soccernow.dto;

public class ArbitroDto extends UtilizadorDto{

    private boolean certificado;

    private int partidasOficiadas;

    private int cartoesMostrados;

    public ArbitroDto(){super();}

    public ArbitroDto(boolean certificado){
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
