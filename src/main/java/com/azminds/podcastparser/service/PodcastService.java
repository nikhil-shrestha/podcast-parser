package com.azminds.podcastparser.service;

import be.ceau.itunesapi.Lookup;
import be.ceau.itunesapi.request.Entity;
import be.ceau.itunesapi.response.Response;
import be.ceau.itunesapi.response.Result;
import com.azminds.podcastparser.dao.entity.EpisodeEntity;
import com.azminds.podcastparser.dao.entity.PodcastEntity;
import com.azminds.podcastparser.dao.repository.PodcastRepository;
import com.icosillion.podengine.models.Episode;
import com.icosillion.podengine.models.Podcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
public class PodcastService {

  private static final Logger logger = LoggerFactory.getLogger(PodcastService.class);

  @Autowired
  PodcastRepository podcastRepository;

  static class GenreData {
    Long id;
    String name;
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

  public Collection<Result> getBatchPodcast(Collection<String> arrIds) {
    Response response = new Lookup()
      .setIds(arrIds)
      .setEntity(Entity.PODCAST)
      .execute();

    return response.getResults();
  }

  public void savePodcastFinal(Result rslt) throws Exception {
    try {
      PodcastEntity podcastEntity = new PodcastEntity();
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

      GenreData[] genreData = new GenreData[rslt.getGenreIds().size()];
      int i = 0;
      for (String id : rslt.getGenreIds()) {
        genreData[i] = new GenreData();
        genreData[i].id = Long.parseLong(id);
        i++;
      }

      i = 0;
      for (String name : rslt.getGenres()) {
        genreData[i].name = name;
        i++;
      }

      Set<String> genresMap = new HashSet<>();
      for (GenreData genre : genreData) {
        if (genre.id != 26 || !genre.name.equals("Podcasts")) {
          genresMap.add(genre.name);
        }
      }
      podcastEntity.setGenres(genresMap);

      URL url = new URL(rslt.getFeedUrl());
      try {
        Podcast podcastData = new Podcast(url);
//        System.out.println("- " + podcastData.getTitle() + " " + podcastData.getEpisodes().size());
        podcastEntity.setDescription(podcastData.getDescription() != null ? podcastData.getDescription() : "");
        podcastEntity.setEpisodeCount(podcastData.getEpisodes().size());

        Collection<Episode> episodes = podcastData.getEpisodes();
        // List all episodes
        for (Episode episode : episodes) {
//          System.out.println("\n- " + episode.getGUID());

          String epiDescription = "";
          try {
            epiDescription = episode.getDescription();
          } catch (Exception e){

          }

          long epiDuration = 0;
          try{
            epiDuration = episode.getEnclosure().getLength();
          } catch (Exception e){

          }

          EpisodeEntity episodeEntity = new EpisodeEntity();
          episodeEntity.setTitle(episode.getTitle());
          episodeEntity.setDescription(epiDescription);
          episodeEntity.setGuid(episode.getGUID());
          episodeEntity.setHostedUrl(episode.getLink() != null ? episode.getLink().toString() : null);
          episodeEntity.setPubDate(episode.getPubDate());
          episodeEntity.setDurationString(episode.getITunesInfo().getDuration()  != null ? episode.getITunesInfo().getDuration(): null);
          episodeEntity.setLink(episode.getEnclosure().getURL().toString());
          episodeEntity.setDuration(epiDuration != 0 ? epiDuration : null);
          episodeEntity.setType(episode.getEnclosure().getType() != null ? episode.getEnclosure().getType() : null);

          System.out.println("episode added!!");
          podcastEntity.addEpisode(episodeEntity);
        }
      } catch (Exception e) {
        e.printStackTrace();
        throw new Exception("[Episode] Exception::", e);
      }
      System.out.println("Podcast before save!!!!");
      podcastRepository.save(podcastEntity);
    } catch (Exception e) {
      // Throwing an exception
      e.printStackTrace();
      throw new Exception("[Podcast] Exception is caught::", e);
    }
  }

  @Async
  public CompletableFuture<String> saveApplePodcast(Collection<String> arrIds) throws Exception {
    long start = System.currentTimeMillis();

    Collection<Result> results = getBatchPodcast(arrIds);
    logger.info("Result {}", results.size(), "" + Thread.currentThread().getName());
    results.forEach(rslt -> {
      if (isUrlValid(rslt.getFeedUrl())) {
        try {
          savePodcastFinal(rslt);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    long end = System.currentTimeMillis();
    logger.info("Total time {}", (end - start));
    return CompletableFuture.completedFuture("complete!!");
  }
}
