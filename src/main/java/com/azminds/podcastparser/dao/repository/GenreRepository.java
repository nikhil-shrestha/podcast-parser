package com.azminds.podcastparser.dao.repository;

import com.azminds.podcastparser.dao.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {

  @Query("SELECT s FROM Genre s WHERE s.genreIdOld = ?1")
  Optional<GenreEntity> findByGenreIdOld(String id);
}