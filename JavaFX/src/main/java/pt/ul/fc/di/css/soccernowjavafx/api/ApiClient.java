package pt.ul.fc.di.css.soccernowjavafx.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ul.fc.di.css.soccernowjavafx.dto.ArbitroDto;
import pt.ul.fc.di.css.soccernowjavafx.dto.ArbitroDtoUpdate;
import pt.ul.fc.di.css.soccernowjavafx.dto.JogadorDto;
import pt.ul.fc.di.css.soccernowjavafx.dto.JogadorDtoUpdate;
import pt.ul.fc.di.css.soccernowjavafx.dto.UtilizadorDto;

public class ApiClient {
    
    private static final String BASE_URL = "http://localhost:8080/apiJavafx";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void createUtilizador(UtilizadorDto utilizador) throws Exception {
        String json = mapper.writeValueAsString(utilizador);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/utilizadores"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new RuntimeException("Failed to create utilizador. HTTP code: " + response.statusCode());
        }
    }

    public static void createJogador(JogadorDto jogador) throws Exception {
        String json = mapper.writeValueAsString(jogador);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/jogadores"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new RuntimeException("Failed to create jogador. HTTP code: " + response.statusCode());
        }
    }

    public static void createArbitro(ArbitroDto arbitro) throws Exception {
        String json = mapper.writeValueAsString(arbitro);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/arbitros"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new RuntimeException("Failed to create arbitro. HTTP code: " + response.statusCode());
        }
    }

    public static boolean autenticarUtilizador(String email) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/utilizadores/por-email?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8)))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;

        } catch (Exception e) {
            return false;
        }
    }

    
    public static UtilizadorDto getUtilizador(String email, String nif) throws Exception {
        if ((email == null || email.isEmpty()) && (nif == null || nif.isEmpty())) {
            throw new IllegalArgumentException("Email ou NIF devem ser fornecidos");
        }

        HttpResponse<String> response = null;

        if (email != null && !email.isEmpty()) {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/utilizadores/por-email?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8)))
                .GET()
                .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } else if (nif != null && !nif.isEmpty()) {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/utilizadores/por-nif?nif=" + URLEncoder.encode(nif, StandardCharsets.UTF_8)))
                .GET()
                .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        if (response == null || (response.statusCode() != 200 && response.statusCode() != 201)) {
            throw new RuntimeException("Failed to find utilizador. HTTP code: " + (response != null ? response.statusCode() : "null response"));
        }

        // Faz parse do JSON para DTO
        UtilizadorDto utilizador = mapper.readValue(response.body(), UtilizadorDto.class);

        return utilizador;
    }


    public static JogadorDto getJogador(String email, String nif) throws Exception {
        if ((email == null || email.isEmpty()) && (nif == null || nif.isEmpty())) {
            throw new IllegalArgumentException("Email ou NIF devem ser fornecidos");
        }

        HttpResponse<String> response = null;

        if (email != null && !email.isEmpty()) {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/jogadores/por-email?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8)))
                .GET()
                .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } else if (nif != null && !nif.isEmpty()) {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/jogadores/por-nif?nif=" + URLEncoder.encode(nif, StandardCharsets.UTF_8)))
                .GET()
                .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        if (response == null || (response.statusCode() != 200 && response.statusCode() != 201)) {
            throw new RuntimeException("Failed to find jogador. HTTP code: " + (response != null ? response.statusCode() : "null response"));
        }

        // Faz parse do JSON para DTO
        JogadorDto jogador = mapper.readValue(response.body(), JogadorDto.class);

        return jogador;
    }   

    public static ArbitroDto getArbitro(String email, String nif) throws Exception {
        if ((email == null || email.isEmpty()) && (nif == null || nif.isEmpty())) {
            throw new IllegalArgumentException("Email ou NIF devem ser fornecidos");
        }

        HttpResponse<String> response = null;

        if (email != null && !email.isEmpty()) {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/arbitros/por-email?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8)))
                .GET()
                .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } else if (nif != null && !nif.isEmpty()) {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/arbitros/por-nif?nif=" + URLEncoder.encode(nif, StandardCharsets.UTF_8)))
                .GET()
                .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        if (response == null || (response.statusCode() != 200 && response.statusCode() != 201)) {
            throw new RuntimeException("Failed to find arbitro. HTTP code: " + (response != null ? response.statusCode() : "null response"));
        }

        // Faz parse do JSON para DTO
        ArbitroDto arbitro = mapper.readValue(response.body(), ArbitroDto.class);

        return arbitro;
    }

    public static void deleteRequest(String tipo, Long id) throws Exception {
        String urlTipo;
        if (tipo.equals("Jogador")){
            urlTipo = "jogadores";
        } else if (tipo.equals( "Arbitro")){
            urlTipo = "arbitros";
        } else if (tipo.equals("Utilizador")){
            urlTipo = "utilizadores";
        } else {
            throw new RuntimeException("Erro ao apagar " + tipo + " com ID " + id);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + urlTipo + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 204) {
            throw new RuntimeException("Erro ao apagar " + urlTipo + " com ID " + id + ". Código HTTP: " + response.statusCode());
        }
    }


    public static JogadorDto updateJogador(JogadorDtoUpdate jogadorDtoUpdate) throws Exception {
        String json = mapper.writeValueAsString(jogadorDtoUpdate);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/jogadores/" + jogadorDtoUpdate.getId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to update jogador. HTTP code: " + response.statusCode());
        }

        return mapper.readValue(response.body(), JogadorDto.class);
    }

    public static UtilizadorDto updateUtilizador(UtilizadorDto utilizadorDto) throws Exception {
        String json = mapper.writeValueAsString(utilizadorDto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/utilizadores/" + utilizadorDto.getId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to update utilizador. HTTP code: " + response.statusCode());
        }

        return mapper.readValue(response.body(), UtilizadorDto.class);
    }

    public static ArbitroDto updateArbitro(ArbitroDtoUpdate arbitroDtoUpdate) throws Exception {
        String json = mapper.writeValueAsString(arbitroDtoUpdate);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/arbitros/" + arbitroDtoUpdate.getId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to update arbitro. HTTP code: " + response.statusCode());
        }

        return mapper.readValue(response.body(), ArbitroDto.class);
    }
    
    public static List<JogadorDto> getTodosJogadores() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/jogadores"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new RuntimeException("Falha ao obter lista de jogadores. Código HTTP: " + response.statusCode());
        }

        // Como é uma lista, usamos um TypeReference para deserializar o JSON para List<JogadorDto>
        List<JogadorDto> jogadores = mapper.readValue(response.body(), new TypeReference<List<JogadorDto>>() {});
        return jogadores;
    }


    public static JogadorDto getJogadorById(Long id) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/jogadores/" + id))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new RuntimeException("Falha ao encontrar jogador. Código HTTP: " + response.statusCode());
        }

        JogadorDto jogador = mapper.readValue(response.body(), JogadorDto.class);
        return jogador;
    }
    //get all Arbitros

    public static List<ArbitroDto> getArbitrosDisponiveis() {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/arbitros"))
            .GET()
            .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200 && response.statusCode() != 201) {
                throw new RuntimeException("Falha ao obter lista de árbitros. Código HTTP: " + response.statusCode());
            }

            return mapper.readValue(response.body(), new TypeReference<List<ArbitroDto>>() {});
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erro ao obter árbitros disponíveis", e);
        }
    }

    public static ArbitroDto getArbitroById(Long arbitroId) throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/arbitros/" + arbitroId))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new RuntimeException("Falha ao encontrar jogador. Código HTTP: " + response.statusCode());
        }

        ArbitroDto arbitro = mapper.readValue(response.body(), ArbitroDto.class);
        return arbitro;
    }





}
