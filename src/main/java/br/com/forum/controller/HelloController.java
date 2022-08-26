package br.com.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
	//URL -> Saber quando o Spring irá chamar o metodo
	@RequestMapping("/")
	//Para o Spring não considerar esse metodo como uma página
	@ResponseBody
	public String hello() {
		return "Hello World!";
	}
}
