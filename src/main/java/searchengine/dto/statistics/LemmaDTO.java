package searchengine.dto.statistics;

import lombok.Data;
import searchengine.model.SiteEntity;

@Data
public class LemmaDTO {
    private Integer id;
    private SiteEntity siteId;
    private String lemma;
    private int frequency;
}
