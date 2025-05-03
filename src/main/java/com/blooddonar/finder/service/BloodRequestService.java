package com.blooddonar.finder.service;

import com.blooddonar.finder.dto.BloodRequestDTO;
import com.blooddonar.finder.model.User;
import com.blooddonar.finder.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BloodRequestService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public int sendBloodRequest(Long requesterId, BloodRequestDTO requestDTO) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("Requester not found"));

        List<User> eligibleDonors = userRepository
                .findByBloodGroupAndDistrictAndAvailable(
                        requestDTO.getBloodGroup(),
                        requestDTO.getDistrict(),
                        true
                );

        List<User> finalDonors = eligibleDonors.stream()
                .filter(user -> user.getLastDonationDate() == null ||
                        user.getLastDonationDate().isBefore(LocalDate.now().minusMonths(3)))
                .collect(Collectors.toList());

        for (User donor : finalDonors) {
            String subject = "Urgent Blood Request for " + requestDTO.getBloodGroup();
            String body = "Dear " + donor.getName() + ",\n\n" +
                    requester.getName() + " needs blood (" + requestDTO.getBloodGroup() + ") in " +
                    requestDTO.getDistrict() + ".\n\nContact: " + requester.getPhone() +
                    "\nEmail: " + requester.getEmail() + "\n\nThank you for being a life-saver!";

            emailService.sendEmail(donor.getEmail(), subject, body);
        }

        return finalDonors.size();
    }
}
