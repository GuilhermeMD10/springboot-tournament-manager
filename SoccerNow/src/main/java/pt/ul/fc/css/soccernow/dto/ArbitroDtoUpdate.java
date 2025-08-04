package pt.ul.fc.css.soccernow.dto;

public class ArbitroDtoUpdate extends UtilizadorDto{
    
    private boolean certificado;

    public ArbitroDtoUpdate(){super();}

    public boolean isCertificado() {
        return certificado;
    }

    public void setCertificado(boolean certificado) {
        this.certificado = certificado;
    };

    
}
