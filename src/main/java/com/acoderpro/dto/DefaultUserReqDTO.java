package com.acoderpro.dto;

import java.sql.Date;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefaultUserReqDTO {
	
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

}
