package hr.fer.ilj.gdd.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import hr.fer.ilj.gdd.DDValue;
import hr.fer.ilj.gdd.common.TemperatureMeasurement;


class GddCalculatorTest {

  @Test
  void valuesForEmptyMeasurements() {
    GddCalculator calc = new GddCalculator(LocalDate.of(2022, 10, 1), LocalDate.of(2022, 11, 1), LocalDate.of(2022, 11, 3), List.of(), List.of(), 10, 30);

    assertThat(calc.getNotCumulativeDDValueList())
      .hasSize(3)
      .allMatch(v -> v != null)
      .allMatch(v -> v.value() >= 0)
      .allMatch(v -> v.value() < 1E-5);
  }

  @Test
  void calculateOneDDValue() throws Exception {
    double tMaxLimit = 30;
    double tMinLimit = 10;

    assertThat(GddCalculator.calculateDDValue(10, 10, tMinLimit, tMaxLimit)).isCloseTo(0, offset(1E-5));
    assertThat(GddCalculator.calculateDDValue(0, 0, tMinLimit, tMaxLimit)).isCloseTo(0, offset(1E-5));
    assertThat(GddCalculator.calculateDDValue(10, 35, tMinLimit, tMaxLimit)).isCloseTo(10, offset(1E-5));

    assertThat(GddCalculator.calculateDDValue(10, 23, tMinLimit, tMaxLimit)).isCloseTo(6.5, offset(1E-5));
  }

  @Test
  void calculateNotCumulativeWithValuesAndStartDateBeforePlantDate() throws Exception {
    GddCalculator calc = new GddCalculator(LocalDate.of(2022, 11, 1), LocalDate.of(2022, 10, 31), LocalDate.of(2022, 11, 5),
        List.of(
            new TemperatureMeasurement(LocalDate.of(2022, 10, 31), 10),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 1), 10),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 2), 0),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 3), 10),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 4), 10)
            ),
        List.of(
            new TemperatureMeasurement(LocalDate.of(2022, 10, 31), 11),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 1), 11),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 2), 12),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 3), 35),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 4), 23)
            ), 10, 30);

    assertThat(calc.getNotCumulativeDDValueList())
      .hasSize(6)
      .contains(
          new DDValue(LocalDate.of(2022, 10, 31), 0),
          new DDValue(LocalDate.of(2022, 11, 1), 0.5),
          new DDValue(LocalDate.of(2022, 11, 2), 0),
          new DDValue(LocalDate.of(2022, 11, 3), 10),
          new DDValue(LocalDate.of(2022, 11, 4), 6.5),
          new DDValue(LocalDate.of(2022, 11, 5), 0)
          );
  }

  @Test
  void calculateNotCumulativeWithValuesAndStartDateAfterPlantDate() throws Exception {
    GddCalculator calc = new GddCalculator(LocalDate.of(2022, 11, 4), LocalDate.of(2022, 11, 1), LocalDate.of(2022, 11, 5),
        List.of(
            new TemperatureMeasurement(LocalDate.of(2022, 10, 31), 10),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 1), 10),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 2), 0),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 3), 10),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 4), 10)
            ),
        List.of(
            new TemperatureMeasurement(LocalDate.of(2022, 10, 31), 11),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 1), 10),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 2), 0),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 3), 35),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 4), 23)
            ), 10, 30);

    assertThat(calc.getNotCumulativeDDValueList())
    .hasSize(5)
    .contains(
        new DDValue(LocalDate.of(2022, 11, 1), 0),
        new DDValue(LocalDate.of(2022, 11, 2), 0),
        new DDValue(LocalDate.of(2022, 11, 3), 0),
        new DDValue(LocalDate.of(2022, 11, 4), 6.5),
        new DDValue(LocalDate.of(2022, 11, 5), 0)
        );
  }

  @Test
  void calculateCumulativeWithValuesAndStartDateBeforePlantDate() throws Exception {
    GddCalculator calc = new GddCalculator(LocalDate.of(2022, 11, 1), LocalDate.of(2022, 10, 31), LocalDate.of(2022, 11, 5),
        List.of(
            new TemperatureMeasurement(LocalDate.of(2022, 10, 31), 10),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 1), 10),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 2), 0),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 3), 10),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 4), 10)
            ),
        List.of(
            new TemperatureMeasurement(LocalDate.of(2022, 10, 31), 11),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 1), 11),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 2), 3),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 3), 35),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 4), 23)
            ), 10, 30);

    assertThat(calc.getCumulativeDDValueList())
      .hasSize(6)
      .contains(
          new DDValue(LocalDate.of(2022, 10, 31), 0),
          new DDValue(LocalDate.of(2022, 11, 1), 0.5),
          new DDValue(LocalDate.of(2022, 11, 2), 0.5),
          new DDValue(LocalDate.of(2022, 11, 3), 10.5),
          new DDValue(LocalDate.of(2022, 11, 4), 17),
          new DDValue(LocalDate.of(2022, 11, 5), 17)
          );
  }

  @Test
  void calculateCumulativeWithValuesAndStartDateAfterPlantDate() throws Exception {
    GddCalculator calc = new GddCalculator(LocalDate.of(2022, 10, 31), LocalDate.of(2022, 11, 2), LocalDate.of(2022, 11, 5),
        List.of(
            new TemperatureMeasurement(LocalDate.of(2022, 10, 31), 10),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 1), 10),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 2), 0),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 3), 10),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 4), 10)
            ),
        List.of(
            new TemperatureMeasurement(LocalDate.of(2022, 10, 31), 11),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 1), 14),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 2), 12),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 3), 35),
            new TemperatureMeasurement(LocalDate.of(2022, 11, 4), 23)
            ), 10, 30);

    assertThat(calc.getCumulativeDDValueList())
    .hasSize(4)
    .contains(
        new DDValue(LocalDate.of(2022, 11, 2), 2.5),
        new DDValue(LocalDate.of(2022, 11, 3), 12.5),
        new DDValue(LocalDate.of(2022, 11, 4), 19),
        new DDValue(LocalDate.of(2022, 11, 5), 19)
        );
  }
}
