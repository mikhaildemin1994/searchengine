package searchengine.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import searchengine.dto.statistics.PageDTO;
import searchengine.dto.statistics.SiteDTO;
import searchengine.model.PageEntity;
import searchengine.model.SiteEntity;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SiteIndexingService extends RecursiveAction {
//    private final SiteRepository siteRepository;
    private PageRepository pageRepository;

    private static String url;
    private static List<String> linksList = new ArrayList<>();

    public SiteIndexingService(String url) {
        this.url = url;
    }

    @Override
    public void compute() {

        List<SiteIndexingService> taskList = new ArrayList<>();
        Document doc;
        url = "http://sendel.ru/";

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
            Set<String> lists = doc.select("a").
                    stream().map(d -> d.attr("href")).collect(Collectors.toSet());

            for(String list : lists) {
                if(!list.contains("https") && !list.contains("#")
                && !list.contains("@") && !linksList.contains(list)) {
                    linksList.add(list);
                    SiteIndexingService task = new SiteIndexingService(url);
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

//    public static SiteDTO mapToDto (SiteEntity siteEntity) {
//        SiteDTO siteDTO = new SiteDTO();
//
//        siteDTO.setId(siteEntity.getId());
//        siteDTO.setStatus(siteEntity.getStatus());
//        siteDTO.setStatusTime(siteEntity.getStatusTime());
//        siteDTO.setLastError(siteEntity.getLastError());
//        siteDTO.setUrl(siteEntity.getUrl());
//        siteDTO.setName(siteEntity.getName());
//        siteDTO.setPages(siteEntity.getPages());
//
//        return siteDTO;
//    }
//
//    public static SiteEntity mapToEntity (SiteDTO siteDTO) {
//        SiteEntity siteEntity = new SiteEntity();
//
//        siteEntity.setId(siteDTO.getId());
//        siteEntity.setStatus(siteDTO.getStatus());
//        siteEntity.setStatusTime(siteDTO.getStatusTime());
//        siteEntity.setLastError(siteDTO.getLastError());
//        siteEntity.setUrl(siteDTO.getUrl());
//        siteEntity.setName(siteDTO.getName());
//        siteEntity.setPages(siteDTO.getPages());
//
//        return siteEntity;
//    }

    public static PageDTO mapToDto (PageEntity pageEntity) {
        PageDTO pageDTO = new PageDTO();

        pageDTO.setId(pageEntity.getId());
        pageDTO.setSiteId(pageEntity.getSiteId());
        pageDTO.setCode(pageEntity.getCode());
        pageDTO.setPath(pageEntity.getPath());
        pageDTO.setContent(pageEntity.getContent());

        return pageDTO;
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

    @SneakyThrows
    public void create() {
        Files.write(Paths.get("C:\\Users\\Velociraptor\\Desktop\\parsing.txt"), linksList);
    }

    public AtomicBoolean writingToDB() {
        return new AtomicBoolean(true);
    }
}
