package searchengine.dto.statistics;

import lombok.Data;
import searchengine.model.PageEntity;
import searchengine.model.Status;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SiteDTO {
    private Integer id;
    private Status status;
    private LocalDateTime statusTime;
    private String lastError;
    private String url;
    private String name;
    private List<PageEntity> pages;
}
