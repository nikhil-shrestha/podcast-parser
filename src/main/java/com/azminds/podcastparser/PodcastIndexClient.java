package com.azminds.podcastparser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Map;

public class PodcastIndexClient {
  private String key;
  private String secret;

  public static final String PI_API_URL = "https://api.podcastindex.org/api/1.0";

  /**
   * Instantiate an instance of the client
   *
   * @param key       The API key issued by podcastindex.org
   * @param secret    The API secret issued by podcastindex.org
   * @param userAgent What you wish to be identified as to podcastindex.org
   */
  public PodcastIndexClient(String key, String secret) {
    this.key = key;
    this.secret = secret;
  }

  private String authHeader(long epoch) throws UnsupportedEncodingException, NoSuchAlgorithmException {
    String auth = key + secret + epoch;

    MessageDigest crypt = MessageDigest.getInstance("SHA-1");
    crypt.reset();
    crypt.update(auth.getBytes("UTF-8"));

    return byteToHex(crypt.digest());
  }

  /**
   * Make a call to the podcastindex.org API
   *
   * @param apiEndpoint The API that should be called, e.g. "/api/1.0/podcasts/byfeedurl"
   * @param parameters  A map of the parameters that should be passed to the API
   * @return The response from the API. You should check that a 200 response code has been returned
   * @throws IOException
   * @throws UnsupportedEncodingException
   * @throws NoSuchAlgorithmException
   */
  public PodcastIndexResponse callAPI(String apiEndpoint, Map<String, String> parameters) throws IOException, UnsupportedEncodingException, NoSuchAlgorithmException {

    long start = System.currentTimeMillis();

    if (apiEndpoint.startsWith("/")) {
      apiEndpoint = PI_API_URL + apiEndpoint;
    }

    StringBuilder p = new StringBuilder(apiEndpoint);
    if (parameters != null) {
      int i = 0;
      for (String k : parameters.keySet()) {
        if (i++ == 0)
          p.append('?');
        else
          p.append('&');
        p.append(k).append('=').append(URLEncoder.encode(parameters.get(k), "UTF-8"));
      }
    }

    URL u = new URL(p.toString());
    HttpsURLConnection conn = (HttpsURLConnection) u.openConnection();
    conn.setRequestMethod("GET");

    long epoch = System.currentTimeMillis() / 1000;
    conn.setRequestProperty("X-Auth-Date", Long.toString(epoch));
    conn.setRequestProperty("X-Auth-Key", key);
    conn.setRequestProperty("Authorization", authHeader(epoch));

    conn.setDoOutput(false);
    conn.setDoInput(true);

    int code = conn.getResponseCode();

    StringBuilder content = new StringBuilder();
    if (code == 200) {
      try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
        String l = br.readLine();
        while (l != null) {
          content.append(l).append("\n");
          l = br.readLine();
        }
      }
    } else {
      try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
        String l = br.readLine();
        while (l != null) {
          content.append(l).append("\n");
          l = br.readLine();
        }
      }
    }

    long end = System.currentTimeMillis();

    // Check performance
    // System.out.println(apiEndpoint + " " + (end - start) + "ms");

    return new PodcastIndexResponse(code, content.toString());
  }

  private String byteToHex(byte[] binary) {
    try (Formatter formatter = new Formatter()) {
      for (byte b : binary) {
        formatter.format("%02x", b);
      }
      return formatter.toString();
    }
  }
}
