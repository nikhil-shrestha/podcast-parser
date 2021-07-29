package com.azminds.podcastparser.dao.entity;

import org.springframework.data.relational.core.mapping.Table;

@Table("podcast_genre")
public class GenreRef {
  private Long genre;

  public GenreRef(Long genre) {
    this.genre = genre;
  }
}
