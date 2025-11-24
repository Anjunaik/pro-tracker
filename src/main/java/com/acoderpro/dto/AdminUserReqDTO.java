package com.acoderpro.dto;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserReqDTO {

	@Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String middleName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private boolean active = true;
    
    @Column(nullable = false)
    private Date userCreated;
    
    @Column(nullable = false)
    private Date userUpdated;
    
    @Column(nullable= false)
    private Set<Integer> roles = new HashSet<Integer>();
}
