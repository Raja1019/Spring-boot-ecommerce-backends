package org.ecommerce.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class OrderedProductInfo
{
	@Id
	@GeneratedValue
	private int orderinfoId;
	private int productId;
	private String productName;
	private double productPrice;
	private int productQuantity;
	private double totalPrice;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Order order;
}