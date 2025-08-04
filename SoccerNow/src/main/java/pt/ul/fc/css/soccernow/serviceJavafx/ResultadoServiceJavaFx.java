package pt.ul.fc.css.soccernow.serviceJavafx;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.soccernow.dtoJavafx.ResultadoCreateDtoJavaFx;
import pt.ul.fc.css.soccernow.dtoJavafx.ResultadoDtoJavaFx;
import pt.ul.fc.css.soccernow.model.Campeonato;
import pt.ul.fc.css.soccernow.model.EquipaTitular;
import pt.ul.fc.css.soccernow.model.Jogador;
import pt.ul.fc.css.soccernow.model.Jogo;
import pt.ul.fc.css.soccernow.model.Pontuacao;
import pt.ul.fc.css.soccernow.model.Resultado;
import pt.ul.fc.css.soccernow.repository.CampeonatoRepository;
import pt.ul.fc.css.soccernow.repository.EquipaTitularRepository;
import pt.ul.fc.css.soccernow.repository.JogadorRepository;
import pt.ul.fc.css.soccernow.repository.JogoRepository;
import pt.ul.fc.css.soccernow.repository.ResultadoRepository;

@Service
public class ResultadoServiceJavaFx {
    @Autowired
    private ResultadoRepository resultadoRepository;

    @Autowired
    private JogoRepository jogoRepository;

    @Autowired
    private EquipaTitularRepository equipaTitularRepository;

    @Autowired
    private JogadorRepository jogadorRepository;

    @Autowired
    private JogadorServiceJavaFx jogadorService;

    @Autowired
    private CampeonatoRepository campeonatoRepository;

    public ResultadoDtoJavaFx mapToDto(Resultado resultado) {
        ResultadoDtoJavaFx resultadoDto = new ResultadoDtoJavaFx();
    
        resultadoDto.setId(resultado.getId());
    
        if (resultado.getVencedor() != null) {
            resultadoDto.setVencedor(resultado.getVencedor().getId());
        }
    
        resultadoDto.setGolosVisitado(resultado.getGolosVisitado()
                    .stream().map(Jogador::getId).collect(Collectors.toList()));
    
        resultadoDto.setGolosVisitante(resultado.getGolosVisitante()
                    .stream().map(Jogador::getId).collect(Collectors.toList()));
    
        resultadoDto.setAmarelosVisitado(resultado.getAmarelosVisitado()
                    .stream().map(Jogador::getId).collect(Collectors.toList()));
    
        resultadoDto.setAmarelosVisitante(resultado.getAmarelosVisitante()
                    .stream().map(Jogador::getId).collect(Collectors.toList()));
    
        resultadoDto.setVermelhosVisitado(resultado.getVermelhosVisitado()
                        .stream().map(Jogador::getId).collect(Collectors.toList()));
    
        resultadoDto.setVermelhosVisitante(resultado.getVermelhosVisitante()
                        .stream().map(Jogador::getId).collect(Collectors.toList()));

        return resultadoDto;
    }

