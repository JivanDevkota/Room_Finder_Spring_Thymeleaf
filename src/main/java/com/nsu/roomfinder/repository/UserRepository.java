package com.nsu.roomfinder.repository;

import com.nsu.roomfinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   Optional<User> findByUsername(String username);
      User findByUsernameOrEmail(String email, String username);
}
