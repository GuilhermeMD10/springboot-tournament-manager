package pt.ul.fc.css.soccernow.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.soccernow.dto.ArbitroDto;
import pt.ul.fc.css.soccernow.dto.ArbitroDtoCreate;
import pt.ul.fc.css.soccernow.dto.ArbitroDtoUpdate;
import pt.ul.fc.css.soccernow.model.Arbitro;
import pt.ul.fc.css.soccernow.model.Jogo;
import pt.ul.fc.css.soccernow.repository.ArbitroRepository;
import pt.ul.fc.css.soccernow.repository.JogoRepository;

@Service
public class ArbitroService {
    
    @Autowired
    ArbitroRepository arbitroRepository;

    @Autowired
    JogoRepository jogoRepository;

    public ArbitroDto mapToDto(Arbitro arbitro){
        ArbitroDto arbitroDto = new ArbitroDto();
        arbitroDto.setNome( arbitro.getNome());
        arbitroDto.setId(arbitro.getId());
        arbitroDto.setEmail(arbitro.getEmail());
        arbitroDto.setNif(arbitro.getNif());
        arbitroDto.setCertificado(arbitro.isCertificado());
        
        return arbitroDto;
    }

    public ArbitroDto createArbitro(ArbitroDtoCreate arbitroDtoCreate){
        if(arbitroDtoCreate == null){
            throw new IllegalArgumentException("ArbitroDtoCreate nao pode ser nulo");
        }

        if(arbitroDtoCreate.getNome() == null || arbitroDtoCreate.getNome().trim().isEmpty()){
            throw new IllegalArgumentException("Nome do arbitro eh obrigatorio");
        }

        if (!arbitroDtoCreate.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Formato de email inválido");
        }

        if (!arbitroDtoCreate.getNif().matches("\\d{9}")) {
            throw new IllegalArgumentException("NIF deve conter exatamente 9 dígitos numéricos.");
        }
        Arbitro arbitro = new Arbitro();
        arbitro.setNome(arbitroDtoCreate.getNome());
        arbitro.setEmail(arbitroDtoCreate.getEmail());
        arbitro.setNif(arbitroDtoCreate.getNif());

        Arbitro savedArbitro = arbitroRepository.save(arbitro);
        ArbitroDto arbitroDto = this.mapToDto(savedArbitro);
        return arbitroDto;
    }

    public ArbitroDto updateArbitro(ArbitroDtoUpdate arbitroDtoUpdate){
        Long id = arbitroDtoUpdate.getId();
        validarId(id);
        Optional<Arbitro> arbitroOptional = arbitroRepository.findById(id);
        if(arbitroOptional.isEmpty()){
            throw new RuntimeException("Arbitro não encontrado");
        }

        Arbitro arbitro = arbitroOptional.get();
        arbitro.setEmail(arbitroDtoUpdate.getEmail());
        arbitro.setNif(arbitroDtoUpdate.getNif());
        arbitro.setNome(arbitroDtoUpdate.getNome());
        arbitro.setCertificado(arbitroDtoUpdate.isCertificado());

        Arbitro savedArbitro = arbitroRepository.save(arbitro);
        ArbitroDto arbitroDto = this.mapToDto(savedArbitro);
        return arbitroDto;

    }

    public List<ArbitroDto> getTodosArbitros(){
        List<Arbitro> arbitros = arbitroRepository.findAll();
        return arbitros.stream()
        .map(this::mapToDto)
        .toList();
    }

    public ArbitroDto procurarPorId(Long id){
        validarId(id);
        Optional<Arbitro> arbitroOptional = arbitroRepository.findById(id);
        if(arbitroOptional.isEmpty()){
            throw new RuntimeException("Arbitro não encontrado");
        }
        return mapToDto(arbitroOptional.get());
    }

    public List<ArbitroDto> procurarPorCertificado(Boolean certificado){
        List<Arbitro> arbitros = arbitroRepository.findByCertificado(certificado);
        return arbitros.stream()
            .map(this::mapToDto)
            .toList();
    }

    public ArbitroDto procurarPorNif(String nif) {
        Optional<Arbitro> arbitroOptional = arbitroRepository.findByNif(nif);
        if (arbitroOptional.isEmpty()) {
            throw new RuntimeException("Árbitro não encontrado");
        }
        return mapToDto(arbitroOptional.get());
    }

    public ArbitroDto procurarPorEmail(String email) {
        Optional<Arbitro> arbitroOptional = arbitroRepository.findByEmail(email);
        if (arbitroOptional.isEmpty()) {
            throw new RuntimeException("Árbitro não encontrado");
        }
        return mapToDto(arbitroOptional.get());
    }

    public List<ArbitroDto> procurarPorNome(String nome){
        if(nome == null || nome.trim().isEmpty()){
            throw new IllegalArgumentException("Nome nao pode ser nulo ou vazio");
        }
        List<Arbitro> arbitros = arbitroRepository.findByNomeContainingIgnoreCase(nome);
        return arbitros.stream()
        .map(this::mapToDto)
        .toList();
    }

