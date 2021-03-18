package com.azminds.podcastparser.repository;

import com.azminds.podcastparser.domain.Podcast;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PodcastRepository extends CrudRepository<Podcast, Long> {
}