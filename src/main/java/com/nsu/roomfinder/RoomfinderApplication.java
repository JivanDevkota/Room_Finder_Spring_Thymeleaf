package com.nsu.roomfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class RoomfinderApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomfinderApplication.class, args);

        String pwd="1234";
        PasswordEncoder encoder=new BCryptPasswordEncoder();
        String test = encoder.encode(pwd);
        System.out.println(test);
    }



}
