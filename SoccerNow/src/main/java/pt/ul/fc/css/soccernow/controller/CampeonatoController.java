package pt.ul.fc.css.soccernow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import pt.ul.fc.css.soccernow.dto.CampeonatoDto;
import pt.ul.fc.css.soccernow.dto.EquipaDto;
import pt.ul.fc.css.soccernow.service.CampeonatoService;

@RestController
@RequestMapping("/api/campeonatos")
@Tag(name = "Campeonatos", description = "Endpoints para gestão de campeonatos")



public class CampeonatoController {

    @Autowired
    private CampeonatoService campeonatoService;
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
    public ResponseEntity<CampeonatoDto> criarCampeonato(@RequestBody CampeonatoDto campeonatoDto) {
        CampeonatoDto campCriado = campeonatoService.createCampeonato(campeonatoDto);   
        return ResponseEntity.ok(campCriado);
    }


    //listar todos os campeonatos
    @GetMapping()
    @Operation(summary = "Listar todos os campeonatos")
    public ResponseEntity<List<CampeonatoDto>> listarTodos() {
        return ResponseEntity.ok(campeonatoService.getAllCampeonatos());
    }

    //listar camp pelo nome
    @GetMapping("/nome")
    @Operation(summary = "Listar campeonato pelo nome dado")
    public ResponseEntity<CampeonatoDto> getCampByNome(@RequestParam String nome) {
        return ResponseEntity.ok(campeonatoService.getCampeonatoByNome(nome));
    }

    /*
     *  – Filtro por n´umero de jogos realizados.
        – Filtro por n´umero de jogos a realizar.
     */
    //listar camp com X jogos realizados
    @GetMapping("/realizados")
    @Operation(summary = "Listar campeonatos com X jogos realizados")
    public ResponseEntity<List<CampeonatoDto>> getCampFinishedGames(@RequestParam Long x) {
        return ResponseEntity.ok(campeonatoService.getCampJogosRealizados(x));
    }

    //listar camp com X jogos por realizar
    @GetMapping("/por_realizar")
    @Operation(summary = "Listar campeonatos com X jogos por realizar")
    public ResponseEntity<List<CampeonatoDto>> getCampUnfinishedGames(@RequestParam Long x) {
        return ResponseEntity.ok(campeonatoService.getCampJogosPorRealizar(x));
    }


    //adicionar equipa ao campeonato
    @PutMapping("/{id}/equipa")
    @Operation(summary = "Adicionar equipa ao campeonato")
    public ResponseEntity<CampeonatoDto> adicionarEquipa(
            @PathVariable Long id,
            @RequestBody Long equipaId) {
        return ResponseEntity.ok(campeonatoService.adicionaEquipa(equipaId, id));
    }

    //remover equipa do campeonato
    @DeleteMapping("/{id}/equipa/{equipaId}")
    @Operation(summary = "Remover equipa do campeonato")
    public ResponseEntity<CampeonatoDto> removerEquipa(
            @PathVariable Long id,
            @PathVariable Long equipaId) {
        return ResponseEntity.ok(campeonatoService.removeEquipa(equipaId, id));
    }

    //atualizar nome do camp
    @PutMapping("/{id}/nome")
    @Operation(summary = "Atualizar nome do campeonato")
    public ResponseEntity<CampeonatoDto> atualizarNome(
            @PathVariable Long id,
            @RequestBody String novoNome) {
        return ResponseEntity.ok(campeonatoService.atualizarNome(id, novoNome));
    }

    //Get campeonatos em que Equipa X(por id) estah a competir
    @GetMapping("/equipa")
    @Operation(summary = "Listar campeonatos em que Equipa X estah a participar ou ja participou")
    public ResponseEntity<List<CampeonatoDto>> getCampsEquipa(@RequestParam Long equipaId) {
        return ResponseEntity.ok(campeonatoService.getCampsPorEquipa(equipaId));
    }

    @PutMapping("/{id}/comecar")
    public ResponseEntity<CampeonatoDto> comecarCampeonato(@PathVariable Long id) {
        CampeonatoDto dto = campeonatoService.comecarCampeoanto(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/terminar")
    public ResponseEntity<CampeonatoDto> terminarCampeonato(@PathVariable Long id) {
        CampeonatoDto dto = campeonatoService.terminarCampeonato(id);
        return ResponseEntity.ok(dto);
    }

}
