package pt.ul.fc.css.soccernow.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.soccernow.dto.CampeonatoDto;
import pt.ul.fc.css.soccernow.dto.EquipaDto;
import pt.ul.fc.css.soccernow.model.Campeonato;
import pt.ul.fc.css.soccernow.model.Equipa;
import pt.ul.fc.css.soccernow.model.Jogador;
import pt.ul.fc.css.soccernow.model.Jogo;
import pt.ul.fc.css.soccernow.model.Pontuacao;
import pt.ul.fc.css.soccernow.model.enums.Campeonato_Estado;
import pt.ul.fc.css.soccernow.repository.CampeonatoRepository;
import pt.ul.fc.css.soccernow.repository.EquipaRepository;

@Service
public class CampeonatoService {

    @Autowired
    private CampeonatoRepository campeonatoRepository;

    @Autowired 
    private EquipaRepository equipaRepository;

    private CampeonatoDto mapToDto(Campeonato camp) {
        CampeonatoDto campDto = new CampeonatoDto();

        campDto.setId(camp.getId());
        campDto.setNome(camp.getNome());

        //aqui eu posso ate sacar todas as pontuaçoes e retirar as equipas para as equipas e os pontos para a classificaçao
        List<Long> equipasIds = new ArrayList<>();
        List<Long> classificacao = new ArrayList<>();

        if (camp.getEquipas() != null) {
            for (Equipa equipa : camp.getEquipas()) {
                equipasIds.add(equipa.getId());
            }
        }
        
        if (camp.getClassificacao() != null) {
            System.out.println(camp.getClassificacao());
            for (Pontuacao pontuacao : camp.getClassificacao()) {
                classificacao.add(pontuacao.getPontos()); 
            }
        }

        campDto.setEquipas(equipasIds);
        campDto.setClassificacao(classificacao);
        List<Long> jogosIds = camp.getJogos().stream()
                .map(jogo -> jogo.getId())
                .collect(Collectors.toList());
        campDto.setJogos(jogosIds);

        return campDto;
    }

    public CampeonatoDto createCampeonato(CampeonatoDto campeonatoDto) {
        if (campeonatoDto == null) {
            throw new IllegalArgumentException("CampeonatoDto nao pode ser nulo");
        }
        if (campeonatoDto.getNome() == null || campeonatoDto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do campeonato eh obrigatorio");
        }
        if (campeonatoRepository.findByNome(campeonatoDto.getNome()) != null) {
            throw new RuntimeException("Campeonato com o mesmo nome ja existe");
        }


        Campeonato campeonato = new Campeonato();
        campeonato.setNome(campeonatoDto.getNome());

        if (campeonatoDto.getEquipas() != null && !campeonatoDto.getEquipas().isEmpty()) {

            //para evitar colocar a mesma equipa 2 vezes no json :D
            List<Long> equipasUnicas = campeonatoDto.getEquipas().stream()
                .distinct()
                .collect(Collectors.toList());
            for (Long equipaId : equipasUnicas) {
                Optional<Equipa> equipaOptional = equipaRepository.findById(equipaId);
                if (equipaOptional.isPresent()) {
                    //como foi feita a adiçao da equipa
                    //usar a funcao addEquipa que adiciona ah lista de equipas, cria uma pontuacao a 0
                    //e coloca essa pontuacao na lista de classificacao
                    

                    //verificar se equipa ja pertence a campeonato
                    //previnir colocar duas vezes o mesmo número
                    campeonato.addEquipa(equipaOptional.get());
                    
                } else {
                    System.out.println("Equipa com ID " + equipaId + " nao encontrada.");
                }
            }
        }

        //parte de jogos
        //verificar para cada jogo se ambas as equipas estao presentes no campeonato
        //utilizar ids de jogos e de equipas ambos do dto?? talvez
        //

        //atualizar conquistas de todas as equipas que entraram

        List<Equipa> equipas = campeonato.getEquipas();
        for(Equipa equipa : equipas){
            equipa.addConquista(campeonato);
        }

        Campeonato savedCamp = campeonatoRepository.save(campeonato);
        campeonatoDto.setId(savedCamp.getId());
        return campeonatoDto;
    }

    CampeonatoDto getCampeonatoById(Long id) {
        Optional<Campeonato> campeonatoOptional = campeonatoRepository.findById(id);
        if (campeonatoOptional.isEmpty()) {
            throw new IllegalArgumentException("Campeonato com ID " + id + " não encontrado");
        }
        return this.mapToDto(campeonatoOptional.get());
    }


