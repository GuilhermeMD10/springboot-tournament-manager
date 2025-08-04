package pt.ul.fc.css.soccernow.controllerJavafx;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import pt.ul.fc.css.soccernow.dtoJavafx.EquipaDtoJavaFx;
import pt.ul.fc.css.soccernow.dtoJavafx.ResultadoCreateDtoJavaFx;
import pt.ul.fc.css.soccernow.dtoJavafx.ResultadoDtoJavaFx;
import pt.ul.fc.css.soccernow.serviceJavafx.ResultadoServiceJavaFx;

@RestController
@RequestMapping("/apiJavafx/resultados")
@Api(value = "Resultado API", tags = "Resultados")
public class ResultadoControllerJavaFx {
    @Autowired
    private ResultadoServiceJavaFx resultadoService;

    @GetMapping
    @ApiOperation(value = "Get all resultados", notes = "Returns a list of all resultados.")
    public ResponseEntity<List<ResultadoDtoJavaFx>> getAllSales() {
        return ResponseEntity.ok(resultadoService.getAllResultados());
    }

    @PostMapping("/create/{jogo_id}")
    @ApiOperation(value = "Create resultado",
        notes = "Creates a new resultado. Provide Vencedor, (Golos/Amarelos/Vermelhos) (visitante/visitado) in the request body.")
    public ResponseEntity<ResultadoDtoJavaFx> createResultado(@RequestBody ResultadoCreateDtoJavaFx resultadoDto, @PathVariable("jogo_id") Long jogo_id) {
        ResultadoDtoJavaFx createdResultado = resultadoService.createResultado(resultadoDto,jogo_id);
        return ResponseEntity.ok(createdResultado);
    }

    //procurar equipa por ID
    @GetMapping("/{id}")
    @Operation(summary = "Procurar equipa por ID")
    public ResponseEntity<ResultadoDtoJavaFx> procurarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(resultadoService.getResultadoById(id));
    }
    
}
