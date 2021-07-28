package com.azminds.podcastparser.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Entity(name = "Episode")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "episode")
public class Episode implements Serializable {

  @Id
  @SequenceGenerator(
    name = "episode_sequence",
    sequenceName = "episode_sequence",
    allocationSize = 1
  )
  @GeneratedValue(
    strategy = SEQUENCE,
    generator = "episode_sequence"
  )
  @Column(
    name = "id",
    updatable = false
  )
  private Long id;

  @Column(
    name = "title",
    nullable = false,
    columnDefinition = "TEXT"
  )
  private String title;

  @Column(
    name = "description",
    columnDefinition = "TEXT"
  )
  private String description;

  @Column(
    name = "guid",
    nullable = false
  )
  private String guid;

  @Column(name = "hosted_url")
  private String hostedUrl;

  @Column(
    name = "pub_date",
    nullable = false,
    columnDefinition = "TIMESTAMP"
  )
  private Date pubDate;

  @Column(name = "episode_number")
  private String episodeNumber;

  @Column(name = "duration_string")
  private String durationString;

  @Column(name = "duration")
  private Long duration;

  @Column(
    name = "link",
    nullable = false
  )
  private String link;

  @Column(name = "type")
  private String type;

  @ManyToOne
  @JoinColumn(
    name = "podcast_id",
    nullable = false,
    referencedColumnName = "id"
  )
  private Podcast podcast;
}
