package com.azminds.podcastparser.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URL;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Episode")
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

  public Episode(String title,
                 String description,
                 String guid,
                 URL hostedUrl,
                 Date pubDate,
                 String durationString,
                 URL link,
                 Long duration,
                 String type
                 ) {
    this.title = title;
    this.description = description;
    this.guid = guid;
    this.hostedUrl = hostedUrl.toString();
    this.pubDate = pubDate;
    this.durationString = durationString;
    this.link = link.toString();
    this.duration = duration;
    this.type = type;
  }

  public Episode() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public String getLink() {
    return link;
  }

  public void setLink(URL link) {
    this.link = link.toString();
  }

  public Date getPubDate() {
    return pubDate;
  }

  public void setPubDate(Date pubDate) {
    this.pubDate = pubDate;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getHostedUrl() {
    return hostedUrl;
  }

  public void setHostedUrl(String hostedUrl) {
    this.hostedUrl = hostedUrl;
  }

  public String getEpisodeNumber() {
    return episodeNumber;
  }

  public void setEpisodeNumber(String episodeNumber) {
    this.episodeNumber = episodeNumber;
  }

  public Long getDuration() {
    return duration;
  }

  public void setDuration(Long duration) {
    this.duration = duration;
  }

  public String getDurationString() {
    return durationString;
  }

  public void setDurationString(String durationString) {
    this.durationString = durationString;
  }

  public Podcast getPodcast() {
    return podcast;
  }

  public void setPodcast(Podcast podcast) {
    this.podcast = podcast;
  }

  @Override
  public String toString() {
    return "Episode{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", description='" + description + '\'' +
        ", guid='" + guid + '\'' +
        ", link='" + link + '\'' +
        ", pubDate=" + pubDate +
        '}';
  }
}
