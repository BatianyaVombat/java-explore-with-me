import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import ru.practicum.EndpointHitDto;


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class EndpointHitDtoJsonTest {
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    void shouldDeserializeTimestampWithSpace() throws Exception {
        String json = "{\n" +
                "    \"app\": \"ewm-main-service\",\n" +
                "    \"uri\": \"/events/1\",\n" +
                "    \"ip\": \"192.163.0.1\",\n" +
                "    \"timestamp\": \"2026-11-15 17:52:29\"\n" +
                "}\n";

        EndpointHitDto dto = objectMapper.readValue(json, EndpointHitDto.class);

        assertThat(dto.getTimestamp())
                .isEqualTo(LocalDateTime.of(2026, 11, 15, 17, 52, 29));
    }

    @Test
    void shouldSerializeWithSpace() throws Exception {
        EndpointHitDto dto = EndpointHitDto.builder()
                .app("ewm-main-service")
                .uri("/events/1")
                .ip("192.163.0.1")
                .timestamp(LocalDateTime.of(2026, 11, 15, 17, 52, 29))
                .build();

        String json = objectMapper.writeValueAsString(dto);
        assertThat(json).contains("\"timestamp\":\"2026-11-15 17:52:29\"");
    }

    @Test
    void shouldThrowWhenTimestampWithT() {
        String json = "{   \"app\": \"ewm-main-service\",\n" +
                "    \"uri\": \"/events/1\",\n" +
                "    \"ip\": \"192.163.0.1\",\n" +
                "    \"timestamp\": \"2026-11-15T17:52:29\"\n" +
                "}\n";

        assertThatThrownBy(() -> objectMapper.readValue(json, EndpointHitDto.class))
                .isInstanceOf(InvalidFormatException.class);
    }
}
