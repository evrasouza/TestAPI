package modulos.Usuarios;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;


public class Usuarios {
	
	@Test
	public void testListarUsuariosCadastrados() {
		
		// Configurando os dados basicos da requisicao
		baseURI = "https://serverest.dev";
		basePath = "/usuarios";

		System.out.println("======================INICIO DOS REQUESTS======================");
		given()
			//.log().all()
			//.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
		.when()
			.get()
		.then()
			//.log().all()
			.statusCode(200)
			.body("quantidade", equalTo(354));
		
		System.out.println("======================FIM DOS REQUESTS======================");
	}
	
	
	@Test
	public void testListarUsuariosCadastradosPorID() {
		
		// Configurando os dados basicos da requisicao
		baseURI = "https://serverest.dev";
		basePath = "/usuarios";

		System.out.println("======================INICIO DOS REQUESTS======================");
		given()
			//.log().all()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
		.when()
			.get("/0DlgwB2rgxJPsn7r")
		.then()
			//.log().all()
			.statusCode(200)
			.body("password", equalTo("teste"))
			.body("administrador", equalTo("false"));
		
		System.out.println("======================FIM DOS REQUESTS======================");
	}
	
	@Test
	public void testListarTodosUsuarios() {
		baseURI = "https://serverest.dev";
		basePath = "/usuarios";
		
		System.out.println("===============================IN�CIO DOS REQUESTS==================================");
		
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
		baseURI = "https://serverest.dev";
		basePath = "/usuarios/";
		
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.pathParam("_id", "zvec4bEXFr6osgk5")
		.when()
			.get("{_id}")
		.then()
			.statusCode(200)
		;
	}
	
	@Test
	public void testListarUsuariosPorIDInexistente() {
		baseURI = "https://serverest.dev";
		basePath = "/usuarios/";
		
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.pathParam("_id", "zvec4bEXFr6osg88")
		.when()
			.get("{_id}")
		.then()
			.statusCode(400)
			.body("message", equalTo("Usu�rio n�o encontrado"))
		;
	}
	
	@Test
	public void testListarUsuarioPorNome() {
		baseURI = "https://serverest.dev";
		basePath = "/usuarios/";
		
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
		baseURI = "https://serverest.dev";
		basePath = "/usuarios/";
		
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
		baseURI = "https://serverest.dev";
		basePath = "/usuarios/";
		
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
		baseURI = "https://serverest.dev";
		basePath = "/usuarios/";
		
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
		baseURI = "https://serverest.dev";
		basePath = "/usuarios/";
		
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
		baseURI = "https://serverest.dev";
		basePath = "/usuarios/";
		
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