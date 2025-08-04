package pt.ul.fc.css.soccernow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pt.ul.fc.css.soccernow.model.Equipa;

public interface EquipaRepository extends JpaRepository<Equipa, Long> {

    Equipa findByNome(String nome);

    List<Equipa> findByNomeContainingIgnoreCase(String nomeParcial);

    @Query("SELECT e FROM Equipa e LEFT JOIN FETCH e.plantel")
    List<Equipa> findAllWithPlantel();
    

}