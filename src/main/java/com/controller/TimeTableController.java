package com.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.entity.academic.TimeTableEntry;
import com.repository.StudentRepository;
import com.repository.TimeTableEntryRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Timetable", description = "Manage and view class timetables")
@RestController
@RequestMapping("/api/admin/timetable")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TimeTableController {

    private final TimeTableEntryRepository timeTableRepo;
    private final StudentRepository studentRepo;

    @Operation(summary = "Get timetable by class")
    @GetMapping("/class/{classRoomId}")
    public ResponseEntity<List<TimeTableEntry>> getByClass(@PathVariable Long classRoomId) {
        return ResponseEntity.ok(timeTableRepo.findByClassRoomIdOrderByDayOfWeekAscPeriodAsc(classRoomId));
    }

    @Operation(summary = "Save bulk timetable entries")
    @PostMapping("/bulk")
    public ResponseEntity<List<TimeTableEntry>> saveBulk(@RequestBody List<TimeTableEntry> entries) {
        return ResponseEntity.ok(timeTableRepo.saveAll(entries));
    }

    @Operation(summary = "Delete timetable entry by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timeTableRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete all timetable entries for a class")
    @DeleteMapping("/class/{classRoomId}")
    public ResponseEntity<Void> deleteByClass(@PathVariable Long classRoomId) {
        timeTableRepo.deleteByClassRoomId(classRoomId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get timetable by student")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<TimeTableEntry>> getByStudent(@PathVariable Long studentId) {
        Long classId = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"))
                .getClassRoom().getId();
        return ResponseEntity.ok(timeTableRepo.findByClassRoomIdOrderByDayOfWeekAscPeriodAsc(classId));
    }

    @Operation(summary = "Get timetable by teacher")
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<TimeTableEntry>> getByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(timeTableRepo.findByTeacherIdOrderByDayOfWeekAscPeriodAsc(teacherId));
    }

    @Operation(summary = "Get all timetable entries")
    @GetMapping
    public ResponseEntity<List<TimeTableEntry>> getAll() {
        return ResponseEntity.ok(timeTableRepo.findAll());
    }
}
