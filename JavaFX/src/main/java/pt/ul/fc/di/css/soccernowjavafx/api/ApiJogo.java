package pt.ul.fc.di.css.soccernowjavafx.api;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import pt.ul.fc.di.css.soccernowjavafx.dto.EquipaDto;
import pt.ul.fc.di.css.soccernowjavafx.dto.JogoDto;

public class ApiJogo {
    
    private static final String BASE_URL = "http://localhost:8080/apiJavafx/jogos";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void createJogo(JogoDto jogo) throws Exception {
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(jogo);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new RuntimeException("Failed to create jogo. HTTP code: " + response.statusCode());
        }
    }

    public static List<JogoDto> getJogosCampeonato(Long campId) {
        mapper.registerModule(new JavaTimeModule());
        try {
            HttpClient client = HttpClient.newHttpClient();
            String url = BASE_URL + "/campeonato/" + campId;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), new TypeReference<List<JogoDto>>() {});
            } else {
                System.err.println("Erro ao buscar jogos do campeonato: " + response.statusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static List<JogoDto> getTodosJogos() {
        mapper.registerModule(new JavaTimeModule());
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), new TypeReference<List<JogoDto>>() {});
            } else {
                System.err.println("Erro ao buscar equipas: " + response.statusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static List<JogoDto> getJogosSemResultado() {
        mapper.registerModule(new JavaTimeModule());
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/filtar/nao_realizados"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), new TypeReference<List<JogoDto>>() {});
            } else {
                System.err.println("Erro ao buscar jogos sem resultado: " + response.statusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public static void removerJogo(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        if (status != 200 && status != 204) {
            throw new RuntimeException("Failed to delete jogo. HTTP code: " + status);
        }
    }
}