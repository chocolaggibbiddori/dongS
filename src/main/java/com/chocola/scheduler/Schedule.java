package com.chocola.scheduler;

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

    public String toString() {
        return "[" + title + " " + startDate + " ~ " + endDate + " (" + dayOfWeek + ") " + startTime + " ~ " + endTime + "]";
    }

    @Override
    public int compareTo(Schedule s) {
        if (this.dayOfWeek != s.dayOfWeek) {
            return this.dayOfWeek.compareTo(s.dayOfWeek);
        }

        return this.startTime.compareTo(s.startTime);
    }
}
