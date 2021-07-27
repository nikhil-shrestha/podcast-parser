package com.azminds.podcastparser;

import be.ceau.itunesapi.response.Result;
import com.azminds.podcastparser.dao.entity.Genre;
import com.azminds.podcastparser.dao.repository.GenreRepository;
import com.azminds.podcastparser.dao.repository.PodcastRepository;
import com.icosillion.podengine.models.Episode;
import com.icosillion.podengine.models.ITunesItemInfo;
import com.icosillion.podengine.models.Podcast;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class PodcastThread implements Runnable {

  private Thread worker;
  private Result rslt;
  private final AtomicBoolean running = new AtomicBoolean(false);

  @Autowired
  private PodcastRepository podcastRepository;
  @Autowired
  private GenreRepository genreRepository;

  PodcastThread(Result rslt) {
    this.rslt = rslt;
  }

  public void start() {
    worker = new Thread(this);
    worker.start();
  }

  public void stop() {
    running.set(false);
  }

  @Override
  public void run() {
    running.set(true);
    final long start = System.currentTimeMillis();
    while (running.get()) {
      System.out.println(
        "Thread " + Thread.currentThread().getId()
          + " is running");
      process();
    }
    System.out.println(
      "Thread " + Thread.currentThread().getId()
        + " has stopped. Elapsed time: {" + ((System.currentTimeMillis() - start) / 1000) + " second}");
  }

  public void process() {
    try {
      Optional<com.azminds.podcastparser.dao.entity.Podcast> podcastDbData = podcastRepository.findByCollectionId(rslt.getCollectionId());
      if (!podcastDbData.isPresent()) {
        com.azminds.podcastparser.dao.entity.Podcast podcastEntity = new com.azminds.podcastparser.dao.entity.Podcast();
        podcastEntity.setCollectionId(rslt.getCollectionId());
        podcastEntity.setCollectionName(rslt.getCollectionName());
        podcastEntity.setDescription(rslt.getDescription());
        podcastEntity.setCollectionViewUrl(rslt.getCollectionViewUrl());
        podcastEntity.setArtistName(rslt.getArtistName());
        podcastEntity.setArtistViewUrl(rslt.getArtistViewUrl());
        podcastEntity.setWrapperType(rslt.getWrapperType());
        podcastEntity.setKind(rslt.getKind());
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
          if (!id.equals("26")){
            Optional<Genre> genreDbData = genreRepository.findByGenreIdOld(id);
            if (genreDbData.isPresent()){
              Genre genreEntity = genreDbData.get();
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
            try { description1 = episode.getDescription(); } catch (Exception e) { }

            String hostedUrl = "";
            try { hostedUrl = episode.getLink().toString(); } catch (Exception e) { }
            String duration = "";
            ITunesItemInfo iTunesItemInfo = episode.getITunesInfo();
            try { duration = iTunesItemInfo.getDuration(); } catch (Exception e) { }

            Episode.Enclosure epEnclousure = episode.getEnclosure();
            String episodeLink = "";
            try { episodeLink = epEnclousure.getURL().toString(); } catch (Exception e) { }
            long episodeLength = 0;
            try { episodeLength = epEnclousure.getLength(); } catch (Exception e) { }
            String type = "";
            try { type = epEnclousure.getType(); } catch (Exception e) { }

            com.azminds.podcastparser.dao.entity.Episode episodeEntity = new com.azminds.podcastparser.dao.entity.Episode();
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
      }

      System.out.println("Thread Terminated!!");
    } catch (Exception e) {
      // Throwing an exception
      System.out.println("Exception is caught");
      e.printStackTrace();
    }
    this.stop();
  }
}