package pt.ul.fc.di.css.soccernowjavafx.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ul.fc.di.css.soccernowjavafx.dto.ResultadoDto;

public class ApiResultado {
        
    private static final String BASE_URL = "http://localhost:8080/apiJavafx/resultados";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient client = HttpClient.newHttpClient();

    public static ResultadoDto getResultadoById(Long resultadoId) throws Exception{
        String url = BASE_URL.trim() + "/" + resultadoId;
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
            return mapper.readValue(response.body(), new TypeReference<ResultadoDto>() {});
        }
    }

    public static void submeterResultado(ResultadoDto resultado, Long jogoId) {
        try {
            String json = mapper.writeValueAsString(resultado);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/create/" + jogoId))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200 && response.statusCode() != 201) {
                throw new RuntimeException("Failed to create resultado. HTTP code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
