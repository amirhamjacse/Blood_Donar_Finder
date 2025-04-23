package com.blooddonar.finder.repository;

import com.blooddonar.finder.model.Donor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonorRepository extends JpaRepository<Donor, Long> {
}
