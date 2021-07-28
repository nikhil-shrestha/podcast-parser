package com.azminds.podcastparser.dao.repository;

import com.azminds.podcastparser.dao.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {

  @Query("SELECT s FROM Genre s WHERE s.genreIdOld = :id")
  GenreEntity findByGenreIdOld(@Param("id") String id);
}