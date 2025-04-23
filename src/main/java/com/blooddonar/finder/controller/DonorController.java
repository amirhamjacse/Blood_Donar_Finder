package com.blooddonar.finder.controller;

import com.blooddonar.finder.model.Donor;
import com.blooddonar.finder.repository.DonorRepository;
import org.springframework.web.bind.annotation.*;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/donors")
public class DonorController {

    private final DonorRepository donorRepository;

    public DonorController(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    @GetMapping
    public List<Donor> getAll() {
        return donorRepository.findAll();
    }

    @PostMapping
    public Donor save(@RequestBody Donor donor) {
        return donorRepository.save(donor);
    }
}
