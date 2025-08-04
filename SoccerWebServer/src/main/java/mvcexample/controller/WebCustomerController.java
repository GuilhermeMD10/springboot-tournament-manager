package mvcexample.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import mvcexample.business.dto.ArbitroDto;
import mvcexample.business.dto.CampeonatoDto;
import mvcexample.business.dto.EquipaDto;
import mvcexample.business.dto.EquipaTitularDtoWithJogadores;
import mvcexample.business.dto.JogadorDto;
import mvcexample.business.dto.JogoDto;
import mvcexample.business.dto.UtilizadorDto;
import mvcexample.business.services.ApplicationException;


@Controller
public class WebCustomerController {
	
    private static final String BASE_URL = "http://localhost:8080/api";
    private static final HttpClient client = HttpClient.newHttpClient();

    Logger logger = LoggerFactory.getLogger(WebCustomerController.class);



    public WebCustomerController() {
        super();
    }

//    @GetMapping({ "/", "/customers" })
//    public String index(final Model model) {
//        model.addAttribute("customers", customerService.getCustomers());
//        return "customer_list";
//    }
    
	@GetMapping({ "/" })
	public String index(final Model model) {
	    return "login";
	}
	
	@GetMapping("/login")
	public String login(final Model model, @RequestParam String email_auth, HttpSession session) {

	    String uri = "http://localhost:8080/api/utilizadores/por-email?email=" + URLEncoder.encode(email_auth, StandardCharsets.UTF_8);
	    
	    try {
	        HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(uri))
	            .GET()
	            .build();

	        HttpClient client = HttpClient.newHttpClient(); // Make sure to instantiate it
	        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
	        if (response.statusCode() >= 400) {
	            model.addAttribute("error", "Utilizador não encontrado.");
	            return "login";
	        }

	        ObjectMapper mapper = new ObjectMapper();
	        UtilizadorDto user = mapper.readValue(response.body(), new TypeReference<UtilizadorDto>() {});
	        
	        // Mock login: save user to session
	        session.setAttribute("user", user);
	        return "redirect:/home";

	    } catch (IOException | InterruptedException e) {
	        model.addAttribute("error", "Erro na autenticação.");
	        return "login";
	    }
	}
	
    @GetMapping("/home")
    public String homePage(final Model model) {
        return "home";
    }


    
    @GetMapping("/jogadores")
    public String jogadores(Model model) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/jogadores"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        // Parse JSON string to list of JogadorDto
        ObjectMapper mapper = new ObjectMapper();
        List<JogadorDto> jogadores = mapper.readValue(
            response.body(),
            new TypeReference<List<JogadorDto>>() {}
        );
        
        model.addAttribute("jogadores", jogadores);
        return "jogadores_list";
    }
    
    @GetMapping("/jogadores/nome")
    public String filtrarPorNome(@RequestParam String nome, Model model) throws IOException, InterruptedException {
        String uri = BASE_URL + "/jogadores/nome/" + nome;
        List<JogadorDto> jogadores = fetchJogadoresFromUri(uri);
        model.addAttribute("jogadores", jogadores);
        return "jogadores_list";
    }

    @GetMapping("/jogadores/posicao")
    public String filtrarPorPosicao(@RequestParam String posicao, Model model) throws IOException, InterruptedException {
    	if(posicao.equals("")) {return "redirect:/jogadores";}
    	String uri = BASE_URL + "/jogadores/filtro/por-posicao?prefPos=" + posicao;
        List<JogadorDto> jogadores = fetchJogadoresFromUri(uri);
        model.addAttribute("jogadores", jogadores);
        return "jogadores_list";
    }

    @GetMapping("/jogadores/golos")
    public String filtrarGolos(
            @RequestParam String golosFiltro,           // "minmax", "top5", "x"
            @RequestParam(required = false) String minOrMaxGolos, // "min" or "max"
            @RequestParam(required = false) Integer golosMin,
            Model model) throws IOException, InterruptedException {

        String uri;
        if (golosFiltro.equals("top5")) {
            uri = BASE_URL + "/jogadores/top5/golos/mais";
        } else if (golosFiltro.equals("golos")) {
            uri = BASE_URL + "/jogadores/filtro/golos?golos=" + golosMin;
        } else { // "minmax"
        	golosMin += (minOrMaxGolos.equals("maior-que")) ?  -1 : 1;
            uri = BASE_URL + "/jogadores/filtro/golos/" + minOrMaxGolos + "?golos=" + golosMin;
        }

        List<JogadorDto> jogadores = fetchJogadoresFromUri(uri);
        model.addAttribute("jogadores", jogadores);
        return "jogadores_list";
    }

    @GetMapping("/jogadores/cartoes")
    public String filtrarCartoes(
            @RequestParam String tipoCartao,           // "amarelos", "vermelhos", "cartoes"
            @RequestParam String filtroCartao,         // "minmax", "top5", "x"
            @RequestParam(required = false) String minOrMaxCartao, // "min" or "max" (only for minmax)
            @RequestParam(required = false) Integer valorCartao,   // null for top5
            Model model) throws IOException, InterruptedException {

        List<JogadorDto> jogadores;

        // Determine URI based on card type(s), filter, and value
        if (filtroCartao.equals("top5")) {
            switch (tipoCartao) {
                case "amarelos":
                    jogadores = fetchJogadoresFromUri(BASE_URL + "/jogadores/top5/cartoes-amarelos/mais");
                    break;
                case "vermelhos":
                    jogadores = fetchJogadoresFromUri(BASE_URL + "/jogadores/top5/cartoes-vermelhos/mais");
                    break;
                case "cartoes":
                    jogadores = fetchJogadoresFromUri(BASE_URL + "/jogadores/top5/total-cartoes/mais");
                    break;
                default:
                    jogadores = List.of();
            }
        } else if (filtroCartao.equals("x")) {
        	jogadores = fetchJogadoresFromUri(BASE_URL + "/jogadores/filtro/" + tipoCartao + "?" + tipoCartao + "=" + valorCartao);
        } else if (filtroCartao.equals("minmax")) {
        	valorCartao += (minOrMaxCartao.equals("maior-que")) ?  -1 : 1;
            jogadores = fetchJogadoresFromUri(BASE_URL + "/jogadores/filtro/" + tipoCartao + "/" + minOrMaxCartao + "?" + tipoCartao + "=" + valorCartao);
        } else {
            jogadores = List.of(); // fallback
        }

        model.addAttribute("jogadores", jogadores);
        return "jogadores_list";
    }

    @GetMapping("/jogadores/jogos")
    public String filtrarJogos(
            @RequestParam String jogosFiltro,           // "minmax", "top5", "x"
            @RequestParam(required = false) String minOrMaxJogos, // "min (maior-que)" or "max (menor-que)"
            @RequestParam(required = false) Integer jogosMin,
            Model model) throws IOException, InterruptedException {

        String uri;
        if (jogosFiltro.equals("top5")) {
            uri = BASE_URL + "/jogadores/top5/mais-partidas";
        } else if (jogosFiltro.equals("xjogos")) {
            uri = BASE_URL + "/jogadores/filtro/partidas?partidas=" + jogosMin;
        } else { // "minmax"
        	jogosMin += (minOrMaxJogos.equals("maior-que")) ?  -1 : 1;
            uri = BASE_URL + "/jogadores/filtro/golos/" + minOrMaxJogos + "?golos=" + jogosMin;
        }

        List<JogadorDto> jogadores = fetchJogadoresFromUri(uri);
        model.addAttribute("jogadores", jogadores);
        return "jogadores_list";
    }


    // Helper method to avoid repetition
    private List<JogadorDto> fetchJogadoresFromUri(String uri) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if response status is not OK (e.g., 404 or 500)
            if (response.statusCode() >= 400) {
                return List.of(); // Return empty list on error
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<List<JogadorDto>>() {});
        } catch (IOException | InterruptedException e) {
            // Log error if needed
            return List.of(); // Return empty list on exception
        }
    }
    private List<EquipaDto> fetchEquipasFromUri(String uri) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                return List.of();
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<List<EquipaDto>>() {});
        } catch (IOException | InterruptedException e) {
            return List.of();
        }
    }
    // Helper method to avoid repetition
    private List<ArbitroDto> fetchArbitrosFromUri(String uri) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if response status is not OK (e.g., 404 or 500)
            if (response.statusCode() >= 400) {
                return List.of(); // Return empty list on error
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<List<ArbitroDto>>() {});
        } catch (IOException | InterruptedException e) {
            // Log error if needed
            return List.of(); // Return empty list on exception
        }
    }
    
    private List<CampeonatoDto> fetchCampeonatosFromUri(String uri) throws JsonProcessingException {
        HttpResponse<String> response;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                return List.of(); // error response
            }

            ObjectMapper mapper = new ObjectMapper();

            try {
                // First try to map to a list
                return mapper.readValue(response.body(), new TypeReference<List<CampeonatoDto>>() {});
            } catch (JsonMappingException e) {
                // If that fails, try mapping to a single object
                CampeonatoDto single = mapper.readValue(response.body(), CampeonatoDto.class);
                return List.of(single); // wrap single object in list
            }

        } catch (IOException | InterruptedException e) {
            // handle exceptions like request failure
            return List.of();
        }
    }
    
    
    private List<JogoDto> fetchJogosFromUri(String uri) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                return List.of();
            }

            ObjectMapper mapper = new ObjectMapper();
            // Register module to support Java 8 date/time types like LocalDate
            mapper.registerModule(new JavaTimeModule());


            return mapper.readValue(response.body(), new TypeReference<List<JogoDto>>() {});
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    @GetMapping("/arbitros")
    public String arbitros(final Model model) throws IOException, InterruptedException{
    	HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/arbitros"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Parse JSON string to list of JogadorDto
        ObjectMapper mapper = new ObjectMapper();
        List<ArbitroDto> arbitros = mapper.readValue(
            response.body(),
            new TypeReference<List<ArbitroDto>>() {}
        );

    	//Buscar com chamada HTTP uma Lista de Dtos de equipas
    	model.addAttribute("arbitros", arbitros);
        return "arbitros_list"; //a pagina HTML do template
    }
    
    @GetMapping("/arbitros/nome")
    public String filtrarArbitrosPorNome(@RequestParam String nome, Model model) throws IOException, InterruptedException {
        String uri = BASE_URL + "/arbitros/by-nome/" + nome;
        List<ArbitroDto> arbitros = fetchArbitrosFromUri(uri);
        model.addAttribute("arbitros", arbitros);
        return "arbitros_list";
    }
    
    @GetMapping("/arbitros/jogos")
    public String filtrarArbitrosByJogos(
            @RequestParam String filtroJogos,           // X ; minmax; mais
            @RequestParam(required = false) String minOrMaxJogos, // min (maior-que) max (menor-que)
            @RequestParam(required = false) Integer numJogos, 
            Model model) throws IOException, InterruptedException {

        String uri;
        if (filtroJogos.equals("mais")) {
            uri = BASE_URL + "/arbitros/mais-jogos";
        } else if (filtroJogos.equals("x")) {
            uri = BASE_URL + "/arbitros/por-jogos?partidas=" + numJogos;
        } else { // "minmax"
        	numJogos += (minOrMaxJogos.equals("maior-que"))? -1 : 1;
            uri = BASE_URL + "/arbitros/jogos-" + minOrMaxJogos + "?partidas=" + numJogos;
        }

        List<ArbitroDto> arbitros = fetchArbitrosFromUri(uri);
        model.addAttribute("arbitros", arbitros);
        return "arbitros_list";
    }
    
    @GetMapping("/arbitros/cartoes")
    public String filtrarArbitrosByCartoes(
            @RequestParam String filtroCartoes,           // X ; minmax; mais
            @RequestParam(required = false) String minOrMaxCartoes, // min (maior-que) max (menor-que)
            @RequestParam(required = false) Integer numCartoes, 
            Model model) throws IOException, InterruptedException {

        String uri;
        if (filtroCartoes.equals("mais")) {
            uri = BASE_URL + "/arbitros/mais-cartoes";
        } else if (filtroCartoes.equals("x")) {
            uri = BASE_URL + "/arbitros/por-cartoes?cartoes=" + numCartoes;
        } else { // "minmax"
        	numCartoes += (minOrMaxCartoes.equals("maior-que"))? -1 : 1;
            uri = BASE_URL + "/arbitros/cartoes-" + minOrMaxCartoes + "?cartoes=" + numCartoes;
        }
        List<ArbitroDto> arbitros = fetchArbitrosFromUri(uri);
        model.addAttribute("arbitros", arbitros);
        return "arbitros_list";
    }
    
    @GetMapping("/equipas")
    public String equipas(final Model model) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/equipas"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        List<EquipaDto> equipas = mapper.readValue(response.body(), new TypeReference<List<EquipaDto>>() {});
        List<EquipaTitularDtoWithJogadores> equipasComJogadores = mapToEquipasWithJogadores(equipas);

        model.addAttribute("equipas", equipasComJogadores);
        return "equipas_list";
    }
    
    @GetMapping("/equipas/nome")
    public String filtrarEquipasPorNome(@RequestParam String nome, Model model) throws IOException, InterruptedException {
        String uri = BASE_URL + "/equipas/search?nome=" + nome;
        List<EquipaDto> equipas = fetchEquipasFromUri(uri);
        List<EquipaTitularDtoWithJogadores> equipasComJogadores = mapToEquipasWithJogadores(equipas);

        model.addAttribute("equipas", equipasComJogadores);
        return "equipas_list";
    }
    
    @GetMapping("/equipas/jogadores")
    public String filtrarJogadores(
            @RequestParam String jogadoresFiltro,
            @RequestParam(required = false) String minOrMaxJogadores,
            @RequestParam(required = false) Integer jogadoresMin,
            Model model) throws IOException, InterruptedException {

        String uri;
        if (jogadoresFiltro.equals("xjogadores")) {
            uri = BASE_URL + "/equipas/plantel?x=" + jogadoresMin;
        } else {
            uri = BASE_URL + "/equipas/" + minOrMaxJogadores + "?x=" + jogadoresMin;
        }

        List<EquipaDto> equipas = fetchEquipasFromUri(uri);
        List<EquipaTitularDtoWithJogadores> equipasComJogadores = mapToEquipasWithJogadores(equipas);

        model.addAttribute("equipas", equipasComJogadores);
        return "equipas_list";
    }
    
    @GetMapping("/equipas/estatisticas")
    public String filtrarEquipasPorEstatisticas(
            @RequestParam String tipoEstatistica,
            @RequestParam(required = false) Integer valorEstatistica,
            Model model) throws IOException, InterruptedException {

        String uri = BASE_URL + "/equipas/" + tipoEstatistica + "?x=" + valorEstatistica;
        List<EquipaDto> equipas = fetchEquipasFromUri(uri);
        List<EquipaTitularDtoWithJogadores> equipasComJogadores = mapToEquipasWithJogadores(equipas);

        model.addAttribute("equipas", equipasComJogadores);
        return "equipas_list";
    }
    
    @GetMapping("/equipas/campeonato")
    public String filtrarEquipasPorCampeonato(
            @RequestParam String nomeConquista,
            Model model) throws IOException, InterruptedException {

        String uri = BASE_URL + "/equipas/conquista_nome?campNome=" + nomeConquista;
        List<EquipaDto> equipas = fetchEquipasFromUri(uri);
        List<EquipaTitularDtoWithJogadores> equipasComJogadores = mapToEquipasWithJogadores(equipas);

        model.addAttribute("equipas", equipasComJogadores);
        return "equipas_list";
    }
    @GetMapping("/equipas/ausencia-posicao")
    public String filtrarEquipasPorPosicaoEmFalta(
            @RequestParam String posicao,
            Model model) throws IOException, InterruptedException {

        String uri = BASE_URL + "/equipas/falta_pos?pos=" + posicao;
        List<EquipaDto> equipas = fetchEquipasFromUri(uri);
        System.out.println(equipas);
        List<EquipaTitularDtoWithJogadores> equipasComJogadores = mapToEquipasWithJogadores(equipas);
        System.out.println(equipasComJogadores);

        model.addAttribute("equipas", equipasComJogadores);
        return "equipas_list";
    }
    
    @GetMapping("/registar_resultados")
    public String registarResultados(final Model model) {
    	//Buscar com chamada HTTP uma Lista de Dtos de campeonatos
    	//model.addAttribute("campeonatos", null);
        return "registar_resultados"; //a pagina HTML do template
    }
    
    @GetMapping("/campeonatos")
    public String campeonatos(final Model model) throws IOException, InterruptedException {
    	HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/campeonatos"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Parse JSON string to list of JogadorDto
        ObjectMapper mapper = new ObjectMapper();
        List<CampeonatoDto> campeonatos = mapper.readValue(
            response.body(),
            new TypeReference<List<CampeonatoDto>>() {}
        );
    	//Buscar com chamada HTTP uma Lista de Dtos de campeonatos
    	model.addAttribute("campeonatos", campeonatos);
        return "campeonatos_list"; //a pagina HTML do template
    }
    
    @GetMapping("/campeonatos/nome")
    public String filtrarCampeonatosPorNome(@RequestParam String nome, Model model) throws IOException, InterruptedException {
        String uri = BASE_URL + "/campeonatos/nome?nome=" + nome;
        List<CampeonatoDto> campeonatos = fetchCampeonatosFromUri(uri);
        model.addAttribute("campeonatos", campeonatos);
        return "campeonatos_list";
    }
    
    @GetMapping("/campeonatos/equipa")
    public String filtrarCampeonatosPorEquipa(@RequestParam String equipaId, Model model) throws IOException, InterruptedException {
        String uri = BASE_URL + "/campeonatos/PLACEHOLDER";
        List<CampeonatoDto> campeonatos = fetchCampeonatosFromUri(uri);
        model.addAttribute("campeonatos", campeonatos);
        return "campeonatos_list";
    }
    
    @GetMapping("/campeonatos/jogos-realizados")
    public String filtrarCampeonatosPorJogosRealizados(@RequestParam String jogosRealizados, Model model) throws IOException, InterruptedException {
        String uri = BASE_URL + "/campeonatos/realizados?x=" + jogosRealizados;
        List<CampeonatoDto> campeonatos = fetchCampeonatosFromUri(uri);
        model.addAttribute("campeonatos", campeonatos);
        return "campeonatos_list";
    }
    
    @GetMapping("/campeonatos/jogos-por-realizar")
    public String filtrarCampeonatosPorJogosPorRealizar(@RequestParam String jogosPorRealizar, Model model) throws IOException, InterruptedException {
        String uri = BASE_URL + "/campeonatos/por_realizar?x=" + jogosPorRealizar;
        List<CampeonatoDto> campeonatos = fetchCampeonatosFromUri(uri);
        model.addAttribute("campeonatos", campeonatos);
        return "campeonatos_list";
    }
    
    
    @GetMapping("/jogos")
    public String jogos(Model model) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/jogos"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Register JavaTimeModule
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        List<JogoDto> jogos = mapper.readValue(
            response.body(),
            new TypeReference<List<JogoDto>>() {}
        );

        model.addAttribute("jogos", jogos);
        return "jogos_list";
    }
    
    @GetMapping("/jogos/realizados")
    public String filtrarJogosRealizados(Model model) throws IOException, InterruptedException {
        String uri = BASE_URL + "/jogos/filtar/realizados";
        List<JogoDto> jogos = fetchJogosFromUri(uri);
        model.addAttribute("jogos", jogos);
        return "jogos_list";
    }
    
    @GetMapping("/jogos/porRealizar")
    public String filtrarJogosPorRealizar(Model model) throws IOException, InterruptedException {
        String uri = BASE_URL + "/jogos/filtar/nao_realizados";
        List<JogoDto> jogos = fetchJogosFromUri(uri);
        model.addAttribute("jogos", jogos);
        return "jogos_list";
    }
    
    @GetMapping("/jogos/golos")
    public String filtrarJogosPorGolos(@RequestParam String filtroGolos, //x, top5 , exato resultadoExato golos
    		@RequestParam(required = false) String golosCasa,
    		@RequestParam(required = false) String golosVisitante,
    		@RequestParam(required = false) String golos,
    		Model model) throws IOException, InterruptedException {
        String uri;
        System.out.println("filtroGolos: " + filtroGolos);
        System.out.println("golosCasa: " + golosCasa);
        System.out.println("golosVisitante: " + golosVisitante);
        System.out.println("golos: " + golos);
        if(filtroGolos.equals("x")) {
        	uri = BASE_URL + "/jogos/filtar/golos/" + golos;
        } else if(filtroGolos.equals("top5")) {
        	uri = BASE_URL + "/jogos/filtar/golos/top5";
        } else { //exato
        	uri = BASE_URL + "/jogos/filtar/golos/resultado/" + golosCasa + ":" + golosVisitante;
        }
        
        List<JogoDto> jogos = fetchJogosFromUri(uri);
        model.addAttribute("jogos", jogos);
        return "jogos_list";
    }
    
    @GetMapping("/jogos/local")
    public String filtrarJogosPorLocal(@RequestParam String local, Model model) throws IOException, InterruptedException {
        String encodedLocal = URLEncoder.encode(local, StandardCharsets.UTF_8);
        String uri = BASE_URL + "/jogos/filtar/localização/" + encodedLocal;
        List<JogoDto> jogos = fetchJogosFromUri(uri);
        model.addAttribute("jogos", jogos);
        return "jogos_list";
    }
    
    @GetMapping("/jogos/turno")
    public String filtrarJogosPorTurno(@RequestParam String turno, Model model) throws IOException, InterruptedException {
        String uri = BASE_URL + "/jogos/filtar/horario?turno=" + turno;
        List<JogoDto> jogos = fetchJogosFromUri(uri);
        model.addAttribute("jogos", jogos);
        return "jogos_list";
    }

    
    @PostMapping("/resultados/save")
    public String salvarResultado(
            @RequestParam Long jogoId,
            @RequestParam(required = false) List<Long> golosVisitado,
            @RequestParam(required = false) List<Long> golosVisitante,
            @RequestParam(required = false) List<Long> amarelosVisitado,
            @RequestParam(required = false) List<Long> vermelhosVisitado,
            @RequestParam(required = false) List<Long> amarelosVisitante,
            @RequestParam(required = false) List<Long> vermelhosVisitante,
            @RequestParam Long vencedor, // You need to collect this from the form too
            Model model
    ) throws IOException, InterruptedException {

        // Build the DTO
        Map<String, Object> resultadoMap = new HashMap<>();
        resultadoMap.put("golosVisitado", golosVisitado != null ? golosVisitado : List.of());
        resultadoMap.put("golosVisitante", golosVisitante != null ? golosVisitante : List.of());
        resultadoMap.put("amarelosVisitado", amarelosVisitado != null ? amarelosVisitado : List.of());
        resultadoMap.put("vermelhosVisitado", vermelhosVisitado != null ? vermelhosVisitado : List.of());
        resultadoMap.put("amarelosVisitante", amarelosVisitante != null ? amarelosVisitante : List.of());
        resultadoMap.put("vermelhosVisitante", vermelhosVisitante != null ? vermelhosVisitante : List.of());
        resultadoMap.put("vencedor", vencedor);

        // Convert to JSON
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(resultadoMap);
        System.out.println(requestBody);

        // Send request
        String uri = BASE_URL + "/resultados/create/" + jogoId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            System.err.println("Failed to save resultado: " + response.body());
            return "error"; // or return to the same form with an error message
        }

        return "redirect:/jogos";
    }
    
    private EquipaTitularDtoWithJogadores mapToEquipaWithJogadores(EquipaDto equipaDto) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        List<JogadorDto> jogadores = new ArrayList<>();

        for (Long jogadorId : equipaDto.getPlantel()) {
            HttpRequest jogadorRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/jogadores/" + jogadorId))
                    .GET()
                    .build();

            HttpResponse<String> jogadorResponse = client.send(jogadorRequest, HttpResponse.BodyHandlers.ofString());
            JogadorDto jogadorDto = mapper.readValue(jogadorResponse.body(), JogadorDto.class);
            jogadores.add(jogadorDto);
        }

        EquipaTitularDtoWithJogadores equipaComJogadores = new EquipaTitularDtoWithJogadores();
        equipaComJogadores.setId(equipaDto.getId());
        equipaComJogadores.setNome(equipaDto.getNome());
        equipaComJogadores.setConquistas(equipaDto.getConquistas());
        equipaComJogadores.setPlantel(jogadores);
        return equipaComJogadores;
    }
    
    private List<EquipaTitularDtoWithJogadores> mapToEquipasWithJogadores(List<EquipaDto> equipas) throws IOException, InterruptedException {
        List<EquipaTitularDtoWithJogadores> result = new ArrayList<>();
        for (EquipaDto dto : equipas) {
            result.add(mapToEquipaWithJogadores(dto));
        }
        return result;
    }

}
