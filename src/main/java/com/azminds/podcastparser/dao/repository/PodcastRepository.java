package com.azminds.podcastparser.dao.repository;

import com.azminds.podcastparser.dao.entity.PodcastEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PodcastRepository extends CrudRepository<PodcastEntity, Long> {

  @Query("SELECT * FROM podcast WHERE collection_id = :id")
  PodcastEntity findByCollectionId(@Param("id") Long id);
}