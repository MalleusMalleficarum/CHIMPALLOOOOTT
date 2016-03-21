/**
 * 
 */
package edu.kit.ipd.chimpalot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Configures the security of the app.
 * 
 * @author Thomas Friedel
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	/**
	 * Sets which URLs need authentication.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/assignment/**").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
				.permitAll();
		http.csrf().disable(); //TODO enable this for proper security. Needs some configuration in the frontend though.
		http.headers().frameOptions().disable();
	}
	
	/**
	 * Sets the credentials for the requester interface. Is only called once on startup.
	 * 
	 * @param auth
	 * @throws Exception if something went wrong
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser(GlobalApplicationConfig.getRequesterUsername())
		.password(GlobalApplicationConfig.getRequesterPassword())
		.roles("USER");
	}
}
