package hr.fer.ilj.gdd.common;

import java.time.LocalDate;

public record TemperatureMeasurement(
    LocalDate date,
    double value
    ) {

}
