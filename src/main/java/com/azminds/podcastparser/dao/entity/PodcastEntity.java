package com.azminds.podcastparser.dao.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Table("podcast")
public class PodcastEntity {

  @Id
  private Long id;
  private String wrapperType;
  private String kind;
  private Long collectionId;
  private String artistName;
  private String collectionName;
  private String artistViewUrl;
  private String collectionViewUrl;
  private String feedUrl;
  private String previewUrl;
  private String artworkUrl30;
  private String artworkUrl60;
  private String artworkUrl100;
  private String artworkUrl512;
  private String artworkUrl600;
  private String releaseDate;
  private Integer trackCount;
  private String copyright;
  private String country;
  private String shortDescription;
  private String longDescription;
  private String description;
  private String currentVersionReleaseDate;
  private Integer episodeCount;

  private List<EpisodeEntity> episodes;

  private Set<GenreRef> genres = new HashSet<>();

  public PodcastEntity() {
  }

  public PodcastEntity(
    String wrapperType,
    String kind,
    Long collectionId,
    String artistName,
    String collectionName,
    String artistViewUrl,
    String collectionViewUrl,
    String feedUrl,
    String previewUrl,
    String artworkUrl30,
    String artworkUrl60,
    String artworkUrl100,
    String artworkUrl512,
    String artworkUrl600,
    String releaseDate,
    Integer trackCount,
    String copyright,
    String country,
    String shortDescription,
    String longDescription,
    String description,
    String currentVersionReleaseDate,
    Integer episodeCount
  ) {
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
    this.shortDescription = shortDescription;
    this.longDescription = longDescription;
    this.description = description;
    this.currentVersionReleaseDate = currentVersionReleaseDate;
    this.episodeCount = episodeCount;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void addEpisodes(EpisodeEntity episode) {
    this.episodes.add(episode);
  }

  public void addGenre(GenreEntity genre){
    this.genres.add(new GenreRef(genre.getId()));
  }


  @Override
  public String toString() {
    return "PodcastEntity{" +
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
      ", episodeCount=" + episodeCount +
      '}';
  }
}