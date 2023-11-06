package hr.fer.ilj.gdd;

import java.time.LocalDate;

public record GddRequest(
    String sensorId,
    LocalDate plantingDate,
    LocalDate startDate,
    LocalDate endDate,
    double minTemp,
    double maxTemp,
    boolean cumulative) {
}