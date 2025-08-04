package pt.ul.fc.css.soccernow.serviceJavafx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.soccernow.dtoJavafx.EquipaDtoJavaFx;
import pt.ul.fc.css.soccernow.model.Campeonato;
import pt.ul.fc.css.soccernow.model.Equipa;
import pt.ul.fc.css.soccernow.model.EquipaTitular;
import pt.ul.fc.css.soccernow.model.Jogador;
import pt.ul.fc.css.soccernow.model.Jogo;
import pt.ul.fc.css.soccernow.model.Resultado;
import pt.ul.fc.css.soccernow.model.enums.Posicao;
import pt.ul.fc.css.soccernow.repository.CampeonatoRepository;
import pt.ul.fc.css.soccernow.repository.EquipaRepository;
import pt.ul.fc.css.soccernow.repository.EquipaTitularRepository;
import pt.ul.fc.css.soccernow.repository.JogadorRepository;
import pt.ul.fc.css.soccernow.repository.JogoRepository;
import pt.ul.fc.css.soccernow.repository.ResultadoRepository;

/**
 * The type Equipa service.
 */
@Service
public class EquipaServiceJavaFx {

    @Autowired
    private EquipaRepository equipaRepository;

    @Autowired
    private EquipaTitularRepository equipaTitularRepository;

    @Autowired
    private JogoRepository jogoRepository;

    @Autowired
    private ResultadoRepository resultadoRepository;

    @Autowired
    private JogadorRepository jogadorRepository;

    @Autowired
    private CampeonatoRepository campeonatoRepository;

    public EquipaDtoJavaFx mapToDto(Equipa equipa) {
        EquipaDtoJavaFx equipaDto = new EquipaDtoJavaFx();
        equipaDto.setId(equipa.getId());
        equipaDto.setNome(equipa.getNome());
        //ir buscar conquistas de equipa e retirar ids dos campeonatos
        //colocar aqui, ja resolvo
        // Obter IDs dos campeonatos conquistados
        List<Long> conquistasIds = equipa.getConquistas().stream()
                .map(Campeonato::getId)
                .collect(Collectors.toList());
        equipaDto.setConquistas(conquistasIds);

        List<Long> plantelIds = equipa.getPlantel().stream()
                .map(Jogador::getId)
                .collect(Collectors.toList());
        equipaDto.setPlantel(plantelIds);
        return equipaDto;
    }

    /**
     * Create equipa dto.
     *
     * @param equipaDto the equipa dto
     * @return the equipa dto
     */
    public EquipaDtoJavaFx createEquipa(EquipaDtoJavaFx equipaDto) {
        if (equipaDto == null) {
            throw new IllegalArgumentException("EquipaDto nao pode ser nulo");
        }

        if (equipaDto.getNome() == null || equipaDto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da equipa eh obrigatorio");
        }
        if (equipaRepository.findByNome(equipaDto.getNome()) != null) {
            throw new RuntimeException("Equipa com o mesmo nome ja existe");
        }
        //if equipa nome ja existe nao criar equipa nova
        Equipa jaExiste = equipaRepository.findByNome(equipaDto.getNome());
        if (jaExiste != null) {
            // Já existe uma equipa com esse nome
            // Tratar conforme necessário, por exemplo:
            throw new IllegalArgumentException("Já existe uma equipa com esse nome.");
        }
        Equipa equipa = new Equipa();
        equipa.setNome(equipaDto.getNome());

        if (equipaDto.getPlantel() != null) {
            List<Jogador> plantel = new ArrayList<>();
            for (Long jogadorId : equipaDto.getPlantel()) {
                Optional<Jogador> jogadorOptional = jogadorRepository.findById(jogadorId);
                if (!jogadorOptional.isEmpty()) {

                    Jogador jogador = jogadorOptional.get();

                    if(jogador.getEquipas() != null){
                        List<Equipa> equipas = jogador.getEquipas();
                        boolean pertence = false;
                        for(Equipa equipaJog : equipas){
                            if(equipaJog.getId() == equipa.getId()){
                                pertence = true;
                                break;
                            }
                        }
                        if(!pertence){
                            equipas.add(equipa);
                            jogador.setEquipas(equipas);
                            //adiciona depois do check se nao pertence ja ah equipa
                            plantel.add(jogador);
                        }
                    } else {
                        throw new RuntimeException("Jogador com id " + jogadorId + " nao encontrado");
                    }
                }
            }
        }
        /* 
        if(equipaDto.getConquistas() != null) {
            List<Jogo> conquistas = new ArrayList<>();
            for (Long jogoId : equipaDto.getConquistas()) {
                Optional<Jogo> jogoOptional = jogadorRepository.findById(jogoId);
                if (!jogoOptional.isEmpty()) {
                    Jogo jogo = jogoOptional.get();
                    conquistas.add(jogo);
                }
            }
            equipa.setConquistas(conquistas);
        }
        */
        Equipa savedEquipa = equipaRepository.save(equipa);
        equipaDto.setId(savedEquipa.getId());
        return equipaDto;
    }

