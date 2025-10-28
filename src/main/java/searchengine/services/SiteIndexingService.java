package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
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

    private static int statusCode;
    private static String content;
    private static PageDTO pageDTO = new PageDTO();
    private static SiteDTO siteDTO = new SiteDTO();
    private static PageEntity pageEntity = new PageEntity();
    private static SiteEntity siteEntity = new SiteEntity();
    private static List<String> linksList = new ArrayList<>();

    @Override
    public void compute() {
        List<SiteIndexingService> taskList = new ArrayList<>();
        Document doc;
        String url = "https://sendel.ru/";

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();
            Connection.Response response = doc.connection().response();
            content = doc.outerHtml();
            statusCode = response.statusCode();
            Set<String> lists = doc.select("a").
                    stream().map(d -> d.attr("href")).collect(Collectors.toSet());

            for(String list : lists) {
                if(!list.contains("https") && !list.contains("#")
                && !list.contains("@") && !linksList.contains(list)) {
                    pageDTO.setCode(statusCode);
                    pageDTO.setPath(list);
                    pageDTO.setContent(content);

                    siteDTO.setStatus(Status.INDEXED);
                    siteDTO.setStatusTime(LocalDateTime.now());
                    siteDTO.setUrl(url);
                    siteDTO.setName(url);

                    pageEntity = mapToEntity(pageDTO);
                    siteEntity = mapToEntity(siteDTO);
                    pageRepository.save(pageEntity);
                    siteRepository.save(siteEntity);

                    linksList.add(list);

                    SiteIndexingService task = new SiteIndexingService(siteRepository, pageRepository);
                    taskList.add(task);
                }
            }
            for(SiteIndexingService task : taskList) {
                task.fork();
                task.join();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
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
