package pt.ul.fc.css.soccernow.serviceJavafx;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.soccernow.dtoJavafx.CampeonatoDtoJavaFx;
import pt.ul.fc.css.soccernow.dtoJavafx.EquipaDtoJavaFx;
import pt.ul.fc.css.soccernow.model.Campeonato;
import pt.ul.fc.css.soccernow.model.Equipa;
import pt.ul.fc.css.soccernow.model.Jogador;
import pt.ul.fc.css.soccernow.model.Jogo;
import pt.ul.fc.css.soccernow.model.Pontuacao;
import pt.ul.fc.css.soccernow.model.enums.Campeonato_Estado;
import pt.ul.fc.css.soccernow.repository.CampeonatoRepository;
import pt.ul.fc.css.soccernow.repository.EquipaRepository;
import pt.ul.fc.css.soccernow.repository.JogoRepository;

@Service
public class CampeonatoServiceJavaFx {

    @Autowired
    private CampeonatoRepository campeonatoRepository;

    @Autowired 
    private EquipaRepository equipaRepository;

    @Autowired
    private JogoRepository JogoRepository;

    @Autowired
    private JogoRepository jogoRepository;

    private CampeonatoDtoJavaFx mapToDto(Campeonato camp) {
        CampeonatoDtoJavaFx campDto = new CampeonatoDtoJavaFx();

        campDto.setId(camp.getId());
        campDto.setNome(camp.getNome());

        //aqui eu posso ate sacar todas as pontuaçoes e retirar as equipas para as equipas e os pontos para a classificaçao
        List<Long> equipasIds = new ArrayList<>();
        List<Long> classificacao = new ArrayList<>();
        if (camp.getEstado() != null) {
            campDto.setEstado(camp.getEstado());
        } else {
            campDto.setEstado(Campeonato_Estado.POR_COMECAR);
        }
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

    public CampeonatoDtoJavaFx createCampeonato(CampeonatoDtoJavaFx campeonatoDto) {
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

    CampeonatoDtoJavaFx getCampeonatoById(Long id) {
        Optional<Campeonato> campeonatoOptional = campeonatoRepository.findById(id);
        if (campeonatoOptional.isEmpty()) {
            throw new IllegalArgumentException("Campeonato com ID " + id + " não encontrado");
        }
        return this.mapToDto(campeonatoOptional.get());
    }


    public List<CampeonatoDtoJavaFx> getAllCampeonatos() {
        List<Campeonato> camps = campeonatoRepository.findAll();  
        System.out.println("Campeonatos: " + camps);
    
        return camps.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CampeonatoDtoJavaFx getCampeonatoByNome(String nome) {
        List<Campeonato> campList = campeonatoRepository.findAll();

        return campList.stream()
            .filter(c -> c.getNome().equalsIgnoreCase(nome))
            .findFirst()
            .map(campeonato -> mapToDto(campeonato))
            .orElse(null);
    }

    public List<CampeonatoDtoJavaFx> getCampJogosRealizados(Long x) {
        return campeonatoRepository.findAll().stream()
            .filter(camp -> camp.getJogos().stream()
                .filter(jogo -> jogo.getResultado() != null)
                .count() == x)
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    public List<CampeonatoDtoJavaFx> getCampJogosPorRealizar(Long x) {
        return campeonatoRepository.findAll().stream()
            .filter(camp -> camp.getJogos().stream()
                .filter(jogo -> jogo.getResultado() == null)
                .count() == x)
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    public CampeonatoDtoJavaFx adicionaEquipa(Long equipaId, Long campId) {
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

    public CampeonatoDtoJavaFx removeEquipa(Long equipaId, Long id) {
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

    public CampeonatoDtoJavaFx atualizarNome(Long id, String novoNome) {
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

    public List<CampeonatoDtoJavaFx> getCampsPorEquipa(Long equipaId) {
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
        List<CampeonatoDtoJavaFx> campeonatosPorEquipa = campeonatos.stream()
            .filter(camp -> camp.getEquipas().stream()
                .anyMatch(e -> e.getId().equals(equipaId)))
            .map(this::mapToDto)
            .collect(Collectors.toList());
        if (campeonatosPorEquipa.isEmpty()) {
            throw new RuntimeException("Equipa não participa em nenhum campeonato");
        }
        return campeonatosPorEquipa;
    }

    public CampeonatoDtoJavaFx comecarCampeoanto(Long id){
        Optional<Campeonato> campeonatoOptional = campeonatoRepository.findById(id);
        if (campeonatoOptional.isEmpty()) {
            throw new IllegalArgumentException("Campeonato com ID " + id + " não encontrado");
        }
        Campeonato campeonato = campeonatoOptional.get();
        campeonato.setEstado(Campeonato_Estado.EM_CURSO);
        Campeonato campeonatoSaved = campeonatoRepository.save(campeonato);
        return this.mapToDto(campeonatoSaved);
    }

    public CampeonatoDtoJavaFx terminarCampeonato(Long id){
                Optional<Campeonato> campeonatoOptional = campeonatoRepository.findById(id);
        if (campeonatoOptional.isEmpty()) {
            throw new IllegalArgumentException("Campeonato com ID " + id + " não encontrado");
        }
        Campeonato campeonato = campeonatoOptional.get();
        campeonato.setEstado(Campeonato_Estado.TERMINADO);
        Campeonato campeonatoSaved = campeonatoRepository.save(campeonato);
        return this.mapToDto(campeonatoSaved);
    }

    public CampeonatoDtoJavaFx adicionarJogo(Long jogoId, Long id) {
        Optional<Campeonato> campeonatoOptional = campeonatoRepository.findById(id);
        if (campeonatoOptional.isEmpty()) {
            throw new IllegalArgumentException("Campeonato com ID " + id + " não encontrado");
        }
        Campeonato campeonato = campeonatoOptional.get();
        if (jogoId == null || jogoId <= 0) {
            throw new IllegalArgumentException("O id do jogo não é válido.");
        }
        Optional<Jogo> jogoOptional = campeonato.getJogos().stream()
            .filter(jogo -> jogo.getId().equals(jogoId))
            .findFirst();
        if (jogoOptional.isPresent()) {
            throw new RuntimeException("Jogo já pertence ao campeonato");
        }
        Jogo jogo = JogoRepository.findById(jogoId)
            .orElseThrow(() -> new RuntimeException("Jogo com ID " + jogoId + " não encontrado"));

        jogo.setCampeonato(campeonato);
        campeonato.getJogos().add(jogo);
        Campeonato updatedCamp = campeonatoRepository.save(campeonato);
        return mapToDto(updatedCamp);
    }

    public CampeonatoDtoJavaFx removerJogo(Long jogoId, Long id) {
        Optional<Campeonato> campeonatoOptional = campeonatoRepository.findById(id);
        if (campeonatoOptional.isEmpty()) {
            throw new IllegalArgumentException("Campeonato com ID " + id + " não encontrado");
        }
        Campeonato campeonato = campeonatoOptional.get();
        if (jogoId == null || jogoId <= 0) {
            throw new IllegalArgumentException("O id do jogo não é válido.");
        }
        Optional<Jogo> jogoOptional = campeonato.getJogos().stream()
            .filter(jogo -> jogo.getId().equals(jogoId))
            .findFirst();
        if (jogoOptional.isEmpty()) {
            throw new RuntimeException("Jogo não pertence ao campeonato");
        }
        Jogo jogo = jogoOptional.get();
        campeonato.getJogos().remove(jogo);
        Campeonato updatedCamp = campeonatoRepository.save(campeonato);
        return mapToDto(updatedCamp);
    }

    public void removeCampeonato(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O id do campeonato não é válido.");
        }

        Campeonato campeonato = campeonatoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Campeonato não encontrado"));

        for (Equipa equipa : campeonato.getEquipas()) {
            equipa.removeConquista(campeonato);
            equipaRepository.save(equipa); // persistir alteração
        }

        for (Jogo jogo : campeonato.getJogos()) {
            jogo.setCampeonato(null);
            jogoRepository.save(jogo);
        }

        campeonatoRepository.delete(campeonato);
    }


}
