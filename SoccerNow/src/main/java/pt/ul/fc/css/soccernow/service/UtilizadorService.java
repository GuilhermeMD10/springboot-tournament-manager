package pt.ul.fc.css.soccernow.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.soccernow.dto.UtilizadorDto;
import pt.ul.fc.css.soccernow.dto.UtilizadorDtoCreate;
import pt.ul.fc.css.soccernow.model.Utilizador;
import pt.ul.fc.css.soccernow.repository.UtilizadorRepository;

/**
 * Serviço responsável por gerir operações relacionadas com Utilizadores.
 * Inclui métodos para criação, leitura, atualização e remoção, bem como mapeamento para DTOs.
 */
@Service
public class UtilizadorService {

    @Autowired
    private UtilizadorRepository utilizadorRepository;


    /**
     * Converte um objeto {@link Utilizador} para um {@link UtilizadorDto}.
     *
     * @param utilizador o utilizador a ser convertido
     * @return o DTO correspondente ao utilizador fornecido
     */
    private UtilizadorDto mapToDto(Utilizador utilizador){
        UtilizadorDto utilizadorDto = new UtilizadorDto();
        utilizadorDto.setId(utilizador.getId());
        utilizadorDto.setNome(utilizador.getNome());
        utilizadorDto.setEmail(utilizador.getEmail());
        utilizadorDto.setNif(utilizador.getNif());;
        return utilizadorDto;
    }

    /**
     * Cria um novo utilizador com base num {@link UtilizadorDto}.
     *
     * @param utilizadorDto o DTO com os dados do utilizador
     * @return o DTO do utilizador criado, com o ID atribuído
     * @throws IllegalArgumentException se o DTO ou o nome for inválido
     */
    public UtilizadorDto createUtilizador(UtilizadorDtoCreate utilizadorDtoCreate){
        if(utilizadorDtoCreate == null){
            throw new IllegalArgumentException("UtilizadorDto nao pode ser nulo");
        }

        if(utilizadorDtoCreate.getNome() == null || utilizadorDtoCreate.getNome().trim().isEmpty()){
            throw new IllegalArgumentException("Nome do utilizador eh obrigatorio");
        }

        if(utilizadorDtoCreate.getEmail() == null || utilizadorDtoCreate.getEmail().trim().isEmpty()){
            throw new IllegalArgumentException("Email eh obrigatorio");
        }
        //FALTA VERIFICAR O EMAIL SE ESTA NO FORMATO CERTO

        if (!utilizadorDtoCreate.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Formato de email inválido");
        }

        if (!utilizadorDtoCreate.getNif().matches("\\d{9}")) {
            throw new IllegalArgumentException("NIF deve conter exatamente 9 dígitos numéricos.");
        }


        Utilizador utilizador = new Utilizador();
        utilizador.setNome(utilizadorDtoCreate.getNome());
        utilizador.setEmail(utilizadorDtoCreate.getEmail());
        utilizador.setNif(utilizadorDtoCreate.getNif());

        Utilizador savedUtilizador = utilizadorRepository.save(utilizador);
        UtilizadorDto utilizadorDto = this.mapToDto(savedUtilizador);
        return utilizadorDto;
    }



    /**
     * Guarda um objeto {@link Utilizador} diretamente no repositório.
     *
     * @param utilizador o utilizador a guardar
     * @return o DTO do utilizador guardado
     * @throws IllegalArgumentException se o utilizador for nulo
     */
    public UtilizadorDto saveUtilizador(Utilizador utilizador) {
        if(utilizador == null){
            throw new IllegalArgumentException("Utilizador invalido");
        }
        return mapToDto(utilizadorRepository.save(utilizador));
    }

    public UtilizadorDto updateUtilizador (UtilizadorDto utilizadorDto){
        Long id = utilizadorDto.getId();
        validarId(id);
        Optional<Utilizador> utilizadorOptional = utilizadorRepository.findById(id);
        if (utilizadorOptional.isEmpty()){
            throw new RuntimeException("Utilizador não encontrado");
        }

        Utilizador utilizador = utilizadorOptional.get();
        utilizador.setEmail(utilizadorDto.getEmail());
        utilizador.setNif(utilizadorDto.getNif());
        utilizador.setNome(utilizadorDto.getNome());

        Utilizador savedUtilizador = utilizadorRepository.save(utilizador);
        UtilizadorDto utilizadorDtoSaved = this.mapToDto(savedUtilizador);
        return utilizadorDtoSaved;
    }


//----------------------------FUNCOES PARA PROCURAR UTILIZADORES----------------------------//

