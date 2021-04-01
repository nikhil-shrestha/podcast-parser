package com.azminds.podcastparser;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

public class PodcastIndexResponse {
  private int code;
  private String content;

  PodcastIndexResponse(int code, String content) {
    this.code = code;
    this.content = content;
  }

  /**
   * Get the response from the API as a JsonObject
   * @return The response wrapped as a JsonObject
   */
  public JsonObject getJsonResponse() {
    try {
      return (JsonObject) Jsoner.deserialize(content);
    } catch (JsonException je) {
      return null;
    }
  }

  /**
   * Get the response from the API as text
   * @return The response as text
   */
  public String getRawResponse() {
    return content;
  }

  /**
   * Get the HTTP response code
   * @return 200 if all was OK, otherwise an appropriate error code
   */
  public int getResponseCode() {
    return code;
  }
}
