package com.azminds.podcastparser;

import be.ceau.itunesapi.Lookup;
import be.ceau.itunesapi.request.Entity;
import be.ceau.itunesapi.response.Response;
import be.ceau.itunesapi.response.Result;
import com.azminds.podcastparser.repository.PodcastRepository;
import com.icosillion.podengine.models.Episode;
import com.icosillion.podengine.models.Podcast;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@SpringBootApplication
public class Application extends SpringBootServletInitializer implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Autowired
  PodcastRepository podcastRepository;

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
    } catch (Exception e) {
      e.printStackTrace();
    }
    return podcasts;
  }

  @Override
  public void run(String... args) throws Exception {
    ArrayList<PodcastCSV> records = readCsv("FileNumber_1.csv");
    logger.info("{}", records);
    Partition<PodcastCSV> arrayChunk = Partition.ofSize(records, 25);
    logger.info("{}", arrayChunk);
    System.out.println("CommandLine Runner!!!");
    arrayChunk.forEach(arr -> {
      Collection<String> arrIds = arr.stream().map(item -> item.getItunesId()).collect(Collectors.toList());
      Response response = new Lookup()
          .setIds(arrIds)
          .setEntity(Entity.PODCAST)
          .execute();

      Collection<Result> results = response.getResults();
      results.forEach(rslt -> {
        System.out.println(rslt);
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

//          GenreData[] genreData = new GenreData[rslt.getGenreIds().size()];
//          int i = 0;
//          for (String id : rslt.getGenreIds()) {
//            genreData[i] = new GenreData();
//            genreData[i].genreId = id;
//            i++;
//          }
//
//          i = 0;
//          for (String name : rslt.getGenres()) {
//            genreData[i].genreName = name;
//            i++;
//          }

        try {
          Podcast podcastData = new Podcast(new URL(rslt.getFeedUrl()));
          System.out.println("- " + podcastData.getTitle() + " " + podcastData.getEpisodes().size());
          podcastEntity.setDescription(podcastData.getDescription());
          podcastEntity.setEpisodeCount(podcastData.getEpisodes().size());

          Collection<Episode> episodes = podcastData.getEpisodes();
          // List all episodes
          for (Episode episode : episodes) {
            com.azminds.podcastparser.domain.Episode episodeEntity = new com.azminds.podcastparser.domain.Episode(
                episode.getTitle(),
                episode.getDescription(),
                episode.getGUID(),
                episode.getLink(),
                episode.getPubDate(),
                episode.getITunesInfo().getDuration()
            );
            podcastEntity.addEpisode(episodeEntity);
          }
        } catch (Exception e) {
          System.out.println("Exception::");
          System.out.println(e);
        }

        podcastRepository.save(podcastEntity);
      });
    });
  }
}