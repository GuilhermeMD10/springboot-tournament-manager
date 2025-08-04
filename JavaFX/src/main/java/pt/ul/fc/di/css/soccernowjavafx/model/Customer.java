package pt.ul.fc.di.css.soccernowjavafx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customer {
    
    private final long id;
    private final StringProperty nome = new SimpleStringProperty();
    private final StringProperty phone = new SimpleStringProperty();
    private final StringProperty vat = new SimpleStringProperty();

    public Customer(long id, String nome, String phone, String vat) {
        this.id = id;
        setNome(nome);
        setPhone(phone);
        setVat(vat);
    }

    public long getId() {
        return id;
    }

    public StringProperty getNomeProperty() {
        return nome;
    }

    public StringProperty getPhoneProperty() {
        return phone;
    }

    public StringProperty getVatProperty() {
        return vat;
    }

    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getVat() {
        return vat.get();
    }

    public void setVat(String vat) {
        this.vat.set(vat);
    }

    @Override
    public String toString() {
        return nome.get() + " - " + vat.get() +
                ", phone=" + phone.get();
    }
}
