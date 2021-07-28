package com.azminds.podcastparser.dao.repository;

import com.azminds.podcastparser.dao.entity.GenreEntity;
import com.azminds.podcastparser.dao.entity.PodcastEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends CrudRepository<GenreEntity, Long> {

  @Query("SELECT s FROM Genre s WHERE s.genre_id_old = :id")
  Optional<PodcastEntity> findByGenreIdOld(@Param("id") Long id);
}