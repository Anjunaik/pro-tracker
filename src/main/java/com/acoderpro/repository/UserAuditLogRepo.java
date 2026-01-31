package com.acoderpro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acoderpro.pojo.UserAuditLog;

public interface UserAuditLogRepo extends JpaRepository<UserAuditLog, Integer> {

}
