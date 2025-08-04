package pt.ul.fc.css.soccernow.serviceJavafx;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import pt.ul.fc.css.soccernow.dtoJavafx.EquipaDtoJavaFx;
import pt.ul.fc.css.soccernow.dtoJavafx.JogadorDtoCreateJavaFx;
import pt.ul.fc.css.soccernow.dtoJavafx.JogadorDtoJavaFx;
import pt.ul.fc.css.soccernow.dtoJavafx.JogadorDtoUpdateJavaFx;
import pt.ul.fc.css.soccernow.model.Equipa;
import pt.ul.fc.css.soccernow.model.EquipaTitular;
import pt.ul.fc.css.soccernow.model.Jogador;
import pt.ul.fc.css.soccernow.model.Resultado;
import pt.ul.fc.css.soccernow.model.enums.Posicao;
import pt.ul.fc.css.soccernow.repository.EquipaTitularRepository;
import pt.ul.fc.css.soccernow.repository.JogadorRepository;
import pt.ul.fc.css.soccernow.repository.ResultadoRepository;

/**
 * Serviço responsável por gerir operações relacionadas com Jogadores.
 * Inclui métodos para criação, leitura, atualização e remoção, bem como mapeamento para DTOs.
 */
@Service
public class JogadorServiceJavaFx {

    @Autowired
    private JogadorRepository jogadorRepository;

    @Autowired
    private ResultadoRepository resultadoRepository;

    @Autowired
    private EquipaTitularRepository equipaTitularRepository;

    @Autowired
    private EquipaServiceJavaFx equipaService;

    /**
     * Converte um objeto {@link Jogador} para um {@link JogadorDtoJavaFx}.
     *
     * @param jogador o jogador a ser convertido
     * @return o DTO correspondente ao jogador fornecido
     */
    public JogadorDtoJavaFx mapToDto(Jogador jogador){
        if(jogador == null){
            throw new IllegalArgumentException("Jogador nao pode ser nulo");
        }
        JogadorDtoJavaFx jogadorDto = new JogadorDtoJavaFx();
        jogadorDto.setId(jogador.getId());
        jogadorDto.setNome(jogador.getNome());
        jogadorDto.setEmail(jogador.getEmail());
        jogadorDto.setNif(jogador.getNif());
        jogadorDto.setPrefPos(jogador.getPrefPos());
        jogadorDto.setGolos(jogador.getGolos());
        jogadorDto.setPartidasJogadas(jogador.getPartidasJogadas());
        jogadorDto.setAmarelos(jogador.getAmarelos());
        jogadorDto.setVermelhos(jogador.getVermelhos());
        List<EquipaDtoJavaFx> equipas_ids = new ArrayList<>();
        if(jogador.getEquipas() != null ){
            for (Equipa equipa : jogador.getEquipas()) {
                equipas_ids.add(equipaService.mapToDto(equipa));
            }
        }

        jogadorDto.setEquipas(equipas_ids);
        return jogadorDto;
    }

        private Jogador mapToEntity(JogadorDtoJavaFx dto) {
        Jogador jogador = new Jogador();
        jogador.setNif(dto.getNif());
        jogador.setNome(dto.getNome());
        jogador.setEmail(dto.getEmail());
        jogador.setGolos(dto.getGolos());
        jogador.setAmarelos(dto.getAmarelos());
        jogador.setVermelhos(dto.getVermelhos());
        jogador.setPartidasJogadas(dto.getPartidasJogadas());
        jogador.setPrefPos(dto.getPrefPos());

        return jogador;
    }


    public JogadorDtoJavaFx setPrefPos(Long id, Posicao prefPos){
        validarId(id);
        Optional<Jogador> jogadorOptional = jogadorRepository.findById(id);
        if (jogadorOptional.isEmpty()){
            throw new RuntimeException("Jogador não encontrado");
        }

        Jogador jogador = jogadorOptional.get();
        jogador.setPrefPos(prefPos);
        jogadorRepository.save(jogador);
        return this.mapToDto(jogador);
    }

