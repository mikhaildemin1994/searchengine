package searchengine.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "site")
@RequiredArgsConstructor
@Getter
@Setter
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status; // Текущий статус полной индексации сайта

    @Column(name = "status_time", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime statusTime; // Дата и время статуса

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError; // Текст ошибки индексации (или NULL, если ее не было)

    @Column(name = "url", columnDefinition = "VARCHAR(255)", nullable = false)
    private String url; // Адрес главной страницы сайта

    @Column(name = "name", columnDefinition = "VARCHAR(255)", nullable = false)
    private String name; // Имя сайта

    @OneToMany(mappedBy = "siteId", cascade = CascadeType.ALL)
    private List<Page> pages;
}
