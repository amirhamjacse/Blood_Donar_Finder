package com.blooddonar.finder.controller;

import com.blooddonar.finder.dto.BloodRequestDTO;
import com.blooddonar.finder.model.User.BloodGroup;
import com.blooddonar.finder.model.BloodRequest;
import com.blooddonar.finder.service.BloodRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

    @GetMapping("/{userId}")
    public List<Map<String, Object>> getRequestsByUserId(@PathVariable Long userId) {
        return requestService.getBloodRequestsByRequesterId(userId);
    }

     @PutMapping("/accept/{id}")
    public String acceptRequest(@PathVariable Long id) {
        return requestService.acceptRequest(id);
    }
}
