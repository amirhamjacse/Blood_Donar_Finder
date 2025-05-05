package com.blooddonar.finder.dto;

import com.blooddonar.finder.model.User.BloodGroup;
import lombok.Data;

@Data
public class BloodRequestDTO {
    private BloodGroup bloodGroup; // Blood group for the request
    private String district;       // District where blood is needed
    private String city;       // District where blood is needed
    private String contactNumber;  // Requester's contact number
    private String message;        // Optional custom message
    private String name;           // Requester's name (new)
    private String email;          // Requester's email (new)
    private Long requesterId;          // Requester's id (new)
}
