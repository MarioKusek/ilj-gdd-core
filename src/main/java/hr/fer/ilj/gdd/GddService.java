package hr.fer.ilj.gdd;

import java.util.List;

import hr.fer.ilj.gdd.common.DataLoader;
import hr.fer.ilj.gdd.common.TemperatureMeasurement;
import hr.fer.ilj.gdd.internal.GddCalculator;

public class GddService {

  private DataLoader dataLoader;

  public GddService(DataLoader dataLoader) {
    this.dataLoader = dataLoader;
  }

  public List<DDValue> search(GddRequest request) {
    List<TemperatureMeasurement> maxValues = dataLoader.loadMaxValues(request.sensorId(), request.plantingDate(), request.endDate());
    List<TemperatureMeasurement> minValues = dataLoader.loadMinValues(request.sensorId(), request.plantingDate(), request.endDate());

    GddCalculator calculator = new GddCalculator(request.plantingDate(), request.startDate(), request.endDate(), minValues, maxValues, request.minTemp(), request.maxTemp());
    if(request.cumulative())
      return calculator.getCumulativeDDValueList();
    else
      return calculator.getNotCumulativeDDValueList();
  }

}
