package com.azminds.podcastparser;


import com.opencsv.bean.CsvBindByPosition;

public class PodcastCSV {
  private String id;
  private String url;
  private String itunes_id;
  private String original_url;
  private String newest_item_pubdate;
  private String oldest_item_pubdate;
  private String language;



  public PodcastCSV(String id,
                    String url,
                    String itunes_id,
                    String original_url,
                    String newest_item_pubdate,
                    String oldest_item_pubdate,
                    String language) {
    this.id = id;
    this.url = url;
    this.itunes_id = itunes_id;
    this.original_url = original_url;
    this.newest_item_pubdate = newest_item_pubdate;
    this.oldest_item_pubdate = oldest_item_pubdate;
    this.language = language;


  }

  public String getId() {
    return id;
  }

  public String getItunesId() {
    return itunes_id;
  }
}
