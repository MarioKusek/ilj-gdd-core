package hr.fer.ilj.gdd.infrastructure;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import hr.fer.ilj.gdd.common.DataLoader;
import hr.fer.ilj.gdd.common.TemperatureMeasurement;

public class FileDataLoader implements DataLoader {

  private Path dir;

  public FileDataLoader(Path dir) {
    this.dir = dir;
  }

  @Override
  public List<TemperatureMeasurement> loadMinValues(String sensorId, LocalDate start, LocalDate end) {
    throw new UnsupportedOperationException();

  }

  @Override
  public List<TemperatureMeasurement> loadMaxValues(String sensorId, LocalDate start, LocalDate end) {
    return List.of(new TemperatureMeasurement(LocalDate.of(2022, 7, 5), 35.5));
  }

}
