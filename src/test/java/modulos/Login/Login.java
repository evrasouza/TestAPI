package modulos.Login;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Test;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;

public class Login {
	
	@Test
	public void testListarUsuariosCadastrados() {
		
		// Configurando os dados basicos da requisicao
		baseURI = "https://serverest.dev";		

		System.out.println("======================INICIO DOS REQUESTS======================");

		// Obter token do usuario para login
		String token = given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.contentType(ContentType.JSON)
			.body("{\r\n"
					+ "  \"email\": \"fulano@qa.com\",\r\n"
					+ "  \"password\": \"teste\"\r\n"
					+ "}")
		.when()
			.post("/login")
		.then()
			.extract()
			.path("authorization");
		
		System.out.println(token);		
		System.out.println("======================FIM DOS REQUESTS======================");
		
		
		System.out.println("======================INICIO DOS REQUESTS======================");
		
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.contentType(ContentType.JSON)
			//Neste caso usamos o token anteriormente adquirido pelo login do usuario para que o sistema possa reconhecer 
			// e permitir que o usuario cadastre um novo usuario
			.header("token", token)
			.body("{\r\n"
					+ "  \"nome\": \"Fulano Lima\",\r\n"
					+ "  \"email\": \"fulanolima@qa.com.br\",\r\n"
					+ "  \"password\": \"teste\",\r\n"
					+ "  \"administrador\": \"true\"\r\n"
					+ "}")
			.when()
				.post("/usuarios")
			.then()
				.assertThat()
					.body("message", equalTo("Cadastro realizado com sucesso"))
					.statusCode(201);
		System.out.println("======================FIM DOS REQUESTS======================");
	}

}
