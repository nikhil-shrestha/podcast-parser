package com.azminds.podcastparser.domain;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Genre")
@Table(name = "genre")
public class Genre implements Serializable {

  @Id
  @SequenceGenerator(
      name = "genre_sequence",
      sequenceName = "genre_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = SEQUENCE,
      generator = "genre_sequence"
  )
  @Column(
      name = "id",
      updatable = false
  )
  private Long id;

  @Column(
      name = "name",
      nullable = false
  )
  private String name;

  @Column(
      name = "genre_id_old",
      nullable = false
  )
  private String genreIdOld;

  @ManyToMany(mappedBy = "genres")
  private Set<Podcast> podcasts;

  public Genre(String name, String genreIdOld) {
    this.name = name;
    this.genreIdOld = genreIdOld;
  }

  public Genre() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGenreIdOld() {
    return genreIdOld;
  }

  public void setGenreIdOld(String genreIdOld) {
    this.genreIdOld = genreIdOld;
  }

  public Set<Podcast> getPodcasts() {
    return podcasts;
  }

  @Override
  public String toString() {
    return "Genre{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", genreIdOld='" + genreIdOld + '\'' +
        '}';
  }
}
