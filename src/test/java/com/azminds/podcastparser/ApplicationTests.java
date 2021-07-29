package com.azminds.podcastparser;

import com.azminds.podcastparser.dao.repository.GenreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

  @Autowired
	private GenreRepository genreRepository;

	@Test
	@DisplayName("Create-Genre_test")
	@Order(1)
	void createGenreTest(){
	}

}
