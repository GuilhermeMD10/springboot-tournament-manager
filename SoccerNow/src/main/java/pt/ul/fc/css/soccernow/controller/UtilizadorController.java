package pt.ul.fc.css.soccernow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pt.ul.fc.css.soccernow.dto.UtilizadorDto;
import pt.ul.fc.css.soccernow.dto.UtilizadorDtoCreate;
import pt.ul.fc.css.soccernow.service.UtilizadorService;

@RestController
@RequestMapping("/api/utilizadores")
@Api(value = "Utilizadores", tags = "Utilizadores")
public class UtilizadorController {

    @Autowired
    private UtilizadorService utilizadorService;

    @PostMapping
    @ApiOperation(value = "Registar novo utilizador", notes = "Registra um novo utilizador na aplicação.")
    public ResponseEntity<UtilizadorDto> criarUtilizador(@RequestBody UtilizadorDtoCreate utilizadorDtoCreate) {
        UtilizadorDto criado = utilizadorService.createUtilizador(utilizadorDtoCreate);
        return ResponseEntity.ok(criado);
    }

    @GetMapping
    @ApiOperation(value = "Listar todos os utilizadores", notes = "Retorna uma lista de todos os utilizadores cadastrados.")
    public ResponseEntity<List<UtilizadorDto>> listarTodos() {
        return ResponseEntity.ok(utilizadorService.getTodosUtilizadores());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Procurar utilizador por ID", notes = "Retorna um utilizador específico baseado no seu ID.")
    public ResponseEntity<UtilizadorDto> procurarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(utilizadorService.procurarPorId(id));
    }

    @GetMapping("/by-nome/{nome}")
    @ApiOperation(value = "Procurar utilizador por nome", notes = "Retorna uma lista de utilizadores baseados no nome fornecido.")
    public ResponseEntity<List<UtilizadorDto>> procurarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(utilizadorService.procurarPorNome(nome));
    }    

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Eliminar utilizador por ID", notes = "Elimina um utilizador específico baseado no seu ID.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        utilizadorService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/por-nif")
    @ApiOperation(value = "Procurar utilizador por NIF", notes = "Retorna o utilizador com o NIF indicado.")
    public ResponseEntity<UtilizadorDto> procurarPorNif(@RequestParam String nif) {
        UtilizadorDto utilizador = utilizadorService.procurarPorNif(nif);
        return ResponseEntity.ok(utilizador);
    }

    @GetMapping("/por-email")
    @ApiOperation(value = "Procurar utilizador por email", notes = "Retorna o utilizador com o email indicado.")
    public ResponseEntity<UtilizadorDto> procurarPorEmail(@RequestParam String email) {
        UtilizadorDto utilizador = utilizadorService.procurarPorEmail(email);
        return ResponseEntity.ok(utilizador);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Atualizar utilizador", notes = "Atualiza email, nif e nome de um utilizador existente.")
    public ResponseEntity<UtilizadorDto> updateUtilizador(@PathVariable Long id, @RequestBody UtilizadorDto utilizadorDto) {
        if (!id.equals(utilizadorDto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            UtilizadorDto atualizado = utilizadorService.updateUtilizador(utilizadorDto);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
