package hr.fer.ilj.gdd.common;

import java.time.LocalDate;
import java.util.List;

public interface DataLoader {
  List<TemperatureMeasurement> loadMinValues(String sensorId, LocalDate start, LocalDate end);
  List<TemperatureMeasurement> loadMaxValues(String sensorId, LocalDate start, LocalDate end);
}
