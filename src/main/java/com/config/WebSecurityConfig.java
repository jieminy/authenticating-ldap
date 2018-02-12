package com.config;

import com.error.MyAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private MyAccessDeniedHandler myAccessDeniedHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/","/home","/about","/register","/doregist","/403").permitAll()
				.antMatchers("/admin/**").hasRole("DEVELOPERS")
				.antMatchers("/user/**").hasRole("TESTER")
				.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
			.and()
				.logout().permitAll()
			.and()
			.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.ldapAuthentication()
//				.userDnPatterns("uid={0},ou=User")
				.userSearchFilter("uid={0}")
				.userSearchBase("ou=Users")
//				.groupSearchBase("ou=UserRelations")
//				.groupSearchFilter("member={0}")
//				.groupRoleAttribute("ROLE_")
				.contextSource()
					.url("ldap://localhost:389/dc=maxcrc,dc=com")
				    .managerDn("cn=Manager,dc=maxcrc,dc=com")
					.managerPassword("secret")
					.and()
				.passwordCompare()
					.passwordAttribute("userPassword");
	}

}
