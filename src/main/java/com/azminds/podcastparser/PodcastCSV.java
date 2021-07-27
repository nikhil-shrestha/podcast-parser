package com.azminds.podcastparser;


public class PodcastCSV {
  private String id;
  private String url;
  private String itunes_id;
  private String original_url;
  private String newest_item_pubdate;
  private String oldest_item_pubdate;
  private String language;

  public PodcastCSV() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getItunesId() {
    return itunes_id;
  }

  public void setItunesId(String itunes_id) {
    this.itunes_id = itunes_id;
  }

  public String getOriginalUrl() {
    return original_url;
  }

  public void setOriginalUrl(String original_url) {
    this.original_url = original_url;
  }

  public String getNewestItemPubdate() {
    return newest_item_pubdate;
  }

  public void setNewestItemPubdate(String newest_item_pubdate) {
    this.newest_item_pubdate = newest_item_pubdate;
  }

  public String getOldestItemPubdate() {
    return oldest_item_pubdate;
  }

  public void setOldestItemPubdate(String oldest_item_pubdate) {
    this.oldest_item_pubdate = oldest_item_pubdate;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }
}
