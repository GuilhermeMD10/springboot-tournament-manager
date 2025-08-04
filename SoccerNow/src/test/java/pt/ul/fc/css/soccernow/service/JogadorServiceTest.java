package pt.ul.fc.css.soccernow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import pt.ul.fc.css.soccernow.dto.JogadorDto;
import pt.ul.fc.css.soccernow.model.enums.Posicao;

@SpringBootTest
@Transactional
public class JogadorServiceTest {
    
    @Autowired
    JogadorService jogadorService;

    @BeforeEach
    void startTest() {
        JogadorDto jogador1 = new JogadorDto();
        jogador1.setNif("111111111");
        jogador1.setGolos(10);
        jogador1.setAmarelos(2);
        jogador1.setEmail("joao@example.com");
        jogador1.setNome("João Silva");
        jogador1.setPrefPos(Posicao.FIXO);
        jogador1.setPartidasJogadas(12);
        jogador1.setVermelhos(0);



        JogadorDto jogador2 = new JogadorDto();
        jogador2.setNif("222222222");
        jogador2.setGolos(8);
        jogador2.setAmarelos(1);
        jogador2.setEmail("maria@example.com");
        jogador2.setNome("Maria Costa");
        jogador2.setPrefPos(Posicao.GUARDA_REDES);
        jogador2.setPartidasJogadas(14);
        jogador2.setVermelhos(0);

        JogadorDto jogador3 = new JogadorDto();
        jogador3.setNif("333333333");
        jogador3.setGolos(15);
        jogador3.setAmarelos(3);
        jogador3.setEmail("carlos@example.com");
        jogador3.setNome("Carlos Mendes");
        jogador3.setPrefPos(Posicao.ALA);
        jogador3.setPartidasJogadas(10);
        jogador3.setVermelhos(1);

        JogadorDto jogador4 = new JogadorDto();
        jogador4.setNif("444444444");
        jogador4.setGolos(5);
        jogador4.setAmarelos(0);
        jogador4.setEmail("ana@example.com");
        jogador4.setNome("Ana Pereira");
        jogador4.setPrefPos(Posicao.PIVO);
        jogador4.setPartidasJogadas(8);
        jogador4.setVermelhos(0);

        JogadorDto jogador5 = new JogadorDto();
        jogador5.setNif("555555555");
        jogador5.setGolos(20);
        jogador5.setAmarelos(4);
        jogador5.setEmail("ricardo@example.com");
        jogador5.setNome("Ricardo Lima");
        jogador5.setPrefPos(Posicao.ALA);
        jogador5.setPartidasJogadas(15);
        jogador5.setVermelhos(2);

        jogadorService.saveJogador(jogador1);
        jogadorService.saveJogador(jogador2);
        jogadorService.saveJogador(jogador3);
        jogadorService.saveJogador(jogador4);
        jogadorService.saveJogador(jogador5);
    }

    @Test
    void testGetJogadoresPorGolos() {
        List<JogadorDto> jogadoresCom20Golos = jogadorService.getJogadoresPorGolos(20);
        assertEquals(1, jogadoresCom20Golos.size());
        assertEquals("Ricardo Lima", jogadoresCom20Golos.get(0).getNome());

        List<JogadorDto> jogadoresCom10Golos = jogadorService.getJogadoresPorGolos(10);
        assertEquals(1, jogadoresCom10Golos.size());
        assertEquals("João Silva", jogadoresCom10Golos.get(0).getNome());

        List<JogadorDto> jogadoresComGolosInexistentes = jogadorService.getJogadoresPorGolos(0);
        assertTrue(jogadoresComGolosInexistentes.isEmpty());
    }

    @Test
    void testGetJogadoresComMaisAmarelos() {
        List<JogadorDto> jogadoresComMaisQue2Amarelos = jogadorService.getJogadoresComMaisAmarelos(2);
        // Jogadores com amarelos > 2: Carlos (3), Ricardo (4)
        assertTrue(jogadoresComMaisQue2Amarelos.stream()
            .anyMatch(j -> j.getNome().equals("Carlos Mendes")));
        assertTrue(jogadoresComMaisQue2Amarelos.stream()
            .anyMatch(j -> j.getNome().equals("Ricardo Lima")));
        assertEquals(2, jogadoresComMaisQue2Amarelos.size());
    }

    @Test
    void testGetJogadoresComVermelhos() {
        List<JogadorDto> jogadoresComVermelhos = jogadorService.getJogadoresComMaisVermelhos(0);
        // Jogadores com vermelhos > 0: Carlos (1), Ricardo (2)
        assertTrue(jogadoresComVermelhos.stream()
            .anyMatch(j -> j.getNome().equals("Carlos Mendes")));
        assertTrue(jogadoresComVermelhos.stream()
            .anyMatch(j -> j.getNome().equals("Ricardo Lima")));
        assertEquals(2, jogadoresComVermelhos.size());
    }

    @Test
    void testGetJogadoresPorPartidasJogadas() {
        List<JogadorDto> jogadoresCom15Partidas = jogadorService.getJogadoresPorPartidasJogadas(15);
        assertEquals(1, jogadoresCom15Partidas.size());
        assertEquals("Ricardo Lima", jogadoresCom15Partidas.get(0).getNome());

        List<JogadorDto> jogadoresCom10Partidas = jogadorService.getJogadoresPorPartidasJogadas(10);
        assertEquals(1, jogadoresCom10Partidas.size());
        assertEquals("Carlos Mendes", jogadoresCom10Partidas.get(0).getNome());
    }



}
