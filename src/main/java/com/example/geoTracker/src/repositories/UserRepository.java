package com.example.geoTracker.src.repositories;

import com.example.geoTracker.src.models.Role;
import com.example.geoTracker.src.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.isActive = true")
    Optional<User> getUserByIdAndActiveIsTrue(@Param("id") UUID id);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isActive = true")
    Optional<User> getUserByEmailAndActiveIsTrue(@Param("email") String email);

    @Modifying
    @Query("UPDATE User u SET u.email = :email WHERE u.id = :id")
    void updateUserEmail(@Param("id") UUID id, @Param("email") String newEmail);

    @Modifying
    @Query("UPDATE User u SET u.hashedPassword = :password WHERE u.id = :id")
    void updateUserPassword(@Param("id") UUID id, @Param("password") String newPassword);

    @Modifying
    @Query("UPDATE User u SET u.role = :role WHERE u.id = :id")
    void updateUserRole(@Param("id") UUID id, @Param("role") Role role);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isActive = CASE WHEN u.isActive = true THEN false ELSE true END WHERE u.id = :id")
    void changeUserActive(@Param("id") UUID id);

    void deleteUserById(UUID id);
}
