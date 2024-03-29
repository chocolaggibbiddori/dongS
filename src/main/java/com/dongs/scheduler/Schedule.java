package com.dongs.scheduler;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class Schedule implements Comparable<Schedule> {

    private final String title;
    private final DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate startDate;
    private LocalDate endDate;

    private Schedule(String title, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Schedule of(String title, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate) {
        return new Schedule(title, dayOfWeek, startTime, endTime, startDate, endDate);
    }

    @Override
    public int compareTo(Schedule s) {
        if (this.dayOfWeek != s.dayOfWeek) {
            return this.dayOfWeek.compareTo(s.dayOfWeek);
        }

        return this.startTime.compareTo(s.startTime);
    }

    @Override
    public String toString() {
        return "[%s %s ~ %s (%s) %s ~ %s]".formatted(title, startDate, endDate, dayOfWeek, startTime, endTime);
    }

    public String toCsvString() {
        return "%s|%s|%s|%s|%s|%s".formatted(
                title,
                startDate,
                endDate,
                getDayOfWeekToString(),
                startTime,
                endTime
        );
    }

    public String getDayOfWeekToString() {
        return switch (dayOfWeek) {
            case MONDAY -> "월";
            case TUESDAY -> "화";
            case WEDNESDAY -> "수";
            case THURSDAY -> "목";
            case FRIDAY -> "금";
            case SATURDAY -> "토";
            case SUNDAY -> "일";
        };
    }
}
