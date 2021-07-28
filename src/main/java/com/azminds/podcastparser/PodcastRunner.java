package com.azminds.podcastparser;

import be.ceau.itunesapi.Lookup;
import be.ceau.itunesapi.request.Entity;
import be.ceau.itunesapi.response.Response;
import be.ceau.itunesapi.response.Result;
import com.azminds.podcastparser.dao.entity.EpisodeEntity;
import com.azminds.podcastparser.dao.entity.GenreEntity;
import com.azminds.podcastparser.dao.entity.PodcastEntity;
import com.azminds.podcastparser.dao.repository.GenreRepository;
import com.azminds.podcastparser.dao.repository.PodcastRepository;
import com.icosillion.podengine.models.Episode;
import com.icosillion.podengine.models.ITunesItemInfo;
import com.icosillion.podengine.models.Podcast;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Profile("test")
@Component
@Order(value = 3)
public class PodcastRunner implements CommandLineRunner {

  private int threadId = 1;
  private static final Logger logger = LoggerFactory.getLogger(PodcastRunner.class);

  @Autowired
  private PodcastRepository podcastRepository;
  @Autowired
  private GenreRepository genreRepository;

  public static ArrayList<PodcastCSV> readCsv(String SAMPLE_CSV_FILE_PATH) {
    ArrayList<PodcastCSV> podcasts = new ArrayList<>();
    try (
      Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH))
    ) {
      CSVReader csvReader = new CSVReader(reader);

      // Reading Records One by One in a String array
      String[] nextRecord;
      while ((nextRecord = csvReader.readNext()) != null) {
        PodcastCSV podcast = new PodcastCSV();
        podcast.setId(nextRecord[0]);
        podcast.setUrl(nextRecord[1]);
        podcast.setItunesId(nextRecord[2]);
        podcast.setOriginalUrl(nextRecord[3]);
        podcast.setNewestItemPubdate(nextRecord[4]);
        podcast.setOldestItemPubdate(nextRecord[5]);
        podcast.setLanguage(nextRecord[6]);
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

  public void produceData(Result rslt) throws InterruptedException {
    logger.info("-- thread-- " + (threadId++) + " is working");
    try {
//      Optional<com.azminds.podcastparser.dao.entity.Podcast> podcastDbData = podcastRepository.findByCollectionId(rslt.getCollectionId());
//      if (!podcastDbData.isPresent()) {
        PodcastEntity podcastEntity = PodcastEntity.create(
          rslt.getWrapperType(),
          rslt.getKind(),

        );
        podcastEntity.setCollectionId(rslt.getCollectionId());
        podcastEntity.setCollectionName(rslt.getCollectionName());
        podcastEntity.setDescription(rslt.getDescription());
        podcastEntity.setCollectionViewUrl(rslt.getCollectionViewUrl());
        podcastEntity.setArtistName(rslt.getArtistName());
        podcastEntity.setArtistViewUrl(rslt.getArtistViewUrl());
        podcastEntity.setWrapperType(rslt.getWrapperType());
        podcastEntity.setKind();
        podcastEntity.setFeedUrl(rslt.getFeedUrl());
        podcastEntity.setPreviewUrl(rslt.getPreviewUrl());
        podcastEntity.setArtworkUrl30(rslt.getArtworkUrl30());
        podcastEntity.setArtworkUrl60(rslt.getArtworkUrl60());
        podcastEntity.setArtworkUrl100(rslt.getArtworkUrl100());
        podcastEntity.setArtworkUrl512(rslt.getArtworkUrl512());
        podcastEntity.setArtworkUrl600(rslt.getArtworkUrl600());
        podcastEntity.setReleaseDate(rslt.getReleaseDate());
        podcastEntity.setTrackCount(rslt.getTrackCount());
        podcastEntity.setCountry(rslt.getCountry());
        podcastEntity.setCountry(rslt.getCountry());
        podcastEntity.setCopyright(rslt.getCopyright());
        podcastEntity.setShortDescription(rslt.getShortDescription());
        podcastEntity.setLongDescription(rslt.getLongDescription());

        for (String id : rslt.getGenreIds()) {
          if (!id.equals("26")) {
            Optional<GenreEntity> genreDbData = genreRepository.findByGenreIdOld(id);
            if (genreDbData.isPresent()) {
              GenreEntity genreEntity = genreDbData.get();
              podcastEntity.addGenre(genreEntity);
            }
          }
        }

        URL url = new URL(rslt.getFeedUrl());
        try {
          Podcast podcastData = new Podcast(url);
          System.out.println("- " + podcastData.getTitle() + " " + podcastData.getEpisodes().size());
          podcastEntity.setDescription(podcastData.getDescription());
          podcastEntity.setEpisodeCount(podcastData.getEpisodes().size());

          Collection<Episode> episodes = podcastData.getEpisodes();
          // List all episodes
          for (Episode episode : episodes) {
            System.out.println("\n- " + episode.getGUID());
            String description1 = "";
            try {
              description1 = episode.getDescription();
            } catch (Exception e) {
            }

            String hostedUrl = "";
            try {
              hostedUrl = episode.getLink().toString();
            } catch (Exception e) {
            }
            String duration = "";
            ITunesItemInfo iTunesItemInfo = episode.getITunesInfo();
            try {
              duration = iTunesItemInfo.getDuration();
            } catch (Exception e) {
            }

            Episode.Enclosure epEnclousure = episode.getEnclosure();
            String episodeLink = "";
            try {
              episodeLink = epEnclousure.getURL().toString();
            } catch (Exception e) {
            }
            long episodeLength = 0;
            try {
              episodeLength = epEnclousure.getLength();
            } catch (Exception e) {
            }
            String type = "";
            try {
              type = epEnclousure.getType();
            } catch (Exception e) {
            }

            EpisodeEntity episodeEntity = new EpisodeEntity();
            episodeEntity.setTitle(episode.getTitle());
            episodeEntity.setDescription(description1);
            episodeEntity.setGuid(episode.getGUID());
            episodeEntity.setHostedUrl(hostedUrl);
            episodeEntity.setPubDate(episode.getPubDate());
            episodeEntity.setDurationString(duration);
            episodeEntity.setLink(episodeLink);
            episodeEntity.setDuration(episodeLength);
            episodeEntity.setType(type);
            podcastEntity.addEpisode(episodeEntity);
          }

        } catch (Exception e) {
          System.out.println("[Episode] Exception::");
          e.printStackTrace();
        }
        System.out.println("before save!!!!");
        podcastRepository.save(podcastEntity);
//      }

      System.out.println("Thread Terminated!!");
    } catch (Exception e) {
      // Throwing an exception
      System.out.println("Exception is caught");
      e.printStackTrace();
    }
  }

  public void saveApplePodcast(Collection<String> arrIds) throws IOException {
    Response response = new Lookup()
      .setIds(arrIds)
      .setEntity(Entity.PODCAST)
      .execute();

    Collection<Result> results = response.getResults();

    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setCorePoolSize(results.size());
    threadPoolTaskExecutor.setMaxPoolSize(results.size());
    threadPoolTaskExecutor.setKeepAliveSeconds(5 * 1000);
    threadPoolTaskExecutor.setQueueCapacity(500);
    // // The thread pool needs to be initialized!
    threadPoolTaskExecutor.initialize();
    // // 100 threads
    results.forEach(rslt -> {
      Callable callable = new Callable() {
        @Override
        public Object call() throws Exception {
          produceData(rslt);
          return "";
        }
      };
      threadPoolTaskExecutor.submit(callable);

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
