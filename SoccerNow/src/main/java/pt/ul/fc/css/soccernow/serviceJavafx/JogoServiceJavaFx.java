package pt.ul.fc.css.soccernow.serviceJavafx;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.soccernow.dtoJavafx.JogoCreateDtoJavaFx;
import pt.ul.fc.css.soccernow.dtoJavafx.JogoDtoJavaFx;
import pt.ul.fc.css.soccernow.dtoJavafx.ResultadoDtoJavaFx;
import pt.ul.fc.css.soccernow.model.Arbitro;
import pt.ul.fc.css.soccernow.model.Campeonato;
//import pt.ul.fc.css.soccernow.model.Campeonato;
import pt.ul.fc.css.soccernow.model.Equipa;
import pt.ul.fc.css.soccernow.model.EquipaTitular;
import pt.ul.fc.css.soccernow.model.Jogador;
import pt.ul.fc.css.soccernow.model.Jogo;
///import pt.ul.fc.css.soccernow.model.Resultado;
import pt.ul.fc.css.soccernow.model.Utilizador;
import pt.ul.fc.css.soccernow.model.enums.Campeonato_Estado;
import pt.ul.fc.css.soccernow.model.enums.Turno;
import pt.ul.fc.css.soccernow.repository.ArbitroRepository;
import pt.ul.fc.css.soccernow.repository.CampeonatoRepository;
import pt.ul.fc.css.soccernow.repository.EquipaRepository;
import pt.ul.fc.css.soccernow.repository.EquipaTitularRepository;
import pt.ul.fc.css.soccernow.repository.JogoRepository;
//import pt.ul.fc.css.soccernow.repository.CampeonatoRepository;
///import pt.ul.fc.css.soccernow.repository.ResultadoRepository;

@Service
public class JogoServiceJavaFx {
    
    @Autowired
    private JogoRepository jogoRepository;

    @Autowired
    private EquipaTitularRepository equipaTitularRepository;

    @Autowired
    private ArbitroRepository arbitroRepository;

/*     @Autowired
    private ResultadoRepository resultadoRepository; */

    @Autowired
    private ResultadoServiceJavaFx resultadoService;

    @Autowired
    private EquipaRepository equipaRepository;

    @Autowired
    private CampeonatoRepository campeonatoRepository;


    ////@Autowired 
    //private CampeonatoRepository campeonatoRepository; 


    public JogoDtoJavaFx mapToDto(Jogo jogo) {
        JogoDtoJavaFx dto = new JogoDtoJavaFx();
        dto.setId(jogo.getId());
        dto.setData(jogo.getData());
        dto.setHorario(jogo.getHorario());
        dto.setLocal(jogo.getLocal());
        dto.setArbitroPrincipal(jogo.getArbitroPrincipal() != null ? jogo.getArbitroPrincipal().getId() : null);
        dto.setEquipaArbitragem(
            jogo.getEquipaArbitragem() != null
                ? jogo.getEquipaArbitragem().stream()
                    .map(Utilizador::getId)
                    .collect(Collectors.toCollection(ArrayList::new))
                : new ArrayList<>()
        );

        dto.setEquipaTitularVisitada(jogo.getEquipaTitularVisitada() != null ? jogo.getEquipaTitularVisitada().getId() : null);
        dto.setEquipaTitularVisitante(jogo.getEquipaTitularVisitante() != null ? jogo.getEquipaTitularVisitante().getId() : null);
        dto.setResultado(jogo.getResultado() != null ? jogo.getResultado().getId() : null);
        dto.setCampeonato(jogo.getCampeonato() != null ? jogo.getCampeonato().getId() : 0L);
        return dto;
    }

