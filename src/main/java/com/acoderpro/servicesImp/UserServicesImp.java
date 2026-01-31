/**
 * 
 */
package com.acoderpro.servicesImp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acoderpro.dto.AdminUserReqDTO;
import com.acoderpro.dto.DefaultUserReqDTO;
import com.acoderpro.dto.UserResponseDto;
import com.acoderpro.exceptions.ConfirmPasswordMismatchException;
import com.acoderpro.exceptions.RoleNotFoundException;
import com.acoderpro.exceptions.UserAlreadyExistsException;
import com.acoderpro.pojo.UserAuditLog;
import com.acoderpro.pojo.UserEntity;
import com.acoderpro.pojo.UserRoles;
import com.acoderpro.repository.RolesRepository;
import com.acoderpro.repository.UserAuditLogRepo;
import com.acoderpro.repository.UserRepository;
import com.acoderpro.services.UserService;
import com.acoderpro.utilities.EmailTemplates;
import com.acoderpro.utilities.JWTUtil;

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
	private NotificationClientImp notification;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserAuditLogRepo auditLogRepo;

	@Autowired
	private PasswordEncoder encode;

	@Autowired
	private JWTUtil util;

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
		UserEntity save = repository.save(userEntity);
		notification.sendOtp(EmailTemplates.createMailContent("Registration Successfull, Please Login", save.getEmail(),
				EmailTemplates.registrationSuccessTemplate(save.getFirstName())));

		return save;
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

	@Override
	@Transactional
	public Map<String, Object> deleteUserById(List<UUID> ids) {

		UserEntity admin = repository.findByEmail(util.getCurrentUsername())
				.orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

		// 1️⃣ Find users that are actually active
		List<UUID> activeUserIds = repository.findActiveUserIds(ids);

		if (activeUserIds.isEmpty()) {
			throw new UsernameNotFoundException("No active users found for deletion");
		}

		// 2️⃣ Soft delete active users
		int deletedUserCount = repository.softDeleteUsers(activeUserIds);

		// 3️⃣ Log ONLY successfully deleted users
		List<UserAuditLog> logs = new ArrayList<>();

		for (UUID userId : activeUserIds) {
			UserAuditLog log = new UserAuditLog();
			log.setAction("BULK_DELETE_USER");
			log.setEntity("USER");
			log.setEntityId(userId);
			log.setPerformedBy(admin.getId());
			log.setTimestamp(LocalDateTime.now());
			log.setDetails("User soft-deleted successfully");
			logs.add(log);
		}

		auditLogRepo.saveAll(logs); // ✅ batch insert

		// 4️⃣ Correct counts
		int failedCount = ids.size() - activeUserIds.size();

		Map<String, Object> response = new HashMap<>();
		response.put("deletedCount", deletedUserCount);
		response.put("failedCount", failedCount);

		return response;
	}

}
