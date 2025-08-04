package pt.ul.fc.css.soccernow.controllerJavafx;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pt.ul.fc.css.soccernow.dtoJavafx.ArbitroDtoJavaFx;
import pt.ul.fc.css.soccernow.dtoJavafx.ArbitroDtoCreateJavaFx;
import pt.ul.fc.css.soccernow.dtoJavafx.ArbitroDtoUpdateJavaFx;
import pt.ul.fc.css.soccernow.serviceJavafx.ArbitroServiceJavaFx;

@RestController
@RequestMapping("/apiJavafx/arbitros")
@Api(value = "Árbitros", tags = "Árbitros")
public class ArbitroControllerJavaFx {

    @Autowired
    private ArbitroServiceJavaFx arbitroService;

    @PostMapping
    @ApiOperation(value = "Criar árbitro", notes = "Cria um novo árbitro e retorna o árbitro criado.")
    public ArbitroDtoJavaFx criarArbitro(@RequestBody ArbitroDtoCreateJavaFx arbitroDtoCreate) {
        return arbitroService.createArbitro(arbitroDtoCreate);
    }

    @GetMapping
    @ApiOperation(value = "Listar árbitros", notes = "Retorna uma lista de todos os árbitros.")
    public List<ArbitroDtoJavaFx> listarTodos() {
        return arbitroService.getTodosArbitros();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Procurar árbitro por ID", notes = "Retorna um árbitro específico baseado no seu ID.")
    public ArbitroDtoJavaFx procurarPorId(@PathVariable Long id) {
        return arbitroService.procurarPorId(id);
    }

    @GetMapping("/certificado")
    @ApiOperation(value = "Procurar árbitros por certificado", notes = "Retorna uma lista de árbitros com base no status de certificado.")
    public List<ArbitroDtoJavaFx> procurarPorCertificado(@RequestParam boolean certificado) {
        return arbitroService.procurarPorCertificado(certificado);
    }

    @GetMapping("/by-nome/{nome}")
    @ApiOperation(value = "Procurar árbitros por nome", notes = "Retorna uma lista de árbitros baseados no nome fornecido.")
    public List<ArbitroDtoJavaFx> procurarPorNome(@PathVariable String nome) {
        return arbitroService.procurarPorNome(nome);
    }    

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Remover árbitro", notes = "Remove um árbitro específico baseado no seu ID.")
    public ResponseEntity<Void> removerArbitro(@PathVariable Long id) {
        arbitroService.removerArbitro(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/certificar")
    @ApiOperation(value = "Certificar árbitro", notes = "Certifica um árbitro com base no seu ID.")
    public ResponseEntity<Void> certificarArbitro(@PathVariable Long id) {
        arbitroService.putCertificado(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mais-jogos")
    @ApiOperation(value = "Encontrar árbitro com mais jogos", notes = "Retorna o árbitro que tem mais jogos na equipa de arbitragem.")
    public List<ArbitroDtoJavaFx> encontrarArbitroComMaisJogos() {
        return arbitroService.findArbitroComMaisJogos();
    }


    @GetMapping("/menos-jogos")
    @ApiOperation(value = "Encontrar árbitro com menos jogos", notes = "Retorna o árbitro que tem menos jogos na equipa de arbitragem.")
    public List<ArbitroDtoJavaFx> encontrarArbitroComMenosJogos() {
        return arbitroService.findArbitroComMenosJogos();
    }

    @GetMapping("/por-jogos")
    @ApiOperation(value = "Procurar árbitros por número de jogos", notes = "Retorna árbitros com exatamente o número de jogos especificado.")
    public List<ArbitroDtoJavaFx> procurarPorNumeroDeJogos(@RequestParam int partidas) {
        return arbitroService.findByPartidasOficiadas(partidas);
    }

    @GetMapping("/jogos-maior-que")
    @ApiOperation(value = "Procurar árbitros com mais jogos", notes = "Retorna árbitros com mais jogos do que o valor especificado.")
    public List<ArbitroDtoJavaFx> procurarPorMaisJogosQue(@RequestParam int partidas) {
        return arbitroService.findByPartidasOficiadasGreaterThan(partidas);
    }

    @GetMapping("/jogos-menor-que")
    @ApiOperation(value = "Procurar árbitros com menos jogos", notes = "Retorna árbitros com menos jogos do que o valor especificado.")
    public List<ArbitroDtoJavaFx> procurarPorMenosJogosQue(@RequestParam int partidas) {
        return arbitroService.findByPartidasOficiadasLessThan(partidas);
    }

    @GetMapping("/menos-cartoes")
    @ApiOperation(value = "Encontrar árbitro com menos cartões", notes = "Retorna o árbitro que mostrou menos cartões.")
    public List<ArbitroDtoJavaFx> encontrarArbitroComMenosCartoes() {
        return arbitroService.findArbitroComMenosCartoesMostrados();
    }

    @GetMapping("/mais-cartoes")
    @ApiOperation(value = "Encontrar árbitro com mais cartões", notes = "Retorna o árbitro que mostrou mais cartões.")
    public List<ArbitroDtoJavaFx> encontrarArbitroComMaisCartoes() {
        return arbitroService.findArbitroComMaisCartoesMostrados();
    }

    @GetMapping("/por-cartoes")
    @ApiOperation(value = "Procurar árbitros por número de cartões", notes = "Retorna árbitros com exatamente o número de cartões mostrados especificado.")
    public List<ArbitroDtoJavaFx> procurarPorNumeroDeCartoes(@RequestParam int cartoes) {
        return arbitroService.findByCartoesMostrados(cartoes);
    }

    @GetMapping("/cartoes-maior-que")
    @ApiOperation(value = "Procurar árbitros com mais cartões", notes = "Retorna árbitros com mais cartões do que o valor especificado.")
    public List<ArbitroDtoJavaFx> procurarPorMaisCartoesQue(@RequestParam int cartoes) {
        return arbitroService.findByCartoesMostradosGreaterThan(cartoes);
    }

    @GetMapping("/cartoes-menor-que")
    @ApiOperation(value = "Procurar árbitros com menos cartões", notes = "Retorna árbitros com menos cartões do que o valor especificado.")
    public List<ArbitroDtoJavaFx> procurarPorMenosCartoesQue(@RequestParam int cartoes) {
        return arbitroService.findByCartoesMostradosLessThan(cartoes);
    }

    @GetMapping("/por-nif")
    @ApiOperation(value = "Procurar árbitro por NIF", notes = "Retorna o árbitro com o NIF indicado.")
    public ResponseEntity<ArbitroDtoJavaFx> procurarPorNif(@RequestParam String nif) {
        ArbitroDtoJavaFx arbitro = arbitroService.procurarPorNif(nif);
        return ResponseEntity.ok(arbitro);
    }

    @GetMapping("/por-email")
    @ApiOperation(value = "Procurar árbitro por email", notes = "Retorna o árbitro com o email indicado.")
    public ResponseEntity<ArbitroDtoJavaFx> procurarPorEmail(@RequestParam String email) {
        ArbitroDtoJavaFx arbitro = arbitroService.procurarPorEmail(email);
        return ResponseEntity.ok(arbitro);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Atualizar árbitro", notes = "Atualiza email, nif e nome de um árbitro existente.")
    public ResponseEntity<ArbitroDtoJavaFx> updateArbitro(@PathVariable Long id, @RequestBody ArbitroDtoUpdateJavaFx arbitroDtoUpdate) {
        if (!id.equals(arbitroDtoUpdate.getId())) {
            return ResponseEntity.badRequest().build();
        }

        try {
            ArbitroDtoJavaFx atualizado = arbitroService.updateArbitro(arbitroDtoUpdate);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
