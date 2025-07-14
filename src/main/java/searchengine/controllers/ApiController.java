package searchengine.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.services.SiteIndexingService;
import searchengine.services.StatisticsService;

import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final StatisticsService statisticsService;
    private final SiteIndexingService indexingService;

//    public ApiController(StatisticsService statisticsService) {
//        this.statisticsService = statisticsService;
//    }


    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<?> startIndexing() {
        indexingService.compute();
        indexingService.create();
        return indexingService.writingToDB().get() ?
                ResponseEntity.ok("result: true") :
                ResponseEntity.ok("result: false");
    }
}
