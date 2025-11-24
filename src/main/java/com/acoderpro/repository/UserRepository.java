package com.acoderpro.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.acoderpro.pojo.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
	
	@Query("SELECT u FROM UserEntity u WHERE u.email = :email")
	Optional<UserEntity> findByEmail(@Param("email") String email);

}
