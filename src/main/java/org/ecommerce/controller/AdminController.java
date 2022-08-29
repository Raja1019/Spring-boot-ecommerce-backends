package org.ecommerce.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.ecommerce.constants.WebConstants;
import org.ecommerce.exception.CategoryNotFoundException;
import org.ecommerce.exception.ProductCustomException;
import org.ecommerce.model.Category;
import org.ecommerce.model.Order;
import org.ecommerce.model.Product;
import org.ecommerce.repository.CartRepository;
import org.ecommerce.repository.CategoryRepository;
import org.ecommerce.repository.OrderRepository;
import org.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

//@CrossOrigin(origins = WebConstants.ALLOWED_URL)
@RestController
@RequestMapping("/admin/api/v1")
public class AdminController {
	
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@PostMapping("/addproduct")
	public ResponseEntity addProduct(
			@RequestParam(name="productImage" , required = false) MultipartFile productImage,
			@RequestParam(name="productName")String productName,
			@RequestParam(name="productPrice")double productPrice,
			@RequestParam(name="productQuantity")int  productQuantity,
			@RequestParam(name="productDescription")String productDescription,
			@RequestParam(name="categoryId") int  categoryId) throws IOException, ProductCustomException
	{
		
		Category category=categoryRepository.getById(categoryId);
		
		
		category.setCategoryId(category.getCategoryId());
		Product product=new Product();
		product.setProductName(productName);
		product.setProductDescription(productDescription);
		product.setProductPrice(productPrice);
		product.setProductQuantity(productQuantity);
		product.setProductImage(productImage.getBytes());//saves product image in table as lob type
		product.setCategory(category);
		
		try
		{
		productRepository.save(product);
		}
		catch(Exception e)
		{
			throw new ProductCustomException("Somthing is wrong in the product details");
		}
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body("product information added!!");
		
	}
	
	
	
	
	
	@PutMapping("/updateproduct/{productId}")
	public ResponseEntity updateProduct(
			@PathVariable("productId") int productId,
			@RequestParam(name="productImage" , required = false) MultipartFile productImage,
			@RequestParam(name="productName")String productName,
			@RequestParam(name="productPrice")double productPrice,
			@RequestParam(name="productQuantity")int  productQuantity,
			@RequestParam(name="productDescription")String productDescription,
			@RequestParam(name="categoryId") int  categoryId) throws IOException, ProductCustomException
	{
		
		
		Category category=categoryRepository.findById(categoryId).get();
		Product existProduct=productRepository.findById(productId ).get();
		if(existProduct!=null && category!=null)
		{
		category.setCategoryId(category.getCategoryId());
		
		existProduct.setProductName(productName);
		existProduct.setProductDescription(productDescription);
		existProduct.setProductPrice(productPrice);
		existProduct.setProductQuantity(productQuantity);
		existProduct.setProductImage(productImage.getBytes());//saves product image in table as lob type
		existProduct.setCategory(category);
		}
		else
		{
			throw new ProductCustomException("Somthing is wrong in the product details");
		}
		
		try
		{
		productRepository.save(existProduct);
		}
		catch(Exception e)
		{
			throw new ProductCustomException("Somthing is wrong in the product details");
		}
		
		
		return ResponseEntity.status(HttpStatus.OK).body("product information updated!!");
		
	}
	
	
	
	@DeleteMapping("/delProduct/{productId}")
	public ResponseEntity delProduct(@PathVariable("productId") int productId ) throws ProductCustomException 
	{
		Product product=productRepository.findProductByProductId(productId);
		if(product!=null)
		{
			productRepository.delete(product);
			cartRepository.deleteProductByproductId(productId);
			return ResponseEntity.status(HttpStatus.OK).body("Product deleted!!");
		}
		else
		{
			
		throw new ProductCustomException("Product Id not Found");
			
		}		
		
	}
	
	
	@GetMapping("/viewOrders")
	public ResponseEntity<?> viewOrders()
	{
		List<Order> orderList=orderRepository.findAll();
		if(orderList.isEmpty())
		{
			return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Orders not found!!");
			

					
		}
		return new ResponseEntity<List<Order>>(orderList,HttpStatus.OK);

	}
	
	
	@PutMapping("/updateOrder/{orderId}")
	public ResponseEntity<?> updateOrder(@PathVariable int orderId,@RequestBody Order order)
	{
		
		
		if(orderRepository.findById(orderId).isPresent())
		{
			Order existingOrder=orderRepository.findById(orderId).get();
			existingOrder.setOrderStatus(order.getOrderStatus());
			existingOrder.setOrderCost(order.getOrderCost());
			
			orderRepository.save(existingOrder);
			return  ResponseEntity.status(HttpStatus.OK).body("Order details updated!!");
			
		}
		else
		{
			return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order id not found!!");
			
		}
			
		
	}
	
	@PostMapping("/addCategory")
	public ResponseEntity<?> addCategory(@RequestBody Category category)
	{
		if(!category.getCategoryName().isEmpty())
		{
			categoryRepository.save(category);
			return ResponseEntity.status(HttpStatus.CREATED).body("category added!!");
		}
		else
		{
			return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("category name not found!!");
		}
	}
		
	
	@DeleteMapping("/delCategory/{categoryId}")
	public ResponseEntity delCategory(@PathVariable("categoryId") int categoryId ) throws ProductCustomException, CategoryNotFoundException 
	{
			
		Optional<Category> category=categoryRepository.findById(categoryId);
		if(category.isPresent())
		{
			categoryRepository.delete(category.get());
			return ResponseEntity.status(HttpStatus.OK).body("Category deleted!!");
		}
		else
		{
			
		throw new CategoryNotFoundException("Category Id not Found");
			
		}		
		
	}
	
	
	}



