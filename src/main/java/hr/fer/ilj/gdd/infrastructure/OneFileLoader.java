package hr.fer.ilj.gdd.infrastructure;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;

public class OneFileLoader {

  private Path file;

  public OneFileLoader(Path file) {
    this.file = file;
  }

  public Map<LocalDate, Double> load() throws UnsupportedEncodingException, IOException {
    Map<LocalDate, Double> result = new TreeMap<>();
    try (Reader reader = new InputStreamReader(new BOMInputStream(Files.newInputStream(file)), "UTF-8");
        final CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());) {
      for (final CSVRecord record : parser) {
        try {
          LocalDate date = LocalDate.parse(record.get("_time").split("T")[0]);
          Double value = Double.parseDouble(record.get("_value"));
          result.put(date, value);
        } catch (IllegalArgumentException e) {
          // this record does not have time or value
        }
      }
    }
    return result;
  }
}
