package ru.practicum;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "hits")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "app")
    private String app;

    @Column(name = "uri")
    private String uri;

    @Column(name = "ip")
    private String ip;

    @Column(name = "created", nullable = false)
    private LocalDateTime timestamp;
}
