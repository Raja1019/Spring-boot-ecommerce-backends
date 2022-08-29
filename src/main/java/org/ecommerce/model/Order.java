package org.ecommerce.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import lombok.Data;

@Data
@Entity
@Table(name="orderinfo")
public class Order {
	
	@Id
	@GeneratedValue
	private int orderId;
	private String orderStatus;
	
	@CreationTimestamp	
	private Date orderDate;
	private double orderCost;
	private String email;
	
	
	
	

}
