package com.azminds.podcastparser;

import be.ceau.itunesapi.Lookup;
import be.ceau.itunesapi.request.Entity;
import be.ceau.itunesapi.response.Response;
import be.ceau.itunesapi.response.Result;
import com.azminds.podcastparser.repository.EpisodeRepository;
import com.azminds.podcastparser.repository.PodcastRepository;
import com.icosillion.podengine.models.Episode;
import com.icosillion.podengine.models.Podcast;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class MyRunner implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(MyRunner.class);

  @Autowired
  private PodcastRepository podcastRepository;

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
    Partition<PodcastCSV> arrayChunk = Partition.ofSize(records, 25);
    arrayChunk.forEach(arr -> {
      Collection<String> arrIds = arr.stream().map(item -> item.getItunesId()).collect(Collectors.toList());
      Response response = new Lookup()
          .setIds(arrIds)
          .setEntity(Entity.PODCAST)
          .execute();

      Collection<Result> results = response.getResults();
      results.forEach(rslt -> {
        com.azminds.podcastparser.domain.Podcast podcastEntity = new com.azminds.podcastparser.domain.Podcast();
        podcastEntity.setWrapperType(rslt.getWrapperType());
        podcastEntity.setKind(rslt.getKind());
        podcastEntity.setArtistId(rslt.getArtistId());
        podcastEntity.setCollectionId(rslt.getCollectionId());
        podcastEntity.setArtistName(rslt.getArtistName());
        podcastEntity.setCollectionName(rslt.getCollectionName());
        podcastEntity.setArtistViewUrl(rslt.getArtistViewUrl());
        podcastEntity.setCollectionViewUrl(rslt.getCollectionViewUrl());
        podcastEntity.setFeedUrl(rslt.getFeedUrl());
        podcastEntity.setPreviewUrl(rslt.getPreviewUrl());
        podcastEntity.setArtworkUrl30(rslt.getArtworkUrl30());
        podcastEntity.setArtworkUrl60(rslt.getArtworkUrl60());
        podcastEntity.setArtworkUrl100(rslt.getArtworkUrl100());
        podcastEntity.setArtworkUrl512(rslt.getArtworkUrl512());
        podcastEntity.setArtworkUrl600(rslt.getArtworkUrl600());
        podcastEntity.setReleaseDate(rslt.getReleaseDate());
        podcastEntity.setCountry(rslt.getCountry());
        podcastEntity.setShortDescription(rslt.getShortDescription());
        podcastEntity.setLongDescription(rslt.getLongDescription());
        podcastEntity.setDescription(rslt.getDescription());
        podcastEntity.setTrackCount(rslt.getTrackCount());
        podcastEntity.setCopyright(rslt.getCopyright());

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

        try {
          Podcast podcastData = new Podcast(new URL(rslt.getFeedUrl()));
          System.out.println("- " + podcastData.getTitle() + " " + podcastData.getEpisodes().size());
          podcastEntity.setDescription(podcastData.getDescription());
          podcastEntity.setEpisodeCount(podcastData.getEpisodes().size());

          Collection<Episode> episodes = podcastData.getEpisodes();
          // List all episodes
          for (Episode episode : episodes) {
            com.azminds.podcastparser.domain.Episode episodeEntity = new com.azminds.podcastparser.domain.Episode();
            episodeEntity.setTitle(episode.getTitle());
            episodeEntity.setDescription(episode.getDescription());
            episodeEntity.setGuid(episode.getGUID());
            episodeEntity.setLink(episode.getLink());
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

  class GenreData {
    String genreName;
    String genreId;
  }
}
