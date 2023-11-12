package hr.fer.ilj.gdd.infrastructure;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hr.fer.ilj.gdd.common.DataLoader;
import hr.fer.ilj.gdd.common.TemperatureMeasurement;

public class FileDataLoader implements DataLoader {

  private Path dir;

  public FileDataLoader(Path dir) {
    this.dir = dir;
  }

  @Override
  public List<TemperatureMeasurement> loadMinValues(String sensorId, LocalDate start, LocalDate end) {
    return loadDataFromFile(start, end, dir.resolve(sensorId + "_min.csv"));
  }

  @Override
  public List<TemperatureMeasurement> loadMaxValues(String sensorId, LocalDate start, LocalDate end) {
    return loadDataFromFile(start, end, dir.resolve(sensorId + "_max.csv"));
  }

  private List<TemperatureMeasurement> loadDataFromFile(LocalDate start, LocalDate end, Path file) {
    OneFileLoader loader = new OneFileLoader(file);
    List<TemperatureMeasurement> result = new LinkedList<>();
    try {
      Map<LocalDate, Double> values = loader.load();
      LocalDate currentDate = start;
      while(currentDate.isBefore(end)) {
        Double value = values.get(currentDate);
        if(value != null)
          result.add(new TemperatureMeasurement(currentDate, value));
        currentDate = currentDate.plus(1, ChronoUnit.DAYS);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return result;
  }

}
