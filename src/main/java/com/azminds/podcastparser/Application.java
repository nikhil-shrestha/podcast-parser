package com.azminds.podcastparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
  exclude = { CsvSplitRunner.class }
)
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}