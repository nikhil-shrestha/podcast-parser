package com.azminds.podcastparser.dao.repository;

import com.azminds.podcastparser.dao.entity.EpisodeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeRepository extends CrudRepository<EpisodeEntity, Long> {
}