    public JogoDtoJavaFx createJogo(JogoCreateDtoJavaFx jogoDto) {
        boolean jogoDeCampeonato = false;
        if (jogoDto == null) {
            throw new IllegalArgumentException("DTO de jogo é nulo");
        }
        if(jogoDto.getEquipaTitularVisitada() == jogoDto.getEquipaTitularVisitante()){
            throw new IllegalArgumentException("Uma equipa titular não pode jogar contra ela mesma");
        }
        if(jogoDto.getData().isBefore(LocalDate.now()) || dataAnteriorNoMesmoDia(jogoDto.getData(), jogoDto.getHorario())){
            throw new IllegalArgumentException("Um jogo tem que ser marcado a uma data após a atual");
        }

        //Antes da data atual - |True - ERRO | True - ? == CORRETO
        // Na data atual antes ou igual ao horario |TRUE - ERRO | true - ? || (se false) - true = errado || correto
        // Na data atual depois do horario -| FALSE NAO ERRO | true - ? || (se false) - false = errado || correto
        // Depois da data atual -| FALSE NAO ERRO | false - false = correto

        //Check same para arbitros?
        Optional<Arbitro> arbitroOptional = arbitroRepository.findById(jogoDto.getArbitroPrincipal());
        if (arbitroOptional.isEmpty()) {
            throw new IllegalArgumentException("Árbitro principal não encontrado");
        }
        Arbitro arbitro = arbitroOptional.get();
        
        List<Arbitro> equipaArbitragem = arbitroRepository.findAllById(jogoDto.getEquipaArbitragem());
        if (equipaArbitragem.size() != jogoDto.getEquipaArbitragem().size()) {
            throw new IllegalArgumentException("Nem todos os árbitros foram encontrados");
        }


        //Visitada
        Optional<EquipaTitular> visitadaOptional = equipaTitularRepository.findById(jogoDto.getEquipaTitularVisitada());
        if (visitadaOptional.isEmpty()) {
            throw new IllegalArgumentException("Equipa visitada não encontrada");
        }
        EquipaTitular visitada = visitadaOptional.get();
        //Check if team already has a game at the time given?
        if(!equipaDisponivelParaJogo(visitada,jogoDto.getData(),jogoDto.getHorario())){
            throw new IllegalArgumentException("Equipa visitada já tem jogo na data inserida");
        }

        //Visitante
        Optional<EquipaTitular> visitanteOptional = equipaTitularRepository.findById(jogoDto.getEquipaTitularVisitante());
        if (visitanteOptional.isEmpty()) {
            throw new IllegalArgumentException("Equipa visitante não encontrada");
        }
        EquipaTitular visitante = visitanteOptional.get();
        //Check if team already has a game at the time given?
        if(!equipaDisponivelParaJogo(visitante,jogoDto.getData(),jogoDto.getHorario())){
            throw new IllegalArgumentException("Equipa visitante já tem jogo na data inserida");
        }
        
        if(visitante.getEquipa() == visitada.getEquipa()){
            throw new IllegalArgumentException("Uma equipa não pode jogar contra ela mesma");
        }

        if(visitada.getJogadores().size() != 5 || visitante.getJogadores().size() != 5){
            throw new IllegalArgumentException("O tamanho das equipas deve ser igual a 5");
        }
        
        this.temJogadoresEmComum(visitada, visitante);

        Long idCampeonato = jogoDto.getCampeonatoId();
        Campeonato campeonato = null;
        if(idCampeonato != null && idCampeonato > 0){
            Optional<Campeonato> campeonatoOptional = campeonatoRepository.findById(idCampeonato);
            if(campeonatoOptional.isEmpty()){
                throw new IllegalArgumentException("Este campeonato nao existe");
            };
            campeonato = campeonatoOptional.get();
            if(campeonato.getEstado() != Campeonato_Estado.POR_COMECAR){
                throw new IllegalArgumentException("Este campeonato já esta em curso ou terminado. Nao é possivel adicionar equipas");
            }
            jogoDeCampeonato = true;
            boolean isSomeOneCertificado = false;
            for (Arbitro arbitroCertificado : equipaArbitragem){
                if(arbitroCertificado.isCertificado()){
                    isSomeOneCertificado = true;
                };
            }
            if(!isSomeOneCertificado){
                throw new IllegalArgumentException("Pelo menos um árbitro deve ser certificado");
            }
            List<Equipa> equipasDoCampeonato = campeonato.getEquipas();

            Long idVisitada = visitada.getEquipa().getId();
            Long idVisitante = visitante.getEquipa().getId();

            boolean visitadaNoCampeonato = equipasDoCampeonato.stream()
                .anyMatch(e -> e.getId().equals(idVisitada));

            boolean visitanteNoCampeonato = equipasDoCampeonato.stream()
                .anyMatch(e -> e.getId().equals(idVisitante));
            if(equipasDoCampeonato.isEmpty()){
                System.out.println("Campeonato vazio por isso nao e possivel adicionar equipas");
            }
            System.out.println("Cheguei ate aqui mas as equipas nao estao no campeonato" + idVisitada +"ID DA VISITANTE->" +  idVisitante);
            if (!visitadaNoCampeonato || !visitanteNoCampeonato) {
                throw new IllegalArgumentException("Ambas as equipas devem pertencer ao campeonato");
            }

        }

        

        /* Resultado resultado = null;
        if (jogoDto.getResultado() != null) {
            Optional<Resultado> resultadoOptional = resultadoRepository.findById(jogoDto.getResultado());
            if (resultadoOptional.isEmpty()) {
                throw new RuntimeException("Resultado não encontrado");
            }
            resultado = resultadoOptional.get();
        } */
/*         
        Campeonato campeonato = null;
        if (jogoDto.getCampeonatoId() > 0) { 
            Optional<Campeonato> campeonatoOptional = campeonatoRepository.findById(jogoDto.getCampeonatoId());
            if (campeonatoOptional.isEmpty()) {
                throw new IllegalArgumentException("Campeonato não encontrado");
            }
            campeonato = campeonatoOptional.get();

            //Tem que ter pelo menos 1 arbitro certificado
            List<Long> listaIdArbitros = jogoDto.getEquipaArbitragem();
            listaIdArbitros.add(jogoDto.getArbitroPrincipal());

            List<Arbitro> arbitros = arbitroRepository.findAllById(listaIdArbitros);

            boolean hasCertifiedArbitro = arbitros.stream().anyMatch(Arbitro::isCertificado);
            if (!hasCertifiedArbitro) {
                throw new IllegalArgumentException(
                    "Tem que ter pelo menos um árbitro certificado num jogo de campeonato.");
            }
        }  */

       //Descomentar quando houver campeonatoRepository



        Jogo jogo = new Jogo();
        jogo.setArbitroPrincipal(arbitro);
        jogo.setEquipaArbitragem(equipaArbitragem);
        jogo.setEquipaTitularVisitada(visitada);
        jogo.setEquipaTitularVisitante(visitante);
        //jogo.setResultado(resultado);
        jogo.setData(jogoDto.getData());
        jogo.setHorario(jogoDto.getHorario());
        jogo.setLocal(jogoDto.getLocal());
        jogo.setCampeonato(campeonato);

        Jogo savedjogo = jogoRepository.save(jogo);
        if(jogoDeCampeonato && campeonato != null){
            List<Jogo> jogos = campeonato.getJogos();
            jogos.add(savedjogo);
            campeonato.setJogos(jogos);
            campeonatoRepository.save(campeonato);
        }

        //jogoDto.setId(savedjogo.getId());

        return mapToDto(savedjogo);
    }

