package com.azminds.podcastparser.service;

import com.azminds.podcastparser.dao.entity.EpisodeEntity;
import com.azminds.podcastparser.dao.repository.EpisodeRepository;
import com.icosillion.podengine.models.Episode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class EpisodeService {

  private static final Logger logger = LoggerFactory.getLogger(PodcastService.class);

  @Autowired
  EpisodeRepository episodeRepository;

  @Transactional
  public void saveEpisodeFinal(List<Episode> episodes, Long podcastId) throws Exception {

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
      episodeEntity.setPodcastId(podcastId);

      episodeEntityList.add(episodeEntity);
    }
    episodeRepository.saveAllAndFlush(episodeEntityList);
    System.out.println("episode added!!");
  }
}
