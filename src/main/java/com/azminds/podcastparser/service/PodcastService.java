package com.azminds.podcastparser.service;

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
import com.icosillion.podengine.models.Podcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class PodcastService {

  private static final Logger logger = LoggerFactory.getLogger(PodcastService.class);

  @Autowired
  PodcastRepository podcastRepository;
  @Autowired
  GenreRepository genreRepository;

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
    if (rslt.getFeedUrl() != null) {
      String description;
      int episodeCount;

      URL url = new URL(rslt.getFeedUrl());
      List<EpisodeEntity> episodeList = new ArrayList<>();
      try {
        Podcast podcastData = new Podcast(url);
//        System.out.println("- " + podcastData.getTitle() + " " + podcastData.getEpisodes().size());
        description = podcastData.getDescription();
        episodeCount = podcastData.getEpisodes().size();

        Collection<Episode> episodes = podcastData.getEpisodes();
        // List all episodes
        for (Episode episode : episodes) {
//          System.out.println("\n- " + episode.getGUID());
          EpisodeEntity episodeEntity = new EpisodeEntity();
          episodeEntity.setTitle(episode.getTitle());
          episodeEntity.setDescription(episode.getDescription());
          episodeEntity.setGuid(episode.getGUID());
          episodeEntity.setHostedUrl(episode.getLink().toString());
          episodeEntity.setPubDate(episode.getPubDate());
          episodeEntity.setDurationString(episode.getITunesInfo().getDuration());
          episodeEntity.setLink(episode.getEnclosure().getURL().toString());
          episodeEntity.setDuration(episode.getEnclosure().getLength());
          episodeEntity.setType(episode.getEnclosure().getType());
          episodeList.add(episodeEntity);
        }
      } catch (Exception e) {
        e.printStackTrace();
        throw new Exception("[Episode] Exception::", e);
      }

      try {
        PodcastEntity podcastEntity = new PodcastEntity();
        podcastEntity.setCollectionId(rslt.getCollectionId());
        podcastEntity.setCollectionName(rslt.getCollectionName());
        podcastEntity.setDescription(rslt.getDescription() != null ? rslt.getDescription() : description);
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
        podcastEntity.setEpisodeCount(episodeCount);

        podcastEntity.setEpisodes(episodeList);

        for (String id : rslt.getGenreIds()) {
          if (!id.equals("26")) {
            try {
              GenreEntity genreFound = genreRepository.findByGenreIdOld(Integer.parseInt(id));
              podcastEntity.addGenre(genreFound);
            } catch (Exception e) {
              throw new Exception("[Genre] Exception::", e);
            }
          }
        }

        System.out.println("before save!!!!");
        podcastRepository.save(podcastEntity);
      } catch (Exception e) {
        // Throwing an exception
        e.printStackTrace();
        throw new Exception("[Podcast] Exception::", e);
      }
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