        /**
     * Cria um novo jogador com base num {@link JogadorDtoJavaFx}.
     *
     * @param jogadorDto o DTO com os dados do jogador
     * @return o DTO do jogador criado, com o ID atribuído
     * @throws IllegalArgumentException se o DTO ou campos obrigatórios forem inválidos
     */
    public JogadorDtoJavaFx createJogador(JogadorDtoCreateJavaFx jogadorDtoCreate) {
        if (jogadorDtoCreate == null || jogadorDtoCreate.getNome() == null || jogadorDtoCreate.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do jogador é obrigatório.");
        }

        if (jogadorDtoCreate.getEmail() == null || jogadorDtoCreate.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do jogador é obrigatório.");
        }
        
        if (!jogadorDtoCreate.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Formato de email inválido");
        }

        if (!jogadorDtoCreate.getNif().matches("\\d{9}")) {
            throw new IllegalArgumentException("NIF deve conter exatamente 9 dígitos numéricos.");
        }
    
        Jogador jogador = new Jogador();
        jogador.setNome(jogadorDtoCreate.getNome());
        jogador.setEmail(jogadorDtoCreate.getEmail());
        jogador.setNif(jogadorDtoCreate.getNif());
        


        Jogador savedJogador = jogadorRepository.save(jogador);
        JogadorDtoJavaFx jogadorDto = this.mapToDto(savedJogador);
        return jogadorDto;
    }
    

    public JogadorDtoJavaFx saveJogador(JogadorDtoJavaFx jogadorDto) {
        if (jogadorDto == null) {
            throw new IllegalArgumentException("Jogador DTO não pode ser nulo");
        }

        // Converte o DTO para a entidade
        Jogador jogador = mapToEntity(jogadorDto);

        // Guarda no repositório e devolve como DTO
        Jogador jogadorSalvo = jogadorRepository.save(jogador);
        return mapToDto(jogadorSalvo);
    }


    public JogadorDtoJavaFx updateJogador(JogadorDtoUpdateJavaFx jogadorDtoUpdate){
        Long id = jogadorDtoUpdate.getId();
        validarId(id);
        
        Optional<Jogador> jogadorOptional = jogadorRepository.findById(id);
        if(jogadorOptional.isEmpty()){
            throw new RuntimeException("Jogador não encontrado");
        }

        Jogador jogador = jogadorOptional.get();
        jogador.setEmail(jogadorDtoUpdate.getEmail());
        jogador.setNif(jogadorDtoUpdate.getNif());
        jogador.setNome(jogadorDtoUpdate.getNome());
        jogador.setPrefPos(jogadorDtoUpdate.getPrefPos());

        Jogador savedJogador = jogadorRepository.save(jogador);
        JogadorDtoJavaFx jogadorDto = this.mapToDto(savedJogador);
        return jogadorDto;
    }



//----------------------------FUNCOES PARA PROCURAR JOGADORES----------------------------//


    /**
     * Procura um jogador pelo seu ID.
     *
     * @param id o ID do jogador
     * @return o DTO correspondente
     * @throws IllegalArgumentException se o ID for inválido
     * @throws RuntimeException se o jogador não for encontrado
     */
    public JogadorDtoJavaFx procurarPorId(Long id) {
        validarId(id);
        Optional<Jogador> jogadorOptional = jogadorRepository.findById(id);
        if (jogadorOptional.isEmpty()){
            throw new RuntimeException("Jogador não encontrado");
        }
        return mapToDto(jogadorOptional.get());
    }

    public JogadorDtoJavaFx procurarPorNif(String nif) {
        Optional<Jogador> jogadorOptional = jogadorRepository.findByNif(nif);
        if (jogadorOptional.isEmpty()) {
            throw new RuntimeException("Jogador não encontrado");
        }
        return mapToDto(jogadorOptional.get());
    }

    public JogadorDtoJavaFx procurarPorEmail(String email) {
        Optional<Jogador> jogadorOptional = jogadorRepository.findByEmail(email);
        if (jogadorOptional.isEmpty()) {
            throw new RuntimeException("Jogador não encontrado");
        }
        return mapToDto(jogadorOptional.get());
    }

    /**
     * Obtém todos os jogadores existentes.
     *
     * @return lista de {@link JogadorDtoJavaFx} com todos os jogadores
     */
    public List<JogadorDtoJavaFx> getTodosJogadores() {
        List<Jogador> jogadores = jogadorRepository.findAll();
        return jogadores.stream()
                .map(this::mapToDto)
                .toList();
    }

    /**
     * Procura jogadores pelo nome (coincidência parcial, ignorando maiúsculas/minúsculas).
     *
     * @param nome o nome ou parte do nome a procurar
     * @return lista de jogadores encontrados
     * @throws IllegalArgumentException se o nome for nulo ou vazio
     * @throws RuntimeException se nenhum jogador for encontrado
     */
    public List<JogadorDtoJavaFx> procurarPorNome(String nome) {
        validarString(nome);

        List<Jogador> jogadores = jogadorRepository.findByNomeContainingIgnoreCase(nome);
    
        if (jogadores.isEmpty()) {
            throw new RuntimeException("Nenhum jogador encontrado com o nome fornecido.");
        }
    
        return jogadores.stream()
                .map(this::mapToDto)
                .toList();
    }



//----------------------------FUNCOES PARA ELIMINAR JOGADORES----------------------------//

