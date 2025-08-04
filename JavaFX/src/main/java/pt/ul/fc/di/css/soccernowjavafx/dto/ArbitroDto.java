package pt.ul.fc.di.css.soccernowjavafx.dto;

public class ArbitroDto extends UtilizadorDto{

    private boolean certificado;

    private int partidasOficiadas;

    private int cartoesMostrados;

    public ArbitroDto(){};
    
    public ArbitroDto(Long id, String nome, String email, String nif){
        super(id, nome, email, nif);
    }

    public boolean isCertificado() {
        return certificado;
    }

    public void setCertificado(boolean certificado) {
        this.certificado = certificado;
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
    }

    @Override
    public String toString() {
        return "ArbitroDto [certificado=" + certificado + ", partidasOficiadas=" + partidasOficiadas
                + ", cartoesMostrados=" + cartoesMostrados + ", getNome()=" + getNome() + ", getEmail()=" + getEmail()
                + ", getNif()=" + getNif() + "]";
    }

    
}
