package com.acoderpro.pojo;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "audit_logs")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserAuditLog {

	@Id
	@GeneratedValue
	private Integer id;

	private String action; // DELETE_USER
	private String entity; // USER
	private UUID entityId; // deleted user id
	private UUID performedBy; // admin id

	private LocalDateTime timestamp;

	@Column(columnDefinition = "TEXT")
	private String details;
}
