package com.acoderpro.services;

import java.util.List;
import java.util.UUID;

import com.acoderpro.dto.UserResponseDto;
import com.acoderpro.pojo.UserEntity;

public interface UserService {
	
	public UserEntity createUserAccount(Object dto, boolean isAdmin);
	public UserResponseDto findUserByID(UUID id);
	public String deleteUserById(List<UUID> ids);

}
