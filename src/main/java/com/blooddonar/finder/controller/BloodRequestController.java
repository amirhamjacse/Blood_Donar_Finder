package com.blooddonar.finder.controller;

import com.blooddonar.finder.dto.BloodRequestDTO;
import com.blooddonar.finder.model.User.BloodGroup;
import com.blooddonar.finder.service.BloodRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blood-request")
public class BloodRequestController {

    @Autowired
    private BloodRequestService requestService;

    @PostMapping
    public String sendRequest( @RequestBody BloodRequestDTO requestDTO) {
        // System.err.println("working ................");
        int notified = requestService.sendBloodRequest(requestDTO);
        return notified + " donor(s) have been notified.";
    }
}
