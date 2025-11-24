/**
 * 
 */
package com.acoderpro.servicesImp;

import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.acoderpro.dto.AdminUserReqDTO;
import com.acoderpro.exceptions.RoleNotFoundException;
import com.acoderpro.exceptions.UserAlreadyExistsException;
import com.acoderpro.pojo.UserEntity;
import com.acoderpro.pojo.UserRoles;
import com.acoderpro.repository.RolesRepository;
import com.acoderpro.repository.UserRepository;
import com.acoderpro.services.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Service
@Slf4j
public class UserServicesImp implements UserService {

	@Autowired
	private UserRepository repository;
	@Autowired
	private RolesRepository rolesRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder encode;

	@Override
	public void createUserAccount(Object dto, boolean isAdmin) {

		log.info("User creation method called");
		UserEntity userEntity = modelMapper.map(dto, UserEntity.class);
		userEntity.setPassword(encode.encode(userEntity.getPassword()));

		// Check duplicate email
		if (repository.findByEmail(userEntity.getEmail()).isPresent()) {
			throw new UserAlreadyExistsException("User Already Exists");
		}

		// CASE 1: Normal User → only default role
		if (!isAdmin ) {
			
			//log.error();

			UserRoles defaultRole = rolesRepository.findByName("ROLE_USER")
					.orElseThrow(() -> new RoleNotFoundException("Default USER role not found"));

			userEntity.setRoles(Set.of(defaultRole));
		}

		// CASE 2: Admin User → roles come from DTO roleIds (NOT from entity)
		else {

			AdminUserReqDTO adminDto = (AdminUserReqDTO) dto;

			if (adminDto.getRoles() == null || adminDto.getRoles().isEmpty()) {
				throw new RuntimeException("Admin must assign at least one role");
			}

			// Load all roles from DB
			Set<UserRoles> roles = adminDto.getRoles().stream().map(
					id -> rolesRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found: " + id)))
					.collect(Collectors.toSet());

			userEntity.setRoles(roles); // final roles list
		}

		repository.save(userEntity);
		log.info("User Created Successfully");
	}

	
}
