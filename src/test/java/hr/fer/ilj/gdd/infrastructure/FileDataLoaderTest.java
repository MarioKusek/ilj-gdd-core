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
    DataLoader loader = new FileDataLoader(Path.of("data"));

    List<TemperatureMeasurement> values = loader.loadMaxValues("fakeId", LocalDate.of(2022, 7, 5), LocalDate.of(2022, 7, 6));

    assertThat(values)
      .hasSize(1)
      .containsExactly(new TemperatureMeasurement(LocalDate.of(2022, 7, 5), 35.5));

  }

}
