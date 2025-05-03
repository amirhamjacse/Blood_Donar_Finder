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
    public int sendBloodRequest(Long userId, BloodRequestDTO requestDTO) {

        // Find donors by blood group, district, and availability
        List<User> eligibleDonors = userRepository
                .findByBloodGroupAndDistrictAndAvailable(
                        requestDTO.getBloodGroup(),
                        requestDTO.getDistrict(),
                        true
                );

        // Filter donors who haven't donated in last 3 months
        List<User> finalDonors = eligibleDonors.stream()
                .filter(user -> user.getLastDonationDate() == null ||
                        user.getLastDonationDate().isBefore(LocalDate.now().minusMonths(3)))
                .collect(Collectors.toList());

        // Notify each eligible donor by email
        for (User donor : finalDonors) {
            String subject = "Urgent Blood Request - " + requestDTO.getBloodGroup();

            String body = "Dear " + donor.getName() + ",\n\n" +
                    "🚨 A blood donation request has been made by " + requestDTO.getName() + ".\n\n" +
                    "🩸 Blood Group: " + requestDTO.getBloodGroup() + "\n" +
                    "📍 Location: " + requestDTO.getDistrict() + "\n" +
                    "📞 Contact Number: " + requestDTO.getContactNumber() + "\n" +
                    "📧 Email: " + requestDTO.getEmail() + "\n\n" +
                    "📝 Message from requester:\n" +
                    requestDTO.getMessage() + "\n\n" +
                    "🙏 Please consider donating if you are eligible.\n\n" +
                    "Thank you for being a life-saver!\n\n" +
                    "— Blood Donor Finder Team";

            emailService.sendEmail(donor.getEmail(), subject, body);
        }

        return finalDonors.size(); // Number of notified donors
    }
}
