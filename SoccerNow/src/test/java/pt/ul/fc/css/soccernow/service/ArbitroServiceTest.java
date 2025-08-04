package pt.ul.fc.css.soccernow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import pt.ul.fc.css.soccernow.dto.ArbitroDto;
import pt.ul.fc.css.soccernow.dto.ArbitroDtoCreate;
import pt.ul.fc.css.soccernow.dto.ArbitroDtoUpdate;

@SpringBootTest
@Transactional
public class ArbitroServiceTest {

    @Autowired
    ArbitroService arbitroService;

    @Test
    void testCreateArbitro(){
        ArbitroDtoCreate arbitroDtoCreate = new ArbitroDtoCreate();
        arbitroDtoCreate.setEmail("a@b.com");
        arbitroDtoCreate.setNif("99999999999999999999999999");
        arbitroDtoCreate.setNome("Artur Soares Dias");

        //Invalid nif
        assertThrows(IllegalArgumentException.class, () -> {
            arbitroService.createArbitro(arbitroDtoCreate);
        });

        arbitroDtoCreate.setNif("333999333");
        arbitroDtoCreate.setEmail("invalidemail");

        //Invalid email
        assertThrows(IllegalArgumentException.class, () -> {
            arbitroService.createArbitro(arbitroDtoCreate);
        });

        arbitroDtoCreate.setEmail("teste@gmail.com");


        ArbitroDto arbitroDtoSaved = arbitroService.createArbitro(arbitroDtoCreate);

        ArbitroDto arbitroDto = arbitroService.procurarPorEmail("teste@gmail.com");
        
        assertEquals(arbitroDtoSaved.getId(), arbitroDto.getId());
        assertEquals(arbitroDtoSaved.getNif(), arbitroDto.getNif());
        assertEquals(arbitroDtoSaved.getEmail(), arbitroDto.getEmail());
    }

    @Test
    void testUpdateArbitro(){
        ArbitroDtoCreate arbitroDtoCreate = new ArbitroDtoCreate();
        arbitroDtoCreate.setEmail("a@b.com");
        arbitroDtoCreate.setNif("333222333");
        arbitroDtoCreate.setNome("Artur Soares Dias");

        ArbitroDto arbitroDto = arbitroService.createArbitro(arbitroDtoCreate);

        assertFalse(arbitroDto.isCertificado());



        arbitroService.putCertificado(arbitroDto.getId());

        ArbitroDtoUpdate arbitroDtoUpdate = new ArbitroDtoUpdate();
        arbitroDtoUpdate.setId(arbitroDto.getId());
        arbitroDtoUpdate.setCertificado(true);
        arbitroDtoUpdate.setEmail("novo@email.pt");
        arbitroDtoUpdate.setNif("777999777");
        arbitroDtoUpdate.setNome("João Pinheiro");
        ArbitroDto arbitroDtoUpdated = arbitroService.updateArbitro(arbitroDtoUpdate);

        assertEquals(arbitroDtoUpdated.getEmail(), "novo@email.pt", "O email deve alterar");
        assertEquals(arbitroDtoUpdated.getNif(), "777999777", "O nif deve ser alterado");
        assertEquals(arbitroDtoUpdated.getNome(), "João Pinheiro");
        assertEquals(arbitroDtoUpdated.getId(), arbitroDto.getId(), "O id deve se manter igual");

    }
    
}
