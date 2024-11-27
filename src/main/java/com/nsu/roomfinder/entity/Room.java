package com.nsu.roomfinder.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String location;
    private String image1;
    private String type;
    private double price;

}
