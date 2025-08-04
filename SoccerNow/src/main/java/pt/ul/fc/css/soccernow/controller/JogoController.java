package pt.ul.fc.css.soccernow.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pt.ul.fc.css.soccernow.dto.JogoCreateDto;
import pt.ul.fc.css.soccernow.dto.JogoDto;
import pt.ul.fc.css.soccernow.dto.ResultadoDto;
import pt.ul.fc.css.soccernow.model.enums.Turno;
import pt.ul.fc.css.soccernow.service.JogoService;

@RestController
@RequestMapping("/api/jogos")
@Api(value = "Jogos API", tags = "Jogos")
public class JogoController {
    
    @Autowired
    private JogoService jogoService;

    @GetMapping
    @ApiOperation(value = "Get all jogos", notes = "Returns a list of all jogos.")
    public ResponseEntity<List<JogoDto>> getAllJogos() {
        return ResponseEntity.ok(jogoService.getAllJogos());
    }

    @PostMapping("/create")
    @ApiOperation(value = "Create Jogo", notes = "Create a new Jogo and returns the jogo DTO.")
    public ResponseEntity<JogoDto> createJogo(@RequestBody JogoCreateDto jogoCreateDto) {
        JogoDto responseDto = jogoService.createJogo(jogoCreateDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}/resultado")
    @ApiOperation(value = "Get Resultado de um Jogo", notes = "Get o resultado de um jogo.")
    public ResponseEntity<ResultadoDto> getResultadoDoJogo(@PathVariable("id") Long id){
        ResultadoDto responseDto = jogoService.getResultadoDoJogo(id);
        return ResponseEntity.ok(responseDto);
    }


    @GetMapping("/id/{equipa_id}")
    @ApiOperation(value = "Get Jogos de uma equipa, de ID", notes = "Get os jogos de uma equipaID.")
    public ResponseEntity<List<JogoDto>> getJogosDeEquipaId(@PathVariable("equipa_id") Long equipa_id){
        List<JogoDto> responseDto = jogoService.getJogosDeEquipaId(equipa_id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/nome/{equipa_nome}")
    @ApiOperation(value = "Get Jogos de uma equipa, de ID", notes = "Get os jogos de uma equipaNome.")
    public ResponseEntity<List<JogoDto>> getJogosDeEquipaNome(@PathVariable("equipa_nome") String equipa_nome){
        List<JogoDto> responseDto = jogoService.getJogosDeEquipaNome(equipa_nome);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("filtar/realizados")
    @ApiOperation(value = "Filtrar por Jogos realizados", notes = "Retorna os jogos que já foram realizados")
    public ResponseEntity<List<JogoDto>> getJogosRealizados(){
        return ResponseEntity.ok(jogoService.getJogosRealizados());
    }

    @GetMapping("filtar/nao_realizados")
    @ApiOperation(value = "Filtrar por Jogos não realizados", notes = "Retorna os jogos que não foram realizados")
    public ResponseEntity<List<JogoDto>> getJogosNaoRealizados(){
        return ResponseEntity.ok(jogoService.getJogosNaoRealizados());
    }
    
    @GetMapping("filtar/golos/top5")
    @ApiOperation(value = "Filtrar jogos pelos 5 que tiveram mais golos", notes = "Retorna os 5 jogos em que houve mais golos")
    public ResponseEntity<List<JogoDto>> getTopJogosByGolos(){
        return ResponseEntity.ok(jogoService.getTopJogosPorGolos());
    }

    @GetMapping("filtar/golos/{num_golos}")
    @ApiOperation(value = "Filtrar jogos que tiveram X golos", notes = "Retorna os jogos que tenham X golos")
    public ResponseEntity<List<JogoDto>> getJogosByGolos(@PathVariable("num_golos") Integer num_golos){
        return ResponseEntity.ok(jogoService.getJogosByGolos(num_golos));
    }

    @GetMapping("filtar/golos/resultado/{golos_visitado}:{golos_visitante}")
    @ApiOperation(value = "Filtrar jogos que tiveram o resultado dado", notes = "Retorna os jogos que tenham o resultado dado")
    public ResponseEntity<List<JogoDto>> getJogosByGolos(@PathVariable("golos_visitado") Integer golos_visitado,
    @PathVariable("golos_visitante") Integer golos_visitante){
        return ResponseEntity.ok(jogoService.getJogosByGolosOfResultado(golos_visitado,golos_visitante));
    }

    @GetMapping("filtar/localização/{local}")
    @ApiOperation(value = "Filtrar jogos que foram jogados nessa localzação", 
        notes = "Retorna os jogos que tenham sido jogados na localzação dada")
    public ResponseEntity<List<JogoDto>> getJogosByLocalizacao(@PathVariable("local") String local){
        return ResponseEntity.ok(jogoService.getJogosByLocalizacao(local));
    }

    @GetMapping("filtar/horario")
    @ApiOperation(value = "Filtrar jogos que foram jogados nesse horario (Manha/Tarde/Noite)", 
        notes = "Retorna os jogos que tenham sido jogados no horario dado")
    public ResponseEntity<List<JogoDto>> getJogosByHorario(@RequestParam Turno turno){
        return ResponseEntity.ok(jogoService.getJogosByHorario(turno));
    }

    @GetMapping("filtar/data")
    @ApiOperation(value = "Filtrar jogos que foram jogados nessa data)", 
        notes = "Retorna os jogos que tenham sido jogados na data dada")
    public ResponseEntity<List<JogoDto>> getJogosByDate(@RequestParam LocalDate data){
        return ResponseEntity.ok(jogoService.getJogosByData(data));
    }
}


