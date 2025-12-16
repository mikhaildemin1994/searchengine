package searchengine.dto.statistics;

import lombok.Data;
import searchengine.model.LemmaEntity;
import searchengine.model.PageEntity;

@Data
public class IndexDTO {
    private Integer id;
    private PageEntity pageId;
    private LemmaEntity lemmaId;
    private float ranks;
}
