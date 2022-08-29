package org.ecommerce.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class Cart {

	@Id
	@GeneratedValue
	private int cartId;
	private int productId;
	private double price;
	private int quantity;
	private String email;
	
	
		
}
