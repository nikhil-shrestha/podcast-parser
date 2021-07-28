package com.azminds.podcastparser.dao.entity;



import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


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

  public PodcastEntity(
    Long id,
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
    this.id = id;
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

  PodcastEntity create(
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
  ){
    return new PodcastEntity(
      null,
      wrapperType,
      kind,
      collectionId,
      artistName,
      collectionName,
      artistViewUrl,
      collectionViewUrl,
      feedUrl,
      previewUrl,
      artworkUrl30,
      artworkUrl60,
      artworkUrl100,
      artworkUrl512,
      artworkUrl600,
      releaseDate,
      trackCount,
      copyright,
      country,
      shortDescription,
      longDescription,
      description,
      currentVersionReleaseDate,
      episodeCount
    );
  }

  public void setId(Long id) {
    this.id = id;
  }
}