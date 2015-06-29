package com.cisco.apicem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.cisco.apicem.service.MongoDBAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private MongoDBAuthenticationProvider authenticationProvider;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**/*.html", //
				"/css/**", //
				"/js/**", //
				"/i18n/**",//
				"/libs/**",//
				"/img/**", //
				"/webjars/**",//
				"/ico/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/api/public/**").permitAll().and().authorizeRequests()
				.antMatchers("/api/mgt/**").hasRole("ADMIN").and().authorizeRequests().antMatchers("/api/**")
				.authenticated().and().authorizeRequests().anyRequest().permitAll().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.ALWAYS).and().httpBasic().and().csrf().disable();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}

}
