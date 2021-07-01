package com.azminds.podcastparser.repository;

import com.azminds.podcastparser.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

  @Query("SELECT s FROM Genre s WHERE s.genreIdOld = ?1")
  Optional<Genre> findByGenreIdOld(String id);
}