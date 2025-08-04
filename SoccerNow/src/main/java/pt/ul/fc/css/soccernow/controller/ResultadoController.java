package pt.ul.fc.css.soccernow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pt.ul.fc.css.soccernow.dto.ResultadoCreateDto;
import pt.ul.fc.css.soccernow.dto.ResultadoDto;
import pt.ul.fc.css.soccernow.service.ResultadoService;

@RestController
@RequestMapping("/api/resultados")
@Api(value = "Resultado API", tags = "Resultados")
public class ResultadoController {
    @Autowired
    private ResultadoService resultadoService;

    @GetMapping
    @ApiOperation(value = "Get all resultados", notes = "Returns a list of all resultados.")
    public ResponseEntity<List<ResultadoDto>> getAllSales() {
        return ResponseEntity.ok(resultadoService.getAllResultados());
    }

    @PostMapping("/create/{jogo_id}")
    @ApiOperation(value = "Create resultado",
        notes = "Creates a new resultado. Provide Vencedor, (Golos/Amarelos/Vermelhos) (visitante/visitado) in the request body.")
    public ResponseEntity<ResultadoDto> createResultado(@RequestBody ResultadoCreateDto resultadoDto, @PathVariable("jogo_id") Long jogo_id) {
        ResultadoDto createdResultado = resultadoService.createResultado(resultadoDto,jogo_id);
        return ResponseEntity.ok(createdResultado);
    }
    
}
