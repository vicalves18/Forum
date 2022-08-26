package br.com.forum.config.validacao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*Tratamento do erro
	Adicionando uma exceção para validação do formulario*/
@RestControllerAdvice
public class ErroDeValidacaoHandler {
	
	/*Alterar a mensagem de erro quando faz uma validação
		Ajuda a pegar as mensagens de erro
		Intercionalização: message + locale*/
	@Autowired
	private MessageSource messageSource;
	
	//Pra Spring não devolver a requisao 200(Tudo certo) 
	@ResponseStatus(code = HttpStatus.BAD_REQUEST )
	@ExceptionHandler(MethodArgumentNotValidException.class)
	
	//MethodArgumentNotValidException -> recebe todos os erros ocorridos na requisição
		//Irá devolver uma lista com cada um dos erros que aconteceram
	public List<ErroDeFormularioDto> handle(MethodArgumentNotValidException exception) {
		List<ErroDeFormularioDto> dto = new ArrayList<>();
		
		//Traz do exception o resultado das validações que contem todos os erros do formulario 
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		
		//LocaleContextHolder.getLocale() -> Spring Detecta qual idioma, para poder retornar a mensagem no idioma passado
			//Para cada erro criar um erro de formulario dto(campo, mensagem)
		fieldErrors.forEach(e -> {
			String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
			
			//sempre que houver um erro ele chama o interceptador, passando a lista com todos os erros
			ErroDeFormularioDto erro = new ErroDeFormularioDto(e.getField(), mensagem);
			dto.add(erro);
		});
		
		//retorna a lista de erros dto
		return dto;
	}
}
