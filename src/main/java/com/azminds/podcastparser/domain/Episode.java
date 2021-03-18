package com.azminds.podcastparser.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDateTime;
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
      nullable = false
  )
  private String title;

  @Column(
      name = "description",
      nullable = false
  )
  private String description;

  @Column(
      name = "guid",
      nullable = false
  )
  private String guid;

  @Column(
      name = "link",
      nullable = false
  )
  private String link;

  @Column(
      name = "pub_date",
      nullable = false,
      columnDefinition = "TIMESTAMP"
  )
  private Date pubDate;

  @Column(name = "episode_number")
  private String episodeNumber;

  @Column(name = "duration")
  private String duration;

  @ManyToOne
  @JoinColumn(
      name = "podcast_id",
      nullable = false,
      referencedColumnName = "id",
      foreignKey = @ForeignKey(
          name = "podcast_episode_fk"
      )
  )
  private Podcast podcast;

  public Episode(String title,
                 String description,
                 String guid,
                 String link,
                 Date pubDate,
                 String episodeNumber,
                 String duration) {
    this.title = title;
    this.description = description;
    this.guid = guid;
    this.link = link;
    this.pubDate = pubDate;
    this.episodeNumber = episodeNumber;
    this.duration = duration;
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
