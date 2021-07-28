package com.azminds.podcastparser.dao.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;


@Table("episode")
public class EpisodeEntity {

  @Id
  private Long id;
  private String title;
  private String description;
  private String guid;
  private String hostedUrl;
  private Date pubDate;
  private String episodeNumber;
  private String durationString;
  private Long duration;
  private String link;
  private String type;

  public EpisodeEntity(
    Long id,
    String title,
    String description,
    String guid,
    String hostedUrl,
    Date pubDate,
    String episodeNumber,
    String durationString,
    Long duration,
    String link,
    String type
  ) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.guid = guid;
    this.hostedUrl = hostedUrl;
    this.pubDate = pubDate;
    this.episodeNumber = episodeNumber;
    this.durationString = durationString;
    this.duration = duration;
    this.link = link;
    this.type = type;
  }

  EpisodeEntity create(
    String title,
    String description,
    String guid,
    String hostedUrl,
    Date pubDate,
    String episodeNumber,
    String durationString,
    Long duration,
    String link,
    String type
  ) {
    return new EpisodeEntity(
      null,
      title,
      description,
      guid,
      hostedUrl,
      pubDate,
      episodeNumber,
      durationString,
      duration,
      link,
      type
    );
  }


  public void setId(Long id) {
    this.id = id;
  }
}
