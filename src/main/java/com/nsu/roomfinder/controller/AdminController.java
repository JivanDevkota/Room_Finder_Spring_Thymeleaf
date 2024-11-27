package com.nsu.roomfinder.controller;

import com.nsu.roomfinder.entity.Room;
import com.nsu.roomfinder.entity.User;
import com.nsu.roomfinder.service.AdminService;
import com.nsu.roomfinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username",username);
        return "admin_dashboard";  // Returns the view for the admin dashboard
    }



    @GetMapping("/rooms/new")
    public String showCreateRoomForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username",username);
        model.addAttribute("room", new Room());
        return "room_form";  // Shows the form to create a new room
    }

    @PostMapping("/save")
    public String saveRoom(@ModelAttribute Room room, Model model,
                           @RequestParam("file") MultipartFile file) throws Exception {
        try {
            if (file.isEmpty()) {
                model.addAttribute("error", "Please select a file to upload.");
                return "room_form";  // This should redirect back to the form if no file is selected
            }
            adminService.createRoom(room, file);  // Save the room with the file
            model.addAttribute("message", "Room saved successfully!");
            return "redirect:/admin/dashboard";  // Redirect to the dashboard
        } catch (Exception e) {
            model.addAttribute("error", "Error saving room: " + e.getMessage());
            return "room_form";  // This should redirect back to the form in case of error
        }
    }

    @GetMapping("/room/lists")
    public String getAllRooms(@ModelAttribute Room room, Model model) {
        List<Room> rooms = adminService.getAllRooms();
        model.addAttribute("rooms", rooms);
        return "room_list";
    }

    @GetMapping("/get/all/users")
    public String getAllUsers(@ModelAttribute User user, Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user_list";
    }

}
