package org.ecommerce.repository;

import org.ecommerce.model.Address;
import org.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository  extends JpaRepository<Category, Integer>{

	
	public void deleteBycategoryId(int categoryId);//delete product by id
}
