package pt.ul.fc.di.css.soccernowjavafx.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class Jogo {

    private final LongProperty id = new SimpleLongProperty();
    private final ObjectProperty<LocalDate> data = new SimpleObjectProperty<>();
    private final ObjectProperty<Turno> horario = new SimpleObjectProperty<>();
    private final StringProperty local = new SimpleStringProperty();

    private final ObjectProperty<Arbitro> arbitroPrincipal = new SimpleObjectProperty<>();
    private final ListProperty<Arbitro> equipaArbitragem = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<EquipaTitular> equipaTitularVisitada = new SimpleObjectProperty<>();
    private final ObjectProperty<EquipaTitular> equipaTitularVisitante = new SimpleObjectProperty<>();
    private final ObjectProperty<Resultado> resultado = new SimpleObjectProperty<>();
    private final ObjectProperty<Campeonato> campeonato = new SimpleObjectProperty<>();

    public Jogo() {}

    public Jogo(Arbitro arbitroPrincipal, ObservableList<Arbitro> equipaArbitragem, EquipaTitular equipaTitularVisitante,
                EquipaTitular equipaTitularVisitada, Resultado resultado, LocalDate data, Turno horario,
                String local, Campeonato campeonato) {
        this.arbitroPrincipal.set(arbitroPrincipal);
        this.equipaArbitragem.set(equipaArbitragem);
        this.equipaTitularVisitante.set(equipaTitularVisitante);
        this.equipaTitularVisitada.set(equipaTitularVisitada);
        this.resultado.set(resultado);
        this.data.set(data);
        this.horario.set(horario);
        this.local.set(local);
        this.campeonato.set(campeonato);
    }

    public long getId() { return id.get(); }
    public void setId(long id) { this.id.set(id); }
    public LongProperty idProperty() { return id; }

    public LocalDate getData() { return data.get(); }
    public void setData(LocalDate data) { this.data.set(data); }
    public ObjectProperty<LocalDate> dataProperty() { return data; }

    public Turno getHorario() { return horario.get(); }
    public void setHorario(Turno horario) { this.horario.set(horario); }
    public ObjectProperty<Turno> horarioProperty() { return horario; }

    public String getLocal() { return local.get(); }
    public void setLocal(String local) { this.local.set(local); }
    public StringProperty localProperty() { return local; }

    public Arbitro getArbitroPrincipal() { return arbitroPrincipal.get(); }
    public void setArbitroPrincipal(Arbitro arbitroPrincipal) { this.arbitroPrincipal.set(arbitroPrincipal); }
    public ObjectProperty<Arbitro> arbitroPrincipalProperty() { return arbitroPrincipal; }

    public ObservableList<Arbitro> getEquipaArbitragem() { return equipaArbitragem.get(); }
    public void setEquipaArbitragem(ObservableList<Arbitro> lista) { this.equipaArbitragem.set(lista); }
    public ListProperty<Arbitro> equipaArbitragemProperty() { return equipaArbitragem; }

    public EquipaTitular getEquipaTitularVisitada() { return equipaTitularVisitada.get(); }
    public void setEquipaTitularVisitada(EquipaTitular equipa) { this.equipaTitularVisitada.set(equipa); }
    public ObjectProperty<EquipaTitular> equipaTitularVisitadaProperty() { return equipaTitularVisitada; }

    public EquipaTitular getEquipaTitularVisitante() { return equipaTitularVisitante.get(); }
    public void setEquipaTitularVisitante(EquipaTitular equipa) { this.equipaTitularVisitante.set(equipa); }
    public ObjectProperty<EquipaTitular> equipaTitularVisitanteProperty() { return equipaTitularVisitante; }

    public Resultado getResultado() { return resultado.get(); }
    public void setResultado(Resultado resultado) { this.resultado.set(resultado); }
    public ObjectProperty<Resultado> resultadoProperty() { return resultado; }

    public Campeonato getCampeonato() { return campeonato.get(); }
    public void setCampeonato(Campeonato campeonato) { this.campeonato.set(campeonato); }
    public ObjectProperty<Campeonato> campeonatoProperty() { return campeonato; }
}
