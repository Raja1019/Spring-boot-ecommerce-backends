package org.ecommerce.repository;

import java.util.List;

import org.ecommerce.model.Address;
import org.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	
	
  public List<Product> findAll(); //find all products
  
  public void deleteByProductId(int productId);//delete product by id

  public Product findProductByProductId(int productId);//get single product information 
	

}
