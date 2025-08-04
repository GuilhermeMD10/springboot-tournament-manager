package pt.ul.fc.css.soccernow.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import pt.ul.fc.css.soccernow.model.Jogo;
import pt.ul.fc.css.soccernow.model.enums.Turno;

public interface JogoRepository extends JpaRepository<Jogo, Long>{

    List<Jogo> findByCampeonatoId(Long campeonato_id);

    List<Jogo> findByEquipaTitularVisitadaIdOrEquipaTitularVisitanteId(Long equipaVisitada_id, Long equipaVisitante_id);

    //List<Jogo> findByUtilizadorId(Long utilizador_id);

    // Jogos em que o árbitro foi o principal
    List<Jogo> findByArbitroPrincipalId(Long arbitroId);

    // Jogos em que o árbitro faz parte da equipa de arbitragem (mesmo sem o lado inverso)
    List<Jogo> findByEquipaArbitragemId(Long arbitroId);

    //Jogos da equipa visitada numa determinada Data e turno
    List<Jogo> findByEquipaTitularVisitada_IdAndDataAndHorario(Long equipaVisitadaId, LocalDate data, Turno turno);
    
    //Jogos da equipa visitante numa determinada Data e turno
    List<Jogo> findByEquipaTitularVisitante_IdAndDataAndHorario(Long equipaVisitanteId, LocalDate data, Turno turno);

    List<Jogo> findByHorario(Turno turno);

    List<Jogo> findByLocal(String local);

    List<Jogo> findByData(LocalDate data);
}
