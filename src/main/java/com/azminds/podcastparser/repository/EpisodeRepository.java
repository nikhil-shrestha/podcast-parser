package com.azminds.podcastparser.repository;

import com.azminds.podcastparser.domain.Episode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeRepository extends CrudRepository<Episode, Long> {
}