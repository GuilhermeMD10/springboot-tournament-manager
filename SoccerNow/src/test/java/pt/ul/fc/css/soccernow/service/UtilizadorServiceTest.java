package pt.ul.fc.css.soccernow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import pt.ul.fc.css.soccernow.dto.UtilizadorDto;
import pt.ul.fc.css.soccernow.dto.UtilizadorDtoCreate;

@SpringBootTest
@Transactional
public class UtilizadorServiceTest {
    
    @Autowired
    UtilizadorService utilizadorService;

    @Test
    void testeBadCreateAttemps(){
        UtilizadorDtoCreate dtoCreate = new UtilizadorDtoCreate();
        dtoCreate.setNif("123");
        dtoCreate.setNome("Utilizador Test");
        dtoCreate.setEmail("user@teste.com");

        // Teste 1: NIF inválido    
        assertThrows(IllegalArgumentException.class, () ->{
            utilizadorService.createUtilizador(dtoCreate);
        });


        dtoCreate.setNif("123123123");
        dtoCreate.setNome("Utilizador Test");
        dtoCreate.setEmail("emailteste");

        // Teste 2: Email inválido
        assertThrows(IllegalArgumentException.class, () ->{
            utilizadorService.createUtilizador(dtoCreate);
        });

        dtoCreate.setNif("123123123");
        dtoCreate.setNome("Utilizador Test");
        dtoCreate.setEmail("email@teste.com");
        utilizadorService.createUtilizador(dtoCreate);

        UtilizadorDtoCreate dtoCreate2 = new UtilizadorDtoCreate();
        dtoCreate2.setNif("123123123");
        dtoCreate2.setNome("Utilizador Test");
        dtoCreate2.setEmail("user@teste.com");

        // Teste 3: NIF duplciado
        assertThrows(DataIntegrityViolationException.class, () ->{
            utilizadorService.createUtilizador(dtoCreate2);
        });

        dtoCreate2.setNif("222333444");
        dtoCreate2.setNome("Utilizador Test");
        dtoCreate2.setEmail("email@teste.com");

        // Teste 3: Email duplciado
        assertThrows(DataIntegrityViolationException.class, () ->{
            utilizadorService.createUtilizador(dtoCreate2);
        });
    }

    @Test
    void testCreateAndFindUtilizador(){
        UtilizadorDtoCreate dtoCreate = new UtilizadorDtoCreate();
        dtoCreate.setNif("222333123");
        dtoCreate.setNome("Utilizador Test");
        dtoCreate.setEmail("user@teste.com");

        UtilizadorDto dtoCriado = utilizadorService.createUtilizador(dtoCreate);

        assertNotNull(dtoCriado, "O DTO retornado não deveria ser nulo.");
        assertEquals("222333123", dtoCriado.getNif(), "O NIF retornado não corresponde.");
        assertEquals("Utilizador Test", dtoCriado.getNome(), "O nome retornado não corresponde.");
        assertEquals("user@teste.com", dtoCriado.getEmail(), "O email retornado não corresponde.");

        UtilizadorDto dtoEncontradoPorId = utilizadorService.procurarPorId(dtoCriado.getId());

        assertNotNull(dtoEncontradoPorId, "O utilizador deveria ter sido encontrado.");
        assertEquals(dtoCriado.getId(), dtoEncontradoPorId.getId(), "Os IDs deveriam coincidir.");

        UtilizadorDto dtoEncontradoPorNif = utilizadorService.procurarPorNif("222333123");

        assertNotNull(dtoEncontradoPorNif, "O utilizador deveria ter sido encontrado.");
        assertEquals(dtoCriado.getId(), dtoEncontradoPorNif.getId(), "Os IDs deveriam coincidir.");

        UtilizadorDto dtoEncontradoPorEmail = utilizadorService.procurarPorEmail("user@teste.com");

        assertNotNull(dtoEncontradoPorEmail, "O utilizador deveria ter sido encontrado.");
        assertEquals(dtoCriado.getId(), dtoEncontradoPorEmail.getId(), "Os IDs deveriam coincidir.");
    }

