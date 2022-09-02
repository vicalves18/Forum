package br.com.forum.controller.form;

//Form -> pega os parametros que chegam do cliente
public class LoginForm {
	private String email;
	private String senha;
	
	
	
	public String getEmail() {
		return email;
	}
	public String getSenha() {
		return senha;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
}
