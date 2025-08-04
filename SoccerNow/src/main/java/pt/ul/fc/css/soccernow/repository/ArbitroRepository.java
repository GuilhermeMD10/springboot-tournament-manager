package pt.ul.fc.css.soccernow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.ul.fc.css.soccernow.model.Arbitro;

@Repository
public interface ArbitroRepository extends JpaRepository<Arbitro, Long>{

    List<Arbitro> findByNome(String nome);
    Optional<Arbitro> findByNif(String nif);
    Optional<Arbitro> findByEmail(String email);

    List<Arbitro> findByNomeContainingIgnoreCase(String nomeParcial);

    List<Arbitro> findByCertificado(boolean certificado);

    List<Arbitro> findByPartidasOficiadas(int partidas);
    List<Arbitro> findByPartidasOficiadasGreaterThan(int partidas);
    List<Arbitro> findByPartidasOficiadasGreaterThanEqual(int partidas);
    List<Arbitro> findByPartidasOficiadasLessThan(int partidas);
    List<Arbitro> findByPartidasOficiadasLessThanEqual(int partidas);


    List<Arbitro> findTop5ByOrderByPartidasOficiadasDesc();
    List<Arbitro> findTop5ByOrderByPartidasOficiadasAsc();


    // === FILTRAGEM POR CARTÕES MOSTRADOS ===

    List<Arbitro> findByCartoesMostrados(int cartoes);
    List<Arbitro> findByCartoesMostradosGreaterThan(int cartoes);
    List<Arbitro> findByCartoesMostradosGreaterThanEqual(int cartoes);
    List<Arbitro> findByCartoesMostradosLessThan(int cartoes);
    List<Arbitro> findByCartoesMostradosLessThanEqual(int cartoes);
    List<Arbitro> findTop5ByOrderByCartoesMostradosDesc();
    List<Arbitro> findTop5ByOrderByCartoesMostradosAsc();


    // === COMBINAÇÕES DE FILTROS ===

    List<Arbitro> findByPartidasOficiadasGreaterThanAndCartoesMostradosLessThan(
        int partidas, int cartoes);

    List<Arbitro> findByPartidasOficiadasLessThanEqualAndCartoesMostradosGreaterThanEqual(
        int partidas, int cartoes);
}
