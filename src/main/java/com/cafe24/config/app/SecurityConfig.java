package com.cafe24.config.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.cafe24.mysite.security.CustomUrlAuthenticationSuccessHandler;
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//	   Security Filter Chain
//
//		 1. ChannelProcessingFilter
//		 2. SecurityContextPersistenceFilter		( auto-config default )
//		 3. ConcurrentSessionFilter
//		 4. LogoutFilter				( auto-config default )
//		 5. UsernamePasswordAuthenticationFilter	( auto-config default )
//		 6. DefaultLoginPageGeneratingFilter		( auto-config default )
//		 7. CasAuthenticationFilter
//		 8. BasicAuthenticationFilter			( auto-config default )
//		 9. RequestCacheAwareFilter			( auto-config default )
//		10. SecurityContextHolderAwareRequestFilter	( auto-config default )
//		11. JaasApiIntegrationFilter
//		12. RememberMeAuthenticationFilter
//		13. AnonymousAuthenticationFilter		( auto-config default )
//		14. SessionManagementFilter			( auto-config default )
//		15. ExceptionTranslationFilter			( auto-config default )
//		16. FilterSecurityInterceptor			( auto-config default )	

	// 스프링 시큐리티 필터 연결
	// WebSecurity 객체는
	// springSecurityFilterChain 라는 이름의
	// DelegatingFilterProxy Bean 객체를 생성
	// DelegatingFilterProxy Bean는 많은
	// Spring Security Filter Chain에 위임한다.

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void configure(WebSecurity web) throws Exception {
		// super.configure(web);

		// ACL(Access Control List)에 등록하지 않을 URL를 설정

		// web.ignoring().antMatchers("/assets/**");
		// web.ignoring().antMatchers("/favicon.ico");
		web.ignoring().regexMatchers("\\A/assets/.*\\Z");
		web.ignoring().regexMatchers("\\A/favicon.ico\\Z");

	}

	// Interceptor URL의 요청을 안전하게 보호(보안)하는 방법을 설정(ACL 작성)
	/*
	 * deny all /user/update -> (ROLE_USER, ROLE_ADMIN) -> Authenticated
	 * /user/logout -> (ROLE_USER, ROLE_ADMIN) -> Authenticated /board/write ->
	 * (ROLE_USER, ROLE_ADMIN) -> Authenticated /board/delete -> (ROLE_USER,
	 * ROLE_ADMIN) -> Authenticated /board/modify -> (ROLE_USER, ROLE_ADMIN) ->
	 * Authenticated /admin/** -> ROLE_ADMIN(Authorized) allow all
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		//
		// 1. ACL 설정
		//
		http.authorizeRequests()

				// 인증이 되었있을 때(authenticated?)
				.antMatchers("/user/update", "/user/logout").authenticated()
				.antMatchers("/board/write", "/board/delete", "/board/modify").authenticated()

				// ADMIN Authority(ADMIN 권한, ROLE_ADMIN)
				// .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
				// .antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")

				// 모두 허용
				// .antMatchers("/**").permitAll()
				.anyRequest().permitAll();

		// 추가
		// CSRF 설정 (기본 되는데 지금하기 귀찮으니까 안쓰게)
		// http.csrf().disable();

		//
		// 2. 로그인 설정
		//
		http.formLogin().loginPage("/user/login").loginProcessingUrl("/user/auth").failureUrl("/user/login?result=fail")
		//.defaultSuccessUrl("/", true)
		
		.successHandler(authenticationSuccessHandler())
		.usernameParameter("email").passwordParameter("password");

		//
		// 3. 로그아웃 설정
		//
		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")).logoutSuccessUrl("/")
				.invalidateHttpSession(true);

		//
		// 4. Access Denial Handler
		http.exceptionHandling().accessDeniedPage("/WEB-INF/views/error/403.jsp");
		
		//
		// 5. RememberMe 
		//
		http
		.rememberMe()
		.key("mysite3")
		.rememberMeParameter("remember-me");
			
		// Temporary for Testing
				http.csrf().disable();
	}

	// UserDetailService 를 설정
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
		.and()
		.authenticationProvider(authenticationProvider());
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = 
				new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// AuthenticationSuccessHandler 등록
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new CustomUrlAuthenticationSuccessHandler();
	}

}
