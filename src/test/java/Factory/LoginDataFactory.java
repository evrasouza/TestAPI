package Factory;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

import Core.BaseTest;
import POJO.LoginPojo;
import POJO.UsersPojo;
import io.restassured.RestAssured;

public class LoginDataFactory extends BaseTest{
	private static String email;
	private static String password;
	
	public void loginAdmin(){	
	UsersPojo usuario = new UserDataFactory().userAdmin();	
	given()	
		.body(usuario)
	.when()
		.post("/usuarios")
	.then()
		.statusCode(201)
		.body("message", is("Cadastro realizado com sucesso"))
		.body("_id", is(notNullValue()))
	;
	email = usuario.getEmail();
	password = usuario.getPassword();
		
	LoginPojo login = new LoginPojo();
	login.setEmail(email);
	login.setPassword(password);
		
	String token = given()
			.body(login)
		.when()
			.post("/login")
		.then()
			.extract().path("authorization")
		;			
	RestAssured.requestSpecification.header("authorization", token);		
	}
	
	public void userNotAdmin(){
		UsersPojo usuario = new UserDataFactory().userNotAdmin();	
		given()	
			.body(usuario)
			.when()
			.post("/usuarios")
		.then()
			.statusCode(201)
			.body("message", is("Cadastro realizado com sucesso"))
			.body("_id", is(notNullValue()))
		;
		email = usuario.getEmail();
		password = usuario.getPassword();
		
		LoginPojo login = new LoginPojo();
		login.setEmail(email);
		login.setPassword(password);
		
		String token = given()
			.body(login)
		.when()
			.post("/login")
		.then()
			.extract().path("authorization")
		;			
		 RestAssured.requestSpecification.header("authorization", token);		
	}
		
}
