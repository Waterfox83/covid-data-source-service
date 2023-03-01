package com.jda.covidpublisher.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jda.covidpublisher.api.stats.CountryStatsData;
import com.jda.covidpublisher.kafka.producer.WorldStatsMessage;
import com.jda.covidpublisher.service.CovidDataService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

  @PostMapping("test-post")
  public void postTestData(@RequestBody Object body) throws IOException {
    System.out.println("In Test-Post");
    log.info("In Test-Post");
    try {
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(body);
      JsonElement jsonElement = new JsonParser().parse(json);
      JsonElement pullRequest = jsonElement.getAsJsonObject().get("pullRequest");
      JsonElement description = pullRequest.getAsJsonObject().get("description");
      System.out.println(description);
      log.info(String.valueOf(description));
    } catch (JsonProcessingException e) {
      log.error("Failed to parse request body",e);
    }
  }

}
