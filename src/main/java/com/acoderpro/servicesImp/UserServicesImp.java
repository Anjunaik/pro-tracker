/**
 * 
 */
package com.acoderpro.servicesImp;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.acoderpro.dto.AdminUserReqDTO;
import com.acoderpro.dto.DefaultUserReqDTO;
import com.acoderpro.dto.UserResponseDto;
import com.acoderpro.exceptions.ConfirmPasswordMismatchException;
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
	public UserEntity createUserAccount(Object dto, boolean isAdmin) {

		log.info("User creation method called");

		// 1️⃣ Validate password & confirm password
		String password;
		String confirmPassword;

		if (dto instanceof DefaultUserReqDTO userDto) {
			password = userDto.getPassword();
			confirmPassword = userDto.getConfirmPassword();

		} else if (dto instanceof AdminUserReqDTO adminDto) {
			password = adminDto.getPassword();
			confirmPassword = adminDto.getConfirmPassword();

		} else {
			throw new IllegalArgumentException("Invalid DTO type");
		}

		if (!password.equals(confirmPassword)) {
			throw new ConfirmPasswordMismatchException("Password and Confirm Password do not match");
		}

		// 2️⃣ Map DTO → Entity
		UserEntity userEntity = modelMapper.map(dto, UserEntity.class);

		// 3️⃣ Encode password
		userEntity.setPassword(encode.encode(password));

		// 4️⃣ Check duplicate email
		if (repository.findByEmail(userEntity.getEmail()).isPresent()) {
			throw new UserAlreadyExistsException("User Already Exists");
		}

		// 5️⃣ Assign roles
		if (!isAdmin) {

			// Normal user → default role
			UserRoles defaultRole = rolesRepository.findByName("ROLE_USER")
					.orElseThrow(() -> new RoleNotFoundException("Default USER role not found"));

			userEntity.setRoles(Set.of(defaultRole));

		} else {

			// Admin user → roles from DTO
			AdminUserReqDTO adminDto = (AdminUserReqDTO) dto;

			if (adminDto.getRoles() == null || adminDto.getRoles().isEmpty()) {
				throw new RuntimeException("Admin must assign at least one role");
			}

			Set<UserRoles> roles = adminDto.getRoles().stream().map(
					id -> rolesRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found: " + id)))
					.collect(Collectors.toSet());

			userEntity.setRoles(roles);
		}

		// 6️⃣ Audit fields
		userEntity.setUserCreated(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		userEntity.setUserUpdated(null);

		log.info("Creating user");
		return repository.save(userEntity);
	}

	@Override
	public UserResponseDto findUserByID(UUID id) {
		UserEntity user = repository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("Resource Not Found"));
		// Set<String> role = new HashSet<>();
		UserResponseDto dto = new UserResponseDto();
		dto.setId(user.getId());
		List<String> role = user.getRoles().stream().map(UserRoles::getName).toList();
		dto.setRoles(role);
		dto.setEmail(user.getEmail());
		dto.setFullName(user.getFirstName() + " " + user.getLastName());

		dto.setCreatedAt(user.getUserCreated());
		return dto;
	}

	public String deleteUserById(List<UUID> ids) {
		return null;
	}
}
