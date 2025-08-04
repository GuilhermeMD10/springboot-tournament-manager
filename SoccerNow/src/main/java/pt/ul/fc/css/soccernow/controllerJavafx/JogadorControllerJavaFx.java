package pt.ul.fc.css.soccernow.controllerJavafx;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pt.ul.fc.css.soccernow.dtoJavafx.JogadorDtoJavaFx;
import pt.ul.fc.css.soccernow.dtoJavafx.JogadorDtoCreateJavaFx;
import pt.ul.fc.css.soccernow.dtoJavafx.JogadorDtoUpdateJavaFx;
import pt.ul.fc.css.soccernow.model.enums.Posicao;
import pt.ul.fc.css.soccernow.serviceJavafx.JogadorServiceJavaFx;

@RestController
@RequestMapping("/apiJavafx/jogadores")
@Api(value = "Jogadores", tags = "Jogadores")
public class JogadorControllerJavaFx {

    @Autowired
    private JogadorServiceJavaFx jogadorService;

    @PostMapping
    @ApiOperation(value = "Criar jogador", notes = "Cria um novo jogador e retorna o jogador criado.")
    public ResponseEntity<JogadorDtoJavaFx> criarJogador(@RequestBody JogadorDtoCreateJavaFx jogadorDtoCreate) {
        JogadorDtoJavaFx novoJogador = jogadorService.createJogador(jogadorDtoCreate);
        return ResponseEntity.ok(novoJogador);
    }

    @GetMapping
    @ApiOperation(value = "Listar jogadores", notes = "Retorna uma lista de todos os jogadores.")
    public ResponseEntity<List<JogadorDtoJavaFx>> listarJogadores() {
        return ResponseEntity.ok(jogadorService.getTodosJogadores());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Procurar jogador por ID", notes = "Retorna um jogador específico baseado no seu ID.")
    public ResponseEntity<JogadorDtoJavaFx> procurarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(jogadorService.procurarPorId(id));
    }

