package com.blooddonar.finder.dto;

import com.blooddonar.finder.model.User.BloodGroup;
import lombok.Data;

@Data
public class BloodRequestDTO {
    private BloodGroup bloodGroup; // Blood group for the request
    private String district;       // District where blood is needed
}
