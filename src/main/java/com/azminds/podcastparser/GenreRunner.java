package com.azminds.podcastparser;

import com.azminds.podcastparser.dao.entity.GenreEntity;
import com.azminds.podcastparser.dao.repository.GenreRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Profile("test")
@Component
@Order(value = 2)
public class GenreRunner implements CommandLineRunner {

  private static final String API_ENDPOINT = "https://3rugykz490.execute-api.us-west-1.amazonaws.com/dev/apple/podcast/genre";

  @Autowired
  private GenreRepository genreRepository;

  @Override
  public void run(String... args) throws IOException {
    Long count = genreRepository.count();
    System.out.println(count);
    if (count == 0) {
      URL urlForGetRequest = new URL(API_ENDPOINT);
      String readLine = null;
      HttpURLConnection conn = (HttpURLConnection) urlForGetRequest.openConnection();
      conn.setRequestMethod("GET");
      int responseCode = conn.getResponseCode();

      if (responseCode == HttpURLConnection.HTTP_OK) {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuffer response = new StringBuffer();
        while ((readLine = in.readLine()) != null) {
          response.append(readLine);
        }
        in.close();
        // print result
        String json = response.toString();
        JSONArray jsonArr = new JSONArray(json);
        jsonArr.forEach(item -> {
          JSONObject obj = (JSONObject) item;
          GenreEntity genre = new GenreEntity();
          genre.setGenreIdOld(obj.get("id").toString());
          genre.setName(obj.get("name").toString());
          genreRepository.save(genre);
        });
      } else {
        System.out.println("GET NOT WORKED");
      }
    }
  }
}
