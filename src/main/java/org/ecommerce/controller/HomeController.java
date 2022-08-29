package org.ecommerce.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ecommerce.exception.UserCustomException;
import org.ecommerce.model.Authorities;
import org.ecommerce.model.User;
import org.ecommerce.repository.AuthoritiesRepository;
import org.ecommerce.repository.UserRepository;
import org.ecommerce.response.JwtResponse;
import org.ecommerce.service.MyUserDetailsService;
import org.ecommerce.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {

	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private AuthoritiesRepository authoritiesRepo;

	

	@PostMapping("/auth")
	public ResponseEntity<?> generateAuthToken(@RequestBody HashMap<String,String> credential) throws UserCustomException
	{
		final String username=credential.get("username");
		final String password=credential.get("password");
		final UserDetails userDetails;
		 JwtResponse response = null;
		try
		{
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
		userDetails=userDetailsService.loadUserByUsername(username);
		final String jwtToken=jwtUtil.generateToken(userDetails);
	    response=new JwtResponse();
		response.setJwtToken(jwtToken);
		response.setStatus("201");
		response.setMessage("Authenticated");
		if(userDetails.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN")))
		{
			response.setUserType("ADMIN");
		}
		else
		{
			response.setUserType("CUSTOMER");
		}
			
		  return new ResponseEntity<JwtResponse>(response,HttpStatus.CREATED);
		
		}
		catch(Exception e)
		{
			response.setStatus("400");
			response.setMessage("Authentication Failed");
			
			return new ResponseEntity<JwtResponse>(response,HttpStatus.BAD_REQUEST);
		}
	
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> addUser(@RequestBody User user) throws UserCustomException
	{
		try
		{
			//System.out.println(user);
			userRepo.save(user);
			
			Authorities roles=new Authorities();
			roles.setAuthority("ROLE_CUSTOMER");
			roles.setAuthorityName("CUSTOMER");
			roles.setUser(user);
			
			authoritiesRepo.save(roles);
			
			return ResponseEntity.status(HttpStatus.CREATED).body("Registration completed!!");
		}
		catch(Exception e)
		{
			throw new UserCustomException("Error occured while saving user record!!");
		}
	}
	
	@GetMapping("/logout")
	public void logout(HttpServletRequest request,HttpServletResponse response)
	{
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		
		if(auth!=null)
		{
			new SecurityContextLogoutHandler().logout(request, response, auth); 
		}
	}
	
	
}
