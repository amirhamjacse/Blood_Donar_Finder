package com.blooddonar.finder.service;

import com.blooddonar.finder.dto.BloodRequestDTO;
import com.blooddonar.finder.model.BloodRequest;
import com.blooddonar.finder.model.User;
import com.blooddonar.finder.repository.UserRepository;
import com.blooddonar.finder.repository.BloodRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.blooddonar.finder.model.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class BloodRequestService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BloodRequestRepository bloodRequestRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public int sendBloodRequest(BloodRequestDTO requestDTO) {

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

            // Save the blood request for the donor
            // send blood request to the user
                BloodRequest bloodRequest = new BloodRequest();
                bloodRequest.setCity(requestDTO.getCity());
                bloodRequest.setDistrict(requestDTO.getDistrict());
                bloodRequest.setMessage(requestDTO.getMessage());
                bloodRequest.setRequestTime(LocalDateTime.now());
                bloodRequest.setStatus(Status.PENDING);
                bloodRequest.setBgGroup(requestDTO.getBloodGroup());
                
            bloodRequest.setRequesterId(donor.getId(), requestDTO.getRequesterId());
            bloodRequestRepository.save(bloodRequest);
        }

        return finalDonors.size(); // Number of notified donors
    }

public List<Map<String, Object>> getBloodRequestsByRequesterId(Long requesterId) {
    List<BloodRequest> bloodRequests = bloodRequestRepository.findByRequesterToId(requesterId);

    // Filter out the ACCEPTED requests
    List<Map<String, Object>> responseList = bloodRequests.stream()
            .filter(req -> req.getStatus() != Status.ACCEPTED) // Exclude ACCEPTED requests
            .map(req -> {
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("id", req.getId());  // Add id explicitly
                responseMap.put("bloodGroup", req.getBgGroup());
                responseMap.put("district", req.getDistrict());
                responseMap.put("city", req.getCity());
                responseMap.put("message", req.getMessage());
                responseMap.put("requesterId", req.getRequesterBy().getId());
                responseMap.put("name", req.getRequesterBy().getName());
                responseMap.put("email", req.getRequesterBy().getEmail());
                responseMap.put("contactNumber", req.getRequesterBy().getPhone());
                return responseMap;
            })
            .collect(Collectors.toList());

    return responseList;
}


   public String acceptRequest(Long bloodRequestId) {
        BloodRequest bloodRequest = bloodRequestRepository.findById(bloodRequestId)
                .orElseThrow(() -> new RuntimeException("BloodRequest not found with ID: " + bloodRequestId));

        bloodRequest.setStatus(Status.ACCEPTED);
        bloodRequestRepository.save(bloodRequest);

        User requesterTo = bloodRequest.getRequesterTo();
        if (requesterTo != null) {
            requesterTo.setLastDonationDate(LocalDate.now());
            userRepository.save(requesterTo);
        } else {
            throw new RuntimeException("Requester user not found for this BloodRequest");
        }

        return "Blood request accepted and user's last donation date updated.";
    }
}
