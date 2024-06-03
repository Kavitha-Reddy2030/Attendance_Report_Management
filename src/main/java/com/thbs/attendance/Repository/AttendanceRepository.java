package com.thbs.attendance.Repository;
 
import com.thbs.attendance.Entity.Attendance;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
 
@Repository
public interface AttendanceRepository extends MongoRepository<Attendance, String> {
	
    Attendance findByUserIdAndCourseIdAndBatchId(Long userId, Long batchId, Long courseId);
 
    List<Attendance> findByBatchIdAndCourseId(Long batchId,Long courseId);
    
    @Query("{ 'batchId' : ?0, 'userId' : { $in: ?1 }, 'attendance.date' : { $gte: ?2, $lte: ?3 } }")
    List<Attendance> findByBatchIdAndUserIdInAndAttendance_DateBetween(Long batchId, List<Long> userIds, String startDate, String endDate);

	List<Attendance> findByUserIdAndAttendance_DateBetween(Long employeeId, String startDate, String endDate);

	List<Attendance> findByBatchIdAndUserIdAndAttendance_DateBetween(Long batchId, Long employeeId, String startDate,
			String endDate);

	List<Attendance> findByBatchIdAndCourseIdAndUserIdInAndAttendance_DateBetween(Long batchId, Long courseId,
			List<Long> employeeIds, String startDate, String endDate);

	List<Attendance> findByBatchIdAndCourseIdAndUserIdAndAttendance_DateBetween(Long batchId, Long courseId,
			Long employeeId, String startDate, String endDate);
 
	List<Attendance> findByBatchId(Long batchId);

	List<Attendance> findByBatchIdAndCourseIdAndUserIdInAndAttendance_Date(Long batchId, Long courseId,
			List<Long> employeeIds, String date);

	List<Attendance> findByBatchIdAndCourseIdAndUserIdAndAttendance_Date(Long batchId, Long courseId, Long employeeId,
			String date);
 
	List<Attendance> findByAttendance_Date(String date);

    List<Attendance> findByBatchIdAndAttendance_Date(Long batchId, String date);
    
    
}
 
 