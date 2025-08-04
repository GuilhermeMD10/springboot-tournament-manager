package pt.ul.fc.css.soccernow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Arbitro extends Utilizador{

    private boolean certificado;

    @Column(nullable = false)
    private int partidasOficiadas = 0;

    @Column(nullable = false)
    private int cartoesMostrados = 0;


    public Arbitro(){super();}

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



    
    
}
