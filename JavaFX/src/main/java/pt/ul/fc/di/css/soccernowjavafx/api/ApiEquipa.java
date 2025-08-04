package pt.ul.fc.di.css.soccernowjavafx.api;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ul.fc.di.css.soccernowjavafx.dto.EquipaDto;

public class ApiEquipa {
    
    private static final String BASE_URL = "http://localhost:8080/apiJavafx/equipas";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void createEquipa(EquipaDto equipa) throws Exception {
        String json = mapper.writeValueAsString(equipa);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new RuntimeException("Failed to create utilizador. HTTP code: " + response.statusCode());
        }
    }

    public static List<EquipaDto> getTodasEquipas() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.body(), new TypeReference<List<EquipaDto>>() {});
            } else {
                System.err.println("Erro ao buscar equipas: " + response.statusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static void adicionarJogador(Long jogadorId, long equipaId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/" + equipaId + "/plantel"))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(jogadorId.toString()))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new RuntimeException("Failed to add jogador. HTTP code: " + response.statusCode());
        }
    }

    public static void removerJogador(Long jogadorId, long equipaId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/" + equipaId + "/plantel/" + jogadorId))
            .DELETE()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to remove jogador. HTTP code: " + response.statusCode());
        }
    }

    public static void atualizarNomeEquipa(Long equipaId, String novoNome) throws Exception {
        String url = BASE_URL.trim() + "/" + equipaId + "/nome";
        System.out.println(url);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(novoNome))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to update equipa name. HTTP code: " + response.statusCode());
        }
    }

    public static EquipaDto getEquipaById(Long equipaId) throws Exception{
        String url = BASE_URL.trim() + "/" + equipaId;
        System.out.println(url);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to update equipa name. HTTP code: " + response.statusCode());
        }
        else{
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<EquipaDto>() {});
        }
    }

    public static void removerEquipa(Long equipaId){



    }

    public static void apagarEquipa(Long id) {
        String url = BASE_URL.trim() + "/" + id;
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .DELETE()
            .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to delete equipa. HTTP code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
