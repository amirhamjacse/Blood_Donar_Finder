package com.blooddonar.finder.repository;

import java.util.List;
import java.util.Optional;

import com.blooddonar.finder.model.User;
import com.blooddonar.finder.model.User.BloodGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // ✅ Custom method for searching eligible donors
    List<User> findByBloodGroupAndDistrictAndAvailable(
        BloodGroup bloodGroup,
        String district,
        boolean available
    );
}
