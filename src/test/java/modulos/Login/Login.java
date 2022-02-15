package modulos.Login;

import org.junit.Test;

import Core.BaseTest;
import POJO.LoginPojo;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.*;

public class Login extends BaseTest {
	
	private static String email = "email" + System.nanoTime() + "@teste.com";
	private static String password = "Teste";
	private static String token;

	@Test
	public void testeFazerLogin() {
		LoginPojo login = new LoginPojo();
		login.setEmail(email);
		login.setPassword(password);
		
		token = 
		given()
			.body(login)
		.when()
			.post("/login")
		.then()
			.extract().path("authorization")
		;	
		
	 	RestAssured.requestSpecification.header("authorization", token);
	}
}
