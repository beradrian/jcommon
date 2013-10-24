package net.sf.jcommon.spring;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository<U extends UserDetails> {
	
	U findByUsername(String username);

}
