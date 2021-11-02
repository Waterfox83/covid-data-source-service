package com.jda.covidpublisher.api;

import com.jda.covidpublisher.api.stats.CountryStatsData;
import com.jda.covidpublisher.kafka.producer.WorldStatsMessage;
import com.jda.covidpublisher.service.CovidDataService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequestMapping(Routes.LATEST_VERSION)
public class CovidDataController {

  private final CovidDataService covidDataService;

  @Autowired
  public CovidDataController(CovidDataService covidDataService) {
    this.covidDataService = covidDataService;
  }
  
  @GetMapping(Routes.WORLD_STAT)
  public WorldStatsMessage getWorldStats() throws IOException, URISyntaxException {
    log.info("Now calling covidDataService.getWorldStats()");
    return covidDataService.getWorldStats();
  }

  @GetMapping(Routes.COUNTRY_STATS)
  public CountryStatsData getCountryStats(@RequestParam("country") String countryName) throws IOException,
      URISyntaxException {
    log.info("Now calling covidDataService.getCountryStats()");
    return covidDataService.getCountryStats(countryName);
  }


  @GetMapping(Routes.TEST)
  public String getTestData() throws IOException {
    return "Hello World";
  }

}
