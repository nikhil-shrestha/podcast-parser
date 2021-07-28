package com.azminds.podcastparser.dao.repository;

import com.azminds.podcastparser.dao.entity.EpisodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeRepository extends JpaRepository<EpisodeEntity, Long> {
}