package com.nsu.roomfinder.service;

import com.nsu.roomfinder.entity.Room;
import com.nsu.roomfinder.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class AdminService {

    @Autowired
    private RoomRepository roomRepository;

//    private final String imgPath = "D:/springboot/roomfinder/src/main/resources/static/img/";
//
//    public Room createRoom(Room room, MultipartFile file)throws IOException {
//    String filePath=imgPath+"/"+file.getOriginalFilename();
//        File destFile=new File(filePath);
//        destFile.getParentFile().mkdirs();
//        file.transferTo(destFile);
//        room.setImage1("/img/"+filePath);
//        return roomRepository.save(room);
//    }

    private final String imgDirectory = "D:/springboot/roomfinder/src/main/resources/static/img/";

    public Room createRoom(Room room, MultipartFile file) throws IOException {
        // Generate a unique filename to avoid overwriting existing files
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Create the full file path (absolute path)
        String filePath = imgDirectory + filename;

        // Create the destination file and ensure its directories exist
        File destFile = new File(filePath);
        destFile.getParentFile().mkdirs();

        // Transfer the uploaded file to the destination file
        file.transferTo(destFile);

        // Set only the relative path to the image in the room object (This will be stored in the DB)
        room.setImage1("/img/" + filename);

        // Save the room in the database
        return roomRepository.save(room);
    }

    public List<Room> getAllRooms() {
//        return roomRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return roomRepository.findAll();
    }



}
