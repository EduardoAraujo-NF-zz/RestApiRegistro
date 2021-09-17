package apiVac.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@Column(unique = true)
	private String email;
	@Column(unique = true)
	private String cpf;
	private String dataNascimento;

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
	
	public Boolean verifyRegexNome(String nome) {
		String regexNome = "[a-zA-Z]{1}([a-z A-Z]*)?";
		Pattern verifyRegex = Pattern.compile(regexNome);
		Matcher regex = verifyRegex.matcher(nome);
		if (regex.matches()) {
			return true;
		}
		System.out.println("Nome invalido");
		return false;
	}

	public Boolean verifyRegexNascimento(String nascimento) {
		String dataNasc = "(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[012])(19|20)[0-9]{2}";
		Pattern verifyRegex = Pattern.compile(dataNasc);
		Matcher regex = verifyRegex.matcher(nascimento);
		if (regex.matches()) {
			return true;
		}
		System.out.println("Nascimento invalido");
		return false;
	}

	public Boolean verifyRegexEmail(String email) {
		String regexEmail = "[a-zA-Z][a-zA-Z0-9._]{2,25}[a-zA-Z0-9]@[a-z]{2,20}[.][a-z]{2,20}(([.][a-z]{2,7})?){1,2}";
		Pattern verifyRegex = Pattern.compile(regexEmail);
		Matcher regex = verifyRegex.matcher(email);
		if (regex.matches()) {
			return true;
		}
		System.out.println("Email invalido");
		return false;
	}

	public Boolean verifyRegexCpf(String cpf) {
		String cpfSemPontuacao = "[0-9]{11}";
		Pattern verifyRegex = Pattern.compile(cpfSemPontuacao);
		Matcher regex = verifyRegex.matcher(cpf);
		if (regex.matches()) {
			return true;
		}
		System.out.println("CPF invalido");
		return false;
	}
}
