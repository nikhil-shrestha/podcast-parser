package com.azminds.podcastparser;

import com.azminds.podcastparser.dao.repository.GenreRepository;
import com.azminds.podcastparser.dao.repository.PodcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Profile("test")
@Component
@Order(value = 3)
public class PodcastRunner implements CommandLineRunner {

  @Autowired
  private PodcastRepository podcastRepository;
  @Autowired
  private GenreRepository genreRepository;

  @Override
  public void run(String... args) throws Exception {
    System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

    Date date = new Date();
    long now = date.getTime();
    System.out.println("Start Time: " + now);
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setCorePoolSize(100);
    threadPoolTaskExecutor.setMaxPoolSize(100);
    threadPoolTaskExecutor.setKeepAliveSeconds(3000);
    threadPoolTaskExecutor.setQueueCapacity(500);
    // The thread pool needs to be initialized!
    threadPoolTaskExecutor.initialize();
    // 100 threads
    for (int i = 0; i < 10; i++) {
      PodcastThread pdThread = new PodcastThread(podcastRepository, genreRepository, i + 1);
      threadPoolTaskExecutor.submit(pdThread);
    }
    System.out.println("Total Time taken: " + ((new Date().getTime() - now) / 1000) + " second ");
  }
}