    @GetMapping("/nome/{nome}")
    @ApiOperation(value = "Procurar jogador por nome", notes = "Retorna uma lista de jogadores baseados no nome fornecido.")
    public ResponseEntity<List<JogadorDtoJavaFx>> procurarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(jogadorService.procurarPorNome(nome));
    }    

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Remover jogador", notes = "Remove um jogador específico baseado no seu ID.")
    public ResponseEntity<Void> removerJogador(@PathVariable Long id) {
        jogadorService.removerJogador(id);
        return ResponseEntity.noContent().build();
    }

    


    @PutMapping("/{id}")
    @ApiOperation(value = "Atualizar jogador", notes = "Atualiza email, nif, nome e posição preferida de um jogador existente.")
    public ResponseEntity<JogadorDtoJavaFx> updateJogador(@PathVariable Long id, @RequestBody JogadorDtoUpdateJavaFx jogadorDtoUpdate) {
        if (!id.equals(jogadorDtoUpdate.getId())) {
            return ResponseEntity.badRequest().build();
        }

        try {
            JogadorDtoJavaFx atualizado = jogadorService.updateJogador(jogadorDtoUpdate);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/por-nif")
    @ApiOperation(value = "Procurar jogador por NIF", notes = "Retorna o jogador com o NIF indicado.")
    public ResponseEntity<JogadorDtoJavaFx> procurarPorNif(@RequestParam String nif) {
        JogadorDtoJavaFx jogador = jogadorService.procurarPorNif(nif);
        return ResponseEntity.ok(jogador);
    }

    @GetMapping("/por-email")
    @ApiOperation(value = "Procurar jogador por email", notes = "Retorna o jogador com o email indicado.")
    public ResponseEntity<JogadorDtoJavaFx> procurarPorEmail(@RequestParam String email) {
        JogadorDtoJavaFx jogador = jogadorService.procurarPorEmail(email);
        return ResponseEntity.ok(jogador);
    }

    @PutMapping("/{id}/set-pref-pos")
    @ApiOperation(value = "Alterar posição preferida do jogador", notes = "Define a posição preferida de um jogador com base no ID.")
    public ResponseEntity<JogadorDtoJavaFx> setPrefPos(
            @PathVariable Long id,
            @RequestParam Posicao prefPos) {

        JogadorDtoJavaFx dto = jogadorService.setPrefPos(id, prefPos);
        return ResponseEntity.ok(dto);
    }

//----------------------------------------------------------FILTROS-----------------------------------------------\\

//----------------------------------------------------------GOLOS-----------------------------------------------\\

    @GetMapping("/filtro/golos")
    @ApiOperation(value = "Filtrar jogadores por número de golos", notes = "Retorna os jogadores que marcaram exatamente o número de golos indicado.")
    public ResponseEntity<List<JogadorDtoJavaFx>> getJogadoresPorGolos(@RequestParam int golos) {
        return ResponseEntity.ok(jogadorService.getJogadoresPorGolos(golos));
    }

    @GetMapping("/filtro/golos/maior-que")
    @ApiOperation("Filtra jogadores com mais golos que o valor dado")
    public ResponseEntity<List<JogadorDtoJavaFx>> getComMaisGolos(@RequestParam int golos) {
        return ResponseEntity.ok(jogadorService.getJogadoresComMaisGolos(golos));
    }

    @GetMapping("/filtro/golos/menor-que")
    @ApiOperation("Filtra jogadores com menos golos que o valor dado")
    public ResponseEntity<List<JogadorDtoJavaFx>> getComMenosGolos(@RequestParam int golos) {
        return ResponseEntity.ok(jogadorService.getJogadoresComMenosGolos(golos));
    }

    @GetMapping("/top5/golos/mais")
    @ApiOperation("Top 5 jogadores com mais golos marcados")
    public ResponseEntity<List<JogadorDtoJavaFx>> getTop5GolosMais() {
        return ResponseEntity.ok(jogadorService.getTop5GolosMais());
    }

    @GetMapping("/top5/golos/menos")
    @ApiOperation("Top 5 jogadores com menos golos marcados")
    public ResponseEntity<List<JogadorDtoJavaFx>> getTop5GolosMenos() {
        return ResponseEntity.ok(jogadorService.getTop5GolosMenos());
    }

    @GetMapping("/media-golos-by-nome")
    @ApiOperation(value = "Obter média de golos de jogadores por nome", notes = "Retorna a média de golos de jogadores com o nome fornecido.")
    public ResponseEntity<Double> mediaGolos(@RequestParam String nome) {
        return ResponseEntity.ok(jogadorService.getMediaGolosPorNome(nome));
    }


//----------------------------------------------------------AMARELOS-----------------------------------------------\\

    @GetMapping("/filtro/amarelos")
    @ApiOperation(value = "Filtrar jogadores por número de amarelos", notes = "Retorna os jogadores que viram exatamente o número de amarelos indicado.")
    public ResponseEntity<List<JogadorDtoJavaFx>> getJogadoresPorAmarelos(@RequestParam int amarelos) {
        return ResponseEntity.ok(jogadorService.getJogadoresPorAmarelos(amarelos));
    }

    @GetMapping("/top5/cartoes-amarelos/mais")
    @ApiOperation("Top 5 jogadores com mais cartões amarelos")
    public ResponseEntity<List<JogadorDtoJavaFx>> getTop5CartoesAmarelosMais() {
        return ResponseEntity.ok(jogadorService.getTop5CartoesAmarelosMais());
    }

    @GetMapping("/top5/cartoes-amarelos/menos")
    @ApiOperation("Top 5 jogadores com menos cartões amarelos")
    public ResponseEntity<List<JogadorDtoJavaFx>> getTop5CartoesAmarelosMenos() {
        return ResponseEntity.ok(jogadorService.getTop5CartoesAmarelosMenos());
}

    @GetMapping("/filtro/amarelos/menor-que")
    @ApiOperation("Filtra jogadores com menos cartões amarelos que o valor dado")
    public ResponseEntity<List<JogadorDtoJavaFx>> getComMenosAmarelos(@RequestParam int amarelos) {
        return ResponseEntity.ok(jogadorService.getJogadoresComMenosAmarelos(amarelos));
    }

    @GetMapping("/filtro/amarelos/maior-que")
    @ApiOperation("Filtra jogadores com mais cartões amarelos que o valor dado")
    public ResponseEntity<List<JogadorDtoJavaFx>> getComMaisAmarelos(@RequestParam int amarelos) {
        return ResponseEntity.ok(jogadorService.getJogadoresComMaisAmarelos(amarelos));
    }

//----------------------------------------------------------VERMELHOS-----------------------------------------------\\
        
    @GetMapping("/filtro/vermelhos")
    @ApiOperation(value = "Filtrar jogadores por número de vermelhos", notes = "Retorna os jogadores que viram exatamente o número de vermelhos indicado.")
    public ResponseEntity<List<JogadorDtoJavaFx>> getJogadoresPorVermelhos(@RequestParam int vermelhos) {
        return ResponseEntity.ok(jogadorService.getJogadoresPorVermelhos(vermelhos));
    }

    @GetMapping("/filtro/vermelhos/menor-que")
    @ApiOperation("Filtra jogadores com menos cartões vermelhos que o valor dado")
    public ResponseEntity<List<JogadorDtoJavaFx>> getComMenosVermelhos(@RequestParam int vermelhos) {
        return ResponseEntity.ok(jogadorService.getJogadoresComMenosVermelhos(vermelhos));
    }

    @GetMapping("/filtro/vermelhos/maior-que")
    @ApiOperation("Filtra jogadores com mais cartões vermelhos que o valor dado")
    public ResponseEntity<List<JogadorDtoJavaFx>> getComMaisVermelhos(@RequestParam int vermelhos) {
        return ResponseEntity.ok(jogadorService.getJogadoresComMaisVermelhos(vermelhos));
    }

    @GetMapping("/top5/cartoes-vermelhos/mais")
    @ApiOperation("Top 5 jogadores com mais cartões vermelhos")
    public ResponseEntity<List<JogadorDtoJavaFx>> getTop5CartoesVermelhosMais() {
        return ResponseEntity.ok(jogadorService.getTop5CartoesVermelhosMais());
    }

    @GetMapping("/top5/cartoes-vermelhos/menos")
    @ApiOperation("Top 5 jogadores com menos cartões vermelhos")
    public ResponseEntity<List<JogadorDtoJavaFx>> getTop5CartoesVermelhosMenos() {
        return ResponseEntity.ok(jogadorService.getTop5CartoesVermelhosMenos());
    }

//----------------------------------------------------------TOTAL-DE-CARTOES-----------------------------------------------\\

    @GetMapping("/filtro/cartoes")
    @ApiOperation(value = "Filtrar jogadores por total de cartões", notes = "Retorna os jogadores cuja soma de cartões amarelos e vermelhos seja igual ao valor indicado.")
    public ResponseEntity<List<JogadorDtoJavaFx>> getJogadoresPorCartoes(@RequestParam int cartoes) {
        return ResponseEntity.ok(jogadorService.getJogadoresPorCartoes(cartoes));
    }

    @GetMapping("/filtro/cartoes/maior-que")
    @ApiOperation(value = "Filtrar jogadores com mais cartões totais", notes = "Retorna os jogadores que têm mais do que o número total de cartões (amarelos + vermelhos) indicado.")
    public ResponseEntity<List<JogadorDtoJavaFx>> getJogadoresComMaisCartoes(@RequestParam int cartoes) {
        return ResponseEntity.ok(jogadorService.getJogadoresComMaisCartoesQue(cartoes));
    }

    @GetMapping("/filtro/cartoes/menor-que")
    @ApiOperation(value = "Filtrar jogadores com menos cartões totais", notes = "Retorna os jogadores que têm menos do que o número total de cartões (amarelos + vermelhos) indicado.")
    public ResponseEntity<List<JogadorDtoJavaFx>> getJogadoresComMenosCartoes(@RequestParam int cartoes) {
        return ResponseEntity.ok(jogadorService.getJogadoresComMenosCartoesQue(cartoes));
    }

    @GetMapping("/top5/total-cartoes/mais")
    @ApiOperation(value = "Top 5 jogadores com mais cartões totais", notes = "Retorna os 5 jogadores com o maior número de cartões totais (amarelos + vermelhos).")
    public ResponseEntity<List<JogadorDtoJavaFx>> getTop5JogadoresComMaisCartoes() {
        return ResponseEntity.ok(jogadorService.getTop5JogadoresComMaisCartoes());
    }



//----------------------------------------------------------POSICAO-----------------------------------------------\\

    @GetMapping("/filtro/por-posicao")
    @ApiOperation(value = "Listar jogadores por posição preferida", notes = "Retorna todos os jogadores com a posição preferida especificada.")
    public ResponseEntity<List<JogadorDtoJavaFx>> procurarPorPosicao(
            @RequestParam Posicao prefPos) {

        List<JogadorDtoJavaFx> jogadores = jogadorService.procurarPorPosicao(prefPos);
        return ResponseEntity.ok(jogadores);
    }


//----------------------------------------------------------PARTIDAS-----------------------------------------------\\

    @GetMapping("/filtro/partidas")
    @ApiOperation(
        value = "Filtrar jogadores por número de partidas jogadas",
        notes = "Retorna os jogadores que jogaram exatamente o número de partidas indicado."
    )
    public ResponseEntity<List<JogadorDtoJavaFx>> getJogadoresPorPartidas(@RequestParam int partidas) {
        return ResponseEntity.ok(jogadorService.getJogadoresPorPartidasJogadas(partidas));
    }

    @GetMapping("/filtro/partidas/menor-que")
    @ApiOperation("Filtra jogadores com menos partidas jogadas que o valor dado")
    public ResponseEntity<List<JogadorDtoJavaFx>> getComMenosPartidas(@RequestParam int partidas) {
        return ResponseEntity.ok(jogadorService.getJogadoresComMenosPartidas(partidas));
    }

    @GetMapping("/filtro/partidas/maior-que")
    @ApiOperation("Filtra jogadores com mais partidas jogadas que o valor dado")
    public ResponseEntity<List<JogadorDtoJavaFx>> getComMaisPartidas(@RequestParam int partidas) {
        return ResponseEntity.ok(jogadorService.getJogadoresComMaisPartidas(partidas));
    }

    @GetMapping("/top5/mais-partidas")
    @ApiOperation("Retorna os 5 jogadores com mais partidas jogadas")
    public ResponseEntity<List<JogadorDtoJavaFx>> getTop5JogadoresMaisPartidas() {
        return ResponseEntity.ok(jogadorService.getTop5JogadoresMaisPartidas());
    }

    @GetMapping("/top5/menos-partidas")
    @ApiOperation("Retorna os 5 jogadores com menos partidas jogadas")
    public ResponseEntity<List<JogadorDtoJavaFx>> getTop5JogadoresMenosPartidas() {
        return ResponseEntity.ok(jogadorService.getTop5JogadoresMenosPartidas());
    }


}
