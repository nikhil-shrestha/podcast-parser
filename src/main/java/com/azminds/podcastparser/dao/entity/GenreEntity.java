package com.azminds.podcastparser.dao.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("genre")
public class GenreEntity {

  @Id
  private Long id;
  private String name;
  private Integer genreIdOld;

  public GenreEntity(Long id, String name, Integer genreIdOld) {
    this.id = id;
    this.name = name;
    this.genreIdOld = genreIdOld;
  }

  GenreEntity create(String name, Integer genreIdOld) {
    return new GenreEntity(null, name, genreIdOld);
  }

  public void setId(Long id) {
    this.id = id;
  }
}