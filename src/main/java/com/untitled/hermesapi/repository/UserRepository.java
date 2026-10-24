package com.untitled.hermesapi.repository;

import com.untitled.hermesapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByKey(String key);

    User getUserByKey(String key);
}