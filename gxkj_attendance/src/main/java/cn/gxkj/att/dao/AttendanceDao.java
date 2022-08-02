package cn.gxkj.att.dao;

import cn.gxkj.model.atte.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface AttendanceDao extends CrudRepository<Attendance,String> , JpaRepository<Attendance, String>, JpaSpecificationExecutor<Attendance> {

    Attendance findByUserIdAndDay(String id, String day);

    @Query(nativeQuery = true,value = "SELECT COUNT(1) AS 'at1', COUNT( CASE WHEN adt_statu = 2 THEN 1 END ) AS 'at2', COUNT( CASE WHEN adt_statu = 3 THEN 1 END ) AS 'at3', COUNT( CASE WHEN adt_statu = 4 THEN 1 END ) AS 'at4', COUNT( CASE WHEN adt_statu = 8 THEN 1 END ) AS 'at8', COUNT( CASE WHEN adt_statu = 17 THEN 1 END ) AS 'at17' FROM atte_attendance WHERE company_id = ?1 AND `day` LIKE ?2 AND user_id = ?3")
    Map<String, Object> queryAtte(String companyId, String atteDate,String userId);
}
