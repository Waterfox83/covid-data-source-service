package com.jda.covidpublisher.service;

import com.jda.covidpublisher.api.stats.CountryStatsData;
import com.jda.covidpublisher.kafka.producer.WorldStatsMessage;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@Service
public class CovidDataService {
  @Value("${service.worldServiceUrl}")
  private String coronavirusWorldStatisticsApiUrl;

  @Value("${service.countryServiceUrl}")
  private String coronavirusCountryStatisticsApiUrl;

  @Value("${api.worldDataApi}")
  private String worldDataApi;

  @Value("${api.countryDataApi}")
  private String countryDataApi;

  public WorldStatsMessage getWorldStats() throws IOException, URISyntaxException {
    log.info("Invoking covid-world-service to fetch data.");
    CloseableHttpClient httpclient = HttpClients.createDefault();
    URIBuilder uriBuilder = new URIBuilder()
        .setHost(coronavirusWorldStatisticsApiUrl)
        .setScheme("http")
        .setPath(worldDataApi);

    HttpGet httpGet = new HttpGet(uriBuilder.build());
    CloseableHttpResponse response = httpclient.execute(httpGet);

    WorldStatsMessage worldStatsMessage = null;
    try {
      HttpEntity entity = response.getEntity();
      String responseValue = EntityUtils.toString(entity);
      worldStatsMessage = (new Gson()).fromJson(responseValue, WorldStatsMessage.class);
      log.info("Response from covid-world-service: " + worldStatsMessage.toString());
    } finally {
      response.close();
    }
    log.info("Returning data");
    return worldStatsMessage;
  }

  public CountryStatsData getCountryStats(String countryName) throws IOException, URISyntaxException {
    log.info("Invoking covid-country-service to fetch data.");
    CloseableHttpClient httpclient = HttpClients.createDefault();

    URIBuilder uriBuilder = new URIBuilder()
        .setHost(coronavirusCountryStatisticsApiUrl)
        .setScheme("http")
        .setPath(countryDataApi)
        .setParameter("country", countryName);

    log.info(uriBuilder.build().toString());

    HttpGet httpGet = new HttpGet(uriBuilder.build());

    CloseableHttpResponse response = httpclient.execute(httpGet);

    CountryStatsData countryStatsData = null;
    try {
      HttpEntity entity = response.getEntity();
      String responseValue = EntityUtils.toString(entity);
      log.info(responseValue);
      countryStatsData = (new Gson()).fromJson(responseValue, CountryStatsData.class);
      log.info("Response from covid-country-service: " + countryStatsData.toString());
    } finally {
      response.close();
    }
    log.info("Returning data");
    return countryStatsData;
  }

}
