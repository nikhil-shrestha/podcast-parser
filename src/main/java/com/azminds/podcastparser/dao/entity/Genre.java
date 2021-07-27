package com.azminds.podcastparser.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
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
    nullable = false,
    unique = true
  )
  private String genreIdOld;

  @ManyToMany(fetch = FetchType.EAGER, mappedBy = "genres")
  private Set<Podcast> podcasts = new HashSet<>();
}