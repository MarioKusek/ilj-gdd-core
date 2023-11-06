package hr.fer.ilj.gdd.internal;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import hr.fer.ilj.gdd.DDValue;
import hr.fer.ilj.gdd.common.TemperatureMeasurement;

public class GddCalculator {

  private LocalDate plantingDate;
  private LocalDate startDate;
  private LocalDate endDate;
  private double tMinLimit;
  private double tMaxLimit;
  private Map<LocalDate, Double> minValues;
  private Map<LocalDate, Double> maxValues;

  public GddCalculator(LocalDate plantingDate, LocalDate startDate, LocalDate endDate, List<TemperatureMeasurement> minValuesList,
      List<TemperatureMeasurement> maxValuesList, double tMinLimit, double tMaxLimit) {
        this.plantingDate = plantingDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tMinLimit = tMinLimit;
        this.tMaxLimit = tMaxLimit;

        minValues = new HashMap<>();
        maxValues = new HashMap<>();

        transform(minValues, minValuesList);
        transform(maxValues, maxValuesList);
  }

  private void transform(Map<LocalDate, Double> mapValues, List<TemperatureMeasurement>listValues) {
    listValues.forEach(v -> mapValues.put(v.date(), v.value()));
  }

  public List<DDValue> getNotCumulativeDDValueList() {

    return getDDValues(calculateStartDateForData(), endDate)
        .filter(value -> !value.date().isBefore(startDate))
        .toList();
  }

  private LocalDate calculateStartDateForData() {
    LocalDate start;
    if(plantingDate.isBefore(startDate))
      start = plantingDate;
    else
      start = startDate;
    return start;
  }

  private Stream<DDValue> getDDValues(LocalDate start, LocalDate end) {
    return start.datesUntil(end.plusDays(1))
      .map(date -> {
        if(date.isBefore(plantingDate))
          return new DDValue(date, 0);
        else
          return new DDValue(date, GddCalculator.calculateDDValue(findMinValue(date), findMaxValue(date), tMinLimit, tMaxLimit));
      });
  }

  public List<DDValue> getCumulativeDDValueList() {
    List<DDValue> values = getDDValues(calculateStartDateForData(), endDate).toList();
    List<DDValue> cumulativeValues = new LinkedList<>();
    double currentValue = 0;
    for(DDValue value: values) {
      currentValue += value.value();
      cumulativeValues.add(new DDValue(value.date(), currentValue));
    }

    return cumulativeValues
        .stream()
        .filter(value -> !value.date().isBefore(startDate))
        .toList();
  }

  private double findMinValue(LocalDate date) {
    return minValues.getOrDefault(date, 0.0);
  }

  private double findMaxValue(LocalDate date) {
    return maxValues.getOrDefault(date, 0.0);
  }

  public static double calculateDDValue(double tMin, double tMax, double tMinLimit, double tMaxLimit) {
    if(tMax > tMaxLimit)
      tMax = tMaxLimit;

    double ddValue = (tMin + tMax) / 2 - tMinLimit;
    if(ddValue < 0)
      ddValue = 0;
    return ddValue;
  }

}
