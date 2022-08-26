package br.com.forum.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.forum.modelo.Topico;

//Parametros - Entidade e Tipo do ID dessa entidade(chave primaria)
public interface TopicoRepository extends JpaRepository<Topico, Long>{

	//filtrando por um relacionamento
		//Curso é a entidade de relacionamento de Topico
			//Nome é o atributo dentro da entidade
	List<Topico> findByCursoNome(String nomeCurso);
	
}
