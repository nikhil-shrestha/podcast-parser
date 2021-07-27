package com.azminds.podcastparser.service;

import com.azminds.podcastparser.dao.entity.Genre;
import com.azminds.podcastparser.dao.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenreService {
  @Autowired
  GenreRepository genreRepository;

  public void save(Genre genre) {
    genreRepository.save(genre);
  }
}
