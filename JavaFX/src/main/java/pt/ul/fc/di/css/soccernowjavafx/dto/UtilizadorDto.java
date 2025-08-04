package pt.ul.fc.di.css.soccernowjavafx.dto;

public class UtilizadorDto {
    
    private long id;
    private String nome;
    private String email;
    private String nif;

    public UtilizadorDto(){}

    public UtilizadorDto(Long id, String nome, String email, String nif){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.nif = nif;
    }

    public long getId(){
        return this.id;
    }


    public void setId(long id) {
        this.id = id;
    }

    public String getNome(){
        return this.nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }


    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }


    public String getNif(){
        return nif;
    }

    public void setNif(String nif){
        this.nif = nif;
    }

    @Override
    public String toString() {
        return nome + " - " + nif +
                ", email = " + email;
    }
}
