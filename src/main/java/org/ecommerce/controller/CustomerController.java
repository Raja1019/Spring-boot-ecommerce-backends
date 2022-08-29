package org.ecommerce.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.ecommerce.exception.AddressCustomException;
import org.ecommerce.exception.CartCustomException;
import org.ecommerce.exception.CartDeleteException;
import org.ecommerce.exception.OrderPlacedException;
import org.ecommerce.exception.ProductCustomException;
import org.ecommerce.exception.UserIdNotFountException;
import org.ecommerce.model.Address;
import org.ecommerce.model.Cart;
import org.ecommerce.model.Order;
import org.ecommerce.model.OrderedProductInfo;
import org.ecommerce.model.Product;
import org.ecommerce.model.User;
import org.ecommerce.repository.AddressRepository;
import org.ecommerce.repository.CartRepository;
import org.ecommerce.repository.OrderRepository;
import org.ecommerce.repository.OrderedProductRepository;
import org.ecommerce.repository.ProductRepository;
import org.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer/api/v1")
public class CustomerController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderedProductRepository orderedProductRepository;

	@GetMapping("/getProducts")
	public ResponseEntity<?> getProducts() {
		List<Product> productList = productRepository.findAll();

		if (productList.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Products not found!!");
		} else {
			return new ResponseEntity<List<Product>>(productList, HttpStatus.OK);
		}
	}

	@PostMapping("/addAddress/{userId}")
	public ResponseEntity<?> addAddress(@RequestBody Address address, @PathVariable int userId)
			throws AddressCustomException {
		try {
			Optional<User> user = userRepository.findById(userId);
			if (user.isPresent()) {

				address.setUser(user.get());
				addressRepository.save(address);
			} else {
				throw new UserIdNotFountException("User id not found!!");
			}

		} catch (Exception e) {
			throw new AddressCustomException("Error in address saving!!");
		}

		return ResponseEntity.status(HttpStatus.CREATED).body("Address added!!");
	}

	
	  @PostMapping("/addToCart/{productId}") 
	  public ResponseEntity<?> addTocart(@PathVariable int productId ) throws  CartCustomException 
	  { 
		  
		  Product product=productRepository.findProductByProductId(productId);
		  if(productRepository.findById(productId).isPresent())
		  {
			  Cart cart=new Cart();
			  cart.setProductId(product.getProductId());
			  cart.setEmail("abc@gmail.com");
			  cart.setPrice(product.getProductPrice());
			  cart.setQuantity(product.getProductQuantity());
			  try {
			  cartRepository.save(cart);
			  return ResponseEntity.status(HttpStatus.CREATED).body("added to the cart!!");
			  }
			  catch(Exception e)
			  {
				 throw new CartCustomException("problem in saving to cart!!");
			  }
			 
		  }
		  else
		  {
			  return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found!!");
		  }
	
		  
	  
	  }
	  
	  
	  @GetMapping("/viewCart")
	  public ResponseEntity<?> viewCart(@RequestParam("email") String email)
	  {
		
			List<Cart> cartProducts=cartRepository.findByemail(email);
			if(cartProducts.isEmpty())
			{
				return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("No product in the cart!!");
			}
			else
			{
				return new ResponseEntity<List<Cart>>(cartProducts,HttpStatus.OK);
			}
	
	  }
	  
	  @DeleteMapping("/delCart")
	  public ResponseEntity<?> deteleCart(@RequestParam("email") String email ) throws CartDeleteException
	  {
		  List<Cart> cartProducts=cartRepository.findByemail(email);
		  if(cartProducts.size()>0)
		  {
			  try
			  {
			  cartRepository.deleteAll();
			  return ResponseEntity.status(HttpStatus.OK).body("all products removed from the cart!!");
			  }
			  catch(Exception e)
			  {
				  throw new CartDeleteException("problem in clearing cart!!");
			  }
		  }
		  else
		  {
			  return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("No product in the cart!!");
		  }
	  }
	  
	  
	  @DeleteMapping("/delCartByProductId/{productId}")
	  public ResponseEntity<?> deletecartByProductId(@PathVariable int productId)
	  {
		  if(cartRepository.findByproductId(productId).isPresent())
		  {
			  cartRepository.deleteById(productId);
			  return  ResponseEntity.status(HttpStatus.OK).body("product deleted form the cart!!");
		  }
		  else
		  {
			  return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("product not founnd in the cart!!");
		  }
	  }
	  
	  
	  @PutMapping("/updateCart/{cartId}")
	  public ResponseEntity<?> updatecart(@PathVariable int cartId,@RequestBody Cart cart) throws CartCustomException
	  {
		  
		  Optional<Cart> existingcCartProduct=cartRepository.findById(cartId);
		  if(existingcCartProduct.isPresent())
		  {
			  try
			  {
			  Cart product=existingcCartProduct.get();
			  product.setQuantity(cart.getQuantity());
			  cartRepository.save(product);
			  return  ResponseEntity.status(HttpStatus.OK).body("cart updated!!");
			  }
			  catch(Exception e)
			  {
				  throw new CartCustomException("product not found in cart!!");
			  }
		  }
		  else
		  {
			  return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("product not founnd in the cart!!");
		  }
		  
	  }
	  
	  @PostMapping("/placeOrder")
	  public ResponseEntity<?> placeOrder(@RequestBody Order order) throws OrderPlacedException
	  {
		  try
		  {
		  order.setOrderStatus("In process");
		  orderRepository.save(order);
		  return  ResponseEntity.status(HttpStatus.CREATED).body("order placed!!");
		  }
		  catch(Exception e)
		  {
			  throw new OrderPlacedException("Probem in placing order..try after sometime!!");
		  }
	  }
	  
	  @PostMapping("/addPlacedOrderInfo")
	  public ResponseEntity<?> addPlacedOrderInfo(@RequestBody List<OrderedProductInfo> productsInfo)
	  {
		  if(productsInfo.size()>0)
		  {
		  for(OrderedProductInfo product:productsInfo)
		  {
			  try
			  {
				  Optional<Product> prod=productRepository.findById(product.getProductId());
				  if(prod.isPresent())
				  {  
				  
					  orderedProductRepository.save(product);
			      }
			  }
			  catch(Exception e)
			  {
				  return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("problem in product information!!");

			  }
		  }
		  return  ResponseEntity.status(HttpStatus.CREATED).body("Order placed!!");

		  }
		  else
		  {
			  return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("products not found!!");
		  }
		  	  
	  }
	 
	 

}
