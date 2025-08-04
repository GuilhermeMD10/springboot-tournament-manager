package pt.ul.fc.css.soccernow.controllerJavafx;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import pt.ul.fc.css.soccernow.dtoJavafx.CampeonatoDtoJavaFx;
import pt.ul.fc.css.soccernow.serviceJavafx.CampeonatoServiceJavaFx;

@RestController
@RequestMapping("/apiJavafx/campeonatos")
@Tag(name = "Campeonatos", description = "Endpoints para gestão de campeonatos")



public class CampeonatoControllerJavaFx {

    @Autowired
    private CampeonatoServiceJavaFx campeonatoService;
    /*
     * criar campeonato apenas com nome
     * para já
     * 
     * 
     * Filtros do Campeonato:
        – Filtro por nome. feito
        – Filtro por equipa.
        – Filtro por n´umero de jogos realizados.
        – Filtro por n´umero de jogos a realizar.
     */

    //criar novo campeonato
    @PostMapping
    @Operation(summary = "Registar novo campeonato")
    public ResponseEntity<CampeonatoDtoJavaFx> criarCampeonato(@RequestBody CampeonatoDtoJavaFx campeonatoDto) {
        CampeonatoDtoJavaFx campCriado = campeonatoService.createCampeonato(campeonatoDto);   
        return ResponseEntity.ok(campCriado);
    }


    //listar todos os campeonatos
    @GetMapping()
    @Operation(summary = "Listar todos os campeonatos")
    public ResponseEntity<List<CampeonatoDtoJavaFx>> listarTodos() {
        return ResponseEntity.ok(campeonatoService.getAllCampeonatos());
    }

    //listar camp pelo nome
    @GetMapping("/nome")
    @Operation(summary = "Listar campeonato pelo nome dado")
    public ResponseEntity<CampeonatoDtoJavaFx> getCampByNome(@RequestParam String nome) {
        return ResponseEntity.ok(campeonatoService.getCampeonatoByNome(nome));
    }

    /*
     *  – Filtro por n´umero de jogos realizados.
        – Filtro por n´umero de jogos a realizar.
     */
    //listar camp com X jogos realizados
    @GetMapping("/realizados")
    @Operation(summary = "Listar campeonatos com X jogos realizados")
    public ResponseEntity<List<CampeonatoDtoJavaFx>> getCampFinishedGames(@RequestParam Long x) {
        return ResponseEntity.ok(campeonatoService.getCampJogosRealizados(x));
    }

    //listar camp com X jogos por realizar
    @GetMapping("/por_realizar")
    @Operation(summary = "Listar campeonatos com X jogos por realizar")
    public ResponseEntity<List<CampeonatoDtoJavaFx>> getCampUnfinishedGames(@RequestParam Long x) {
        return ResponseEntity.ok(campeonatoService.getCampJogosPorRealizar(x));
    }


    //adicionar equipa ao campeonato
    @PutMapping("/{id}/equipa")
    @Operation(summary = "Adicionar equipa ao campeonato")
    public ResponseEntity<CampeonatoDtoJavaFx> adicionarEquipa(
            @PathVariable Long id,
            @RequestBody Long equipaId) {
        return ResponseEntity.ok(campeonatoService.adicionaEquipa(equipaId, id));
    }

    //remover equipa do campeonato
    @DeleteMapping("/{id}/equipa/{equipaId}")
    @Operation(summary = "Remover equipa do campeonato")
    public ResponseEntity<CampeonatoDtoJavaFx> removerEquipa(
            @PathVariable Long id,
            @PathVariable Long equipaId) {
        return ResponseEntity.ok(campeonatoService.removeEquipa(equipaId, id));
    }

    //atualizar nome do camp
    @PutMapping("/{id}/nome")
    @Operation(summary = "Atualizar nome do campeonato")
    public ResponseEntity<CampeonatoDtoJavaFx> atualizarNome(
            @PathVariable Long id,
            @RequestBody String novoNome) {
        return ResponseEntity.ok(campeonatoService.atualizarNome(id, novoNome));
    }

    //Get campeonatos em que Equipa X(por id) estah a competir
    @GetMapping("/equipa")
    @Operation(summary = "Listar campeonatos em que Equipa X estah a participar ou ja participou")
    public ResponseEntity<List<CampeonatoDtoJavaFx>> getCampsEquipa(@RequestParam Long equipaId) {
        return ResponseEntity.ok(campeonatoService.getCampsPorEquipa(equipaId));
    }

    @PutMapping("/{id}/comecar")
    public ResponseEntity<CampeonatoDtoJavaFx> comecarCampeonato(@PathVariable Long id) {
        CampeonatoDtoJavaFx dto = campeonatoService.comecarCampeoanto(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/terminar")
    public ResponseEntity<CampeonatoDtoJavaFx> terminarCampeonato(@PathVariable Long id) {
        CampeonatoDtoJavaFx dto = campeonatoService.terminarCampeonato(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/jogo")
    @Operation(summary = "Adicionar jogo ao campeonato")
    public ResponseEntity<CampeonatoDtoJavaFx> adicionarJogoAoCampeonato(
            @PathVariable Long id,
            @RequestBody Long jogoId) {
        CampeonatoDtoJavaFx atualizado = campeonatoService.adicionarJogo(jogoId, id);
        return ResponseEntity.ok(atualizado);
    }


    @DeleteMapping("/{id}/jogo/{jogoId}")
    @Operation(summary = "Remover jogo do campeonato")
    public ResponseEntity<CampeonatoDtoJavaFx> removerJogoDoCampeonato(
            @PathVariable Long id,
            @PathVariable Long jogoId) {
        CampeonatoDtoJavaFx atualizado = campeonatoService.removerJogo(jogoId, id);
        return ResponseEntity.ok(atualizado);
    }

    //remover o campeonato
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover campeonato")
    public ResponseEntity<Void> removerCampeonato(@PathVariable Long id) {
        campeonatoService.removeCampeonato(id);
        return ResponseEntity.noContent().build();
    }

}
