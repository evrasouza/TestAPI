package tests.Login;

import static io.restassured.RestAssured.given;

import org.junit.Test;

import Pojo.LoginPojo;
import Utils.BaseTest;
import io.restassured.RestAssured;

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
