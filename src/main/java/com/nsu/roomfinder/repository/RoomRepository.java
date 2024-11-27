package com.nsu.roomfinder.repository;

import com.nsu.roomfinder.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByPriceBetween(Double min, Double max);
    List<Room> findByPriceBetweenAndLocation(Double minPrice, Double maxPrice, String location);

}
