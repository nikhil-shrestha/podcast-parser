package com.azminds.podcastparser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TrendingPodcastCSV {
  private Long id;
  private Long collectionId;
  private String feedUrl;
  private Date lastEpisodeDate;

  public TrendingPodcastCSV() {
  }

  public Long getId() {
    return id;
  }

  public void setId(String id) {
    this.id = Long.parseLong(id);
  }

  public String getFeedUrl() {
    return feedUrl;
  }

  public void setFeedUrl(String feed_url) {
    this.feedUrl = feed_url;
  }

  public Long getCollectionId() {
    return collectionId;
  }

  public void setCollectionId(String collection_id) {
    this.collectionId = Long.parseLong(collection_id);
  }

  public Date getLastEpisodeDate() {
    return lastEpisodeDate;
  }

  public void setLastEpisodeDate(String lastEpisodeDate) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    try {
      this.lastEpisodeDate = sdf.parse(lastEpisodeDate);
    } catch (Exception e) {
      System.out.println("Error occurred " + e.getMessage());
    }
  }
}