    @Test
    void testFindAllUtilizadoresAndDelete(){

        //Verificar estado incial com 1 utilizadore (O Utilizador Admin que é criado no inicio do arranque)
        List<UtilizadorDto> allUtilizadores = utilizadorService.getTodosUtilizadores();
        assertEquals(1, allUtilizadores.size(), "O número de utilizadores devia ser um");

        //Criar e adicionar 2 utilizadores
        UtilizadorDtoCreate dtoCreate1 = new UtilizadorDtoCreate();
        dtoCreate1.setNif("454989888");
        dtoCreate1.setNome("João Ferreira");
        dtoCreate1.setEmail("joao.ferreira@gmail.com");

        UtilizadorDtoCreate dtoCreate2 = new UtilizadorDtoCreate();
        dtoCreate2.setNif("987654321");
        dtoCreate2.setNome("Maria Silva");
        dtoCreate2.setEmail("maria.silva@hotmail.com");

        UtilizadorDto dtoCriado1 = utilizadorService.createUtilizador(dtoCreate1);
        UtilizadorDto dtoCriado2 = utilizadorService.createUtilizador(dtoCreate2);

        //Verificar que foram criados 2 utilizadores corretamente
        List<UtilizadorDto> allUtilizadoresTwo = utilizadorService.getTodosUtilizadores();
        assertEquals(allUtilizadoresTwo.size(), 3, "O número de utilizadores devia ser três");

        //Adicionar 3 novos utilizadores
        UtilizadorDtoCreate dtoCreate3 = new UtilizadorDtoCreate();
        dtoCreate3.setNif("456789123");
        dtoCreate3.setNome("Carlos Mendes");
        dtoCreate3.setEmail("c.mendes@example.pt");

        UtilizadorDtoCreate dtoCreate4 = new UtilizadorDtoCreate();
        dtoCreate4.setNif("321654987");
        dtoCreate4.setNome("Ana Costa");
        dtoCreate4.setEmail("ana.costa@empresa.com");

        UtilizadorDtoCreate dtoCreate5 = new UtilizadorDtoCreate();
        dtoCreate5.setNif("789123456");
        dtoCreate5.setNome("Rui Oliveira");
        dtoCreate5.setEmail("rui.oliveira@dominio.pt");

        utilizadorService.createUtilizador(dtoCreate3);
        utilizadorService.createUtilizador(dtoCreate4);
        utilizadorService.createUtilizador(dtoCreate5);

        //Verificar que agora estão 5 utilizadores na base de dados
        List<UtilizadorDto> allUtilizadoresFive = utilizadorService.getTodosUtilizadores();
        assertEquals(allUtilizadoresFive.size(), 6, "O número de utilizadores devia ser seis");

        //Eliminar 2 utilizadores da base de dados
        utilizadorService.eliminarPorId(dtoCriado1.getId());
        utilizadorService.eliminarPorId(dtoCriado2.getId());

        //Verificar se o numero total de utilizadores baixou para 3
        List<UtilizadorDto> allUtilizadoresThree = utilizadorService.getTodosUtilizadores();
        assertEquals(allUtilizadoresThree.size(), 4, "O número de utilizadores devia ser quatro");

        assertThrows(RuntimeException.class,() ->{
            utilizadorService.procurarPorId(dtoCriado1.getId());
        });

        assertThrows(RuntimeException.class,() ->{
            utilizadorService.procurarPorId(dtoCriado2.getId());
        });
    }

    @Test
    void testPesquisaNomeParcial() {
        String[] nomesValidos = {
            "Angel Di Maria",
            "João Angel Di Maria",
            "Angel Di Maria Silva",
            "Angel Roberto Di Maria",
            "ANGEL DI MARIA",
            "angel di maria",
            "  Angel   Di    Maria "
        };

        int nifBase = 111111111;

        for (int i = 0; i < nomesValidos.length; i++) {
            UtilizadorDtoCreate dto = new UtilizadorDtoCreate();
            dto.setNif(String.valueOf(nifBase + i));
            dto.setNome(nomesValidos[i]);
            dto.setEmail("test" + i + "@example.com");
            utilizadorService.createUtilizador(dto);
        }

        List<UtilizadorDto> resultados = utilizadorService.procurarPorNome("Angel Di Maria");

        assertEquals(6, resultados.size(), "Deve encontrar todos os nomes que contêm 'Angel Di Maria'");

        for (UtilizadorDto dto : resultados) {
            String nomeNormalizado = dto.getNome().trim().replaceAll("\\s+", " ").toLowerCase();
            assertTrue(nomeNormalizado.contains("angel di maria"), "O nome deve conter 'Angel Di Maria'");
        }

    }

    @Test
    void testCriacaoEAtualizacaoNome() {
        // Criar utilizador
        UtilizadorDtoCreate dtoCreate = new UtilizadorDtoCreate();
        dtoCreate.setNif("232111090");
        dtoCreate.setNome("Vangelis Pavlidis");
        dtoCreate.setEmail("pavlidis.v@benfica.com");

        UtilizadorDto dtoCriado = utilizadorService.createUtilizador(dtoCreate);
        assertEquals("Vangelis Pavlidis", dtoCriado.getNome(), "O nome deve ser o mesmo após criação");

        // Atualizar nome
        UtilizadorDto dtoChangedName = utilizadorService.atualizarNome(dtoCriado.getId(), "Angel Di Maria");
        assertEquals("Angel Di Maria", dtoChangedName.getNome(), "O nome deve ter sido atualizado corretamente");
    }

    @Test
    void testPesquisaNomeNaoCorrespondente() {
        UtilizadorDtoCreate dto = new UtilizadorDtoCreate();
        dto.setNif("888888888");
        dto.setNome("Angela Di Martino");
        dto.setEmail("angela.dm@example.com");

        utilizadorService.createUtilizador(dto);

        assertThrows(RuntimeException.class, () -> {
            utilizadorService.procurarPorNome("Angel Di Maria");
        });
    }


}