    public List<CampeonatoDto> getAllCampeonatos() {
        List<Campeonato> camps = campeonatoRepository.findAll();  
        System.out.println("Campeonatos: " + camps);
    
        return camps.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CampeonatoDto getCampeonatoByNome(String nome) {
        List<Campeonato> campList = campeonatoRepository.findAll();

        return campList.stream()
            .filter(c -> c.getNome().equalsIgnoreCase(nome))
            .findFirst()
            .map(campeonato -> mapToDto(campeonato))
            .orElse(null);
    }

    public List<CampeonatoDto> getCampJogosRealizados(Long x) {
        return campeonatoRepository.findAll().stream()
            .filter(camp -> camp.getJogos().stream()
                .filter(jogo -> jogo.getResultado() != null)
                .count() == x)
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    public List<CampeonatoDto> getCampJogosPorRealizar(Long x) {
        return campeonatoRepository.findAll().stream()
            .filter(camp -> camp.getJogos().stream()
                .filter(jogo -> jogo.getResultado() == null)
                .count() == x)
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    public CampeonatoDto adicionaEquipa(Long equipaId, Long campId) {
        if (campId == null || campId <= 0) {
            throw new IllegalArgumentException("O id do campeonato não é válido.");
        }
        Optional<Campeonato> campOptional = campeonatoRepository.findById(campId);
        if (campOptional.isEmpty()) {
            throw new RuntimeException("campeonato não encontrado");
        }
        Campeonato camp = campOptional.get();
        if (equipaId == null || equipaId <= 0) {
            throw new IllegalArgumentException("O id da equipa não é válido.");
        }
        Optional<Equipa> equipaOptional = equipaRepository.findById(equipaId);
        if (equipaOptional.isEmpty()) {
            throw new RuntimeException("Equipa não encontrada");
        }
        Equipa equipa = equipaOptional.get();
        if (camp.getEquipas().contains(equipa)) {
            throw new RuntimeException("Equipa já pertence ao campeonato");
        }
        List<Equipa> equipas = camp.getEquipas();
        List<Pontuacao> classificacao = camp.getClassificacao();
        Pontuacao pontuacaoEquipa = new Pontuacao();
        pontuacaoEquipa.setPontos(0L);
        pontuacaoEquipa.setEquipa(equipa);
        classificacao.add(pontuacaoEquipa);

        equipas.add(equipa);
        camp.setEquipas(equipas);
        camp.setClassificacao(classificacao);
        Campeonato updatedCamp = campeonatoRepository.save(camp);

        return mapToDto(updatedCamp);
    }

    public CampeonatoDto removeEquipa(Long equipaId, Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O id do campeonato não é válido.");
        }
        Optional<Campeonato> campOptional = campeonatoRepository.findById(id);
        if (campOptional.isEmpty()) {
            throw new RuntimeException("campeonato não encontrado");
        }
        Campeonato camp = campOptional.get();
        if (equipaId == null || equipaId <= 0) {
            throw new IllegalArgumentException("O id da equipa não é válido.");
        }
        Optional<Equipa> equipaOptional = equipaRepository.findById(equipaId);
        if (equipaOptional.isEmpty()) {
            throw new RuntimeException("Equipa não encontrada");
        }
        Equipa equipa = equipaOptional.get();
        if (!camp.getEquipas().contains(equipa)) {
            throw new RuntimeException("Equipa nao pertence ao campeonato");
        }
        
        List<Equipa> equipas = camp.getEquipas();
        equipas.remove(equipa);
        camp.setEquipas(equipas);
        Campeonato updatedCamp = campeonatoRepository.save(camp);

        return mapToDto(updatedCamp);
    }

    public CampeonatoDto atualizarNome(Long id, String novoNome) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O id não é válido.");
        }

        if (novoNome == null || novoNome.trim().isEmpty()) {
            throw new IllegalArgumentException("O novo nome nao pode ser nulo ou vazio.");
        }

        Campeonato camp = campeonatoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipa não encontrada"));

        camp.setNome(novoNome);
        Campeonato updatedCamp = campeonatoRepository.save(camp);

        return mapToDto(updatedCamp);
    }

    public List<CampeonatoDto> getCampsPorEquipa(Long equipaId) {
        //get all campeonatos
        //verificar nas equipas deles
        //quais têm equipa X como participante
        //guardar esses camps 
        //retornar...
        if (equipaId == null || equipaId <= 0) {
            throw new IllegalArgumentException("O id da equipa não é válido.");
        }
        Optional<Equipa> equipaOptional = equipaRepository.findById(equipaId);
        if (equipaOptional.isEmpty()) {
            throw new RuntimeException("Equipa não encontrada");
        }
        List<Campeonato> campeonatos = campeonatoRepository.findAll();
        List<CampeonatoDto> campeonatosPorEquipa = campeonatos.stream()
            .filter(camp -> camp.getEquipas().stream()
                .anyMatch(e -> e.getId().equals(equipaId)))
            .map(this::mapToDto)
            .collect(Collectors.toList());
        if (campeonatosPorEquipa.isEmpty()) {
            throw new RuntimeException("Equipa não participa em nenhum campeonato");
        }
        return campeonatosPorEquipa;
    }

    public CampeonatoDto comecarCampeoanto(Long id){
        Optional<Campeonato> campeonatoOptional = campeonatoRepository.findById(id);
        if (campeonatoOptional.isEmpty()) {
            throw new IllegalArgumentException("Campeonato com ID " + id + " não encontrado");
        }
        Campeonato campeonato = campeonatoOptional.get();
        campeonato.setEstado(Campeonato_Estado.EM_CURSO);
        Campeonato campeonatoSaved = campeonatoRepository.save(campeonato);
        return this.mapToDto(campeonatoSaved);
    }

    public CampeonatoDto terminarCampeonato(Long id){
                Optional<Campeonato> campeonatoOptional = campeonatoRepository.findById(id);
        if (campeonatoOptional.isEmpty()) {
            throw new IllegalArgumentException("Campeonato com ID " + id + " não encontrado");
        }
        Campeonato campeonato = campeonatoOptional.get();
        campeonato.setEstado(Campeonato_Estado.TERMINADO);
        Campeonato campeonatoSaved = campeonatoRepository.save(campeonato);
        return this.mapToDto(campeonatoSaved);
    }


}
