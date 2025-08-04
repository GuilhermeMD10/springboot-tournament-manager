package pt.ul.fc.css.soccernow.controllerJavafx;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pt.ul.fc.css.soccernow.dtoJavafx.EquipaTitularCreateDtoJavaFx;
import pt.ul.fc.css.soccernow.dtoJavafx.EquipaTitularDtoJavaFx;
import pt.ul.fc.css.soccernow.serviceJavafx.EquipaTitularServiceJavaFx;

@RestController
@RequestMapping("/apiJavafx/equipastitulares")
@Api(value = "Equipa Titular API", tags = "EquipasTitulares")
public class EquipaTitularControllerJavaFx {
    
    @Autowired
    private EquipaTitularServiceJavaFx equipaTitularService;

    @GetMapping
    @ApiOperation(value = "Get all equipas titulares", notes = "Returns a list of all equipasTitulares.")
    public ResponseEntity<List<EquipaTitularDtoJavaFx>> getAllEquipasTitulares() {
        return ResponseEntity.ok(equipaTitularService.getAllEquipaTitular());
    }

    @GetMapping("/by-equipa/{equipa}")
    @ApiOperation(value = "Get equipaTitular by equipa", notes = "Returns equipaTitulares de uma determinada equipa.")
     public ResponseEntity<List<EquipaTitularDtoJavaFx>> getCustomerByVat(@PathVariable("equipa") Long equipa_id) {
        List<EquipaTitularDtoJavaFx> equipaTitularDto = equipaTitularService.getEquipasTitularesByEquipa(equipa_id);
        if (equipaTitularDto != null) {
            return ResponseEntity.ok(equipaTitularDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ApiOperation(value = "Create equipa titular", notes = "Creates a new equipa titular and returns the created equipa titular DTO.")
    public ResponseEntity<EquipaTitularDtoJavaFx> createCustomer(@RequestBody EquipaTitularCreateDtoJavaFx equipaTitularDto) {
        EquipaTitularDtoJavaFx responseDto = equipaTitularService.createEquipaTitular(equipaTitularDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get EquipaTitular by id")
     public ResponseEntity<EquipaTitularDtoJavaFx> getEquipaTitularId(@PathVariable("id") Long titularId) {
        EquipaTitularDtoJavaFx equipaTitularDto = equipaTitularService.getEquipaTitularById(titularId);
        if (equipaTitularDto != null) {
            return ResponseEntity.ok(equipaTitularDto);
        }
        return ResponseEntity.notFound().build();
    }

}
