package com.nuhi.Nuhi.repository;

import com.nuhi.Nuhi.enums.ProfileStatus;
import com.nuhi.Nuhi.model.NurseProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NurseProfileRepository extends JpaRepository<NurseProfile, Long> {

    Optional<NurseProfile> findByLicenseNumber(String licenseNumber);


    //JPQL
    @Query("SELECT n FROM NurseProfile  n WHERE " +
          "n.status = :status AND" +
            "(:location IS NULL OR n.location =:location) AND" +
            "(:specialization IS NULL OR n.specialization =:specialization)")
    List<NurseProfile> findByStatusAndFilters(
        @Param("status")ProfileStatus status,
                @Param("location") String location,
                        @Param("specialization") String specialization);
    }