    public UtilizadorDto procurarPorNif(String nif) {
        Optional<Utilizador> utilizadorOptional = utilizadorRepository.findByNif(nif);
        if (utilizadorOptional.isEmpty()){
            throw new RuntimeException("Utilizador não encontrado");
        }
        return mapToDto(utilizadorOptional.get());
    }

    public UtilizadorDto procurarPorEmail(String email) {
        Optional<Utilizador> utilizadorOptional = utilizadorRepository.findByEmail(email);
        if (utilizadorOptional.isEmpty()){
            throw new RuntimeException("Utilizador não encontrado");
        }
        return mapToDto(utilizadorOptional.get());
    }

    /**
     * Procura um utilizador pelo seu ID.
     *
     * @param id o ID do utilizador
     * @return o DTO correspondente
     * @throws IllegalArgumentException se o ID for inválido
     * @throws RuntimeException se o utilizador não for encontrado
     */
    public UtilizadorDto procurarPorId(Long id) {
        validarId(id);
        Optional<Utilizador> utilizadorOptional = utilizadorRepository.findById(id);
        if (utilizadorOptional.isEmpty()){
            throw new RuntimeException("Utilizador não encontrado");
        }
        return mapToDto(utilizadorOptional.get());
    }

    /**
     * Obtém todos os utilizadores existentes.
     *
     * @return lista de {@link UtilizadorDto} com todos os utilizadores
     */
    public List<UtilizadorDto> getTodosUtilizadores() {
        List<Utilizador> utilizadores = utilizadorRepository.findAll();
        return utilizadores.stream()
                .map(this::mapToDto)
                .toList();
    }

    /**
     * Procura utilizadores pelo nome coincidência parcial (ignorando maiúsculas/minúsculas).
     *
     * @param nome o nome ou parte do nome a procurar
     * @return lista de utilizadores encontrados
     * @throws IllegalArgumentException se o nome for nulo ou vazio
     * @throws RuntimeException se nenhum utilizador for encontrado
     */
    public List<UtilizadorDto> procurarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()){
            throw new IllegalArgumentException("Nome inválido ou vazio");
        }

        String nomeNormalizado = nome.trim().replaceAll("\\s+", " ").toLowerCase();

        List<Utilizador> utilizadores = utilizadorRepository.findAll(); // carregar todos (menos eficiente)

        List<Utilizador> filtrados = utilizadores.stream()
            .filter(u -> u.getNome() != null &&
                        u.getNome().trim().replaceAll("\\s+", " ").toLowerCase().contains(nomeNormalizado))
            .toList();

        if (filtrados.isEmpty()) {
            throw new RuntimeException("Nenhum utilizador encontrado com o nome fornecido.");
        }

        return filtrados.stream()
                .map(this::mapToDto)
                .toList();
    }



//----------------------------FUNCOES PARA ELIMINAR UTILIZADORES----------------------------//

    /**
     * Elimina um utilizador com base no seu ID.
     *
     * @param id o ID do utilizador a eliminar
     * @throws IllegalArgumentException se o ID for inválido
     * @throws RuntimeException se o utilizador não for encontrado
     */
    public void eliminarPorId(Long id) {
        validarId(id);
        if (!utilizadorRepository.existsById(id)) {
            throw new RuntimeException("Utilizador não encontrado");
        }
        utilizadorRepository.deleteById(id);
    }

//----------------------------FUNCOES PARA UPDATE UTILIZADORES----------------------------//

    /**
     * Atualiza o nome de um utilizador com base no seu ID.
     *
     * @param id o ID do utilizador
     * @param novoNome o novo nome a ser atribuído
     * @return o DTO do utilizador atualizado
     * @throws IllegalArgumentException se o ID ou o novo nome forem inválidos
     * @throws RuntimeException se o utilizador não for encontrado
     */
    public UtilizadorDto atualizarNome(Long id, String novoNome) {
        validarId(id);

        if (novoNome == null || novoNome.trim().isEmpty()) {
            throw new IllegalArgumentException("O novo nome nao pode ser nulo ou vazio.");
        }
    
        Utilizador utilizador = utilizadorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));
    
        utilizador.setNome(novoNome);
        Utilizador updatedUtilizador = utilizadorRepository.save(utilizador);
    
        return mapToDto(updatedUtilizador);
    }






//----------------------------FUNCOES AUXILIARES----------------------------//

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
