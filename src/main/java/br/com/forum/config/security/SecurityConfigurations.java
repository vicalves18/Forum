package br.com.forum.config.security;

import org.jboss.jandex.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter{
	@Autowired
	private AutenticacaoService autenticacaoService;
	
	//Configuração de autenticacao(controle de acesso, login)
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//Passa que a clase que possue a logica de autenticacaos
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
	
	}
	
	//Configuração de autorizacao(URL,perfil de acesso)
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers(HttpMethod.GET,"/topicos").permitAll()
		.antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
		.anyRequest().authenticated()
		.and().formLogin();
	}
	
	//Configuração de recursos estaticos(js,css,img)
	@Override
	public void configure(WebSecurity web) throws Exception {
	
	}
	
	/*Gera senha no formato BCrypt
	 * public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("123456"));
	}*/
}
