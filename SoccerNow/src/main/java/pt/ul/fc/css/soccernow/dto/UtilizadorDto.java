package pt.ul.fc.css.soccernow.dto;

public class UtilizadorDto {
    private Long id;
    private String nome;
    private String email;
    private String nif;

    public UtilizadorDto(){};

    public UtilizadorDto(long id, String nome){
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    
}
