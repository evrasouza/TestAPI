package modulos.Usuarios;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;


public class Usuarios {
	
	@BeforeClass
	public static void setup() {
		baseURI = "https://serverest.dev";
		basePath = "/usuarios";
	}
	
	@Test
	public void testListarUsuariosCadastrados() {

		System.out.println("======================INICIO DOS REQUESTS======================");
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
		.when()
			.get()
		.then()
			.statusCode(200)
			.body("quantidade", equalTo(298));
		
		System.out.println("======================FIM DOS REQUESTS======================");
	}
	
	
	@Test
	public void testListarUsuariosCadastradosPorID() {

		System.out.println("======================INICIO DOS REQUESTS======================");
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
		.when()
			.get("/0J4JA3RSXjoMNg0A")
		.then()
			.statusCode(200)
			.body("password", equalTo("teste"))
			.body("administrador", equalTo("true"));
		
		System.out.println("======================FIM DOS REQUESTS======================");
	}
	
	@Test
	public void testListarTodosUsuarios() {

		System.out.println("===============================INÍCIO DOS REQUESTS==================================");
		
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
		.when()
			.get()
		.then()
			.statusCode(200)
		;
		
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testListarUsuariosPorID() {
		
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.pathParam("_id", "zH77859afSAeNwMH")
		.when()
			.get("{_id}")
		.then()
			.statusCode(200)
		;
	}
	
	@Test
	public void testListarUsuariosPorIDInexistente() {
		
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
	}
	
	@Test
	public void testListarUsuarioPorNome() {
		
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.queryParam("nome", "Stephen King")
		.when()
			.get()
		.then()
			.statusCode(200)
			.body("usuarios.nome", hasItems("Stephen King"))
		;
	}
	
	@Test
	public void testListarUsuarioPorNomeInexistente() {
		
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
	}

	@Test
	public void testListarUsuarioPorEmail() {
		
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.queryParam("email", "fulano@qa.com")
		.when()
			.get()
		.then()
			.statusCode(200)
			.body("quantidade", is(1))
			.body("usuarios.email", hasItem("fulano@qa.com"))
			.body("usuarios._id", hasItem("0uxuPY0cbmQhpEz1"))
		;
	}
	
	@Test
	public void testListarUsuarioPorPassword() {
		
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
	}
	
	@Test
	public void testListarUsuarioPorAdministrador() {
		
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.queryParam("administrador", "true")
		.when()
			.get()
		.then()
			.statusCode(200)
		;
		
	}
	
	@Test
	public void testListarUsuarioPorIDNomeEmailPasswordEAdministrador() {

		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.queryParam("_id", "0Vh5VLqFVFjb1kZC")
			.queryParam("nome", "Stephen King")
			.queryParam("email", "462853978818387abc@test.com")
			.queryParam("password", "teste")
			.queryParam("administrador", "true")
		.when()
			.get()
		.then()
			.statusCode(200)
			.body("usuarios.nome", hasItem("Stephen King"))
			.body("usuarios.email", hasItem("462853978818387abc@test.com"))
			.body("usuarios.password", hasItem("teste"))
			.body("usuarios.administrador", hasItem("true"))
			.body("usuarios._id", hasItem("0Vh5VLqFVFjb1kZC"))
		;
		
	}
}
