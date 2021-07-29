package com.azminds.podcastparser;

import com.azminds.podcastparser.dao.entity.GenreEntity;
import com.azminds.podcastparser.dao.repository.GenreRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  ApplicationRunner applicationRunner(GenreRepository genreRepository){
    return args -> {
      GenreEntity genre1 = new GenreEntity("Food", 1306);
      GenreEntity genre2 = new GenreEntity("Design", 1402);

      System.out.println(genreRepository.save(genre1));
      System.out.println(genreRepository.save(genre2));

      System.out.println(genreRepository.findByGenreIdOld(1306));
    };
  }
}