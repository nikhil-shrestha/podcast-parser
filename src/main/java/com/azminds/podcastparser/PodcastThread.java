package com.azminds.podcastparser;

import be.ceau.itunesapi.response.Result;
import com.azminds.podcastparser.domain.Genre;
import com.azminds.podcastparser.repository.GenreRepository;
import com.azminds.podcastparser.repository.PodcastRepository;
import com.icosillion.podengine.models.Episode;
import com.icosillion.podengine.models.Podcast;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.Collection;
import java.util.Optional;

class GenreData {
  String genreId;
  String genreName;
}

public class PodcastThread extends Thread {

  private Result rslt;

  public PodcastThread(Result rslt) {
    this.rslt = rslt;
  }

  @Autowired
  PodcastRepository podcastRepository;

  @Autowired
  GenreRepository genreRepository;

  public void run(){
    try {
      System.out.println(
        "Thread " + Thread.currentThread().getId()
          + " is running");

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
        Optional<Genre> genreDbData = this.genreRepository.findByGenreIdOld(genre.genreId);
        com.azminds.podcastparser.domain.Genre genreEntity;

        if (!genreDbData.isPresent()) {
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

      System.out.println("before save!!!!");
      this.podcastRepository.save(podcastEntity);
      Thread.sleep(50);
    } catch (Exception e) {
      // Throwing an exception
      System.out.println("Exception is caught");
    }
  }
}
