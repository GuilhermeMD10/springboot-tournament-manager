package pt.ul.fc.css.soccernow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import pt.ul.fc.css.soccernow.dto.EquipaDto;
import pt.ul.fc.css.soccernow.service.EquipaService;


@RestController
@RequestMapping("/api/equipas")
@Tag(name = "Equipas", description = "Endpoints para gestão de equipas")

public class EquipaController {

    @Autowired
    private EquipaService equipaService;

    //criar nova equipa
    @PostMapping
    @Operation(summary = "Registar nova equipa")
    public ResponseEntity<EquipaDto> criarEquipa(@RequestBody EquipaDto equipaDto) {
        EquipaDto equipaCriada = equipaService.createEquipa(equipaDto);   
        return ResponseEntity.ok(equipaCriada);
    }

    //listar todas as equipas
    @GetMapping
    @Operation(summary = "Listar todas as equipas")
    public ResponseEntity<List<EquipaDto>> listarTodos() {
        return ResponseEntity.ok(equipaService.getAllEquipas());
    }

    //procurar equipa por ID
    @GetMapping("/{id}")
    @Operation(summary = "Procurar equipa por ID")
    public ResponseEntity<EquipaDto> procurarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(equipaService.procurarPorId(id));
    }

    //gets para filtar equipa por nome
    //jogador
    @GetMapping("/search")
    @Operation(summary = "Procurar equipa por nome")
    public ResponseEntity<List<EquipaDto>> procurarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(equipaService.procurarPorNome(nome));
    }

    @GetMapping("/searchJogador")
    @Operation(summary = "Procurar equipa por id")
    public ResponseEntity<List<EquipaDto>> procurarPorNomeJogador(@RequestParam Long id) {
        return ResponseEntity.ok(equipaService.procurarPorNomeJogador(id));
    }
    
    //deletar equipa por ID
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar equipa por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean result = equipaService.eliminarPorId(id);
        if(result){
            return ResponseEntity.ok().header("Mensagem", "Equipa eliminada com sucesso!").body(null);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    //atualizar nome da equipa
    @PutMapping("/{id}/nome")
    @Operation(summary = "Atualizar nome da equipa")
    public ResponseEntity<EquipaDto> atualizarNome(
            @PathVariable Long id,
            @RequestBody String novoNome) {
        return ResponseEntity.ok(equipaService.atualizarNome(id, novoNome));
    }

    //adicionar jogador ao plantel da equipa
    @PutMapping("/{id}/plantel")
    @Operation(summary = "Adicionar jogador ao plantel da equipa")
    public ResponseEntity<EquipaDto> adicionarJogador(
            @PathVariable Long id,
            @RequestBody Long jogadorId) {
        return ResponseEntity.ok(equipaService.adicionaJogador(jogadorId, id));
    }


    //remover jogador do plantel da equipa
    @DeleteMapping("/{id}/plantel/{jogadorId}")
    @Operation(summary = "Remover jogador do plantel da equipa")
    public ResponseEntity<EquipaDto> removerJogador(
            @PathVariable Long id,
            @PathVariable Long jogadorId) {
        return ResponseEntity.ok(equipaService.removeJogador(jogadorId, id));
    }

    //get equipas com x jogadores ou mais
    @GetMapping("/plantelMais")
    @Operation(summary = "Listar equipas com x jogadores ou mais")
    public ResponseEntity<List<EquipaDto>> listarEquipasComXJogadoresOuMais(@RequestParam Long x) {
        return ResponseEntity.ok(equipaService.getEquipasComXJogadoresOuMais(x));
    }

    //get equipas com x jogadores ou menos
    @GetMapping("/plantelMenos")
    @Operation(summary = "Listar equipas com x jogadores ou menos")
    public ResponseEntity<List<EquipaDto>> listarEquipasComXJogadoresOuMenos(@RequestParam Long x) {
        return ResponseEntity.ok(equipaService.getEquipasComXJogadoresOuMenos(x));
    }

    //get equipas com x jogadores
    @GetMapping("/plantel")
    @Operation(summary = "Listar equipas com x jogadores")
    public ResponseEntity<List<EquipaDto>> listarEquipasComXJogadores(@RequestParam Long x) {
        return ResponseEntity.ok(equipaService.getEquipasComXJogadores(x));
    }

    // Endpoint para listar as equipas com mais vitórias
    @GetMapping("/mais-vitorias")
    @Operation(summary = "Listar equipas com mais vitórias")
    public ResponseEntity<List<EquipaDto>> listarEquipasMaisVitorias(@RequestParam Long x) {
        return ResponseEntity.ok(equipaService.getTop5EquipasMaisVitorias());
    }

    // Endpoint para listar as equipas com X vitórias
    @GetMapping("/vitorias")
    @Operation(summary = "Listar equipas com X vitórias")
    public ResponseEntity<List<EquipaDto>> listarEquipasXVitorias(@RequestParam Long x) {
        return ResponseEntity.ok(equipaService.getEquipasXVitorias(x));
    }

    // Endpoint para listar as equipas com X derrotas
    @GetMapping("/derrotas")
    @Operation(summary = "Listar equipas com X derrotas")
    public ResponseEntity<List<EquipaDto>> listarEquipasXDerrotas(@RequestParam Long x) {
        return ResponseEntity.ok(equipaService.getEquipasXDerrotas(x));
    }

    // Endpoint para listar as equipas com X empates
    @GetMapping("/empates")
    @Operation(summary = "Listar equipas com X empates")
    public ResponseEntity<List<EquipaDto>> listarEquipasXEmpates(@RequestParam Long x) {
        return ResponseEntity.ok(equipaService.getEquipasXEmpates(x));
    }


    /*
     • Filtros da Equipa:
        – Filtro por nome. feito
        – Filtro por n´umero de jogadores. feito
        – Filtro por n´umero de vit´orias, empates ou derrotas. 
        – Filtro por conquistas.    //equipas que apareceram em X campeonato, criar endpoint para id e para nome de campeonato
        – Filtro por ausˆencia de jogadores numa posi¸c˜ao espec´ıfica.
    */

    // Endpoint para listar equipa que venceu determinado campeonato
    @GetMapping("/conquista_nome")
    @Operation(summary = "Filtrar pelas equipas que possuem certo campeonato nas conquistas")
    public ResponseEntity<List<EquipaDto>> listarEquipaConquista(@RequestParam String campNome) {
        return ResponseEntity.ok(equipaService.getEquipasConquista(campNome));
    }
    
    @GetMapping("/falta_pos")
    @Operation(summary = "Filtrar pelas equipas que nao possuem jogadores com a posicao pos")
    public ResponseEntity<List<EquipaDto>> listarEquipasSemPos(@RequestParam String pos) {
        return ResponseEntity.ok(equipaService.getEquipasSemPos(pos));
    }
    
}