    public ResultadoDtoJavaFx createResultado(ResultadoCreateDtoJavaFx resultadoDto, Long jogo_id){
        if(resultadoDto == null){
            throw new IllegalArgumentException("Resultado nao pode ser nulo");
        }
            EquipaTitular vencedor = null;
        if(resultadoDto.getVencedor() != 0){
            vencedor = verificarVencedorNoJogo(resultadoDto, jogo_id);
        }

        

        List<Jogador> golosVisitante = resultadoDto.getGolosVisitante().stream()
            .map(id -> jogadorRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Jogador com ID " + id + " não encontrado")))
            .collect(Collectors.toList());
        List<Jogador> golosVisitado = resultadoDto.getGolosVisitado().stream()
        .map(id -> jogadorRepository.findById(id).orElseThrow(() ->
            new RuntimeException("Jogador com ID " + id + " não encontrado")))
        .collect(Collectors.toList());
        List<Jogador> amarelosVisitante = resultadoDto.getGolosVisitante().stream()
        .map(id -> jogadorRepository.findById(id).orElseThrow(() ->
            new RuntimeException("Jogador com ID " + id + " não encontrado")))
        .collect(Collectors.toList());
        List<Jogador> amarelosVisitado = resultadoDto.getAmarelosVisitado().stream()
        .map(id -> jogadorRepository.findById(id).orElseThrow(() ->
            new RuntimeException("Jogador com ID " + id + " não encontrado")))
        .collect(Collectors.toList());
        List<Jogador> vermelhosVisitante = resultadoDto.getVermelhosVisitante().stream()
        .map(id -> jogadorRepository.findById(id).orElseThrow(() ->
            new RuntimeException("Jogador com ID " + id + " não encontrado")))
        .collect(Collectors.toList());
        List<Jogador> vermelhosVisitado = resultadoDto.getVermelhosVisitado().stream()
        .map(id -> jogadorRepository.findById(id).orElseThrow(() ->
            new RuntimeException("Jogador com ID " + id + " não encontrado")))
        .collect(Collectors.toList());


        verificarJogadoresNoResultado(resultadoDto, jogo_id);
        updateEstatisticasJogadores(resultadoDto, jogo_id);

        Resultado resultado = new Resultado();
        resultado.setVencedor(vencedor);
        resultado.setGolosVisitante(golosVisitante);
        resultado.setGolosVisitado(golosVisitado);
        resultado.setAmarelosVisitante(amarelosVisitante);
        resultado.setAmarelosVisitado(amarelosVisitado);
        resultado.setVermelhosVisitante(vermelhosVisitante);
        resultado.setVermelhosVisitado(vermelhosVisitado);

        Resultado savedResultado = resultadoRepository.save(resultado);
        updateJogo(resultado,jogo_id);

        return mapToDto(savedResultado);
    }

    private EquipaTitular verificarVencedorNoJogo(ResultadoCreateDtoJavaFx resultadoDto, Long jogo_id) {

        Optional<EquipaTitular> vencedorOptional = equipaTitularRepository.findById(resultadoDto.getVencedor());
        if (vencedorOptional.isEmpty()) {
            throw new RuntimeException("Equipa vencedora nao encontrada");
        }
        Optional<Jogo> jogoOptional = jogoRepository.findById(jogo_id);
        if (jogoOptional.isEmpty()) {
            throw new RuntimeException("jogo nao encontrado");
        }
        Jogo jogo = jogoOptional.get();
        if(jogo.getEquipaTitularVisitada().getId() != resultadoDto.getVencedor() && jogo.getEquipaTitularVisitante().getId() != resultadoDto.getVencedor()){
            throw new RuntimeException("Equipa vencedora nao pertence ao jogo");
        }
        return vencedorOptional.get();
    }

    public ResultadoDtoJavaFx getResultadoById(Long id){
        Optional<Resultado> resultadoOptional = resultadoRepository.findById(id);
        if (resultadoOptional.isEmpty()) {
            throw new RuntimeException("Resultado nao encontrado");
        }
        return mapToDto(resultadoOptional.get());
    }

    public ResultadoDtoJavaFx getResultadoByJogo(Long jogoId) {
        Optional<Jogo> jogoOptional = jogoRepository.findById(jogoId);
        if (jogoOptional.isEmpty()) {
            throw new RuntimeException("Jogo nao encontrado");
        }
        Resultado resultado = jogoOptional.get().getResultado();
        if (resultado == null) {
            throw new RuntimeException("Resultado deste jogo eh null");
        }
        return mapToDto(resultado);
    }

    public List<ResultadoDtoJavaFx> getAllResultados(){
        return resultadoRepository.findAll()
                    .stream().map(this::mapToDto).toList(); 
    }

    public ResultadoDtoJavaFx saveResultado(Resultado resultado){
        return mapToDto(resultadoRepository.save(resultado));
    }

    private void updateJogo(Resultado resultado, Long jogo_id){
        Optional<Jogo> jogoOptional = jogoRepository.findById(jogo_id);
        if(jogoOptional.isEmpty()){
            throw new RuntimeException("Jogo nao encontrado.");
        }


        Jogo jogo = jogoOptional.get();
        if(jogo.getCampeonato() != null){
            this.updateClassificacao(jogo,resultado);
        }
        jogo.setResultado(resultado);
        jogoRepository.save(jogo);
        return;
    }

    private void updateClassificacao(Jogo jogo, Resultado resultado) {
        Optional<Campeonato> campeonatoOpt = campeonatoRepository.findById(jogo.getCampeonato().getId());
        if (campeonatoOpt.isEmpty()) {
            throw new RuntimeException("Campeonato não encontrado.");
        }

        Campeonato campeonato = campeonatoOpt.get();
        List<Pontuacao> classificacao = campeonato.getClassificacao();
        Long vencedorId = resultado.getVencedor().getId(); // ID da equipa vencedora

        if(resultado.getVencedor().getId() == 0){
                EquipaTitular visitada = jogo.getEquipaTitularVisitada();
                EquipaTitular visitante = jogo.getEquipaTitularVisitante();
                Pontuacao pVisitada = classificacao.stream()
                    .filter(p -> p.getEquipa().getId().equals(visitada.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Equipa visitada não encontrada na classificação"));
                Pontuacao pVisitante = classificacao.stream()
                    .filter(p -> p.getEquipa().getId().equals(visitante.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Equipa visitante não encontrada na classificação"));
                pVisitada.addPontos(1L);
                pVisitante.addPontos(1L);
        }
        for (Pontuacao p : classificacao) {
            if (p.getEquipa().getId().equals(vencedorId)) {
                p.addPontos(3L);
                break;
            }
        }

        campeonatoRepository.save(campeonato);
    }


    private void verificarJogadoresNoResultado(ResultadoCreateDtoJavaFx resultadoDto, Long id){
        Optional<Jogo> jogoOptional = jogoRepository.findById(id);
        if(jogoOptional.isEmpty()){
            throw new RuntimeException("Jogo não encontrado");
        }
        Jogo jogo = jogoOptional.get();

        if(!(resultadoDto.getVencedor() == jogo.getEquipaTitularVisitada().getId()) && 
            !(resultadoDto.getVencedor() == jogo.getEquipaTitularVisitante().getId())){
                throw new RuntimeException("Vencedor não pertence ao jogo");
            }

        List<Long> visitados_ids = new ArrayList<>();
        List<Long> visitantes_ids = new ArrayList<>();
        for (Jogador jogador : jogo.getEquipaTitularVisitada().getJogadores()){
            visitados_ids.add(jogador.getId());
        }
        for (Jogador jogador : jogo.getEquipaTitularVisitante().getJogadores()){
            visitantes_ids.add(jogador.getId());
        }


        if(!visitados_ids.containsAll(resultadoDto.getGolosVisitado()) ||
            !visitados_ids.containsAll(resultadoDto.getAmarelosVisitado()) ||
            !visitados_ids.containsAll(resultadoDto.getVermelhosVisitado()) ||

            !visitantes_ids.containsAll(resultadoDto.getGolosVisitante()) ||
            !visitantes_ids.containsAll(resultadoDto.getAmarelosVisitante()) ||
            !visitantes_ids.containsAll(resultadoDto.getAmarelosVisitante())){
                throw new RuntimeException("Ha jogadores incorretos na lista de golos ou cartoes");
            };
    }

    private void updateEstatisticasJogadores(ResultadoCreateDtoJavaFx resultadoDto, Long jogo_id){
        Jogo jogo = jogoRepository.findById(jogo_id).get();
        for(Jogador jogador : jogo.getEquipaTitularVisitada().getJogadores()){
            jogadorService.incrementarPartidasJogadas(jogador.getId());
        }
        for(Jogador jogador : jogo.getEquipaTitularVisitante().getJogadores()){
            jogadorService.incrementarPartidasJogadas(jogador.getId());
        }
        for(Long id : resultadoDto.getGolosVisitado()){
            jogadorService.incrementarGolos(id);
        }
        for(Long id : resultadoDto.getGolosVisitante()){
            jogadorService.incrementarGolos(id);
        }
        for(Long id : resultadoDto.getAmarelosVisitado()){
            jogadorService.incrementarAmarelos(id);
        }
        for(Long id : resultadoDto.getAmarelosVisitante()){
            jogadorService.incrementarAmarelos(id);
        }
        for(Long id : resultadoDto.getVermelhosVisitado()){
            jogadorService.incrementarVermelhos(id);
        }
        for(Long id : resultadoDto.getVermelhosVisitante()){
            jogadorService.incrementarVermelhos(id);
        }
    }

    public void deleteResultado(Long id) {
        Optional<Resultado> resultadoOptional = resultadoRepository.findById(id);
        if (resultadoOptional.isEmpty()) {
            throw new RuntimeException("Resultado não encontrado");
        }
        Resultado resultado = resultadoOptional.get();
        resultadoRepository.delete(resultado);
    }



}
