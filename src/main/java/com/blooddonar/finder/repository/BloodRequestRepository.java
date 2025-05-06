package com.blooddonar.finder.repository;

import com.blooddonar.finder.model.BloodRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {
     List<BloodRequest> findByRequesterToId(Long requesterToId);
     
}
