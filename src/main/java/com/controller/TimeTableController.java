package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.entity.academic.TimeTableEntry;
import com.repository.StudentRepository;
import com.repository.TimeTableEntryRepository;

@RestController
@RequestMapping("/api/admin/timetable")
@CrossOrigin(origins = "*")
public class TimeTableController {

    @Autowired
    private TimeTableEntryRepository timeTableRepo;
    private StudentRepository studentRepo;

    @GetMapping("/class/{classRoomId}")
    public ResponseEntity<List<TimeTableEntry>> getByClass(@PathVariable Long classRoomId) {
        List<TimeTableEntry> entries = timeTableRepo.findByClassRoomIdOrderByDayOfWeekAscPeriodAsc(classRoomId);
        return ResponseEntity.ok(entries);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<TimeTableEntry>> saveBulk(@RequestBody List<TimeTableEntry> entries) {
        List<TimeTableEntry> saved = timeTableRepo.saveAll(entries);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timeTableRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/class/{classRoomId}")
    public ResponseEntity<Void> deleteByClass(@PathVariable Long classRoomId) {
        timeTableRepo.deleteByClassRoomId(classRoomId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<TimeTableEntry>> getByStudent(@PathVariable Long studentId) {


    	Long classId = studentRepo.findById(studentId).get().getClassRoom().getId();
        List<TimeTableEntry> entries = timeTableRepo.findByClassRoomIdOrderByDayOfWeekAscPeriodAsc(classId);
        return ResponseEntity.ok(entries);
    }
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<TimeTableEntry>> getByTeacher(@PathVariable Long teacherId) {
        List<TimeTableEntry> entries = timeTableRepo.findByTeacherIdOrderByDayOfWeekAscPeriodAsc(teacherId);
        return ResponseEntity.ok(entries);
    }

    @GetMapping
    public ResponseEntity<List<TimeTableEntry>> getAll() {
        return ResponseEntity.ok(timeTableRepo.findAll());
    }


    @GetMapping("/")
    public ResponseEntity<String> root() {
        return ResponseEntity.ok("Timetable Controller Root OK");
    }


}
