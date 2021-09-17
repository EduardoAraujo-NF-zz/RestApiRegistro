package apiVac.controller.odt;
import apiVac.model.Usuario;


public class UsuarioResponse {
	private Long id;
	private String nome;
	private String email;
	private String cpf;
	private String dataNascimento;

	public static UsuarioResponse converter(Usuario p) {
		var usuario = new UsuarioResponse();
		usuario.setId(p.getId());
		usuario.setNome(p.getNome());
		usuario.setEmail(p.getEmail());
		usuario.setCpf(p.getCpf());
		usuario.setDataNascimento(p.getDataNascimento());
		return usuario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
}
