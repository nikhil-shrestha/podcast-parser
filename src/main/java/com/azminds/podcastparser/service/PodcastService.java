package com.azminds.podcastparser.service;

import com.azminds.podcastparser.dao.entity.PodcastEntity;
import com.azminds.podcastparser.dao.repository.PodcastRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PodcastService {
  @Autowired
  PodcastRepository podcastRepository;

  public void save(PodcastEntity podcast) {
//    log.info(podcast.toString());
    podcastRepository.save(podcast);
  }
}
