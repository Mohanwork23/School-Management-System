package com.repository;

import com.entity.academic.TimeTableEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TimeTableEntryRepository extends JpaRepository<TimeTableEntry, Long> {
    List<TimeTableEntry> findByClassRoomIdOrderByDayOfWeekAscPeriodAsc(Long classRoomId);

	void deleteByClassRoomId(Long classRoomId);

	List<TimeTableEntry> findByTeacherIdOrderByDayOfWeekAscPeriodAsc(Long teacherId);
}
