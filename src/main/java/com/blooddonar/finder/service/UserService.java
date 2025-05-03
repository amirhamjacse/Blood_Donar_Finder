package com.blooddonar.finder.service;

import com.blooddonar.finder.model.User;
import com.blooddonar.finder.model.User.BloodGroup;
import com.blooddonar.finder.exception.UserAlreadyExistsException;
import com.blooddonar.finder.exception.UserNotFoundException;
import com.blooddonar.finder.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ✅ Create new user
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists: " + user.getEmail());
        }

        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    // ✅ Delete user by ID
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        userRepository.delete(user);
    }

    // ✅ Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Get user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    // ✅ Search for eligible blood donors
    public List<User> searchDonors(BloodGroup bloodGroup, String district) {
        List<User> allMatching = userRepository.findByBloodGroupAndDistrictAndAvailable(bloodGroup, district, true);
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);

        return allMatching.stream()
                .filter(user -> user.getLastDonationDate() == null || user.getLastDonationDate().isBefore(threeMonthsAgo))
                .collect(Collectors.toList());
    }
}
