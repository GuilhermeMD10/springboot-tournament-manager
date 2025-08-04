package pt.ul.fc.di.css.soccernowjavafx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Utilizador{
    
    private final long id;
    private final StringProperty nome = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty nif = new SimpleStringProperty();

    public Utilizador(Long id, String nome, String email, String nif){
        this.id = id;
        setEmail(email);
        setNome(nome);
        setNif(nif);
    }

    public long getId(){
        return this.id;
    }

    public StringProperty getNomeProperty(){
        return nome;
    }

    public String getNome(){
        return nome.get();
    }

    public void setNome(String nome){
        this.nome.set(nome);
    }

    public StringProperty getEmailProperty(){
        return email;
    }

    public String getEmail(){
        return email.get();
    }

    public void setEmail(String email){
        this.email.set(email);
    }

    public StringProperty getNifProperty(){
        return nif;
    }

    public String getNif(){
        return nif.get();
    }

    public void setNif(String nif){
        this.nif.set(nif);
    }

    @Override
    public String toString() {
        return nome.get() + " - " + nif.get() +
                ", email=" + email.get();
    }
}