    public void removerArbitro(Long arbitroId) {
        validarId(arbitroId);
        Optional<Arbitro> arbitroOptional = arbitroRepository.findById(arbitroId);
        if (arbitroOptional.isEmpty()) {
            throw new RuntimeException("Arbitro não encontrado");
        }
    
        List<Jogo> jogosComoPrincipal = jogoRepository.findByArbitroPrincipalId(arbitroId);
        for (Jogo jogo : jogosComoPrincipal) {
            if (jogo.getArbitroPrincipal() != null && jogo.getArbitroPrincipal().getId().equals(arbitroId)) {
                jogo.setArbitroPrincipal(null);
            }
        }
        jogoRepository.saveAll(jogosComoPrincipal);
    

        List<Jogo> jogosComoAdjunto = jogoRepository.findByEquipaArbitragemId(arbitroId);
        for (Jogo jogo : jogosComoAdjunto) {
            jogo.getEquipaArbitragem().removeIf(a -> a.getId().equals(arbitroId)); 
        }
        jogoRepository.saveAll(jogosComoAdjunto); 

        arbitroRepository.deleteById(arbitroId);
    }
    
    

    public void putCertificado(Long id) {
        Arbitro arbitro = arbitroRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Árbitro não encontrado"));
    
        arbitro.setCertificado(true);
        arbitroRepository.save(arbitro);
    }

    // === PARTIDAS OFICIADAS ===

    public List<ArbitroDto> findArbitroComMaisJogos() {
        List<Arbitro> arbitros = arbitroRepository.findTop5ByOrderByPartidasOficiadasDesc();

        if (arbitros.isEmpty()) {
            throw new RuntimeException("Nenhum árbitro encontrado");
        }

        return arbitros.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
    }

    public List<ArbitroDto> findArbitroComMenosJogos() {
        List<Arbitro> arbitros = arbitroRepository.findTop5ByOrderByPartidasOficiadasAsc();

        if (arbitros.isEmpty()) {
            throw new RuntimeException("Nenhum árbitro encontrado");
        }

        return arbitros.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
    }


    public List<ArbitroDto> findByPartidasOficiadas(int partidas) {
        return arbitroRepository.findByPartidasOficiadas(partidas)
                .stream().map(this::mapToDto).toList();
    }

    public List<ArbitroDto> findByPartidasOficiadasGreaterThan(int partidas) {
        return arbitroRepository.findByPartidasOficiadasGreaterThan(partidas)
                .stream().map(this::mapToDto).toList();
    }

    public List<ArbitroDto> findByPartidasOficiadasGreaterThanEqual(int partidas) {
        return arbitroRepository.findByPartidasOficiadasGreaterThanEqual(partidas)
                .stream().map(this::mapToDto).toList();
    }

    public List<ArbitroDto> findByPartidasOficiadasLessThan(int partidas) {
        return arbitroRepository.findByPartidasOficiadasLessThan(partidas)
                .stream().map(this::mapToDto).toList();
    }

    public List<ArbitroDto> findByPartidasOficiadasLessThanEqual(int partidas) {
        return arbitroRepository.findByPartidasOficiadasLessThanEqual(partidas)
                .stream().map(this::mapToDto).toList();
    }

    // === CARTÕES MOSTRADOS ===

    public List<ArbitroDto> findArbitroComMenosCartoesMostrados() {
        List<Arbitro> arbitros = arbitroRepository.findTop5ByOrderByCartoesMostradosAsc();

        if (arbitros.isEmpty()) {
            throw new RuntimeException("Nenhum árbitro encontrado");
        }

        return arbitros.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
    }

    public List<ArbitroDto> findArbitroComMaisCartoesMostrados() {
        List<Arbitro> arbitros = arbitroRepository.findTop5ByOrderByCartoesMostradosDesc();

        if (arbitros.isEmpty()) {
            throw new RuntimeException("Nenhum árbitro encontrado");
        }

        return arbitros.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
    }


    public List<ArbitroDto> findByCartoesMostrados(int cartoes) {
        return arbitroRepository.findByCartoesMostrados(cartoes)
                .stream().map(this::mapToDto).toList();
    }

    public List<ArbitroDto> findByCartoesMostradosGreaterThan(int cartoes) {
        return arbitroRepository.findByCartoesMostradosGreaterThan(cartoes)
                .stream().map(this::mapToDto).toList();
    }

    public List<ArbitroDto> findByCartoesMostradosGreaterThanEqual(int cartoes) {
        return arbitroRepository.findByCartoesMostradosGreaterThanEqual(cartoes)
                .stream().map(this::mapToDto).toList();
    }

    public List<ArbitroDto> findByCartoesMostradosLessThan(int cartoes) {
        return arbitroRepository.findByCartoesMostradosLessThan(cartoes)
                .stream().map(this::mapToDto).toList();
    }

    public List<ArbitroDto> findByCartoesMostradosLessThanEqual(int cartoes) {
        return arbitroRepository.findByCartoesMostradosLessThanEqual(cartoes)
                .stream().map(this::mapToDto).toList();
    }
    



        /**
     * Valida se o ID fornecido é válido. Um ID válido é um número maior que zero.
     *
     * @param id o ID a ser validado
     * @throws IllegalArgumentException se o ID for nulo ou menor ou igual a zero
     */
    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O id não é válido.");
        }
    }


}
