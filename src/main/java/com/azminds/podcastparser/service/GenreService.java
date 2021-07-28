package com.azminds.podcastparser.service;

import com.azminds.podcastparser.dao.entity.GenreEntity;
import com.azminds.podcastparser.dao.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenreService {
  @Autowired
  GenreRepository genreRepository;

  public void save(GenreEntity genre) {
    genreRepository.save(genre);
  }
}
