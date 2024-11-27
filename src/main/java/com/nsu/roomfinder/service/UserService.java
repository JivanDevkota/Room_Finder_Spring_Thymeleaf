package com.nsu.roomfinder.service;

import com.nsu.roomfinder.entity.Booking;
import com.nsu.roomfinder.entity.Role;
import com.nsu.roomfinder.entity.Room;
import com.nsu.roomfinder.entity.User;
import com.nsu.roomfinder.repository.BookingRepository;
import com.nsu.roomfinder.repository.RoleRepository;
import com.nsu.roomfinder.repository.RoomRepository;
import com.nsu.roomfinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;


    public User registerUser(User user, String roleName) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<Role> role = roleRepository.findByName(roleName);
        if (role.isPresent()) {
            Set<Role> roles=new HashSet<>();
            roles.add(role.get());
            user.setRoles(roles);
        }
        else {
            System.out.println("Role not found");
        }
        return userRepository.save(user);
    }

    public User login(String username){
        User byUsernameOrEmail = userRepository.findByUsernameOrEmail(username,username);
        if (byUsernameOrEmail==null){
            System.out.println("User not found with this email or username");
        }
        return byUsernameOrEmail;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Booking createBooking(Long userId,Long roomId,LocalDate startDate,LocalDate endDate){
        Optional<User> user = userRepository.findById(userId);
        Optional<Room> room = roomRepository.findById(roomId);
        if (user.isEmpty()){
            System.out.println("User not found");
        }
        if (room.isEmpty()){
            System.out.println("Room not found");
        }

        Booking booking = new Booking();
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setUser(user.get());
        booking.setRoom(room.get());
        booking.calculateTotalPrice();
        System.out.println(booking.getTotalPrice());
       return bookingRepository.save(booking);
    }

    public Room getRoomById(Long roomId){
        Optional<Room> room = roomRepository.findById(roomId);
        if (room.isPresent()){
            return room.get();
        }
        return null;
    }

    public List<Room>searchRoomsByPriceAndLocation(Double minPrice,Double maxPrice,String location){
        if (minPrice == null && maxPrice == null && (location == null || location.isEmpty())) {
            return roomRepository.findAll();
        } else if (location == null || location.isEmpty()) {
            return roomRepository.findByPriceBetween(minPrice, maxPrice);
        } else {
            return roomRepository.findByPriceBetweenAndLocation(minPrice, maxPrice, location);
        }
    }

    public Page<Room> getPaginatedRooms(int page, int size, String sortField, String sortDirection) {
        Sort sort=sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ?Sort.by(sortField).ascending()
                :Sort.by(sortField).descending();
        Pageable pageable= PageRequest.of(page, size, sort);
        Page<Room> rooms = roomRepository.findAll(pageable);
        System.out.println("Total rooms fetched service: " + rooms.getTotalElements());
        return rooms;
    }
}
