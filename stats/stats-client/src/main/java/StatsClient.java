import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class StatsClient {
    private final RestTemplate restTemplate;
    private final String serverUrl;

    public StatsClient(RestTemplateBuilder builder, @Value("${stats-server.url}") String serverUrl) {
        this.restTemplate = builder.build();
        this.serverUrl = serverUrl;
    }

    public void hit(EndpointHitDto dto) {
        log.info("POST /hit: {}", dto);

        String fullUrl = serverUrl + "/hit";
        try {
            restTemplate.postForEntity(fullUrl, dto, Void.class);
        } catch (HttpClientErrorException excClient) {
            log.error(String.valueOf(excClient));
        } catch (HttpServerErrorException | ResourceAccessException excServer) {
            log.error(excServer.getMessage());
        }
    }

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        log.info("GET /stats: start={}, end={}, uris={}, unique={}", start, end, uris, unique);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serverUrl).path("/stats")
                .queryParam("start", start.format(formatter))
                .queryParam("end", end.format(formatter))
                .queryParam("unique", unique);

        if (uris != null && !uris.isEmpty()) {
            builder.queryParam("uris", uris);
        }

        String url = builder.encode().toUriString();

        try {
            ResponseEntity<List<ViewStatsDto>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<>() {
                    });

            return response.getBody() != null ? response.getBody() : List.of();
        } catch (HttpClientErrorException e) {
            log.error("Client error: status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());

            return List.of();
        } catch (HttpServerErrorException | ResourceAccessException e) {
            log.error("Server error: {}", e.getMessage());

            return List.of();
        }
    }
}
