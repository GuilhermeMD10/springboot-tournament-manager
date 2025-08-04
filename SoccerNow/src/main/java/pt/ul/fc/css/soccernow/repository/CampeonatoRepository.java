package pt.ul.fc.css.soccernow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.ul.fc.css.soccernow.model.Campeonato;

public interface CampeonatoRepository extends JpaRepository<Campeonato, Long> {

    Campeonato findByNome(String nome);

    List<Campeonato> findByNomeContainingIgnoreCase(String nomeParcial);


}