    /**
     * Elimina um jogador com base no seu ID.
     *
     * @param id o ID do jogador a eliminar
     * @throws IllegalArgumentException se o ID for inválido
     * @throws RuntimeException se o jogador não for encontrado
     */
    @Transactional
    public void removerJogador(Long jogadorId) {
        validarId(jogadorId);
        Optional<Jogador> jogadorOptional = jogadorRepository.findById(jogadorId);
        if (jogadorOptional.isEmpty()){
            throw new RuntimeException("Jogador não encontrado");
        }
        Jogador jogador = jogadorOptional.get();

        removerJogadorDeEquipas(jogador);
        removerJogadorDeResultados(jogador);
        removerJogadorDeEquipasTitulares(jogador);
        jogadorRepository.deleteById(jogadorId); // agora já podes apagar o jogador
    }

    @Transactional
    public void removerJogadorDeResultados(Jogador jogador) {
        List<Resultado> resultados = resultadoRepository.findAll();

        for (Resultado resultado : resultados) {
            resultado.getGolosVisitado().remove(jogador);
            resultado.getGolosVisitante().remove(jogador);
            resultado.getAmarelosVisitado().remove(jogador);
            resultado.getAmarelosVisitante().remove(jogador);
            resultado.getVermelhosVisitado().remove(jogador);
            resultado.getVermelhosVisitante().remove(jogador);
        }

        resultadoRepository.saveAll(resultados);
    }

    @Transactional
    public void removerJogadorDeEquipas(Jogador jogador) {
        for (Equipa equipa : jogador.getEquipas()) {
            equipa.getPlantel().remove(jogador);
        }
        jogador.getEquipas().clear(); // remove todas as equipas do jogador
    }

    
    @Transactional
    public void removerJogadorDeEquipasTitulares(Jogador jogador) {
        List<EquipaTitular> equipasTitulares = equipaTitularRepository.findAll();

        for (EquipaTitular et : equipasTitulares) {
            et.getJogadores().remove(jogador);

            // Se for o guarda-redes, remove a referência
            if (jogador.equals(et.getGuardaRedes())) {
                et.setGuardaRedes(null);
            }
        }

        equipaTitularRepository.saveAll(equipasTitulares);
    }   
    
//----------------------------------------------------------GOLOS-----------------------------------------------\\

    /**
     * Incrementa em 1 o número de golos marcados por um jogador.
     *
     * @param id o ID do jogador
     * @throws IllegalArgumentException se o ID for inválido
     * @throws RuntimeException se o jogador não for encontrado
     */
    @Transactional
    public void incrementarGolos(Long id) {
        validarId(id);
        Jogador jogador = jogadorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Jogador nao encontrado"));
        jogador.setGolos(jogador.getGolos() + 1);
        jogadorRepository.save(jogador);
    }

    /**
     * Adiciona um número específico de golos a um jogador.
     *
     * @param id o ID do jogador
     * @param golos o número de golos a adicionar
     * @throws IllegalArgumentException se o ID for inválido
     * @throws RuntimeException se o jogador não for encontrado
     */
    public void adicionarGolos(Long id, int golos){
        validarId(id);
        Jogador jogador = jogadorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Jogador nao encontrado"));
        jogador.setGolos(jogador.getGolos() + golos);
        jogadorRepository.save(jogador);
    }

    /**
     * Obtém o número total de golos marcados por um jogador.
     *
     * @param id o ID do jogador
     * @return o número de golos marcados
     * @throws IllegalArgumentException se o ID for inválido
     * @throws RuntimeException se o jogador não for encontrado
     */
    public int getGolos(Long id) {
        validarId(id);
        Jogador jogador = jogadorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Jogador nao encontrado"));
        return jogador.getGolos();
    }


    public List<JogadorDtoJavaFx> getJogadoresPorGolos(int golos){
        List<Jogador> jogadores = jogadorRepository.findByGolos(golos);
        return jogadores.stream()
            .map(this::mapToDto)
            .toList();
    }

    public List<JogadorDtoJavaFx> getJogadoresComMaisGolos(int golos) {
        return jogadorRepository.findByGolosGreaterThan(golos)
            .stream().map(this::mapToDto).toList();
    }

    public List<JogadorDtoJavaFx> getJogadoresComMenosGolos(int golos) {
        return jogadorRepository.findByGolosLessThan(golos)
            .stream().map(this::mapToDto).toList();
    }

