package com.azminds.podcastparser;

import be.ceau.itunesapi.Lookup;
import be.ceau.itunesapi.request.Entity;
import be.ceau.itunesapi.response.Response;
import be.ceau.itunesapi.response.Result;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Order(value = 3)
public class PodcastRunner implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(PodcastRunner.class);

  @Autowired
  private ApplicationContext applicationContext;

  public static ArrayList<PodcastCSV> readCsv(String SAMPLE_CSV_FILE_PATH) {
    ArrayList<PodcastCSV> podcasts = new ArrayList<>();
    try (
      Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH))
    ) {
      CSVReader csvReader = new CSVReader(reader);

      // Reading Records One by One in a String array
      String[] nextRecord;
      while ((nextRecord = csvReader.readNext()) != null) {
        PodcastCSV podcast = new PodcastCSV(nextRecord[0], nextRecord[1], nextRecord[2], nextRecord[3], nextRecord[4], nextRecord[5], nextRecord[6]);
        podcasts.add(podcast);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (CsvValidationException e) {
      e.printStackTrace();
    }
    return podcasts;
  }

  public static boolean isUrlValid(String url) {
    try {
      URL obj = new URL(url);
      obj.toURI();
      return true;
    } catch (MalformedURLException | URISyntaxException e) {
      return false;
    }
  }


  public void saveApplePodcast(Collection<String> arrIds) throws IOException {
    Response response = new Lookup()
      .setIds(arrIds)
      .setEntity(Entity.PODCAST)
      .execute();

    Collection<Result> results = response.getResults();
    results.forEach(rslt -> {
      if (isUrlValid(rslt.getFeedUrl())) {
        Runnable myThread = new PodcastThread(rslt);
        applicationContext.getAutowireCapableBeanFactory().autowireBean(myThread);
        Thread object = new Thread(myThread);
        object.start();
      }
    });
  }

  public void savePodcastIndex(Collection<String> arrIds) throws Exception {
    arrIds.forEach(itunesId -> {
      try {
        PodcastIndexClient indexClient = new PodcastIndexClient("JCB8PKWPNRFNG5TWMFZN", "UkstyfaAhBftkvG4FUq8AJMQxKLSVjp^mcUf^8M#");
        Map<String, String> map = new HashMap();
        map.put("id", itunesId);
        PodcastIndexResponse response = indexClient.callAPI("/podcasts/byitunesid", map);
        System.out.println(response.getJsonResponse());
        Thread.sleep(3 * 1000);
      } catch (Exception e) {
        System.out.println("Exception::");
        e.printStackTrace();
      }
    });
  }


  @Override
  public void run(String... args) {
    System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

    Date date = new Date();
    long now = date.getTime();
    System.out.println("Start Time: " + now);
    for (int i = 1; i <= 10; i++) {
      ArrayList<PodcastCSV> records = readCsv("FileNumber_" + i + ".csv");
      logger.info("{}", records);
      Partition<PodcastCSV> arrayChunk = Partition.ofSize(records, 100);
      logger.info("{}", arrayChunk);
      System.out.println("CommandLine Runner!!!");
      arrayChunk.forEach(arr -> {
        try {
          Date date1 = new Date();
          long now1 = date1.getTime();
          System.out.println("Loop Start Time: " + now1);
          Collection<String> arrIds = arr.stream().map(item -> item.getItunesId()).collect(Collectors.toList());
          System.out.println("chunk>>>" + arrIds.size());
          saveApplePodcast(arrIds);

//          savePodcastIndex(arrIds);

          System.out.println("Loop Time taken: " + ((new Date().getTime() - now1) / 1000) + " second ");
        } catch (IOException e) {
          System.out.println("[MAIN] IOException::");
          e.printStackTrace();
        } catch (Exception e) {
          System.out.println("[MAIN] Exception::");
          e.printStackTrace();
        }
      });

    }
    System.out.println("Total Time taken: " + ((new Date().getTime() - now) / 1000) + " second ");
  }
}
