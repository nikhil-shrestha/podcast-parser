package com.azminds.podcastparser.service;

import be.ceau.itunesapi.Lookup;
import be.ceau.itunesapi.request.Entity;
import be.ceau.itunesapi.response.Response;
import be.ceau.itunesapi.response.Result;
import com.azminds.podcastparser.TrendingPodcastCSV;
import com.azminds.podcastparser.dao.entity.EpisodeEntity;
import com.azminds.podcastparser.dao.entity.PodcastEntity;
import com.azminds.podcastparser.dao.repository.EpisodeRepository;
import com.azminds.podcastparser.dao.repository.PodcastRepository;
import com.icosillion.podengine.models.Episode;
import com.icosillion.podengine.models.Podcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class PodcastService {

  private static final Logger logger = LoggerFactory.getLogger(PodcastService.class);

  @Autowired
  PodcastRepository podcastRepository;
  @Autowired
  EpisodeRepository episodeRepository;

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

//  //Episodes
//  public List<Episode> getLatestEpisodes(Podcast podcast, String prevReleaseDate) throws Exception{
//    // Create Field object
//    Field privateField
//            = Podcast.class.getDeclaredField("channelElement");
//
//    // Set the accessibility as true
//    privateField.setAccessible(true);
//
//    // Store the value of private field in variable
//    Element channelElement = (Element) privateField.get(podcast);
//    List<Episode> episodes = new ArrayList<>();
//    for (Object itemObject : channelElement.elements("item")) {
//      if (!(itemObject instanceof Element)) {
//        continue;
//      }
//      Episode episode = new Episode((Element) itemObject);
//      System.out.println("episode >>> with published date "+ episode.getPubDate());
//      if (ComparingDates(episode.getPubDate(),StringToDate(prevReleaseDate)) > 0) {
//        System.out.println("Episode added ");
//        episodes.add(episode);
//      } else {
//        break; // The episodes in the rss feed are always sorted in ascending order and when we encounter episode published date lesser than release date, we break the loop
//      }
//    }
//
//    if (episodes.size() == 0) {
//      return null;
//    }
//
//    return Collections.unmodifiableList(episodes);
//  }

  @Transactional
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
        // System.out.println("- " + podcastData.getTitle() + " " + podcastData.getEpisodes().size());
        podcastEntity.setDescription(podcastData.getDescription() != null ? podcastData.getDescription() : "");
        podcastEntity.setEpisodeCount(podcastData.getEpisodes().size());
        System.out.println("Podcast before save!!!!");
        podcastRepository.saveAndFlush(podcastEntity);

        Collection<Episode> episodes = podcastData.getEpisodes();
        List<EpisodeEntity> episodeEntityList = new ArrayList<>();

        for (Episode episode : episodes) {
          String epiDescription = "";
          try {
            epiDescription = episode.getDescription();
          } catch (Exception e) {
          }

          long epiDuration = 0;
          try {
            epiDuration = episode.getEnclosure().getLength();
          } catch (Exception e) {
          }

          EpisodeEntity episodeEntity = new EpisodeEntity();
          episodeEntity.setTitle(episode.getTitle());
          episodeEntity.setDescription(epiDescription);
          episodeEntity.setGuid(episode.getGUID());
          episodeEntity.setHostedUrl(episode.getLink() != null ? episode.getLink().toString() : null);
          episodeEntity.setPubDate(episode.getPubDate());
          episodeEntity.setDurationString(episode.getITunesInfo().getDuration() != null ? episode.getITunesInfo().getDuration() : null);
          episodeEntity.setLink(episode.getEnclosure().getURL().toString());
          episodeEntity.setDuration(epiDuration != 0 ? epiDuration : null);
          episodeEntity.setType(episode.getEnclosure().getType() != null ? episode.getEnclosure().getType() : null);
          episodeEntity.setPodcastId(podcastEntity.getId());

          episodeEntityList.add(episodeEntity);
        }
        episodeRepository.saveAllAndFlush(episodeEntityList);
        System.out.println("episode added!!");
      } catch (Exception e) {
        e.printStackTrace();
        throw new Exception("[Episode] Exception::", e);
      }
    } catch (Exception e) {
      // Throwing an exception
      e.printStackTrace();
      throw new Exception("[Podcast] Exception is caught::", e);
    }
  }

  @Transactional
  public void updatePodcastFinal(Result rslt) throws Exception {
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
        // System.out.println("- " + podcastData.getTitle() + " " + podcastData.getEpisodes().size());
        podcastEntity.setDescription(podcastData.getDescription() != null ? podcastData.getDescription() : "");
        podcastEntity.setEpisodeCount(podcastData.getEpisodes().size());
        System.out.println("Podcast before save!!!!");
        podcastRepository.saveAndFlush(podcastEntity);

        Collection<Episode> episodes = podcastData.getEpisodes();
        List<EpisodeEntity> episodeEntityList = new ArrayList<>();

        for (Episode episode : episodes) {
          String epiDescription = "";
          try {
            epiDescription = episode.getDescription();
          } catch (Exception e) {
          }

          long epiDuration = 0;
          try {
            epiDuration = episode.getEnclosure().getLength();
          } catch (Exception e) {
          }

          EpisodeEntity episodeEntity = new EpisodeEntity();
          episodeEntity.setTitle(episode.getTitle());
          episodeEntity.setDescription(epiDescription);
          episodeEntity.setGuid(episode.getGUID());
          episodeEntity.setHostedUrl(episode.getLink() != null ? episode.getLink().toString() : null);
          episodeEntity.setPubDate(episode.getPubDate());
          episodeEntity.setDurationString(episode.getITunesInfo().getDuration() != null ? episode.getITunesInfo().getDuration() : null);
          episodeEntity.setLink(episode.getEnclosure().getURL().toString());
          episodeEntity.setDuration(epiDuration != 0 ? epiDuration : null);
          episodeEntity.setType(episode.getEnclosure().getType() != null ? episode.getEnclosure().getType() : null);
          episodeEntity.setPodcastId(podcastEntity.getId());

          episodeEntityList.add(episodeEntity);
        }
        episodeRepository.saveAllAndFlush(episodeEntityList);
        System.out.println("episode added!!");
      } catch (Exception e) {
        e.printStackTrace();
        throw new Exception("[Episode] Exception::", e);
      }
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
//          e.printStackTrace();
        }
      }
    });
    long end = System.currentTimeMillis();
    logger.info("Total time {}", (end - start));
    return CompletableFuture.completedFuture("complete!!");
  }

  @Async
  public CompletableFuture<String> updateApplePodcast(TrendingPodcastCSV trendingPodcast) throws Exception {
    long start = System.currentTimeMillis();

    PodcastEntity podcastEntity = podcastRepository.getById(trendingPodcast.getId());

    if (isUrlValid(trendingPodcast.getFeedUrl())) {
      try {
        URL url = new URL(trendingPodcast.getFeedUrl());
        try {
          Podcast podcastData = new Podcast(url);
          // System.out.println("- " + podcastData.getTitle() + " " + podcastData.getEpisodes().size());
          podcastEntity.setEpisodeCount(podcastData.getEpisodes().size());
          System.out.println("Podcast before save!!!!");
          podcastRepository.save(podcastEntity);

          Collection<Episode> episodes = podcastData.getEpisodes();
          List<EpisodeEntity> episodeEntityList = new ArrayList<>();

          for (Episode episode : episodes) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date oldPubDate = sdf.parse(podcastEntity.getReleaseDate());
            Date newPubDate = episode.getPubDate();

            if (!oldPubDate.after(newPubDate) && !newPubDate.before(oldPubDate)) {
              String epiDescription = "";
              try {
                epiDescription = episode.getDescription();
              } catch (Exception e) {
              }

              long epiDuration = 0;
              try {
                epiDuration = episode.getEnclosure().getLength();
              } catch (Exception e) {
              }

              EpisodeEntity episodeEntity = new EpisodeEntity();
              episodeEntity.setTitle(episode.getTitle());
              episodeEntity.setDescription(epiDescription);
              episodeEntity.setGuid(episode.getGUID());
              episodeEntity.setHostedUrl(episode.getLink() != null ? episode.getLink().toString() : null);
              episodeEntity.setPubDate(episode.getPubDate());
              episodeEntity.setDurationString(episode.getITunesInfo().getDuration() != null ? episode.getITunesInfo().getDuration() : null);
              episodeEntity.setLink(episode.getEnclosure().getURL().toString());
              episodeEntity.setDuration(epiDuration != 0 ? epiDuration : null);
              episodeEntity.setType(episode.getEnclosure().getType() != null ? episode.getEnclosure().getType() : null);
              episodeEntity.setPodcastId(podcastEntity.getId());

              episodeEntityList.add(episodeEntity);
            } else {
              break;
            }
          }

          episodeRepository.saveAllAndFlush(episodeEntityList);
        } catch (Exception e) {
          e.printStackTrace();
        }
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }
    }
    long end = System.currentTimeMillis();
    logger.info("Total time {}", (end - start));
    return CompletableFuture.completedFuture("complete!!");
  }
}
