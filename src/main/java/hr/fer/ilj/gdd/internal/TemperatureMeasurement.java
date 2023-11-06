package hr.fer.ilj.gdd.internal;

import java.time.LocalDate;

public record TemperatureMeasurement(
    LocalDate date,
    double value
    ) {

}
