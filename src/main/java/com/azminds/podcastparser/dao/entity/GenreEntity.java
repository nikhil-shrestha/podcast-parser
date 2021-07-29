package com.azminds.podcastparser.dao.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("genre")
public class GenreEntity {

  @Id
  private Long id;
  private String name;
  private Integer genreIdOld;

  public GenreEntity(String name, Integer genreIdOld) {
    this.name = name;
    this.genreIdOld = genreIdOld;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}