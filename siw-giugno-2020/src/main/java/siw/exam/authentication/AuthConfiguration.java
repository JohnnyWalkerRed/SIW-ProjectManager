package siw.exam.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import siw.exam.model.Credentials;


@Component
public class AuthConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	javax.sql.DataSource datasource;
	protected void configure(HttpSecurity http) throws Exception{
		http
			.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/", "/index", "/login", "/users/register").permitAll()
			.antMatchers(HttpMethod.POST, "/login", "/users/register").permitAll()
			.antMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(Credentials.ADMIN_ROLE)
			.antMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(Credentials.ADMIN_ROLE)
			.anyRequest().authenticated()
			.and().formLogin()
			.defaultSuccessUrl("/home")
			.and().logout()
			.logoutUrl("/logout")
			.logoutSuccessUrl("/index")
			.invalidateHttpSession(true)
			.clearAuthentication(true).permitAll();
	}
	@Override
	public void configure(AuthenticationManagerBuilder auth)throws Exception{
		auth.jdbcAuthentication()
			.dataSource(this.datasource)
			.authoritiesByUsernameQuery("SELECT user_name, role FROM credentials WHERE user_name=?")
			.usersByUsernameQuery("SELECT user_name, password, 1 as enabled FROM credentials WHERE user_name=?");
	}
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
