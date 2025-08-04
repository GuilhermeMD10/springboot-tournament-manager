package pt.ul.fc.di.css.soccernowjavafx.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ul.fc.di.css.soccernowjavafx.dto.EquipaDto;
import pt.ul.fc.di.css.soccernowjavafx.dto.EquipaTitularDto;

public class ApiEquipaTitular {

    private static final String BASE_URL = "http://localhost:8080/apiJavafx/equipastitulares";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient client = HttpClient.newHttpClient();

    public static EquipaTitularDto createEquipaTitular(EquipaTitularDto titular) throws Exception {
        String json = mapper.writeValueAsString(titular);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new RuntimeException("Failed to create equipa titular. HTTP code: " + response.statusCode());
        }

        return mapper.readValue(response.body(), EquipaTitularDto.class);
    }

    public static EquipaTitularDto getEquipaTitularById(Long equipaTitId) throws Exception{
                String url = BASE_URL.trim() + "/" + equipaTitId;
        System.out.println(url);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to get equipa Titular. HTTP code: " + response.statusCode());
        }
        else{
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<EquipaTitularDto>() {});
        }
    }


}
