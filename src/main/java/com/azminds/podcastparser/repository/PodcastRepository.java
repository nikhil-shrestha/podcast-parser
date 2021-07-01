package com.azminds.podcastparser.repository;

import com.azminds.podcastparser.domain.Podcast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PodcastRepository extends JpaRepository<Podcast, Long> {

  @Query("SELECT s FROM Podcast s WHERE s.collectionId = ?1")
  Optional<Podcast> findByCollectionId(Long id);

}