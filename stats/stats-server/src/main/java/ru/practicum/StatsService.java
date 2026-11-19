package ru.practicum;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final EndpointHitRepository endpointHitRep;

    public void createEndpointHit(EndpointHitDto dto) {
        EndpointHit newHit = EndpointHitMapper.toEntity(dto);
        endpointHitRep.save(newHit);
    }

    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, boolean unique) {
        if (start == null || start.isBlank() || end == null || end.isBlank()) {
            throw new ValidationException("Параметры start и end обязательны");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime formatedStart;
        LocalDateTime formatedEnd;

        try {
            formatedStart = LocalDateTime.parse(start, formatter);
            formatedEnd = LocalDateTime.parse(end, formatter);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Неверный формат даты. Ожидается yyyy-MM-dd HH:mm:ss");
        }

        List<EndpointHit> hits = endpointHitRep.findAllByTimestampBetween(formatedStart, formatedEnd);

        if (hits.isEmpty()) {
            return List.of();
        }

        List<EndpointHit> filtered = (uris == null || uris.isEmpty())
                ? hits
                : hits.stream().filter(h -> uris.contains(h.getUri())).toList();

        record AppAndUri(String app, String uri) {
        }

        Map<AppAndUri, List<EndpointHit>> grouped = filtered.stream()
                .collect(Collectors.groupingBy(h -> new AppAndUri(h.getApp(), h.getUri())));

        List<ViewStatsDto> result = new ArrayList<>();
        for (var entry : grouped.entrySet()) {
            long hitCount = unique
                    ? entry.getValue().stream().map(EndpointHit::getIp).distinct().count()
                    : entry.getValue().size();
            result.add(new ViewStatsDto(entry.getKey().app(), entry.getKey().uri, hitCount));
        }

        result.sort(Comparator.comparing(ViewStatsDto::getHits).reversed()
                .thenComparing(ViewStatsDto::getApp)
                .thenComparing(ViewStatsDto::getUri));

        return result;
    }
}
