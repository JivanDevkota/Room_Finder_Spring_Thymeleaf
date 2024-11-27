package com.nsu.roomfinder.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id",nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    private LocalDate startDate;
    private LocalDate endDate;

    @Transient
    private double totalPrice;

    public void calculateTotalPrice() {
        if (startDate != null && endDate != null) {
            long monthsBetween = java.time.temporal.ChronoUnit.MONTHS.between(startDate, endDate);
            if (monthsBetween >= 1) {
                this.totalPrice = room.getPrice() * monthsBetween; // price is monthly, so multiply by the number of months
            }
        }
    }
}
