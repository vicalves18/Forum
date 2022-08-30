package br.com.forum.controller;

import java.net.URI;
import java.util.Optional;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import br.com.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.forum.controller.dto.TopicoDto;
import br.com.forum.controller.form.AtualizacaoTopicoForm;
import br.com.forum.controller.form.TopicoForm;
import br.com.forum.modelo.Topico;
import br.com.forum.repository.CursoRepository;
import br.com.forum.repository.TopicoRepository;

//Combinação da Anotação ResponseBody e Controller
@RestController
//Responde as requisoes que contem /topicos
@RequestMapping("/topicos")
public class TopicosController {
	//Injeção de dependencia
	@Autowired
	private TopicoRepository topicoRepository;
	@Autowired
	private CursoRepository cursoRepository;
	
	@GetMapping
	@Cacheable(value="listaDeTopicos")
	//DTO -> dados que saem da api
	public Page<TopicoDto> lista(@RequestParam(required=false) String nomeCurso,
			@PageableDefault(sort="id", direction = Direction.DESC) Pageable paginacao) {
		//Passa na url os parametros:?page=""&size=""&sort="id","asc"
		//@PageableDefault: parametros default
		
		//Adicionando filtro de pesquisa
		if(nomeCurso == null) {
			//Carrega todos os topicos do banco de dados
			//findAll() herdado do JpaRepository
			 Page<Topico> topicos = topicoRepository.findAll(paginacao);
			//transforma os objetos instanciados em lista
			return TopicoDto.converter(topicos);
		}else {
			//findBy() + nome do atributo na entidade: busca pelo atributo passado
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
			return TopicoDto.converter(topicos);
		}	
	}
	
	//Form -> Dados que chegam 
	@PostMapping
	@Transactional
	//Limpar determinado cache
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	//@RequestBody ->recebe os parametros do corpo da requisicao
	//ResponseEntity<> -> retorna o tipo de objeto que sera devolvido no corpo da resposta
	//@Valid -> verifica as anotações especificadas no parametro passado
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
		
		//Retorno da requisição
		//URI-> Identificador de recurso 
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
	
	//detalhar um topico 
		//parametro dinamico
		//@PathVariable -> dizer que o parametro vira da url
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);
		//Tratando erro com 404 - caso nao encontrado o id
		if(topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
		}
		return ResponseEntity.notFound().build();
		
	}
	
	@PutMapping("/{id}")
	//Avisa que é para haver a mudança no banco - commita a transação no final do metodo
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id,@RequestBody @Valid AtualizacaoTopicoForm form){
		Optional<Topico> opcional = topicoRepository.findById(id);
		//Tratando erro com 404 - caso nao encontrado o id
		if(opcional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);
			//corpo devolvido como resposta
			return ResponseEntity.ok(new TopicoDto(topico));
		}
		return ResponseEntity.notFound().build();
		
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable Long id){
		Optional<Topico> opcional = topicoRepository.findById(id);
		if(opcional.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
}
