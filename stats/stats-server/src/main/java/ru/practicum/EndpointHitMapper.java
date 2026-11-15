package ru.practicum;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class EndpointHitMapper {

    public EndpointHit toEntity(EndpointHitDto dto) {
        return EndpointHit.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
