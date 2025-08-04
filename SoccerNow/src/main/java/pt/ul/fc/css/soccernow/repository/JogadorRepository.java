package pt.ul.fc.css.soccernow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ul.fc.css.soccernow.model.Jogador;
import pt.ul.fc.css.soccernow.model.enums.Posicao;

public interface JogadorRepository extends JpaRepository<Jogador, Long>{

    //Procura por nome com caracteres exatos
    List<Jogador> findByNome(String nome);

    Optional<Jogador> findByNif(String nif);
    Optional<Jogador> findByEmail(String email);


    //Procura por nome independentemente dos caracteres maiusculos
    List<Jogador> findByNomeContainingIgnoreCase(String nomeParcial);

    //Procura todos os jogadores que tenham pelo menos uma equipa com esse nome
    //List<Jogador> findByEquipasNome(String nomeEquipa);

    //GOLOS
    List<Jogador> findByGolosGreaterThan(int golos);
    List<Jogador> findByGolosLessThan(int golos);
    List<Jogador> findByGolos(int golos);
    List<Jogador> findTop5ByOrderByGolosAsc();
    List<Jogador> findTop5ByOrderByGolosDesc();

    //PARTIDAS
    List<Jogador> findByPartidasJogadas(int partidas);
    List<Jogador> findByPartidasJogadasLessThan(int partidas);
    List<Jogador> findByPartidasJogadasGreaterThan(int partidas);
    List<Jogador> findTop5ByOrderByPartidasJogadasDesc();
    List<Jogador> findTop5ByOrderByPartidasJogadasAsc();

    //CARTOES MAIOR OU MENOR QUE
    List<Jogador> findByAmarelosGreaterThan(int amarelos);
    List<Jogador> findByVermelhosGreaterThan(int vermelhos);

    List<Jogador> findByAmarelosLessThan(int amarelos);
    List<Jogador> findByVermelhosLessThan(int vermelhos);

    //CARTOES EXATOS
    List<Jogador> findByAmarelos(int amarelos);
    List<Jogador> findByVermelhos(int vermelhos);

    // TOP 5 MAIS
    List<Jogador> findTop5ByOrderByAmarelosDesc();
    List<Jogador> findTop5ByOrderByVermelhosDesc();


    // TOP 5 MENOS
    List<Jogador> findTop5ByOrderByAmarelosAsc();
    List<Jogador> findTop5ByOrderByVermelhosAsc();


    //TOTAIS
    @Query("SELECT j FROM Jogador j WHERE (j.amarelos + j.vermelhos) = :cartoes")
    List<Jogador> findBySomaCartoes(@Param("cartoes") int cartoes);

    @Query("SELECT j FROM Jogador j WHERE (j.amarelos + j.vermelhos) > :cartoes")
    List<Jogador> findByCartoesTotaisMaiorQue(@Param("cartoes") int cartoes);

    @Query("SELECT j FROM Jogador j WHERE (j.amarelos + j.vermelhos) < :cartoes")
    List<Jogador> findByCartoesTotaisMenorQue(@Param("cartoes") int cartoes);

    @Query("SELECT j FROM Jogador j ORDER BY (j.amarelos + j.vermelhos) DESC")
    List<Jogador> findTopJogadoresByCartoesTotais();




    //POSICAO
    List<Jogador> findByPrefPos(Posicao posicao);


}
