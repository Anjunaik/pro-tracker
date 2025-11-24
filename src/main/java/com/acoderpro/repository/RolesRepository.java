package com.acoderpro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acoderpro.pojo.UserRoles;

public interface RolesRepository extends JpaRepository<UserRoles, Integer> {
	
	 //Optional<UserRoles> findByName(String name);
	
	Optional<UserRoles> findByName(String name);

}
