package hr.fer.ilj.gdd.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import hr.fer.ilj.gdd.common.DataLoader;
import hr.fer.ilj.gdd.common.TemperatureMeasurement;

public class FileDataLoaderTest {

  @Test
  void loadMaxData() throws Exception {
    DataLoader loader = new FileDataLoader(Path.of("data", "sensorData"));

    List<TemperatureMeasurement> values = loader.loadMaxValues("0004A30B00F7EC6D", LocalDate.of(2022, 7, 5),
        LocalDate.of(2022, 7, 6));

    assertThat(values)
      .hasSize(1)
      .containsExactly(new TemperatureMeasurement(LocalDate.of(2022, 7, 5), 35.5));

  }

  @Test
  void noDataFile() throws Exception {
    DataLoader loader = new FileDataLoader(Path.of("data", "sensorData"));

    List<TemperatureMeasurement> values = loader.loadMaxValues("notExisting", LocalDate.of(2022, 7, 5),
        LocalDate.of(2022, 7, 6));

    assertThat(values).hasSize(0);
  }

  @Test
  void loadMaxDataForMoreDays() throws Exception {
    DataLoader loader = new FileDataLoader(Path.of("data", "sensorData"));

    List<TemperatureMeasurement> values = loader.loadMaxValues("0004A30B00F7EC6D", LocalDate.of(2022, 7, 5),
        LocalDate.of(2022, 7, 7));

    assertThat(values)
      .hasSize(2)
      .usingRecursiveComparison()
      .withEqualsForType((a, b) -> Math.abs(a - b) < 1E-2, Double.class)
      .isEqualTo(List.of(
          new TemperatureMeasurement(LocalDate.of(2022, 7, 5), 35.5),
          new TemperatureMeasurement(LocalDate.of(2022, 7, 6), 20.9)));

  }

  @Test
  void loadMinDataForMoreDays() throws Exception {
    DataLoader loader = new FileDataLoader(Path.of("data", "sensorData"));

    List<TemperatureMeasurement> values = loader.loadMinValues("0004A30B00F7EC6D", LocalDate.of(2022, 7, 5),
        LocalDate.of(2022, 7, 7));

    assertThat(values)
      .hasSize(2)
      .usingRecursiveComparison()
      .withEqualsForType((a, b) -> Math.abs(a - b) < 1E-2, Double.class)
      .isEqualTo(List.of(
          new TemperatureMeasurement(LocalDate.of(2022, 7, 5), 20.0),
          new TemperatureMeasurement(LocalDate.of(2022, 7, 6), 16.7)));

  }
}
