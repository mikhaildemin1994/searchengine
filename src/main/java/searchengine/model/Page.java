package searchengine.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "page", indexes = {@Index(columnList = "path", name = "path_index")})
@RequiredArgsConstructor
@Getter
@Setter
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site siteId; // ID сайта (табл. site)

    @Column(name = "path", columnDefinition = "VARCHAR(255)", nullable = false)
    private String path; // Адрес страницы от корня сайта

    @Column(name = "code", nullable = false)
    private int code; // Код HTTP-ответа

    @Column(name = "content", columnDefinition = "MEDIUMTEXT ", nullable = false)
    private String content; // Контент страницы (HTML-код)
}
