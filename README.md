A criação de uma API em Java pode ser um pouco desgastante, visto que será necessário a criação e configuração de vários arquivos para pouco trabalho de código.

Diante deste problema é possível encontrarmos soluções como por exemplo o **Spring Boot**. Com ele é possível iniciarmos a criação de uma API com poucos cliques através de uma configuração guiada e, junto ao **Maven**, é possível realizar o gerenciamento de dependências.

Para a construção de uma API de registro de usuários é necessário criarmos um database. Dado esta situação é necessário escolhermos um gerenciador de Banco de Dados. No caso optei pelo **Mysql**.

Como iremos utilizar ferramentas de database é possível adicionarmos o **Spring Data JPA**, se trata de uma tecnologia que usara o **Hibernate** e que fará a persistência dos dados, trazendo mais praticidade ao realizar uma query no database.

## Criando a Entidade

Após a configuração do projeto podemos criar uma Classe com o nome de um objeto que iremos manipular no nosso algoritmo. Esta Classe deve ficar no mesmo package que a Classe principal ou em um subpackage. Para a situação criaremos a Classe Usuario.

Com esta Classe em mãos usaremos algumas features do Spring Data JPA, adicionaremos a anotação `@Entity` acima da Classe Usuario.

Esta anotação fará com que a classe Usuario se torne uma Entidade, com isto o JPA fará uma conexão entre a Entidade e uma tabela de mesmo nome.

A seguir adicionamos as seguintes anotações para o atributo id.

`@Id`

`@GeneratedValue(strategy = GenerationType.IDENTITY)`

Isso fará com que a variável id seja interpretada como um Primary Key. Esta sera incrementada a cada novo objeto.

```java
@Entity
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String email;
	private String cpf;
	private String dataNascimento;
}
```

Também é possível utilizar a anotações `@Column(unique = true)` acima de uma variável para indicar que não pode haver mais de um objeto com o mesmo atributo na tabela.

## Criando o Repositório

Criaremos agora uma interface que atuara como Repositório para o nosso projeto. Para isso a interface deve herdar de JpaRepository, abrindo a oportunidade de facilitar as operações CRUD através de métodos como `save(Obj)` que enviara os atributos presente na Classe Obj para o database. Para esta situação criaremos a Interface UsuarioRepository e adicionaremos a anotação `@Repository` a ela.

```java
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}
```

## Criando o Controller

Com a Entidade e o Repositório criados precisaremos agora de uma classe Controller para que possamos finalizar a nossa aplicação. Nesta classe faremos os métodos para que nossa API passe a trabalhar como REST API.

Para começar precisaremos criar uma Classe com a anotação `@RestController` .

Em seguida referenciaremos a nossa Interface Repositório a uma variável e adicionaremos a anotação `@Autowired` a ela. Esta anotação fará com que o Spring Boot inicialize o objeto.

Com isso pronto podemos criar Métodos para fazer as requisições HTTP conforme for necessário.

- Para criarmos um método para receber as informações presentes no database usaremos a anotação `@GetMapping(Caminho)`
- Para uma situação de enviar dados ao database usaremos a anotação `@PostMapping(Caminho)`
- Para atualizarmos um dado presente no arquivo podemos utilizar a anotação `@PutMapping(Caminho)`

Para a requisição Post torna-se interessante enviarmos um status de respostas HTTP.

Usaremos, portando, o tipo `ResponseEntity<Classe Entidade>` no método e poderemos retornar o status de respostas ao serviço.

```java
@RestController
@RequestMapping("/registro")
public class UsuarioController {
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@GetMapping("/")
	public List<UsuarioRs> findAll() {
		var usuarios = usuarioRepository.findAll();
		return usuarios.stream().map(UsuarioRs::converter).collect(Collectors.toList());
	}
	
	@PostMapping("/")
	public ResponseEntity<Usuario> saveUsuario(@RequestBody Usuario usuario) {
		try {
			usuarioRepository.save(user);
			System.out.println("Requisição foi bem sucedida");
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			System.out.println("Algum dado esta incorreto ou é invalido");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
```

Com nossa REST API pronta precisaremos agora configurar o mysql. Para isso entraremos na pasta resources e abriremos o arquivo `application.properties` .

Neste arquivo iremos adicionar as seguintes linhas:

```java
spring.datasource.url=jdbc:mysql://localhost/{Database}
spring.datasource.username={Usuario Mysql}
spring.datasource.password={Senha Mysql}
```

Realize a alteração nos campos *{Database}, {Usuario Mysql} e {Senha Mysql}* conforme configurado em sua máquina.

Feito isso, ainda podemos configurar como o database irá reagir ao iniciarmos a aplicação adicionando a seguinte linha:

```java
spring.jpa.hibernate.ddl-auto={Reacao}
```

Altere o campo {Reacao} conforme necessário utilizando essas 5 opções:

- `create` : Cria a Table.
- `create-drop` : Realiza o drop da Table.
- `update` : Adicionara colunas à Table caso ocorra alguma modificação na Classe Entidade.
- `validate` : Valida a Table, não permitindo mudanças nos dados.
- `none`

## Rodando a aplicação

Com tudo devidamente configurado podemos iniciar nossa aplicação.

Para realizarmos os testes podemos utilizar o Postman.

Após logarmos na aplicação iremos criar uma Collection e adicionar uma requisição.

Selecionaremos qual é o tipo de requisição HTTP e colocaremos `localhost:8080/registro/` no campo Request URL.

Para a o caso de realizar um Post usaremos a seguinte estrutura JSON devidamente preenchida:

```json
{
    "nome": "",
    "email": "",
    "cpf": "",
    "dataNascimento": ""
}
```

É importante que os dados sigam um padrão para assegurar por exemplo que um email válido seja enviado, por esta razão é possível implementar algoritmos de Regex para validar os padrões.

Nome:

```java
public Boolean verifyRegexNome(String nome) {
	String regexNome = "[a-z A-Z]*";
	Pattern verifyRegex = Pattern.compile(regexNome);
	Matcher regex = verifyRegex.matcher(nome);
	if (regex.matches()) {
		return true;
	}
	System.out.println("Nome invalido");
	return false;
}
```

Limitara ao uso apenas de letras.

Email:

```java
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
```

CPF:

```java
public Boolean verifyRegexCpf(String cpf) {
	String cpfSemPontuacao = "[0-9]{11}";
	Pattern verifyRegex = Pattern.compile(cpfSemPontuacao);
	Matcher regex = verifyRegex.matcher(cpf.replaceAll("[.]?[-]?", ""));
	if (regex.matches()) {
		return true;
	}
	System.out.println("CPF invalido");
	return false;
}
```

Limitara a exatamente 11 caracteres sendo eles números.

Data de Nascimento:

```java
public Boolean verifyRegexNascimento(String nascimento) {
	String dataNasc = "(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[012])(19|20)[0-9]{2}";
	Pattern verifyRegex = Pattern.compile(dataNasc);
	Matcher regex = verifyRegex.matcher(nascimento.replaceAll("/", ""));
	if (regex.matches()) {
		return true;
	}
	System.out.println("Nascimento invalido");
	return false;
}
```
