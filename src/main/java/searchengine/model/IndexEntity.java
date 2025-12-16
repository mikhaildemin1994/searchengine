package searchengine.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "indexes")
@RequiredArgsConstructor
@Getter
@Setter
public class IndexEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "page_id")
//    private PageEntity pageId; // Идентификатор страницы
    @Column(name = "page_id")
    private Long pageId; // Идентификатор страницы

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "lemma_id")
//    private LemmaEntity lemmaId; // Идентификатор леммы
    @Column(name = "lemma_id")
    private Long lemmaId; // Идентификатор леммы

    @Column(name = "ranks", nullable = false)
    private float ranks; // Кол-во данной леммы для данной страницы
}
