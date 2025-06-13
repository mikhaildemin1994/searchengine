package searchengine.dto.statistics;

import lombok.Data;
import searchengine.model.SiteEntity;

@Data
public class PageDTO {
    private Integer id;
    private SiteEntity siteId;
    private String path;
    private int code;
    private String content;
}
