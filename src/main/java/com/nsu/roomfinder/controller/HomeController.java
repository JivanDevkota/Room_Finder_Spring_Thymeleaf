package com.nsu.roomfinder.controller;

import com.nsu.roomfinder.entity.Room;
import com.nsu.roomfinder.service.AdminService;
import com.nsu.roomfinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String homePage(Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);
        System.out.println("Home page accessed");
        return "home";
    }

    @GetMapping("/all/roomss")
    public String getAllRooms(@ModelAttribute Room room, Model model){
        List<Room> rooms = adminService.getAllRooms();
        model.addAttribute("rooms", rooms);
        return "home_rooms";

    }

    @GetMapping("/paginated/rooms")
    public String getPaginatedRooms(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "4") int size,
                                    @RequestParam(defaultValue = "id") String sortField,
                                    @RequestParam(defaultValue = "asc") String sortDirection,
                                    Model model){
        Page<Room> rooms = userService.getPaginatedRooms(page, size, sortField, sortDirection);
        model.addAttribute("rooms", rooms.getContent());
        System.out.println(rooms.getContent());
        System.out.println("Total rooms fetched controller: " + rooms.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize",size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("totalPages", rooms.getTotalPages());
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        return "home_rooms";

    }

}
