package pt.ul.fc.css.soccernow.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Resultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private EquipaTitular vencedor;

    @ManyToMany
    @JoinTable(name = "golos_visitado")
    private List<Jogador> golosVisitado = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "golos_visitante")
    private List<Jogador> golosVisitante = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "amarelos_visitado")
    private List<Jogador> amarelosVisitado = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "amarelos_visitante")
    private List<Jogador> amarelosVisitante = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(name = "vermelhos_visitado")
    private List<Jogador> vermelhosVisitado = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "vermelhos_visitante")
    private List<Jogador> vermelhosVisitante = new ArrayList<>();

    public Resultado(){}

    public Resultado(EquipaTitular vencedor,List<Jogador> golosVisitado, List<Jogador> golosVisitante,
                 List<Jogador> amarelosVisitado, List<Jogador> amarelosVisitante,
                 List<Jogador> vermelhosVisitado,List<Jogador> vermelhosVisitante) {
        this.vencedor = vencedor;
        this.golosVisitado = golosVisitado;
        this.golosVisitante = golosVisitante;
        this.amarelosVisitado = amarelosVisitado;
        this.amarelosVisitante = amarelosVisitante;
        this.vermelhosVisitado = vermelhosVisitado;
        this.vermelhosVisitante = vermelhosVisitante;
    }

    public Long getId() {
        return id;
    }

    public EquipaTitular getVencedor() {
        return vencedor;
    }

    public void setVencedor(EquipaTitular vencedor) {
        this.vencedor = vencedor;
    }

    public List<Jogador> getGolosVisitado() {
        return golosVisitado;
    }

    public void setGolosVisitado(List<Jogador> golosVisitado) {
        this.golosVisitado = golosVisitado;
    }

    public List<Jogador> getGolosVisitante() {
        return golosVisitante;
    }

    public void setGolosVisitante(List<Jogador> golosVisitante) {
        this.golosVisitante = golosVisitante;
    }

    public List<Jogador> getAmarelosVisitado() {
        return amarelosVisitado;
    }

    public void setAmarelosVisitado(List<Jogador> amarelosVisitado) {
        this.amarelosVisitado = amarelosVisitado;
    }

    public List<Jogador> getAmarelosVisitante() {
        return amarelosVisitante;
    }

    public void setAmarelosVisitante(List<Jogador> amarelosVisitante) {
        this.amarelosVisitante = amarelosVisitante;
    }

    public List<Jogador> getVermelhosVisitado() {
        return vermelhosVisitado;
    }

    public void setVermelhosVisitado(List<Jogador> vermelhosVisitado) {
        this.vermelhosVisitado = vermelhosVisitado;
    }

    public List<Jogador> getVermelhosVisitante() {
        return vermelhosVisitante;
    }

    public void setVermelhosVisitante(List<Jogador> vermelhosVisitante) {
        this.vermelhosVisitante = vermelhosVisitante;
    }
}
