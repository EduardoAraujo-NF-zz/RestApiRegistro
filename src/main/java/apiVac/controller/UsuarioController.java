package apiVac.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apiVac.controller.odt.UsuarioRequest;
import apiVac.controller.odt.UsuarioResponse;
import apiVac.model.Usuario;
import apiVac.repository.UsuarioRepository;

@RestController
@RequestMapping("/registro")
public class UsuarioController {
	private final UsuarioRepository usuarioRepository;

	public UsuarioController(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@GetMapping("/")
	public List<UsuarioResponse> findAll() {
		var usuarios = usuarioRepository.findAll();
		return usuarios.stream().map((user) -> UsuarioResponse.converter(user)).collect(Collectors.toList());
	}

	@PostMapping("/")
	public ResponseEntity<Usuario> saveUsuario(@RequestBody UsuarioRequest usuario) {
		var user = new Usuario();
		user.setId(usuario.getId());
		user.setNome(usuario.getNome());
		user.setEmail(usuario.getEmail());
		user.setCpf(usuario.getCpf().replaceAll("[.]?[-]?", "")); // Remove caractéres especiais do cpf
		user.setDataNascimento(usuario.getDataNascimento().replaceAll("/", "")); // Remove caractéres especiais da data de nascimento
		if (!user.verifyRegexCpf(user.getCpf()) || !user.verifyRegexEmail(user.getEmail())
				|| !user.verifyRegexNascimento(user.getDataNascimento()) || !user.verifyRegexNome(user.getNome())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		try {
			// Tenta enviar os dados, se der conflito relacionado a email
			// ou cpf ja cadastrado o codigo continua no catch
			usuarioRepository.save(user); 	
			System.out.println("Criado com sucesso");
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			System.out.println("Algum dado esta incorreto ou repetido");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