    public double getMediaGolosPorNome(String nome){
        validarString(nome);
        List<JogadorDtoJavaFx> jogadorDtos = procurarPorNome(nome);
        int jogos = 0;
        int golos = 0;
        for (JogadorDtoJavaFx jogadorDto : jogadorDtos ){
            jogos += jogadorDto.getPartidasJogadas();
            golos += jogadorDto.getGolos();
        }
        return jogos == 0 ? 0.0 : (double) golos / jogos;
    }

    public List<JogadorDtoJavaFx> getTop5GolosMais() {
        return jogadorRepository.findTop5ByOrderByGolosDesc()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<JogadorDtoJavaFx> getTop5GolosMenos() {
        return jogadorRepository.findTop5ByOrderByGolosAsc()
                .stream()
                .map(this::mapToDto)
                .toList();
    }


//----------------------------------------------------------AMARELOS-----------------------------------------------\\

        /**
     * Incrementa em 1 o número de cartões amarelos de um jogador.
     *
     * @param id o ID do jogador
     * @throws IllegalArgumentException se o ID for inválido
     * @throws RuntimeException se o jogador não for encontrado
     */
    @Transactional
    public void incrementarAmarelos(Long id){
        validarId(id);
        Jogador jogador = jogadorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Jogador nao encontrado"));
        jogador.setAmarelos(jogador.getAmarelos() + 1);
        jogadorRepository.save(jogador);
    }



    /**
     * Obtém o número de cartões amarelos recebidos por um jogador.
     *
     * @param id o ID do jogador
     * @return o número de cartões amarelos
     * @throws IllegalArgumentException se o ID for inválido
     * @throws RuntimeException se o jogador não for encontrado
     */
    public int getAmarelos(Long id) {
        validarId(id);
        Jogador jogador = jogadorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Jogador nao encontrado"));
        return jogador.getAmarelos();
    }

    public List<JogadorDtoJavaFx> getJogadoresPorAmarelos(int amarelos) {
        List<Jogador> jogadores = jogadorRepository.findByAmarelos(amarelos);
        return jogadores.stream()
            .map(this::mapToDto)
            .toList();
    }

    public List<JogadorDtoJavaFx> getJogadoresComMenosAmarelos(int amarelos) {
        return jogadorRepository.findByAmarelosLessThan(amarelos)
            .stream().map(this::mapToDto).toList();
    }

    public List<JogadorDtoJavaFx> getJogadoresComMaisAmarelos(int amarelos) {
        return jogadorRepository.findByAmarelosGreaterThan(amarelos)
            .stream().map(this::mapToDto).toList();
    }

    public List<JogadorDtoJavaFx> getTop5CartoesAmarelosMais() {
        return jogadorRepository.findTop5ByOrderByAmarelosDesc()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<JogadorDtoJavaFx> getTop5CartoesAmarelosMenos() {
        return jogadorRepository.findTop5ByOrderByAmarelosAsc()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

//----------------------------------------------------------VERMELHOS-----------------------------------------------\\

    /**
     * Incrementa em 1 o número de cartões vermelhos de um jogador.
     *
     * @param id o ID do jogador
     * @throws IllegalArgumentException se o ID for inválido
     * @throws RuntimeException se o jogador não for encontrado
     */
    @Transactional
    public void incrementarVermelhos(Long id){
        validarId(id);
        Jogador jogador = jogadorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Jogador nao encontrado"));
        jogador.setVermelhos(jogador.getVermelhos() + 1);
        jogadorRepository.save(jogador);
    }

    /**
     * Obtém o número de cartões vermelhos recebidos por um jogador.
     *
     * @param id o ID do jogador
     * @return o número de cartões vermelhos
     * @throws IllegalArgumentException se o ID for inválido
     * @throws RuntimeException se o jogador não for encontrado
     */
    public int getVermelhos(Long id) {
        validarId(id);
        Jogador jogador = jogadorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Jogador nao encontrado"));
        return jogador.getVermelhos();
    }

    public List<JogadorDtoJavaFx> getJogadoresPorVermelhos(int vermelhos) {
        List<Jogador> jogadores = jogadorRepository.findByVermelhos(vermelhos);
        return jogadores.stream()
            .map(this::mapToDto)
            .toList();
    }

    public List<JogadorDtoJavaFx> getTop5CartoesVermelhosMais() {
        return jogadorRepository.findTop5ByOrderByVermelhosDesc()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<JogadorDtoJavaFx> getTop5CartoesVermelhosMenos() {
        return jogadorRepository.findTop5ByOrderByAmarelosAsc()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<JogadorDtoJavaFx> getJogadoresComMenosVermelhos(int vermelhos) {
        return jogadorRepository.findByVermelhosLessThan(vermelhos)
            .stream().map(this::mapToDto).toList();
    }

    public List<JogadorDtoJavaFx> getJogadoresComMaisVermelhos(int vermelhos) {
        return jogadorRepository.findByVermelhosGreaterThan(vermelhos)
            .stream().map(this::mapToDto).toList();
    }


//----------------------------------------------------------TOTAL-DE-CARTOES-----------------------------------------------\\


    public List<JogadorDtoJavaFx> getJogadoresPorCartoes(int cartoes) {
        List<Jogador> jogadores = jogadorRepository.findBySomaCartoes(cartoes);
        return jogadores.stream()
            .map(this::mapToDto)
            .toList();
    }

        public List<JogadorDtoJavaFx> getJogadoresComMaisCartoesQue(int cartoes) {
        return jogadorRepository.findByCartoesTotaisMaiorQue(cartoes)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<JogadorDtoJavaFx> getJogadoresComMenosCartoesQue(int cartoes) {
        return jogadorRepository.findByCartoesTotaisMenorQue(cartoes)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<JogadorDtoJavaFx> getTop5JogadoresComMaisCartoes() {
        return jogadorRepository.findTopJogadoresByCartoesTotais()
                .stream()
                .limit(5)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }



//----------------------------------------------------------PARTIDAS-----------------------------------------------\\


    /**
     * Incrementa em 1 o número de partidas jogadas por um jogador.
     *
     * @param id o ID do jogador
     * @throws IllegalArgumentException se o ID for inválido
     * @throws RuntimeException se o jogador não for encontrado
     */
    public void incrementarPartidasJogadas(Long id){
        validarId(id);
        Jogador jogador = jogadorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Jogador nao encontrado"));
        jogador.setPartidasJogadas(jogador.getPartidasJogadas() + 1);
        jogadorRepository.save(jogador);
    }

    /**
     * Obtém o número total de partidas jogadas por um jogador.
     *
     * @param id o ID do jogador
     * @return o número de partidas jogadas
     * @throws IllegalArgumentException se o ID for inválido
     * @throws RuntimeException se o jogador não for encontrado
     */
    public int getPartidasJogadas(Long id) {
        validarId(id);
        Jogador jogador = jogadorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Jogador nao encontrado"));
        return jogador.getPartidasJogadas();
    }

    public List<JogadorDtoJavaFx> getJogadoresPorPartidasJogadas(int partidas) {
        List<Jogador> jogadores = jogadorRepository.findByPartidasJogadas(partidas);
        return jogadores.stream()
            .map(this::mapToDto)
            .toList();
    }

    public List<JogadorDtoJavaFx> getJogadoresComMenosPartidas(int partidas) {
        return jogadorRepository.findByPartidasJogadasLessThan(partidas)
            .stream().map(this::mapToDto).toList();
    }

    public List<JogadorDtoJavaFx> getJogadoresComMaisPartidas(int partidas) {
        return jogadorRepository.findByPartidasJogadasGreaterThan(partidas)
            .stream().map(this::mapToDto).toList();
    }

    public List<JogadorDtoJavaFx> getTop5JogadoresMaisPartidas() {
        return jogadorRepository.findTop5ByOrderByPartidasJogadasDesc()
                .stream().map(this::mapToDto).toList();
    }

    public List<JogadorDtoJavaFx> getTop5JogadoresMenosPartidas() {
        return jogadorRepository.findTop5ByOrderByPartidasJogadasAsc()
                .stream().map(this::mapToDto).toList();
    }

//----------------------------------------------------------POSICAO-----------------------------------------------\\

    public List<JogadorDtoJavaFx> procurarPorPosicao(Posicao posicao) {
        List<Jogador> jogadores = jogadorRepository.findByPrefPos(posicao);
        return jogadores.stream()
                        .map(this::mapToDto)
                        .collect(Collectors.toList());
    }

//----------------------------FUNCOES AUXILIARES----------------------------//

    /**
     * Valida se o ID fornecido é válido. Um ID válido é um número maior que zero.
     *
     * @param id o ID a ser validado
     * @throws IllegalArgumentException se o ID for nulo ou menor ou igual a zero
     */
    private void validarId(Long id){
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O id não é válido.");
        }
    }

    /**
     * Valida se uma string não é nula nem vazia (após remover espaços).
     *
     * @param string a string a validar
     * @throws IllegalArgumentException se a string for nula ou vazia
     */
    private void validarString(String string) {
        if (string == null || string.trim().isEmpty()) {
            throw new IllegalArgumentException("String invalida ou vazia");
        }
    }

}