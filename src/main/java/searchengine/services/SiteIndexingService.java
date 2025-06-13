package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import searchengine.dto.statistics.SiteDTO;
import searchengine.model.SiteEntity;
import searchengine.repositories.SiteRepository;

import javax.swing.text.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

@Service
@RequiredArgsConstructor
public class SiteIndexingService extends RecursiveAction {
    private final SiteRepository siteRepository;
    private static Document doc;



    @Override
    protected void compute() {
        List<SiteIndexingService> taskList = new ArrayList<>();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            doc = (Document) Jsoup.connect("https://www.facebook.com/")
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean writingToDB() {
        return true;
    }

    public SiteDTO mapToDto (SiteEntity siteEntity) {
        SiteDTO siteDTO = new SiteDTO();
        siteDTO.setId(siteEntity.getId());
        siteDTO.setStatus(siteEntity.getStatus());
        siteDTO.setStatusTime(siteEntity.getStatusTime());
        siteDTO.setLastError(siteEntity.getLastError());
        siteDTO.setUrl(siteEntity.getUrl());
        siteDTO.setName(siteEntity.getName());
        siteDTO.setPages(siteEntity.getPages());

        return siteDTO;
    }

    public SiteEntity mapToEntity (SiteDTO siteDTO) {
        SiteEntity siteEntity = new SiteEntity();
        siteEntity.setId(siteDTO.getId());
        siteEntity.setStatus(siteDTO.getStatus());
        siteEntity.setStatusTime(siteDTO.getStatusTime());
        siteEntity.setLastError(siteDTO.getLastError());
        siteEntity.setUrl(siteDTO.getUrl());
        siteEntity.setName(siteDTO.getName());
        siteEntity.setPages(siteDTO.getPages());

        return siteEntity;
    }
}
