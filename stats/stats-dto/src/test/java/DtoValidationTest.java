import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import ru.practicum.EndpointHitDto;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DtoValidationTest {
    private static Validator validator;
    private static ValidatorFactory factory;

    @BeforeAll
    static void setValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tierDownAll() {
        factory.close();
    }

    //-----EndpointHitDto-----
    @Nested
    class EndpointHitDtoTests {
        @Test
        void shouldPassWhenDtoCreateCorrectly() {
            EndpointHitDto dto = EndpointHitDto.builder()
                    .app("ewm-main-service")
                    .uri("/events/1")
                    .ip("192.163.0.1")
                    .timestamp(LocalDateTime.now())
                    .build();
            assertHasViolations(dto, 0);
        }

        @ParameterizedTest
        @EmptySource
        @NullSource
        void shouldFailWhenAppIsBlank(String invalidApp) {
            EndpointHitDto dto = EndpointHitDto.builder()
                    .app(invalidApp)
                    .uri("/events/1")
                    .ip("192.163.0.1")
                    .timestamp(LocalDateTime.now())
                    .build();
            assertHasViolations(dto, 1);
        }

        @ParameterizedTest
        @EmptySource
        @NullSource
        void shouldFailWhenUriIsBlank(String invalidUri) {
            EndpointHitDto dto = EndpointHitDto.builder()
                    .app("ewm-main-service")
                    .uri(invalidUri)
                    .ip("192.163.0.1")
                    .timestamp(LocalDateTime.now())
                    .build();
            assertHasViolations(dto, 1);
        }

        @ParameterizedTest
        @EmptySource
        @NullSource
        void shouldFailWhenIpIsBlank(String invalidIp) {
            EndpointHitDto dto = EndpointHitDto.builder()
                    .app("ewm-main-service")
                    .uri("/events/1")
                    .ip(invalidIp)
                    .timestamp(LocalDateTime.now())
                    .build();
            assertHasViolations(dto, 1);
        }

        @Test
        void shouldFailWhenTimestampIsNull() {
            EndpointHitDto dto = EndpointHitDto.builder()
                    .app("ewm-main-service")
                    .uri("/events/1")
                    .ip("192.163.0.1")
                    .timestamp(null)
                    .build();
            assertHasViolations(dto, 1);
        }
    }

    private <T> void assertHasViolations(T object, int expectedCount) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        assertThat(violations)
                .as("Ожидалось %d нарушений валидации для %s", expectedCount, object.getClass()
                        .getSimpleName()).hasSize(expectedCount);
    }
}
