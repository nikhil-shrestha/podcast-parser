package com.azminds.podcastparser.dao.repository;

import com.azminds.podcastparser.dao.entity.GenreEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GenreRepository extends CrudRepository<GenreEntity, Long> {

  @Transactional(readOnly = true)
  @Query("SELECT * FROM genre WHERE genre_id_old = :id")
  GenreEntity findByGenreIdOld(@Param("id") int id);
}