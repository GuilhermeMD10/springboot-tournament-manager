package pt.ul.fc.css.soccernow.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import pt.ul.fc.css.soccernow.model.enums.Turno;

@Entity
public class Jogo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDate data; 
    private Turno horario;
    private String local;

    @ManyToOne (cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Arbitro arbitroPrincipal;

    @ManyToMany
    @JoinTable(name = "jogo_equipa_arbitragem",
            joinColumns = @JoinColumn(name = "jogo_id"),
            inverseJoinColumns = @JoinColumn(name = "arbitro_id"))
    private List<Arbitro> equipaArbitragem;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "equipa_visitada_id")
    private EquipaTitular equipaTitularVisitada;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "equipa_visitante_id")
    private EquipaTitular equipaTitularVisitante; //São so placeholder nomes, se quiserem mudar para A e B 

    @OneToOne
    private Resultado resultado;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Campeonato campeonato;

    public Jogo(){}

    public Jogo (Arbitro arbitroPrincipal, List<Arbitro> equipaArbitragem, EquipaTitular equipaTitularVisitante, EquipaTitular equipaTitularVisitada,
     Resultado resultado,LocalDate data, Turno horario, String local, Campeonato campeonato){
        this.arbitroPrincipal = arbitroPrincipal;
        this.equipaArbitragem = equipaArbitragem;
        this.equipaTitularVisitante = equipaTitularVisitante;
        this.equipaTitularVisitada = equipaTitularVisitada;
        this.resultado = resultado;
        this.data = data;
        this.horario = horario;
        this.local = local;
        this.campeonato = campeonato;
     }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Turno getHorario() {
        return horario;
    }

    public void setHorario(Turno horario) {
        this.horario = horario;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Arbitro getArbitroPrincipal() {
        return arbitroPrincipal;
    }

    public void setArbitroPrincipal(Arbitro arbitroPrincipal) {
        this.arbitroPrincipal = arbitroPrincipal;
    }

    public List<Arbitro> getEquipaArbitragem() {
        return equipaArbitragem;
    }

    public void setEquipaArbitragem(List<Arbitro> equipaArbitragem2) {
        this.equipaArbitragem = equipaArbitragem2;
    }

    public EquipaTitular getEquipaTitularVisitada() {
        return equipaTitularVisitada;
    }

    public void setEquipaTitularVisitada(EquipaTitular equipaTitularVisitada) {
        this.equipaTitularVisitada = equipaTitularVisitada;
    }

    public EquipaTitular getEquipaTitularVisitante() {
        return equipaTitularVisitante;
    }

    public void setEquipaTitularVisitante(EquipaTitular equipaTitularVisitante) {
        this.equipaTitularVisitante = equipaTitularVisitante;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }

    public Campeonato getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }
}
