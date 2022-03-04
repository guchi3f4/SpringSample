package com.example.demo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	private static final String USER_SQL = "SELECT user_id, password, true FROM m_user WHERE user_id = ?";
	
	private static final String ROLE_SQL = "SELECT user_id, role FROM m_user WHERE user_id = ?";
	
	private final DataSource dataSource;
	
	@Autowired
	public SecurityConfig(DataSource dataSource){
		this.dataSource = dataSource;
	}

	@Override
	public void configure(WebSecurity web) throws Exception{
		web.ignoring().antMatchers("/webjars/**", "/css/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		//ログイン不要ページの設定
		http.authorizeRequests()
			.antMatchers("/webjars/**").permitAll()
			.antMatchers("/css/**").permitAll()
			.antMatchers("/login").permitAll()
			.antMatchers("/signup").permitAll()
			.antMatchers("/rest/**").permitAll()
			.antMatchers("/admin").hasAuthority("ROLE_ADMIN")
			.anyRequest().authenticated();
		
		//ログイン処理
		http.formLogin()
			.loginProcessingUrl("/login")
			.loginPage("/login")
			.failureUrl("/login")
			.usernameParameter("userId")
			.passwordParameter("password")
			.defaultSuccessUrl("/home", true);
		
		//ログアウト処理
		http.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutUrl("/logout")
			.logoutSuccessUrl("/login");
		
		//csrfを無効
		RequestMatcher csrMatcher = new RestMatcher("/rest/**");
		http.csrf().requireCsrfProtectionMatcher(csrMatcher);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
			.dataSource(dataSource)
			.usersByUsernameQuery(USER_SQL)
			.authoritiesByUsernameQuery(ROLE_SQL)
			.passwordEncoder(passwordEncoder());
	}
	
	
}