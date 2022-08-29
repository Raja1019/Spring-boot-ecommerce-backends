package org.ecommerce.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Authorities {
	
	@Id
	@GeneratedValue
	private int authorityId;
	private String authorityName; //ADMIN
	private String authority;  //ADMIN_ROLE
	
	@ManyToOne
	@JoinColumn(name="userName",referencedColumnName = "username")
	private User user;
	
	
}
