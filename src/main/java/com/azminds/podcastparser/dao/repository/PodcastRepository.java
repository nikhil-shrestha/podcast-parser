package com.azminds.podcastparser.dao.repository;

import com.azminds.podcastparser.dao.entity.PodcastEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PodcastRepository extends JpaRepository<PodcastEntity, Long> {

  @Query("SELECT s FROM Podcast s WHERE s.collectionId = ?1")
  Optional<PodcastEntity> findByCollectionId(Long id);

}