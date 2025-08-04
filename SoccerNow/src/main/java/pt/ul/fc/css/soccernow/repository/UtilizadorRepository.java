package pt.ul.fc.css.soccernow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.ul.fc.css.soccernow.model.Utilizador;

public interface UtilizadorRepository extends JpaRepository<Utilizador, Long> {

    List<Utilizador> findByNome(String nome);


    List<Utilizador> findByNomeContainingIgnoreCase(String nomeParcial);

    Optional<Utilizador> findByNif(String nif);
    Optional<Utilizador> findByEmail(String email);
}

