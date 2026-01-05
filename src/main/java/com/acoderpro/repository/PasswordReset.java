package com.acoderpro.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.acoderpro.pojo.PasswordResetToken;

import reactor.core.publisher.Mono;

public interface PasswordReset extends JpaRepository<PasswordResetToken, Long> {

	@Modifying
	@Query("""
			    DELETE FROM PasswordResetToken t
			    WHERE t.expiryTime <= :now
			""")
	int deleteExpired(@Param("now") LocalDateTime now);

	@Query("from PasswordResetToken where email =:email")
	Optional<PasswordResetToken> findByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM PasswordResetToken t WHERE t.email = :email")
    void deleteByEmail(@Param("email") String email);
}

