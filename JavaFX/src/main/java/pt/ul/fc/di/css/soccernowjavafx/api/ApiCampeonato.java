package pt.ul.fc.di.css.soccernowjavafx.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ul.fc.di.css.soccernowjavafx.dto.CampeonatoDto;
import pt.ul.fc.di.css.soccernowjavafx.dto.JogoDto;
import pt.ul.fc.di.css.soccernowjavafx.model.Campeonato_Estado;

public class ApiCampeonato {
    private static final String BASE_URL = "http://localhost:8080/apiJavafx/campeonatos";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient client = HttpClient.newHttpClient();


    public static void createCampeonato(CampeonatoDto camp) throws Exception {
        camp.setEstado(Campeonato_Estado.POR_COMECAR);
        String json = mapper.writeValueAsString(camp);

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

    public static List<CampeonatoDto> getTodosCampeonatos() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.body(), new TypeReference<List<CampeonatoDto>>() {});
            } else {
                System.err.println("Erro ao buscar campeonatos: " + response.statusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public static void adicionarEquipa(Long equipaId, long campId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/" + campId + "/equipa"))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(equipaId.toString()))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new RuntimeException("Failed to add jogador. HTTP code: " + response.statusCode());
        }
    }

    public static void removerEquipa(Long equipaId, long campId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/" + campId + "/equipa/" + equipaId))
            .DELETE()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to remove jogador. HTTP code: " + response.statusCode());
        }
    }

    public static void atualizarNomeEquipa(Long campId, String novoNome) throws Exception {
        String url = BASE_URL.trim() + "/" + campId + "/nome";
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

    public static void adicionarJogoAoCampeonato(Long jogoId, Long campId) {
        String url = BASE_URL + "/" + campId + "/jogo";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jogoId.toString()))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200 && response.statusCode() != 201) {
                throw new RuntimeException("Failed to add jogo to campeonato. HTTP code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void cancelarJogoDeCampeonato(Long jogoId, Long campId) {
        String url = BASE_URL + "/" + campId + "/jogo/" + jogoId;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to remove jogo from campeonato. HTTP code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void apagarCampeonato(Long campId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/" + campId))
            .DELETE()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 204) {
            throw new RuntimeException("Failed to remove campeonato. HTTP code: " + response.statusCode());
        }
    }

    public static void iniciarCampeonato(Long id) {
        String url = BASE_URL + "/" + id + "/comecar";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to start campeonato. HTTP code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void terminarCampeonato(Long id) {
        String url = BASE_URL + "/" + id + "/terminar";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to end campeonato. HTTP code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
