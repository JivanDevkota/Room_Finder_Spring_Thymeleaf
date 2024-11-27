package com.nsu.roomfinder.controller;

import com.nsu.roomfinder.entity.Booking;
import com.nsu.roomfinder.entity.Room;
import com.nsu.roomfinder.entity.User;
import com.nsu.roomfinder.service.UserService;
import jakarta.persistence.GeneratedValue;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        System.out.println("Received request to register user: " + user.getUsername());
        userService.registerUser(user,"ROLE_ADMIN");
        System.out.println(user.getRoles());
        model.addAttribute("message", "User registered successfully");
        return "register";
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username",username);
        return "user_dashboard";  // Returns the view for the admin dashboard
    }

    @GetMapping("/booking/{roomId}")
    public String showBookingPage(@PathVariable Long roomId, Model model) {
        Room room = userService.getRoomById(roomId);
        model.addAttribute("room", room);
        model.addAttribute("startDate", LocalDate.now()); // Default start date is today
        return "book_room"; // your booking page
    }

    @PostMapping("/book")
    public String createBooking(@RequestParam("roomId")Long roomId,
                                @RequestParam("userId")Long userId,
                                @RequestParam("startDate")String startDate,
                                @RequestParam("endDate")String endDate,
                                Model model) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        try {
            Booking booking = userService.createBooking(userId, roomId, start, end);
            model.addAttribute("message","Booking created successfully");
        }catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/rooms";
    }

    @GetMapping("room/search")
    public String searchRoomsByPriceAndLocation(@RequestParam(name = "minPrice", required = false) Double minPrice,
                                                @RequestParam(name = "maxPrice", required = false) Double maxPrice,
                                                @RequestParam(name = "location", required = false) String location,
                                                Model model) {
        List<Room> rooms = userService.searchRoomsByPriceAndLocation(minPrice, maxPrice, location);
        model.addAttribute("rooms", rooms);
        return "room_search_list"; // Replace with your actual template name
    }
}

