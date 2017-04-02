package biz.thaicom.backoffice.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		  auth.inMemoryAuthentication().withUser("ram").password("ram123").roles("ADMIN");
		  auth.inMemoryAuthentication().withUser("ravan").password("ravan123").roles("USER");
		  auth.inMemoryAuthentication().withUser("user").password("user").roles("USER");
	}
	
	protected void configure(HttpSecurity http) throws Exception {
		http
			.headers().frameOptions().disable()
			.and()
			.authorizeRequests()
				.antMatchers("/static/**").permitAll()
				.antMatchers("/webjars/**").permitAll()
				.antMatchers("/login**").permitAll().anyRequest()
				.fullyAuthenticated()
			.and()
			.csrf().disable()
			.formLogin()
				.loginPage("/login").permitAll()
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/")
			.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout_successful=1");
	}
}
