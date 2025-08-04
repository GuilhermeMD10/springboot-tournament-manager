package pt.ul.fc.css.soccernow.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pt.ul.fc.css.soccernow.dto.EquipaTitularCreateDto;
import pt.ul.fc.css.soccernow.dto.EquipaTitularDto;
import pt.ul.fc.css.soccernow.service.EquipaTitularService;

@RestController
@RequestMapping("/api/equipastitulares")
@Api(value = "Equipa Titular API", tags = "EquipasTitulares")
public class EquipaTitularController {
    
    @Autowired
    private EquipaTitularService equipaTitularService;

    @GetMapping
    @ApiOperation(value = "Get all equipas titulares", notes = "Returns a list of all equipasTitulares.")
    public ResponseEntity<List<EquipaTitularDto>> getAllEquipasTitulares() {
        return ResponseEntity.ok(equipaTitularService.getAllEquipaTitular());
    }

    @GetMapping("/by-equipa/{equipa}")
    @ApiOperation(value = "Get equipaTitular by equipa", notes = "Returns equipaTitulares de uma determinada equipa.")
     public ResponseEntity<List<EquipaTitularDto>> getCustomerByVat(@PathVariable("equipa") Long equipa_id) {
        List<EquipaTitularDto> equipaTitularDto = equipaTitularService.getEquipasTitularesByEquipa(equipa_id);
        if (equipaTitularDto != null) {
            return ResponseEntity.ok(equipaTitularDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ApiOperation(value = "Create equipa titular", notes = "Creates a new equipa titular and returns the created equipa titular DTO.")
    public ResponseEntity<EquipaTitularDto> createCustomer(@RequestBody EquipaTitularCreateDto equipaTitularDto) {
        EquipaTitularDto responseDto = equipaTitularService.createEquipaTitular(equipaTitularDto);
        return ResponseEntity.ok(responseDto);
    }

}
