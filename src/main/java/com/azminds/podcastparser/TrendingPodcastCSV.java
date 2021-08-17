package com.azminds.podcastparser;


public class TrendingPodcastCSV {
    private String id;
    private String collection_id;
    private String feed_url;
    private String release_date;
    private String language;

    public TrendingPodcastCSV() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeedUrl() {
        return feed_url;
    }

    public void setFeedUrl(String feed_url) {
        this.feed_url = feed_url;
    }

    public String getCollectionId() {
        return collection_id;
    }

    public void setCollectionId(String collection_id) {
        this.collection_id = collection_id;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public void setReleaseDate(String release_date) {
        this.release_date = release_date;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