    /**
     * Save equipa equipa dto.
     *
     * @param equipa the equipa
     * @return the equipa dto
     */
    public EquipaDtoJavaFx saveEquipa(Equipa equipa) {
        if(equipa == null){
            throw new IllegalArgumentException("Equipa invalida");
        }
        return mapToDto(equipaRepository.save(equipa));
    }


    /**
     * Procurar por id equipa dto.
     *
     * @param id the id
     * @return the equipa dto
     */
    public EquipaDtoJavaFx procurarPorId(Long id) {
        validarId(id);
        Optional<Equipa> equipaOptional = equipaRepository.findById(id);
        if (equipaOptional.isEmpty()){
            throw new RuntimeException("Equipa não encontrado");
        }
        return mapToDto(equipaOptional.get());
    }


    /**
     * Gets all equipas.
     *
     * @return the all equipas
     */
    public List<EquipaDtoJavaFx> getAllEquipas() {
        List<Equipa> equipas = equipaRepository.findAll();  // Agora carrega o plantel
        System.out.println("Equipas: " + equipas);
    
        return equipas.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }



    /**
     * Procurar por nome list.
     *
     * @param nome the nome
     * @return the list
     */
    public List<EquipaDtoJavaFx> procurarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()){
            throw new IllegalArgumentException("Nome invalido ou vazio");
        }
        List<Equipa> equipas = equipaRepository.findByNomeContainingIgnoreCase(nome);

        if (equipas.isEmpty()) {
            throw new RuntimeException("Nenhum euipa encontrado com o nome fornecido.");
        }

        return equipas.stream()
                .map(this::mapToDto)
                .toList();
    }


    /**
     * Eliminar por id boolean.
     *
     * @param id the id
     * @return the boolean
     */
    public boolean eliminarPorId(Long id) {
        validarId(id);

        Equipa equipa = equipaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Equipa não encontrada"));

        for (Jogador jogador : equipa.getPlantel()) {
            jogador.getEquipas().remove(equipa);
            jogadorRepository.save(jogador);
        }

        List<EquipaTitular> titulares = equipaTitularRepository.findByEquipaId(id);
        for (EquipaTitular titular : titulares) {
            List<Jogo> jogos = jogoRepository
                .findByEquipaTitularVisitadaIdOrEquipaTitularVisitanteId(titular.getId(), titular.getId());

            for (Jogo jogo : jogos) {
                if (jogo.getResultado() == null) {
                    throw new RuntimeException("Equipa ainda tem jogos marcados");
                }
            }

            for (Jogo jogo : jogos) {
                if (titular.getId().equals(jogo.getEquipaTitularVisitada().getId())) {
                    jogo.setEquipaTitularVisitada(null);
                } else if (titular.getId().equals(jogo.getEquipaTitularVisitante().getId())) {
                    jogo.setEquipaTitularVisitante(null);
                }
                jogoRepository.save(jogo);
            }

            equipaTitularRepository.delete(titular);
        }

        equipa.getPlantel().clear();
        equipaRepository.save(equipa);
        equipaRepository.deleteById(id);
        return true;
    }



    /**
     * Atualizar nome equipa dto.
     *
     * @param id       the id
     * @param novoNome the novo nome
     * @return the equipa dto
     */
    public EquipaDtoJavaFx atualizarNome(Long id, String novoNome) {
        validarId(id);

        if (novoNome == null || novoNome.trim().isEmpty()) {
            throw new IllegalArgumentException("O novo nome nao pode ser nulo ou vazio.");
        }

        Equipa equipa = equipaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipa não encontrada"));

        equipa.setNome(novoNome);
        Equipa updatedEquipa = equipaRepository.save(equipa);

        return mapToDto(updatedEquipa);
    }

    public EquipaDtoJavaFx adicionaJogador(Long jogadorId, Long equipaId) {
        if (equipaId == null || equipaId <= 0) {
            throw new IllegalArgumentException("O id da equipa não é válido.");
        }
        Optional<Equipa> equipaOptional = equipaRepository.findById(equipaId);
        if (equipaOptional.isEmpty()) {
            throw new RuntimeException("Equipa não encontrada");
        }
        Equipa equipa = equipaOptional.get();
        if (jogadorId == null || jogadorId <= 0) {
            throw new IllegalArgumentException("O id do jogador não é válido.");
        }
        Optional<Jogador> jogadorOptional = jogadorRepository.findById(jogadorId);
        if (jogadorOptional.isEmpty()) {
            throw new RuntimeException("Jogador não encontrado");
        }
        Jogador jogador = jogadorOptional.get();
        if (equipa.getPlantel().contains(jogador)) {
            throw new RuntimeException("Jogador já pertence à equipa");
        }
        equipa.getPlantel().add(jogador);
        List<Equipa> equipas = jogador.getEquipas();
        equipas.add(equipa);
        jogador.setEquipas(equipas);
        
        jogadorRepository.save(jogador);
        equipaRepository.save(equipa);


        //equipa.setPlantel(equipa.getPlantel());

        return mapToDto(equipa);
    }


    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O id não é válido.");
        }
    }

    public EquipaDtoJavaFx removeJogador(Long jogadorId, Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O id da equipa não é válido.");
        }
        Optional<Equipa> equipaOptional = equipaRepository.findById(id);
        if (equipaOptional.isEmpty()) {
            throw new RuntimeException("Equipa não encontrada");
        }
        Equipa equipa = equipaOptional.get();
        if (jogadorId == null || jogadorId <= 0) {
            throw new IllegalArgumentException("O id do jogador não é válido.");
        }
        Optional<Jogador> jogadorOptional = jogadorRepository.findById(jogadorId);
        if (jogadorOptional.isEmpty()) {
            throw new RuntimeException("Jogador não encontrado");
        }
        Jogador jogador = jogadorOptional.get();
        if (!equipa.getPlantel().contains(jogador)) {
            throw new RuntimeException("Jogador não pertence à equipa");
        }
        equipa.getPlantel().remove(jogador);
        List<Equipa> equipas = jogador.getEquipas();
        equipas.remove(equipa);
        jogador.setEquipas(equipas);
        
        jogadorRepository.save(jogador);
        equipaRepository.save(equipa);
        EquipaDtoJavaFx equipaDto = mapToDto(equipa);
        return equipaDto;
    }

    public List<EquipaDtoJavaFx> procurarPorNomeJogador(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Id invalido ou vazio");
        }
        Optional<Jogador> jogadorOptional = jogadorRepository.findById(id);
        if (jogadorOptional.isEmpty()) {
            throw new RuntimeException("Jogador não encontrado");
        }
        Jogador jogador = jogadorOptional.get();
        List<Equipa> equipas = jogador.getEquipas();

        return equipas.stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<EquipaDtoJavaFx> getEquipasComXJogadoresOuMais(Long x) {
        List<Equipa> equipas = equipaRepository.findAll();
        List<Equipa> equipasComXJogadoresOuMais = new ArrayList<>();
        for (Equipa equipa : equipas) {
            if (equipa.getPlantel().size() >= x) {
                equipasComXJogadoresOuMais.add(equipa);
            }
        }
        return equipasComXJogadoresOuMais.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }

    public List<EquipaDtoJavaFx> getEquipasComXJogadoresOuMenos(Long x) {
        List<Equipa> equipas = equipaRepository.findAll();
        List<Equipa> equipasComXJogadoresOuMenos = new ArrayList<>();
        for (Equipa equipa : equipas) {
            if (equipa.getPlantel().size() <= x) {
                equipasComXJogadoresOuMenos.add(equipa);
            }
        }
        return equipasComXJogadoresOuMenos.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<EquipaDtoJavaFx> getEquipasComXJogadores(Long x) {
        List<Equipa> equipas = equipaRepository.findAll();
        List<Equipa> equipasComXJogadores = new ArrayList<>();
        for (Equipa equipa : equipas) {
            if (equipa.getPlantel().size() == x) {
                equipasComXJogadores.add(equipa);
            }
        }
        return equipasComXJogadores.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<EquipaDtoJavaFx> getTop5EquipasMaisVitorias() {
        List<Resultado> resultados = resultadoRepository.findAll();
    
        // Usamos um mapa para contar as vitórias de cada equipa
        Map<Equipa, Integer> vitoriasPorEquipa = new HashMap<>();
    
        // Iteramos sobre os resultados e contamos as vitórias
        for (Resultado resultado : resultados) {
            EquipaTitular equipaVencedora = resultado.getVencedor();  // Assume que vencedor() retorna um objeto EquipaTitular
            if (equipaVencedora != null) {
                Equipa equipa = equipaVencedora.getEquipa();  // Assume que equipa() retorna o objeto Equipa vencedor
                vitoriasPorEquipa.put(equipa, vitoriasPorEquipa.getOrDefault(equipa, 0) + 1);
            }
        }
    
        // Ordena as equipas por número de vitórias (decrescente)
        List<Map.Entry<Equipa, Integer>> listaOrdenada = new ArrayList<>(vitoriasPorEquipa.entrySet());
        listaOrdenada.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())); // Ordenação decrescente
    
        // Pegamos as 5 primeiras equipas (top 5)
        List<Equipa> top5Equipas = listaOrdenada.stream()
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    
        // Retorna as 5 equipas com mais vitórias, convertendo para DTO
        return top5Equipas.stream()
                .map(this::mapToDto)  // Supondo que mapToDto converta a Equipa para EquipaDto
                .collect(Collectors.toList());
    }


    public List<EquipaDtoJavaFx> getEquipasXVitorias(Long x) {
        Map<Equipa, Integer> vitoriasPorEquipa = contarVitoriasPorEquipa();

        return vitoriasPorEquipa.entrySet().stream()
                .filter(entry -> entry.getValue().equals(x.intValue()))
                .map(entry -> mapToDto(entry.getKey()))
                .collect(Collectors.toList());
    }

    private Map<Equipa, Integer> contarVitoriasPorEquipa() {
        List<Resultado> resultados = resultadoRepository.findAll();
        Map<Equipa, Integer> vitoriasPorEquipa = new HashMap<>();

        for (Resultado resultado : resultados) {
            EquipaTitular equipaVencedora = resultado.getVencedor();
            if (equipaVencedora != null) {
                Equipa equipa = equipaVencedora.getEquipa();
                vitoriasPorEquipa.merge(equipa, 1, Integer::sum);
            }
        }
        return vitoriasPorEquipa;
    }


    public List<EquipaDtoJavaFx> getEquipasXDerrotas(Long x) {
        //sacar lista de jogos 
        //olhar para resultado
        //sacar equipa vencedora
        //se for null passar ah frente
        //se tiver equipa
        //comparar com equipaVisitada
        //se for igual ent retirar a equipaVisitante(perdedora)
        //senao ent sacar equipaVisitada(perdedora)
        //colocar na lista de equipas com numero de derrotas a aumentar
        Map<Equipa, Integer> derrotasPorEquipa = contarDerrotasPorEquipa();

        return derrotasPorEquipa.entrySet().stream()
                .filter(entry -> entry.getValue().equals(x.intValue()))
                .map(entry -> mapToDto(entry.getKey()))
                .collect(Collectors.toList());
    }

    private Map<Equipa, Integer> contarDerrotasPorEquipa() {
        List<Jogo> jogos = jogoRepository.findAll(); // Supondo que há um repositório para Jogo
        Map<Equipa, Integer> derrotasPorEquipa = new HashMap<>();

        for (Jogo jogo : jogos) {
            Resultado resultado = jogo.getResultado();
            if (resultado == null || resultado.getVencedor() == null) continue;

            EquipaTitular equipaTVencedora = resultado.getVencedor();
            EquipaTitular equipaTVisitada = jogo.getEquipaTitularVisitada();
            EquipaTitular equipaTVisitante = jogo.getEquipaTitularVisitante();

            //se vencedora for a visitada, ent colocar equipa visitante no mapa
            if (equipaTVencedora.equals(equipaTVisitada) && equipaTVisitante != null) {
                Equipa equipa = jogo.getEquipaTitularVisitante().getEquipa();
                derrotasPorEquipa.merge(equipa, 1, Integer::sum);
            //caso contrario
            } else if (equipaTVencedora.equals(equipaTVisitante) && equipaTVisitada != null) {
                Equipa equipa = jogo.getEquipaTitularVisitante().getEquipa();
                derrotasPorEquipa.merge(equipa, 1, Integer::sum);
            }
        }

        return derrotasPorEquipa;
    }


    public List<EquipaDtoJavaFx> getEquipasXEmpates(Long x) {
        Map<Equipa, Integer> empatesPorEquipa = contarEmpatesPorEquipa();

        return empatesPorEquipa.entrySet().stream()
                .filter(entry -> entry.getValue().equals(x.intValue()))
                .map(entry -> mapToDto(entry.getKey()))
                .collect(Collectors.toList());
    }

    private Map<Equipa, Integer> contarEmpatesPorEquipa() {
        List<Jogo> jogos = jogoRepository.findAll(); 
        Map<Equipa, Integer> empatesPorEquipa = new HashMap<>();

        for (Jogo jogo : jogos) {
            Resultado resultado = jogo.getResultado();

            if (resultado == null || resultado.getVencedor() == null) {
                EquipaTitular equipaVisitada = jogo.getEquipaTitularVisitada();
                EquipaTitular equipaVisitante = jogo.getEquipaTitularVisitante();

                if (equipaVisitada != null) {
                    empatesPorEquipa.merge(equipaVisitada.getEquipa(), 1, Integer::sum);
                }

                if (equipaVisitante != null) {
                    empatesPorEquipa.merge(equipaVisitante.getEquipa(), 1, Integer::sum);
                }
            }
        }

        return empatesPorEquipa;
    }

    public boolean pertenceAoEnum(String valor) {
        for (Posicao pos : Posicao.values()) {
            if (pos.name().equalsIgnoreCase(valor)) {
                return true;
            }
        }
        return false;
    }



    public List<EquipaDtoJavaFx> getEquipasSemPos(String pos) {
        //primeiro verificar se pos eh valida
            //de entre as existentes
        //listar todas as equipas
        //criar uma lista de todas as posiçoes
        //listar todos os jogadores de cada equipa
        //verificar a sua posicao
            //se ela pertence ah lista de posicoes
                //retirar da lista de posi
            //se nao pertence
                //avancar
        //no fim verificar se pos pertence ah lista final
        //se sim guardar a equipa
        //se nao, seguir
        //fim :D        

        boolean posicaoValida = pertenceAoEnum(pos);
        if (!posicaoValida) {
            throw new IllegalArgumentException("Posição inválida: " + pos);
        }

        Posicao posicaoAlvo = Posicao.valueOf(pos.toUpperCase());

        List<Equipa> todasEquipas = equipaRepository.findAll(); 
        List<EquipaDtoJavaFx> equipasSemPos = new ArrayList<>();

        for (Equipa equipa : todasEquipas) {
            List<Jogador> jogadores = equipa.getPlantel(); 

            boolean nenhumComPosicao = jogadores.stream()
                .map(Jogador::getPrefPos)
                .noneMatch(p -> p == posicaoAlvo);

            if (nenhumComPosicao) {
                equipasSemPos.add(mapToDto(equipa));
            }
        }
        return equipasSemPos;
    }





    public List<EquipaDtoJavaFx> getEquipasConquista(String campNome) {
        Campeonato camp = campeonatoRepository.findByNome(campNome);
        //get all equipas 
        //verificar se nas conquistas de cada um se estah o campeonato (pelo campNome)
        List<EquipaDtoJavaFx> returned = new ArrayList<>();
        List<Equipa> equipas = equipaRepository.findAll();
        for(Equipa e : equipas){
            if(e.getConquistas().contains(camp)){
                returned.add(mapToDto(e));
            }
        }
        return returned;
    }

}