package hr.fer.ilj.gdd.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class OneFileLoaderTest {

  @Test
  void loadOneFilefile() throws Exception {
    Path file = Path.of("data", "sensorData", "0004A30B00F7EC6D_max.csv");

    OneFileLoader loader = new OneFileLoader(file);

    Map<LocalDate, Double> result = loader.load();
    assertThat(result.get(LocalDate.of(2022, 7, 5))).isCloseTo(35.5, offset(1E-2));
    assertThat(result.get(LocalDate.of(2022, 11, 14))).isCloseTo(24.5, offset(1E-2));
  }
}
