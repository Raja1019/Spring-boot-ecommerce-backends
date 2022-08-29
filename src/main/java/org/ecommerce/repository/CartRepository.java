package org.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.ecommerce.model.Address;
import org.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

	public List<Cart> findByemail(String email); 
	public Optional<Cart> findByproductId(int productId);
	
	@Query(value="delete from cart  c where c.productId = :productId",nativeQuery = true)
	public void deleteProductByproductId(@Param("productId") int productId);

}
