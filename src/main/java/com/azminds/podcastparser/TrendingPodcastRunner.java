package com.azminds.podcastparser;

import com.azminds.podcastparser.service.PodcastService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Profile("TrendingPodcast")
@Component
@Order(value = 3)
public class TrendingPodcastRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(PodcastRunner.class);

    @Autowired
    private PodcastService service;

    public List<TrendingPodcastCSV> readCsv(String SAMPLE_CSV_FILE_PATH) {
        List<TrendingPodcastCSV> podcasts = new ArrayList<>();
        try (
                Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH))
        ) {
            CSVReader csvReader = new CSVReader(reader);

            // Reading Records One by One in a String array
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                TrendingPodcastCSV podcast = new TrendingPodcastCSV();
                podcast.setId(nextRecord[0]);
                podcast.setFeedUrl(nextRecord[1]);
                podcast.setCollectionId(nextRecord[2]);
                podcast.setReleaseDate(nextRecord[3]);
                podcast.setLanguage(nextRecord[4]);
                podcasts.add(podcast);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        }
        return podcasts;
    }

    @Override
    public void run(String... args) {
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        for (int i = 1; i <= 2;i++) {
            List<TrendingPodcastCSV> records = readCsv("FileNumber_" + i + ".csv");
            logger.debug("{}", records);
            Partition<TrendingPodcastCSV> arrayChunk = Partition.ofSize(records, 100);
            logger.debug("{}", arrayChunk);
            arrayChunk.forEach(arr -> {
                Date date = new Date();
                long now = date.getTime();
                logger.debug("Loop Start Time: " + now + Thread.currentThread().getName());
                try {
                    Collection<String> arrIds = arr.stream().map(TrendingPodcastCSV::getCollectionId).collect(Collectors.toList());
                    service.updateApplePodcast(arrIds);
                    logger.debug("Loop Time taken: " + ((new Date().getTime() - now) / 1000) + " second " + Thread.currentThread().getName());
                } catch (Exception e) {
                  e.printStackTrace();
                }
            });
        }
    }
}
