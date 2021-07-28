package com.azminds.podcastparser.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity(name = "Podcast")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "podcast")
public class Podcast implements Serializable {

  @Id
  @SequenceGenerator(
    name = "podcast_sequence",
    sequenceName = "podcast_sequence",
    allocationSize = 1
  )
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "podcast_sequence"
  )
  @Column(
    name = "id",
    updatable = false
  )
  private Long id;

  @Column(name = "wrapper_type")
  private String wrapperType;

  @Column(name = "kind")
  private String kind;

  @Column(
    name = "collection_id",
    nullable = false
  )
  private Long collectionId;

  @Column(name = "artist_name")
  private String artistName;

  @Column(
    name = "collection_name",
    nullable = false
  )
  private String collectionName;

  @Column(name = "artist_view_url")
  private String artistViewUrl;

  @Column(name = "collection_view_url")
  private String collectionViewUrl;

  @Column(name = "feed_url")
  private String feedUrl;

  @Column(name = "preview_url")
  private String previewUrl;

  @Column(name = "artwork_url_30")
  private String artworkUrl30;

  @Column(name = "artwork_url_60")
  private String artworkUrl60;

  @Column(name = "artwork_url_100")
  private String artworkUrl100;

  @Column(name = "artwork_url_512")
  private String artworkUrl512;

  @Column(name = "artwork_url_600")
  private String artworkUrl600;

  @Column(name = "release_date")
  private String releaseDate;

  @Column(name = "track_count")
  private Integer trackCount;

  @Column(name = "copyright")
  private String copyright;

  @Column(name = "country")
  private String country;

  @Column(
    name = "short_description",
    columnDefinition = "TEXT"
  )
  private String shortDescription;

  @Column(
    name = "long_description",
    columnDefinition = "TEXT",
    length = 1024
  )
  private String longDescription;

  @Column(
    name = "description",
    columnDefinition = "TEXT",
    length = 500
  )
  private String description;

  @Column(name = "current_version_release_date")
  private String currentVersionReleaseDate;

  @Column(name = "episode_count")
  private Integer episodeCount;

  @ManyToMany(
    fetch = FetchType.LAZY,
    cascade = {CascadeType.MERGE}
  )
  @JoinTable(
    name = "podcast_genre",
    joinColumns = @JoinColumn(name = "podcast_id"),
    inverseJoinColumns = @JoinColumn(name = "genre_id")
  )
  private Set<Genre> genres = new HashSet<>();

  @OneToMany(
    cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
    mappedBy = "podcast"
  )
  private List<Episode> episodes = new ArrayList<>();

  public void addGenre(Genre genre) {
    if (!this.episodes.contains(genre)) {
      this.genres.add(genre);
    }
  }

  public void addEpisode(Episode episode) {
    if (!this.episodes.contains(episode)) {
      this.episodes.add(episode);
      episode.setPodcast(this);
    }
  }

  public void removeEpisode(Episode episode) {
    if (this.episodes.contains(episode)) {
      this.episodes.remove(episode);
      episode.setPodcast(null);
    }
  }
}