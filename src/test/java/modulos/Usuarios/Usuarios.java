package modulos.Usuarios;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

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
			.body("quantidade", equalTo(63));
		
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

}