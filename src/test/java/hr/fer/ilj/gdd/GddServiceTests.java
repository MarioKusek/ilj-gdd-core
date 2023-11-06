package hr.fer.ilj.gdd;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

import hr.fer.ilj.gdd.common.DataLoader;
import hr.fer.ilj.gdd.common.TemperatureMeasurement;

class GddServiceTests {

  private class DataLoaderStub implements DataLoader {
    private List<TemperatureMeasurement> minValues, maxValues;

    public DataLoaderStub() {
      this(List.of(), List.of());
    }

    public DataLoaderStub(List<TemperatureMeasurement> minValues, List<TemperatureMeasurement> maxValues) {
      this.minValues = minValues;
      this.maxValues = maxValues;
    }

    @Override
    public List<TemperatureMeasurement> loadMinValues(String sensorId, LocalDate start, LocalDate end) {
      return minValues;
    }

    @Override
    public List<TemperatureMeasurement> loadMaxValues(String sensorId, LocalDate start, LocalDate end) {
      return maxValues;
    }
  }

  @Test
  void loadingDataCalled() {
    AtomicBoolean minCalled = new AtomicBoolean(false);
    AtomicBoolean maxCalled = new AtomicBoolean(false);
    DataLoader dataLoader = new DataLoaderStub() {
      @Override
      public List<TemperatureMeasurement> loadMinValues(String sensorId, LocalDate start, LocalDate end) {
        minCalled.set(true);
        return super.loadMinValues(sensorId, start, end);
      }

      @Override
      public List<TemperatureMeasurement> loadMaxValues(String sensorId, LocalDate start, LocalDate end) {
        maxCalled.set(true);
        return super.loadMaxValues(sensorId, start, end);
      }

    };

    GddService service = new GddService(dataLoader);

    service.search(new GddRequest(null, LocalDate.now(), LocalDate.now(), LocalDate.now(), 0, 0, false));

    assertThat(minCalled).isTrue();
    assertThat(maxCalled).isTrue();
  }

  @Test
  void getDummyDDValueList() throws Exception {
    DataLoader loader = new DataLoaderStub();
    GddService service = new GddService(loader);

    List<DDValue> ddValueList = service.search(new GddRequest(null, LocalDate.of(2022, 11, 1), LocalDate.of(2022, 11, 1), LocalDate.of(2022, 11, 5), 0, 0, false));

    assertThat(ddValueList).hasSize(5);
  }

  @Test
  void getDDValueList() throws Exception {
    DataLoader loader = new DataLoaderStub(
        List.of(new TemperatureMeasurement(LocalDate.of(2022, 11, 2), 5.0)),
        List.of(new TemperatureMeasurement(LocalDate.of(2022, 11, 2), 20.0)));
    GddService service = new GddService(loader);

    List<DDValue> ddValueList = service.search(new GddRequest(null, LocalDate.of(2022, 11, 1), LocalDate.of(2022, 11, 1), LocalDate.of(2022, 11, 3), 10, 30, false));

    assertThat(ddValueList)
      .containsExactly(
          new DDValue(LocalDate.of(2022, 11, 1), 0),
          new DDValue(LocalDate.of(2022, 11, 2), 2.5),
          new DDValue(LocalDate.of(2022, 11, 3), 0)
          );
  }

  @Test
  void getCumulativeDDValueList() throws Exception {
    DataLoader loader = new DataLoaderStub(
        List.of(
            new TemperatureMeasurement(LocalDate.of(2022, 11, 2), 5.0),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 3), 10.0)),
        List.of(
            new TemperatureMeasurement(LocalDate.of(2022, 11, 2), 20.0),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 3), 25.0)));
    GddService service = new GddService(loader);

    List<DDValue> ddValueList = service.search(new GddRequest(null, LocalDate.of(2022, 11, 1), LocalDate.of(2022, 11, 1), LocalDate.of(2022, 11, 3), 10, 30, true));

    assertThat(ddValueList)
    .containsExactly(
        new DDValue(LocalDate.of(2022, 11, 1), 0),
        new DDValue(LocalDate.of(2022, 11, 2), 2.5),
        new DDValue(LocalDate.of(2022, 11, 3), 10)
        );
  }

}
