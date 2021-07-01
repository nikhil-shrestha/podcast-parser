package com.azminds.podcastparser;

import be.ceau.itunesapi.Lookup;
import be.ceau.itunesapi.request.Entity;
import be.ceau.itunesapi.response.Response;
import be.ceau.itunesapi.response.Result;
import com.azminds.podcastparser.repository.GenreRepository;
import com.azminds.podcastparser.repository.PodcastRepository;
import com.icosillion.podengine.models.Episode;
import com.icosillion.podengine.models.Podcast;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class Application extends SpringBootServletInitializer implements CommandLineRunner, ExitCodeGenerator {

  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  private int exitCode; // initialized with 0

  public static void main(String[] args) {
    System.exit(SpringApplication.exit(SpringApplication.run(Application.class, args)));
  }

  @Autowired
  PodcastRepository podcastRepository;

  @Autowired
  GenreRepository genreRepository;

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
    } catch (MalformedURLException e) {
      return false;
    } catch (URISyntaxException e) {
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
      System.out.println(rslt);
      Optional<com.azminds.podcastparser.domain.Podcast> podcastDbData = this.podcastRepository.findByCollectionId(rslt.getCollectionId());

      if (!podcastDbData.isPresent()) {
        com.azminds.podcastparser.domain.Podcast podcastEntity = new com.azminds.podcastparser.domain.Podcast(
          rslt.getCollectionId(),
          rslt.getCollectionName(),
          rslt.getDescription(),
          rslt.getCollectionViewUrl(),
          rslt.getArtistName(),
          rslt.getArtistViewUrl(),
          rslt.getWrapperType(),
          rslt.getKind(),
          rslt.getFeedUrl(),
          rslt.getPreviewUrl(),
          rslt.getArtworkUrl30(),
          rslt.getArtworkUrl60(),
          rslt.getArtworkUrl100(),
          rslt.getArtworkUrl512(),
          rslt.getArtworkUrl600(),
          rslt.getReleaseDate(),
          rslt.getTrackCount(),
          rslt.getCountry(),
          rslt.getCopyright(),
          rslt.getShortDescription(),
          rslt.getLongDescription()
        );
        GenreData[] genreData = new GenreData[rslt.getGenreIds().size()];
        int i = 0;
        for (String id : rslt.getGenreIds()) {
          genreData[i] = new GenreData();
          genreData[i].genreId = id;
          i++;
        }

        i = 0;
        for (String name : rslt.getGenres()) {
          genreData[i].genreName = name;
          i++;
        }

        for (GenreData genre : genreData) {
          Optional<com.azminds.podcastparser.domain.Genre> genreDbData = this.genreRepository.findByGenreIdOld(genre.genreId);
          com.azminds.podcastparser.domain.Genre genreEntity;

          if (!genreDbData.isPresent()){
            genreEntity = new com.azminds.podcastparser.domain.Genre(genre.genreName, genre.genreId);
            this.genreRepository.save(genreEntity);

            podcastEntity.addGenre(genreEntity);
          } else {
            genreEntity = genreDbData.get();
          }
          podcastEntity.addGenre(genreEntity);

          System.out.println("added Genre!!!!");
        }

        System.out.println("URL::" + rslt.getFeedUrl());
        if (isUrlValid(rslt.getFeedUrl())) {
          System.out.println("isValid URL>>>");
          try {
            Podcast podcastData = new Podcast(new URL(rslt.getFeedUrl()));
            System.out.println("- " + podcastData.getTitle() + " " + podcastData.getEpisodes().size());
            podcastEntity.setDescription(podcastData.getDescription());
            podcastEntity.setEpisodeCount(podcastData.getEpisodes().size());

            System.out.println("get Episode Collection>>>");
            Collection<Episode> episodes = podcastData.getEpisodes();
            // List all episodes
            for (Episode episode : episodes) {
              com.azminds.podcastparser.domain.Episode episodeEntity = new com.azminds.podcastparser.domain.Episode(
                episode.getTitle(),
                episode.getDescription(),
                episode.getGUID(),
                episode.getLink(),
                episode.getPubDate(),
                episode.getITunesInfo().getDuration(),
                episode.getEnclosure().getURL(),
                episode.getEnclosure().getLength(),
                episode.getEnclosure().getType()
              );
              podcastEntity.addEpisode(episodeEntity);
            }
          } catch (Exception e) {
            System.out.println("[Episode] Exception::");
            e.printStackTrace();
          }
        } else {
          System.out.println("Enter valid URL");
        }
        System.out.println("before save!!!!");
        this.podcastRepository.save(podcastEntity);
      }
    });
  }

  class GenreData {
    String genreId;
    String genreName;
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

//    CsvSplit.splitFile();

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
          long now1 = date.getTime();
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

    this.exitCode = 1;
  }


  /**
   * This is overridden from ExitCodeGenerator
   */
  @Override
  public int getExitCode() {
    return this.exitCode;
  }
}