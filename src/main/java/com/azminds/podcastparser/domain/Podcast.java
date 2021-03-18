package com.azminds.podcastparser.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity(name = "Podcast")
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

  @Column(name = "short_description")
  private String shortDescription;

  @Column(name = "long_description")
  private String longDescription;

  @Column(
      name = "description",
      columnDefinition = "TEXT"
  )
  private String description;

  @Column(name = "current_version_release_date")
  private String currentVersionReleaseDate;

  @Column(name = "episode_count")
  private Integer episodeCount;

  @ManyToMany
  @JoinTable(
      name = "podcast_genre",
      joinColumns = @JoinColumn(
          name = "podcast_id",
          foreignKey = @ForeignKey(name = "podcast_id_fk")
      ),
      inverseJoinColumns = @JoinColumn(
          name = "genre_id",
          foreignKey = @ForeignKey(name = "genre_id_fk")
      )
  )
  private Set<Genre> genres;

  @OneToMany(
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      mappedBy = "podcast"
  )
  private List<Episode> episodes = new ArrayList<>();

  public Podcast() {
  }

  public Podcast(
      Long collectionId,
      String collectionName,
      String description,
      String collectionViewUrl,
      String artistName,
      String artistViewUrl,
      String wrapperType,
      String kind,
      String feedUrl,
      String previewUrl,
      String artworkUrl30,
      String artworkUrl60,
      String artworkUrl100,
      String artworkUrl512,
      String artworkUrl600,
      String releaseDate,
      Integer trackCount,
      String country,
      String copyright,
      String shortDescription,
      String longDescription) {
    this.wrapperType = wrapperType;
    this.kind = kind;
    this.collectionId = collectionId;
    this.artistName = artistName;
    this.collectionName = collectionName;
    this.artistViewUrl = artistViewUrl;
    this.collectionViewUrl = collectionViewUrl;
    this.feedUrl = feedUrl;
    this.previewUrl = previewUrl;
    this.artworkUrl30 = artworkUrl30;
    this.artworkUrl60 = artworkUrl60;
    this.artworkUrl100 = artworkUrl100;
    this.artworkUrl512 = artworkUrl512;
    this.artworkUrl600 = artworkUrl600;
    this.releaseDate = releaseDate;
    this.trackCount = trackCount;
    this.copyright = copyright;
    this.country = country;
    this.description = description;
    this.shortDescription = shortDescription;
    this.longDescription = longDescription;
  }

  public void setWrapperType(String wrapperType) {
    this.wrapperType = wrapperType;
  }

  public void setKind(String kind) {
    this.kind = kind;
  }

  public void setCollectionId(Long collectionId) {
    this.collectionId = collectionId;
  }

  public void setArtistName(String artistName) {
    this.artistName = artistName;
  }

  public void setCollectionName(String collectionName) {
    this.collectionName = collectionName;
  }

  public void setArtistViewUrl(String artistViewUrl) {
    this.artistViewUrl = artistViewUrl;
  }

  public void setCollectionViewUrl(String collectionViewUrl) {
    this.collectionViewUrl = collectionViewUrl;
  }

  public void setFeedUrl(String feedUrl) {
    this.feedUrl = feedUrl;
  }

  public void setPreviewUrl(String previewUrl) {
    this.previewUrl = previewUrl;
  }

  public void setArtworkUrl30(String artworkUrl30) {
    this.artworkUrl30 = artworkUrl30;
  }

  public void setArtworkUrl60(String artworkUrl60) {
    this.artworkUrl60 = artworkUrl60;
  }

  public void setArtworkUrl100(String artworkUrl100) {
    this.artworkUrl100 = artworkUrl100;
  }

  public void setArtworkUrl512(String artworkUrl512) {
    this.artworkUrl512 = artworkUrl512;
  }

  public void setArtworkUrl600(String artworkUrl600) {
    this.artworkUrl600 = artworkUrl600;
  }

  public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }

  public void setTrackCount(Integer trackCount) {
    this.trackCount = trackCount;
  }

  public void setCopyright(String copyright) {
    this.copyright = copyright;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public void setLongDescription(String longDescription) {
    this.longDescription = longDescription;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setEpisodeCount(Integer episodeCount) {
    this.episodeCount = episodeCount;
  }

  public void addGenre(Genre genre) {
    genres.add(genre);
    genre.getPodcasts().add(this);
  }

  public void removeGenre(Genre genre) {
    genres.remove(genre);
    genre.getPodcasts().remove(this);
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

  public List<Episode> getEpisodes() {
    return episodes;
  }


  @Override
  public String toString() {
    return "Podcast{" +
        "id=" + id +
        ", wrapperType='" + wrapperType + '\'' +
        ", kind='" + kind + '\'' +
        ", collectionId=" + collectionId +
        ", artistName='" + artistName + '\'' +
        ", collectionName='" + collectionName + '\'' +
        ", artistViewUrl='" + artistViewUrl + '\'' +
        ", collectionViewUrl='" + collectionViewUrl + '\'' +
        ", feedUrl='" + feedUrl + '\'' +
        ", previewUrl='" + previewUrl + '\'' +
        ", artworkUrl30='" + artworkUrl30 + '\'' +
        ", artworkUrl60='" + artworkUrl60 + '\'' +
        ", artworkUrl100='" + artworkUrl100 + '\'' +
        ", artworkUrl512='" + artworkUrl512 + '\'' +
        ", artworkUrl600='" + artworkUrl600 + '\'' +
        ", releaseDate='" + releaseDate + '\'' +
        ", trackCount=" + trackCount +
        ", copyright='" + copyright + '\'' +
        ", country='" + country + '\'' +
        ", shortDescription='" + shortDescription + '\'' +
        ", longDescription='" + longDescription + '\'' +
        ", description='" + description + '\'' +
        ", currentVersionReleaseDate='" + currentVersionReleaseDate + '\'' +
        '}';
  }
}