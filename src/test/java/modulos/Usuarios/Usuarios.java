package modulos.Usuarios;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;


public class Usuarios {
	
	private static Integer qtde;
	private static String email = "email" + System.nanoTime() + "@teste.com";
	
	@BeforeClass
	public static void setup() {
		baseURI = "https://serverest.dev";
		basePath = "/usuarios";
	}
	
	@Test
	public void testListarTodosUsuarios() {
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");		
		 qtde = given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
		.when()
			.get()
		.then()
			.statusCode(200)
			.extract()
			.path("quantidade")
		;	
		System.out.println("===============================FIM DOS REQUESTS==================================");

		System.out.println("===============================INÍCIO DOS REQUESTS==================================");		
		 given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
		 .when()
		 	.get()
		 .then()
		 	.body("quantidade", is(qtde))
		 ;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testListarUsuariosCadastradosPorID() {

		System.out.println("======================INICIO DOS REQUESTS======================");
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
		.when()
			.get("/9LBET1VLsE7K0GUX")
		.then()
			.statusCode(200)
			.body("password", equalTo("teste"))
			.body("administrador", equalTo("true"))
		;		
		System.out.println("======================FIM DOS REQUESTS======================");
	}
	
	@Test
	public void testListarUsuariosPorID() {
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.pathParam("_id", "9YhaiKZCjuzi0xiH")
		.when()
			.get("{_id}")
		.then()
			.statusCode(200)
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testListarUsuariosPorIDInexistente() {
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.pathParam("_id", "zvec4bEXFr6osg88")
		.when()
			.get("{_id}")
		.then()
			.statusCode(400)
			.body("message", equalTo("Usuário não encontrado"))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testListarUsuarioPorNome() {
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.queryParam("nome", "Pokémon Ultra Moon")
		.when()
			.get()
		.then()
			.statusCode(200)
			.body("usuarios.nome", hasItems("Pokémon Ultra Moon"))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testListarUsuarioPorNomeInexistente() {
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.queryParam("nome", "Usuario Inexistente")
		.when()
			.get()
		.then()
			.statusCode(200)
			.body("quantidade", is(0))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");

	}

	@Test
	public void testListarUsuarioPorEmail() {
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.queryParam("email", "herman.cummings@schneider.net")
		.when()
			.get()
		.then()
			.statusCode(200)
			.body("quantidade", is(1))
			.body("usuarios.email", hasItem("herman.cummings@schneider.net"))
			.body("usuarios._id", hasItem("9l4MsVKlZz2shSjL"))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testListarUsuarioPorPassword() {
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.queryParam("password", "teste")
		.when()
			.get()
		.then()
			.statusCode(200)
			.body("usuarios.password", hasItems("teste"))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testListarUsuarioPorAdministrador() {
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.queryParam("administrador", "true")
		.when()
			.get()
		.then()
			.statusCode(200)
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testListarUsuarioPorIDNomeEmailPasswordEAdministrador() {
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.queryParam("_id", "9sGgelvpCljGoPd0")
			.queryParam("nome", "Wolfenstein: The New Order")
			.queryParam("email", "yong@hilpert.info")
			.queryParam("password", "teste")
			.queryParam("administrador", "true")
		.when()
			.get()
		.then()
			.statusCode(200)
			.body("usuarios.nome", hasItem("Wolfenstein: The New Order"))
			.body("usuarios.email", hasItem("yong@hilpert.info"))
			.body("usuarios.password", hasItem("teste"))
			.body("usuarios.administrador", hasItem("true"))
			.body("usuarios._id", hasItem("9sGgelvpCljGoPd0"))
		;	
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void TestCadastrarNovoUsuario() {
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");
		Map<String, String> login = new HashMap<String, String>();
		login.put("nome","Julia da Silva");
		login.put("email", email);
		login.put("password", "teste");
		login.put("administrador", "true");
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())	
			.contentType(ContentType.JSON)
			.body(login)
		.when()
			.post()
		.then()
			.statusCode(201)
			.contentType(ContentType.JSON)
			.body("message", is("Cadastro realizado com sucesso"))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testCadastrarUsuarioComEmailInvalido() {
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");
		Map<String, String> login = new HashMap<String, String>();
		login.put("nome","Julia da Silva");
		login.put("email", "email");
		login.put("password", "teste");
		login.put("administrador", "true");
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())	
			.contentType(ContentType.JSON)
			.body(login)
		.when()
			.post()
		.then()
			.statusCode(400)
			.contentType(ContentType.JSON)
			.body("email", is("email deve ser um email válido"))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
		
	@Test
	public void testCadastrarUsuarioComEmailJaCadastrado() {
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())	
			.contentType(ContentType.JSON)
			.body("{\"nome\": \"Julia da Silva\", \"email\": \"natalia@qa.com.br\", \"password\": \"teste\", \"administrador\": \"true\"}")
		.when()
			.post()
		.then()
			.statusCode(400)
			.contentType(ContentType.JSON)
			.body("message", is("Este email já está sendo usado"))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
}
