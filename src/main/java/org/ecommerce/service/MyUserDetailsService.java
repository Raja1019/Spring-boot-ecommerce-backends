package org.ecommerce.service;

import java.util.Optional;


import org.ecommerce.model.User;
import org.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService
{
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> user=userRepository.findByuserName(username);
		user.orElseThrow(()-> new UsernameNotFoundException("User not found!!"));
		return user.map(MyUserDetails::new).get();
	}
	
}