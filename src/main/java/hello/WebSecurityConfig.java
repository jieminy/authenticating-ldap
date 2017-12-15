package hello;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.anyRequest().authenticated()
				.and()
				.formLogin().and()
				.httpBasic();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.ldapAuthentication()
//				.userDnPatterns("uid={0},ou=User")
				.userSearchFilter("uid={0}")
				.userSearchBase("ou=Users")
				.groupSearchBase("ou=UserRelations")
				.groupSearchFilter("member={0}")
//				.groupRoleAttribute("ROLE_")
				.contextSource()
					.url("ldap://localhost:389/dc=maxcrc,dc=com")
				    .managerDn("cn=Manager,dc=maxcrc,dc=com")
					.managerPassword("root")
					.and()
				.passwordCompare()
					.passwordAttribute("userPassword");
	}

}
