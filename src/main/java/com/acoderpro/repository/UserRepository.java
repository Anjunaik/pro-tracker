package com.acoderpro.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.acoderpro.pojo.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

	@Query("SELECT u FROM UserEntity u WHERE u.email = :email")
	Optional<UserEntity> findByEmail(@Param("email") String email);
	
	@Query("SELECT u.id FROM UserEntity u WHERE u.id IN :ids AND u.active = true")
	List<UUID> findActiveUserIds(@Param("ids") List<UUID> ids);

	@Modifying
	@Query(value = "UPDATE UserEntity u SET u.active = false WHERE u.id IN (:ids) ")
	int softDeleteUsers(@Param("ids") List<UUID> ids);

}
