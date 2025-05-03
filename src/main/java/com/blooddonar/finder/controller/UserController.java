package com.blooddonar.finder.controller;

import com.blooddonar.finder.model.User;
import com.blooddonar.finder.model.User.BloodGroup;
import com.blooddonar.finder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ Create user
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // ✅ Delete user
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully.";
    }

    // ✅ Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // ✅ Get single user
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // ✅ Search donors: by blood group & district (only eligible donors)
    @GetMapping("/search")
    public List<User> searchDonors(
            @RequestParam BloodGroup bloodGroup,
            @RequestParam String district
    ) {
        return userService.searchDonors(bloodGroup, district);
    }
}
