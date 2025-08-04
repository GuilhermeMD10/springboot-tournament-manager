package pt.ul.fc.css.soccernow.serviceJavafx;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.soccernow.dtoJavafx.ArbitroDtoJavaFx;
import pt.ul.fc.css.soccernow.dtoJavafx.ArbitroDtoCreateJavaFx;
import pt.ul.fc.css.soccernow.dtoJavafx.ArbitroDtoUpdateJavaFx;
import pt.ul.fc.css.soccernow.model.Arbitro;
import pt.ul.fc.css.soccernow.model.Jogo;
import pt.ul.fc.css.soccernow.repository.ArbitroRepository;
import pt.ul.fc.css.soccernow.repository.JogoRepository;

@Service
public class ArbitroServiceJavaFx {
    
    @Autowired
    ArbitroRepository arbitroRepository;

    @Autowired
    JogoRepository jogoRepository;

    private ArbitroDtoJavaFx mapToDto(Arbitro arbitro){
        ArbitroDtoJavaFx arbitroDto = new ArbitroDtoJavaFx();
        arbitroDto.setNome( arbitro.getNome());
        arbitroDto.setId(arbitro.getId());
        arbitroDto.setEmail(arbitro.getEmail());
        arbitroDto.setNif(arbitro.getNif());
        arbitroDto.setCertificado(arbitro.isCertificado());
        
        return arbitroDto;
    }

    public ArbitroDtoJavaFx createArbitro(ArbitroDtoCreateJavaFx arbitroDtoCreate){
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
        ArbitroDtoJavaFx arbitroDto = this.mapToDto(savedArbitro);
        return arbitroDto;
    }

    public ArbitroDtoJavaFx updateArbitro(ArbitroDtoUpdateJavaFx arbitroDtoUpdate){
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
        ArbitroDtoJavaFx arbitroDto = this.mapToDto(savedArbitro);
        return arbitroDto;

    }

    public List<ArbitroDtoJavaFx> getTodosArbitros(){
        List<Arbitro> arbitros = arbitroRepository.findAll();
        return arbitros.stream()
        .map(this::mapToDto)
        .toList();
    }

    public ArbitroDtoJavaFx procurarPorId(Long id){
        validarId(id);
        Optional<Arbitro> arbitroOptional = arbitroRepository.findById(id);
        if(arbitroOptional.isEmpty()){
            throw new RuntimeException("Arbitro não encontrado");
        }
        return mapToDto(arbitroOptional.get());
    }

    public List<ArbitroDtoJavaFx> procurarPorCertificado(Boolean certificado){
        List<Arbitro> arbitros = arbitroRepository.findByCertificado(certificado);
        return arbitros.stream()
            .map(this::mapToDto)
            .toList();
    }

    public ArbitroDtoJavaFx procurarPorNif(String nif) {
        Optional<Arbitro> arbitroOptional = arbitroRepository.findByNif(nif);
        if (arbitroOptional.isEmpty()) {
            throw new RuntimeException("Árbitro não encontrado");
        }
        return mapToDto(arbitroOptional.get());
    }

    public ArbitroDtoJavaFx procurarPorEmail(String email) {
        Optional<Arbitro> arbitroOptional = arbitroRepository.findByEmail(email);
        if (arbitroOptional.isEmpty()) {
            throw new RuntimeException("Árbitro não encontrado");
        }
        return mapToDto(arbitroOptional.get());
    }

    public List<ArbitroDtoJavaFx> procurarPorNome(String nome){
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

    public List<ArbitroDtoJavaFx> findArbitroComMaisJogos() {
        List<Arbitro> arbitros = arbitroRepository.findTop5ByOrderByPartidasOficiadasDesc();

        if (arbitros.isEmpty()) {
            throw new RuntimeException("Nenhum árbitro encontrado");
        }

        return arbitros.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
    }

    public List<ArbitroDtoJavaFx> findArbitroComMenosJogos() {
        List<Arbitro> arbitros = arbitroRepository.findTop5ByOrderByPartidasOficiadasAsc();

        if (arbitros.isEmpty()) {
            throw new RuntimeException("Nenhum árbitro encontrado");
        }

        return arbitros.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
    }


    public List<ArbitroDtoJavaFx> findByPartidasOficiadas(int partidas) {
        return arbitroRepository.findByPartidasOficiadas(partidas)
                .stream().map(this::mapToDto).toList();
    }

    public List<ArbitroDtoJavaFx> findByPartidasOficiadasGreaterThan(int partidas) {
        return arbitroRepository.findByPartidasOficiadasGreaterThan(partidas)
                .stream().map(this::mapToDto).toList();
    }

    public List<ArbitroDtoJavaFx> findByPartidasOficiadasGreaterThanEqual(int partidas) {
        return arbitroRepository.findByPartidasOficiadasGreaterThanEqual(partidas)
                .stream().map(this::mapToDto).toList();
    }

    public List<ArbitroDtoJavaFx> findByPartidasOficiadasLessThan(int partidas) {
        return arbitroRepository.findByPartidasOficiadasLessThan(partidas)
                .stream().map(this::mapToDto).toList();
    }

    public List<ArbitroDtoJavaFx> findByPartidasOficiadasLessThanEqual(int partidas) {
        return arbitroRepository.findByPartidasOficiadasLessThanEqual(partidas)
                .stream().map(this::mapToDto).toList();
    }

    // === CARTÕES MOSTRADOS ===

    public List<ArbitroDtoJavaFx> findArbitroComMenosCartoesMostrados() {
        List<Arbitro> arbitros = arbitroRepository.findTop5ByOrderByCartoesMostradosAsc();

        if (arbitros.isEmpty()) {
            throw new RuntimeException("Nenhum árbitro encontrado");
        }

        return arbitros.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
    }

    public List<ArbitroDtoJavaFx> findArbitroComMaisCartoesMostrados() {
        List<Arbitro> arbitros = arbitroRepository.findTop5ByOrderByCartoesMostradosDesc();

        if (arbitros.isEmpty()) {
            throw new RuntimeException("Nenhum árbitro encontrado");
        }

        return arbitros.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
    }


    public List<ArbitroDtoJavaFx> findByCartoesMostrados(int cartoes) {
        return arbitroRepository.findByCartoesMostrados(cartoes)
                .stream().map(this::mapToDto).toList();
    }

    public List<ArbitroDtoJavaFx> findByCartoesMostradosGreaterThan(int cartoes) {
        return arbitroRepository.findByCartoesMostradosGreaterThan(cartoes)
                .stream().map(this::mapToDto).toList();
    }

    public List<ArbitroDtoJavaFx> findByCartoesMostradosGreaterThanEqual(int cartoes) {
        return arbitroRepository.findByCartoesMostradosGreaterThanEqual(cartoes)
                .stream().map(this::mapToDto).toList();
    }

    public List<ArbitroDtoJavaFx> findByCartoesMostradosLessThan(int cartoes) {
        return arbitroRepository.findByCartoesMostradosLessThan(cartoes)
                .stream().map(this::mapToDto).toList();
    }

    public List<ArbitroDtoJavaFx> findByCartoesMostradosLessThanEqual(int cartoes) {
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
