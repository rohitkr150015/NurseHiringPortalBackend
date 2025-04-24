package com.nuhi.Nuhi.repository;

import com.nuhi.Nuhi.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking , Long> {

    List<Booking> findByClientId(Long clientId);;

    List<Booking > findByNurseId(Long nurseId);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Booking b WHERE " +
            "b.nurse.id = :nurseId AND " +  // Added space after colon
            "((b.startTime < :end AND b.endTime > :start))")
    boolean existsByNurseIdAndTimeRange(  // Fixed typo from 'exits' to 'exists'
                                          @Param("nurseId") Long nurseId,
                                          @Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end
    );



}
