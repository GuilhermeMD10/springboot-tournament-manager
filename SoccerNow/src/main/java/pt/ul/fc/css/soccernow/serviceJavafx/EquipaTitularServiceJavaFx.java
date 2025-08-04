package pt.ul.fc.css.soccernow.serviceJavafx;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.soccernow.dtoJavafx.EquipaTitularCreateDtoJavaFx;
import pt.ul.fc.css.soccernow.dtoJavafx.EquipaTitularDtoJavaFx;
import pt.ul.fc.css.soccernow.model.Equipa;
import pt.ul.fc.css.soccernow.model.EquipaTitular;
import pt.ul.fc.css.soccernow.model.Jogador;
import pt.ul.fc.css.soccernow.repository.EquipaRepository;
import pt.ul.fc.css.soccernow.repository.EquipaTitularRepository;
import pt.ul.fc.css.soccernow.repository.JogadorRepository;

@Service
public class EquipaTitularServiceJavaFx {
    

    @Autowired
    private EquipaTitularRepository equipaTitularRepository;

    @Autowired
    private EquipaRepository equipaRepository;

    @Autowired
    private JogadorRepository jogadorRepository;

    /**
     * Converte um objeto{@link EquipaTitular} para um {@link EquipaTitularDtoJavaFx}.
     * 
     * @param equipaTitular a equipaTitular a ser convertida
     * @return o DTO correspondente ah equipa titular fornecida
     */
    public EquipaTitularDtoJavaFx mapToDto(EquipaTitular equipaTitular){
        EquipaTitularDtoJavaFx equipaTitularDto = new EquipaTitularDtoJavaFx();
        equipaTitularDto.setId(equipaTitular.getId());
        equipaTitularDto.setEquipa(equipaTitular.getEquipa().getId());
        List<Long> jogadoresDtos = equipaTitular.getJogadores().stream().map(Jogador::getId)
            .collect(Collectors.toList());
        equipaTitularDto.setJogadores(jogadoresDtos);
        equipaTitularDto.setGuardaRedes(equipaTitular.getGuardaRedes().getId());
        return equipaTitularDto;
    }

    /**
     * Cria uma nova equipa titular com base num {@link EquipaTitularDtoJavaFx}.
     *
     * @param equipaTitularDto o DTO com os dados da equipa titular
     * @return o DTO da equipaTitular criada, com o ID atribuído
     * @throws IllegalArgumentException se o DTO ou o nome for inválido
     */
    public EquipaTitularDtoJavaFx createEquipaTitular(EquipaTitularCreateDtoJavaFx equipaTitularDto){
        if(equipaTitularDto == null){
            throw new IllegalArgumentException("Equipa titular nao pode ser nula");
        }
        if(equipaTitularDto.getEquipa() == null){
            throw new IllegalStateException("A equipa titular deve ter uma equipa associada.");
        }
        if(equipaTitularDto.getJogadores() == null || equipaTitularDto.getJogadores().size() != 5){
            throw new IllegalStateException("A equipa titular deve ter exatamente 5 jogadores.");
        }
        if (equipaTitularDto.getGuardaRedes() == null || !equipaTitularDto.getJogadores().contains(equipaTitularDto.getGuardaRedes())) {
            throw new IllegalStateException("O guarda-redes deve ser um dos 5 jogadores.");
        }
        //Buscar dos repositorios das outras entidades a equipa e os jogadores(com guardaredes)
        Optional<Equipa> equipaOptional = equipaRepository.findById(equipaTitularDto.getEquipa());
        if (equipaOptional.isEmpty()) {
            throw new RuntimeException("Equipa não encontrada.");
        }
        List<Jogador> jogadores = jogadorRepository.findAllById(equipaTitularDto.getJogadores());
        if (jogadores.size() != 5) {
            throw new RuntimeException("Nem todos os jogadores foram encontrados.");
        }
        // Check if all players belong to the given equipaId
        Long equipaId = equipaTitularDto.getEquipa(); // adjust this based on your DTO
        boolean allFromEquipa = jogadores.stream()
            .allMatch(j -> j.getEquipas().stream().anyMatch(e -> e.getId().equals(equipaId)));

        if (!allFromEquipa) {
            throw new RuntimeException("Nem todos os jogadores pertencem à equipa fornecida.");
        }
        Optional<Jogador> guardaRedesOptional = jogadores.stream()
            .filter(j -> j.getId().equals(equipaTitularDto.getGuardaRedes()))
            .findFirst();
        if (guardaRedesOptional.isEmpty()) {
            throw new RuntimeException("Guarda-redes não está na lista de jogadores");
        }

        EquipaTitular equipaTitular = new EquipaTitular();
        equipaTitular.setEquipa(equipaOptional.get());
        equipaTitular.setJogadores(jogadores);
        equipaTitular.setGuardaRedes(guardaRedesOptional.get());

        EquipaTitular savedEquipaTitular = equipaTitularRepository.save(equipaTitular);

        return mapToDto(savedEquipaTitular);
    }

    public EquipaTitularDtoJavaFx getEquipaTitularById(Long id){
        Optional<EquipaTitular> equipaTitularOptional = equipaTitularRepository.findById(id);
        if (equipaTitularOptional.isEmpty()) {
            return null;
        }
        return mapToDto(equipaTitularOptional.get());
    }

    public List<EquipaTitularDtoJavaFx> getEquipasTitularesByEquipa(Long equipa_id) {
        return equipaTitularRepository.findByEquipaId(equipa_id)
                    .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public EquipaTitularDtoJavaFx saveEquipaTitular(EquipaTitular equipaTitular){
        return mapToDto(equipaTitularRepository.save(equipaTitular));
    }

    public List<EquipaTitularDtoJavaFx> getAllEquipaTitular() {
        return equipaTitularRepository.findAll()
                    .stream().map(this::mapToDto).toList(); 
    }


}
