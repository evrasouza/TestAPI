package modulos.Login;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

public class Login {
	
	@BeforeClass
	public static void setup() {
		baseURI = "https://serverest.dev";
		//basePath = "/login";
	}
	
	@Test
	public void testeFazerLoginCadastrarUsuarios() {
		System.out.println("======================INICIO DOS REQUESTS======================");
		String token = 
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.contentType(ContentType.JSON)
			.body("	{\"email\": \"fulano@qa.com\",\"password\": \"teste\"}\r\n")
		.when()
			.post("/login")
		.then()
			.extract()
			.path("authorization")
		;		
		System.out.println("======================FIM DOS REQUESTS======================");
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.contentType(ContentType.JSON)
			.header("token", token)
			.body("{\r\n"
					+ "  \"nome\": \"Fulano Pedreira\",\r\n"
					+ "  \"email\": \"fulanoli@qa.com.br\",\r\n"
					+ "  \"password\": \"teste\",\r\n"
					+ "  \"administrador\": \"true\"\r\n"
					+ "}")
		.when()
			.post("/usuarios")
		.then()
			.statusCode(201)
			.body("message", is("Cadastro realizado com sucesso"))
			.body("_id", is(notNullValue()))
		;
		

	}
}