    private void temJogadoresEmComum(EquipaTitular visitada, EquipaTitular visitante){
        Set<Jogador> plantelVisitada = new HashSet<>(visitada.getJogadores());
        Set<Jogador> plantelVisitante = new HashSet<>(visitante.getJogadores());

        // Verifica se há interseção, ou seja, algum jogador em comum:
        boolean temJogadorEmComum = plantelVisitada.stream()
                .anyMatch(plantelVisitante::contains);

        if (temJogadorEmComum) {
            throw new IllegalArgumentException("As equipas não podem ter jogadores em comum");
        }
    }

    public JogoDtoJavaFx getJogoById(Long id) {
        Optional<Jogo> jogoOptional = jogoRepository.findById(id);
        if (jogoOptional.isEmpty()) {
            return null;
        }
        return mapToDto(jogoOptional.get());
    }

    public List<JogoDtoJavaFx> getJogoByEquipa(Long equipaId) {
        return jogoRepository.findByEquipaTitularVisitadaIdOrEquipaTitularVisitanteId(equipaId, equipaId)
                    .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    /*  public List<JogoDto> getJogoByUser(Long user_id){
        return jogoRepository.findByUtilizadorId(user_id)
                    .stream().map(this::mapToDto).collect(Collectors.toList());
    }  */

    /*
    public List<JogoDto> getJogoByUser(Long user_id){
        return jogoRepository.findByUtilizadorId(user_id)
                    .stream().map(this::mapToDto).collect(Collectors.toList());
    } 
    */
    public List<JogoDtoJavaFx> getJogosByCampeonato(Long campeonatoId) {
        Optional<Campeonato> campeonatoOptional = campeonatoRepository.findById(campeonatoId);
        if (campeonatoOptional.isEmpty()) {
            throw new RuntimeException("Campeonato não encontrado");
        }
        Campeonato campeonato = campeonatoOptional.get();
        if (campeonato.getEstado() != Campeonato_Estado.POR_COMECAR) {
            throw new RuntimeException("Campeonato não está no estado correto para buscar jogos");
        }
        return campeonato.getJogos()
                    .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public JogoDtoJavaFx saveJogo(Jogo jogo){
        return mapToDto(jogoRepository.save(jogo));
    } 

    public List<JogoDtoJavaFx> getAllJogos(){
        return jogoRepository.findAll()
                    .stream().map(this::mapToDto).toList(); 
    } 

    public ResultadoDtoJavaFx getResultadoDoJogo(Long jogo_id){
        Optional<Jogo> jogoOptional = jogoRepository.findById(jogo_id);
        if(jogoOptional.isEmpty()){
            throw new RuntimeException("Jogo não encontrado");
        }
        ResultadoDtoJavaFx resultadoDto = resultadoService.mapToDto(jogoOptional.get().getResultado());
        return resultadoDto;
    }

    public List<JogoDtoJavaFx> getJogosDeEquipaId(Long equipa_id){
        // Step 1: Get all EquipaTitulars for the given Equipa
        List<EquipaTitular> equipasTitulares = equipaTitularRepository.findByEquipaId(equipa_id);
            return equipasTitulares.stream()
        .flatMap(et -> getJogoByEquipa(et.getId()).stream())
        .collect(Collectors.toList());
    }

    public List<JogoDtoJavaFx> getJogosDeEquipaNome(String equipa_nome){
        Equipa equipaOptional = equipaRepository.findByNome(equipa_nome);
        if(equipaOptional == null){
            throw new RuntimeException("Equipa Não encontrada");
        }
        return getJogoByEquipa(equipaOptional.getId());
    }

    public boolean equipaDisponivelParaJogo(EquipaTitular equipaTitular, LocalDate data, Turno turno) {
        List<EquipaTitular> lista = equipaTitularRepository.findByEquipaId(equipaTitular.getEquipa().getId());
        for (EquipaTitular equipaT : lista){
            boolean visitadaLivre = jogoRepository.findByEquipaTitularVisitada_IdAndDataAndHorario(equipaT.getId(), data, turno).isEmpty();
            boolean visitanteLivre = jogoRepository.findByEquipaTitularVisitante_IdAndDataAndHorario(equipaT.getId(), data, turno).isEmpty();
            
            if (!visitadaLivre || !visitanteLivre) {
                return false;
            }
        }
        return true;
    }

    //Retorna true caso seja uma data no mesmo dia da atual mas anterior ao turno atual.
    //False caso não seja o mesmo ano que a data atual ou seja depois do turno atual.
    public boolean dataAnteriorNoMesmoDia(LocalDate dataToCheck, Turno turnoToCheck){
        int anoToCheck = dataToCheck.getYear();
        int diaDoAnoToCheck = dataToCheck.getDayOfYear();
        if(anoToCheck != LocalDate.now().getYear() || diaDoAnoToCheck != LocalDate.now().getDayOfYear()){
            return false;
        }
        //check o turno
        if(!turnoToCheck.getHoraInicio().isAfter(LocalTime.now())){
            return false;
        }
        return true;
    }
//Usando Data e Hora
/*     public List<JogoDto> getJogosRealizados(){
        List<JogoDto> listOfJogos = jogoRepository.findAll()
                    .stream().filter(jogo -> jogo.getData().isBefore(LocalDate.now()) || 
                        dataAnteriorNoMesmoDia(jogo.getData(), jogo.getHorario()))
                    .map(this::mapToDto).toList(); 
        return listOfJogos;
    } */ 
    public List<JogoDtoJavaFx> getJogosRealizados(){
        List<JogoDtoJavaFx> listOfJogos = jogoRepository.findAll()
                    .stream().filter(jogo -> jogo.getResultado() != null)
                    .map(this::mapToDto).toList(); 
        return listOfJogos;
    } 
//Usando Data e Hora
/*     public List<JogoDto> getJogosNaoRealizados(){
        List<JogoDto> listOfJogos = jogoRepository.findAll()
                    .stream().filter(jogo -> !jogo.getData().isBefore(LocalDate.now()) && 
                        !dataAnteriorNoMesmoDia(jogo.getData(), jogo.getHorario()))
                        //Condiçao Jogos cuja data é apos a atual, 
                    .map(this::mapToDto).toList(); 
        return listOfJogos;
    }   */ 
    public List<JogoDtoJavaFx> getJogosNaoRealizados(){
        List<JogoDtoJavaFx> listOfJogos = jogoRepository.findAll()
                    .stream().filter(jogo -> jogo.getResultado() == null)
                    .map(this::mapToDto).toList(); 
        return listOfJogos;
    } 

    public List<JogoDtoJavaFx> getTopJogosPorGolos(){
        List<JogoDtoJavaFx> listOfJogos = jogoRepository.findAll().stream().filter(j -> j.getResultado() != null)
        .sorted((j1, j2) -> {
            int golosJ1 = j1.getResultado().getGolosVisitado().size() + j1.getResultado().getGolosVisitante().size();
            int golosJ2 = j2.getResultado().getGolosVisitado().size() + j2.getResultado().getGolosVisitante().size();
            return Integer.compare(golosJ2, golosJ1);
        }).limit(5).map(this::mapToDto).toList();
        return listOfJogos;
    }

    public List<JogoDtoJavaFx> getJogosByGolos(Integer num_golos){
        List<JogoDtoJavaFx> listOfJogos = jogoRepository.findAll().stream().filter(j -> j.getResultado() != null)
            .filter(jogo -> {
            int golos = jogo.getResultado().getGolosVisitado().size() + jogo.getResultado().getGolosVisitante().size();
            return Integer.compare(golos, num_golos) == 0;
        }).map(this::mapToDto).toList();
        return listOfJogos;
    }

    public List<JogoDtoJavaFx> getJogosByGolosOfResultado(Integer num_golos_visitado, Integer num_golos_visitante){
        List<JogoDtoJavaFx> listOfJogos = jogoRepository.findAll().stream().filter(j -> j.getResultado() != null).filter(jogo -> {
            int golosVisitado = jogo.getResultado().getGolosVisitado().size();
            int golosVisitante = jogo.getResultado().getGolosVisitante().size();
            return Integer.compare(golosVisitado, num_golos_visitado) == 0 && 
                    Integer.compare(golosVisitante, num_golos_visitante) == 0;
        }).map(this::mapToDto).toList();
        return listOfJogos;
    }

    public List<JogoDtoJavaFx> getJogosByHorario(Turno turno){
        List<JogoDtoJavaFx> listOfJogos = jogoRepository.findByHorario(turno).stream()
        .map(this::mapToDto).toList();
        return listOfJogos;
    }

    public List<JogoDtoJavaFx> getJogosByLocalizacao(String local){
        List<JogoDtoJavaFx> listOfJogos = jogoRepository.findByLocal(local).stream()
        .map(this::mapToDto).toList();
        return listOfJogos;
    }
    
    public List<JogoDtoJavaFx> getJogosByData(LocalDate data_aProcurar){
        List<JogoDtoJavaFx> listOfJogos  = jogoRepository.findByData(data_aProcurar).stream()
        .map(this::mapToDto).toList();
        return listOfJogos;
    }

    
    public void deleteJogo(Long id) {
        Optional<Jogo> jogoOptional = jogoRepository.findById(id);
        if (jogoOptional.isEmpty()) {
            throw new RuntimeException("Jogo não encontrado");
        }

        Jogo jogo = jogoOptional.get();

        if (jogo.getResultado() != null) {
            resultadoService.deleteResultado(jogo.getResultado().getId());
        }

        Campeonato campeonato = jogo.getCampeonato();
        if (campeonato != null) {
            campeonato.getJogos().remove(jogo);
            campeonatoRepository.save(campeonato);
        }

        jogoRepository.delete(jogo);
    }

}
