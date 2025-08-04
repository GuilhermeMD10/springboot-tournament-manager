package pt.ul.fc.di.css.soccernowjavafx.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Arbitro extends Utilizador{

    private final BooleanProperty certificado = new SimpleBooleanProperty();
    private final IntegerProperty partidasOficiadas = new SimpleIntegerProperty(0);
    private final IntegerProperty cartoesMostrados = new SimpleIntegerProperty(0);

    public Arbitro(Long id, String nome, String email, String nif) {
        super(id, nome, email, nif);
    }

    public boolean isCertificado() {
        return certificado.get();
    }

    public void setCertificado(boolean certificado) {
        this.certificado.set(certificado);
    }

    public BooleanProperty certificadoProperty() {
        return certificado;
    }

    public int getPartidasOficiadas() {
        return partidasOficiadas.get();
    }

    public void setPartidasOficiadas(int partidasOficiadas) {
        this.partidasOficiadas.set(partidasOficiadas);
    }

    public IntegerProperty partidasOficiadasProperty() {
        return partidasOficiadas;
    }

    public int getCartoesMostrados() {
        return cartoesMostrados.get();
    }

    public void setCartoesMostrados(int cartoesMostrados) {
        this.cartoesMostrados.set(cartoesMostrados);
    }

    public IntegerProperty cartoesMostradosProperty() {
        return cartoesMostrados;
    }

    @Override
    public String toString() {
        return "Arbitro{" +
            "id=" + getId() +
            ", nome=" + getNome() +
            ", email=" + getEmail() +
            ", nif=" + getNif() +
            ", certificado=" + isCertificado() +
            ", partidasOficiadas=" + getPartidasOficiadas() +
            ", cartoesMostrados=" + getCartoesMostrados() +
            "}";
    }

}
