package com.azminds.podcastparser;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Scanner;

@Profile("TrendingPodcast")
@Component
@Order(value = 1)
public class CsvSplitRunner implements CommandLineRunner {
  public static void splitLargeFile(
    final String fileName,
    final String extension,
    final int maxLines) {

    try (Scanner s = new Scanner(new FileReader(String.format("%s.%s", fileName, extension)))) {
      int file = 0;
      int cnt = 0;
      BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s_%d.%s", fileName, file, extension)));

      while (s.hasNext()) {
        writer.write(s.next() + System.lineSeparator());
        if (++cnt == maxLines && s.hasNext()) {
          writer.close();
          writer = new BufferedWriter(new FileWriter(String.format("%s_%d.%s", fileName, ++file, extension)));
          cnt = 0;
        }
      }
      writer.close();
    } catch (Exception e) {
//      e.printStackTrace();
    }
  }

  public void myFunction(int lines, int files) throws FileNotFoundException, IOException {
    String inputfile = "file.csv";
    BufferedReader br = new BufferedReader(new FileReader(inputfile)); //reader for input file intitialized only once

    // URL path = Main.class.getResource("file.csv");
    // BufferedReader br = new BufferedReader(new FileReader(path.getFile())); //reader for input file intitialized only once
    String strLine = null;
    for (int i = 1; i <= files; i++) {
      FileWriter fstream1 = new FileWriter("FileNumber_" + i + ".csv"); //creating a new file writer.
      BufferedWriter out = new BufferedWriter(fstream1);
      for (int j = 0; j < lines; j++) {   //iterating the reader to read only the first few lines of the csv as defined earlier
        strLine = br.readLine();
        if (strLine != null) {
          String strar[] = strLine.split(",");
          if (!strar[0].equals("id")) {     // removing heading row of the csv file
            out.write(strLine);
            out.newLine();
          }
        }
      }
      out.close();
    }
  }

  @Override
  public void run(String... args) {
    try {
      int lines = 2;  //set this to whatever number of lines you need in each file
      int count = 0;
      String inputfile = "file.csv";
      File file = new File(inputfile);
      Scanner scanner = new Scanner(file);
      while (scanner.hasNextLine()) {  //counting the lines in the input file
        scanner.nextLine();
        count++;
      }
      System.out.println(count);
      int files = 0;
      if ((count % lines) == 0) {
        files = (count / lines);
      } else {
        files = (count / lines) + 1;
      }
      System.out.println(files); //number of files that shall eb created

      myFunction(lines, files);
    } catch (IOException e) {
//      e.printStackTrace();
    }
  }
}
