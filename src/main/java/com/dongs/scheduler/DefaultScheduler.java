package com.dongs.scheduler;

import com.dongs.common.exception.InvalidExtensionException;
import com.dongs.settings.Setting;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.dongs.common.Paths.getResourcesPathInMain;

@Slf4j
final class DefaultScheduler extends EnumMap<DayOfWeek, Set<Schedule>> implements Scheduler {

    private static final DefaultScheduler INSTANCE = new DefaultScheduler(DayOfWeek.class);

    static {
        INSTANCE.put(DayOfWeek.MONDAY, new HashSet<>());
        INSTANCE.put(DayOfWeek.TUESDAY, new HashSet<>());
        INSTANCE.put(DayOfWeek.WEDNESDAY, new HashSet<>());
        INSTANCE.put(DayOfWeek.THURSDAY, new HashSet<>());
        INSTANCE.put(DayOfWeek.FRIDAY, new HashSet<>());
        INSTANCE.put(DayOfWeek.SATURDAY, new HashSet<>());
        INSTANCE.put(DayOfWeek.SUNDAY, new HashSet<>());
    }

    private DefaultScheduler(Class<DayOfWeek> keyType) {
        super(keyType);
    }

    public static DefaultScheduler getInstance() {
        return INSTANCE;
    }

    @Override
    public void readAndInspect(String csvPath) throws InvalidExtensionException, FileNotFoundException {
        Objects.requireNonNull(csvPath, "csvPath is null");

        try (CsvReader reader = new CsvReader(csvPath)) {
            List<Schedule> scheduleList = reader.readSchedules();
            inspect(scheduleList);
            autoRemove(csvPath, scheduleList);
        } catch (InvalidExtensionException | FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }

    private void inspect(Collection<Schedule> schedules) {
        for (Schedule schedule : schedules) {
            DayOfWeek key = schedule.getDayOfWeek();
            Set<Schedule> scheduleSet = get(key);
            boolean clash = false;

            for (Schedule sch : scheduleSet) {
                if (isClash(sch, schedule)) {
                    log.info("[Clash] \"{}\" is impossible. Cause: {}", schedule.getTitle(), sch);
                    clash = true;
                    break;
                }
            }

            if (!clash) {
                scheduleSet.add(schedule);
            }
        }
    }

    private boolean isClash(Schedule s1, Schedule s2) {
        LocalDate s1StartDate = s1.getStartDate();
        LocalDate s1EndDate = s1.getEndDate();
        LocalDate s2StartDate = s2.getStartDate();
        LocalDate s2EndDate = s2.getEndDate();

        if (s2StartDate.isAfter(s1EndDate)) {
            return false;
        }

        if (s2EndDate.isBefore(s1StartDate)) {
            return false;
        }

        LocalTime s1StartTime = s1.getStartTime();
        LocalTime s1EndTime = s1.getEndTime();
        LocalTime s2StartTime = s2.getStartTime();
        LocalTime s2EndTime = s2.getEndTime();

        if ((s2StartTime.isAfter(s1StartTime) || s2StartTime.equals(s1StartTime)) && s2StartTime.isBefore(s1EndTime)) {
            return true;
        }

        if (s2StartTime.isBefore(s1StartTime) && s2EndTime.isAfter(s1StartTime)) {
            return true;
        }

        return false;
    }

    private void autoRemove(String csvPath, List<Schedule> scheduleList) {
        Setting setting = Setting.getInstance();
        boolean autoRemove = setting.schedule().autoRemove().value();
        if (!autoRemove) return;

        File dataFile = new File(csvPath);
        List<Schedule> newScheduleList = scheduleList.stream()
                .filter(s -> {
                    LocalDate endDate = s.getEndDate();
                    return !endDate.isBefore(LocalDate.now());
                })
                .toList();

        boolean removed = scheduleList.size() > newScheduleList.size();
        createBackupDataFile(dataFile, removed);
        dataFile.delete();
        createDataFileByAutoRemove(dataFile, newScheduleList);
    }

    private void createBackupDataFile(File file, boolean removed) {
        if (!removed) return;

        String backupDataFilePath = getResourcesPathInMain("backup/", file.getName(), "");
        Path path = file.toPath();
        Path target = Paths.get(backupDataFilePath);

        try {
            Files.deleteIfExists(target);
            Files.copy(path, target);
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }

    private void createDataFileByAutoRemove(File file, List<Schedule> scheduleList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Schedule schedule : scheduleList) {
                String csvString = schedule.toCsvString();

                writer.append(csvString);
                writer.newLine();
            }

            writer.flush();
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }
}
