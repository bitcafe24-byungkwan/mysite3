package com.cafe24.mysite.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.cafe24.mysite.repository.UserDao;
import com.cafe24.mysite.vo.UserVo;

@Component 
public class UserDatilsServiceImpl implements UserDetailsService {
	@Autowired
	private UserDao userDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserVo userVo = userDao.get(username);
		SecurityUser securityUser = new SecurityUser();
		
		if( userVo != null ) {
			// mock data
			//String role = userVo.getRole();
			String role = "ROLE_USER";
			if(userVo.getRole()==UserVo.UserRole.ADMIN)
				role = "ROLE_ADMIN";
			securityUser.setNo(userVo.getNo());
			securityUser.setName(userVo.getName());         // biz data  
			securityUser.setUsername(userVo.getEmail());    // principal
			securityUser.setPassword(userVo.getPassword()); // credential
		
//			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//			authorities.add(new SimpleGrantedAuthority(role));			
			securityUser.setAuthorities(Arrays.asList(new SimpleGrantedAuthority(role)));	
		}
		
		return securityUser;
		
		//return new User();
	}
	

}
