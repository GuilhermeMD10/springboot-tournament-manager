package pt.ul.fc.css.soccernow.dtoJavafx;

public class UtilizadorDtoCreate {

    private String nome;
    private String email;
    private String nif;

    public UtilizadorDtoCreate(){};

    public UtilizadorDtoCreate(long id, String nome, String email, String nif){
        this.nome = nome;
        this.email = email;
        this.nif = nif;
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
