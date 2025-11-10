package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.statistics.PageDTO;
import searchengine.dto.statistics.SiteDTO;
import searchengine.model.PageEntity;
import searchengine.model.SiteEntity;
import searchengine.model.Status;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SiteIndexingService extends RecursiveAction {
    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final SitesList sites;

    private final PageDTO pageDTO = new PageDTO();
    private final SiteDTO siteDTO = new SiteDTO();
    protected PageEntity pageEntity;
    protected SiteEntity siteEntity;
    private static final List<String> linksList = new ArrayList<>();

    @Override
    public void compute() {
        List<SiteIndexingService> taskList = new ArrayList<>();
        List<Site> sitesList = sites.getSites();

//        for (Site site : sitesList) {
//            String url = site.getUrl();

        for(int i = 0; i < sitesList.size(); i++) {
            Site site = sitesList.get(i);
            String url = site.getUrl();
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                Connection.Response response = doc.connection().response();
                String content = doc.outerHtml();
                int statusCode = response.statusCode();
                Set<String> lists = doc.select("a").
                        stream().map(d -> d.attr("href")).collect(Collectors.toSet());

                for (String list : lists) {
                    if (!list.contains("https") && !list.contains("#")
                            && !list.contains("@") && !linksList.contains(list)) {
                        siteDTO.setId(i + 1);
                        siteDTO.setStatus(Status.INDEXED);
                        siteDTO.setStatusTime(LocalDateTime.now());
                        siteDTO.setUrl(url);
                        siteDTO.setName(site.getName());
                        siteEntity = mapToEntity(siteDTO);
                        siteRepository.save(siteEntity);

                        pageDTO.setSiteId(siteEntity);
                        pageDTO.setCode(statusCode);
                        pageDTO.setPath(list);
                        pageDTO.setContent(content);
                        pageEntity = mapToEntity(pageDTO);
                        pageRepository.save(pageEntity);

                        linksList.add(list);

                        SiteIndexingService task = new SiteIndexingService(siteRepository, pageRepository, sites);
                        taskList.add(task);
                    }
                }
                for (SiteIndexingService task : taskList) {
                    task.fork();
                    task.join();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static PageEntity mapToEntity (PageDTO pageDTO) {
        PageEntity pageEntity = new PageEntity();
        pageEntity.setId(pageDTO.getId());
        pageEntity.setSiteId(pageDTO.getSiteId());
        pageEntity.setCode(pageDTO.getCode());
        pageEntity.setPath(pageDTO.getPath());
        pageEntity.setContent(pageDTO.getContent());

        return pageEntity;
    }

    public static SiteEntity mapToEntity (SiteDTO siteDTO) {
        SiteEntity siteEntity = new SiteEntity();
        siteEntity.setId(siteDTO.getId());
        siteEntity.setUrl(siteDTO.getUrl());
        siteEntity.setName(siteDTO.getName());
        siteEntity.setStatus(siteDTO.getStatus());
        siteEntity.setPages(siteDTO.getPages());
        siteEntity.setStatusTime(siteDTO.getStatusTime());

        return siteEntity;
    }

    public AtomicBoolean writingToDB() {
        return new AtomicBoolean(true);
    }
}
