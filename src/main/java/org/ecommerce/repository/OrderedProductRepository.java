package org.ecommerce.repository;

import org.ecommerce.model.OrderedProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface OrderedProductRepository extends JpaRepository<OrderedProductInfo, Integer>{

}
