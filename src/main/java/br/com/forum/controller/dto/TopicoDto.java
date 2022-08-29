package br.com.forum.controller.dto;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;

import br.com.forum.modelo.Topico;

//Classe para transporte de dados 
	//Somente enviar os dados que queremos pegar da classe Topico
	//Melhor controle de retorno
public class TopicoDto {
	private Long id;
	private String titulo;
	private String mensagem;
	private LocalDateTime dataCriacao;
	
	//Contrutor que a partir do topico preenche os atributos 
		//Ao instaciar passa o objeto  do tipo Topico
	public TopicoDto(Topico topico) {
		this.id = topico.getId();
		this.titulo = topico.getTitulo();
		this.mensagem = topico.getMensagem();
		this.dataCriacao = topico.getDataCriacao();
	}
	
	public Long getId() {
		return id;
	}
	public String getTitulo() {
		return titulo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	//Encapsulando para conversão de TopicoDto para Topico 
	public static Page<TopicoDto> converter(Page<Topico> topicos) {
		//1- stream():Recebe o fluxo de listas de topicos
		//2-map():função intermediaria que toma cada elemento de uma stream como parametro e retorna o elemento processado como resposta.
			//Nesse caso irá chamar o construtor como parametro
		//3- collect():possibilita coletar os elementos de uma stream em forma de coleção
		//4-Collector.toList: reuni o resultado da stream e os retorna como lista
		//return topicos.stream().map(TopicoDto::new).collect(Collectors.toList());
		return topicos.map(TopicoDto::new);
	}
	
	
	
}
