package searchengine.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "lemma")
@RequiredArgsConstructor
@Getter
@Setter
public class LemmaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private SiteEntity siteId; // Идентификатор сайта

    @Column(name = "lemma", columnDefinition = "VARCHAR(255)", nullable = false)
    private String lemma; // Нормальная форма слова (лемма)

    @Column(name = "frequency", nullable = false)
    private int frequency; // Кол-во страниц, на которых слово встречается хотя бы один раз
}
