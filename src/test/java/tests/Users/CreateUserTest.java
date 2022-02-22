package tests.Users;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.junit.Test;

import Core.BaseTest;
import Factory.UserDataFactory;
import POJO.UsersPojo;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

public class CreateUserTest extends BaseTest{

	//Criado CLasse de Testes compondo somente os testes e validacoes de criacao de usuarios
	
	
	@Test
	public void createUserAdmin() {
		UsersPojo user = new UserDataFactory().userAdmin();	
		
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.body(user)
		.when()
			.post("/usuarios")
		.then()
			.statusCode(201)
			.body("message", is("Cadastro realizado com sucesso"))
			.body("_id", is(notNullValue()))
			.extract().path("_id");
	}
	
	@Test
	public void createUserWithoutTypeAdmin() {
		UsersPojo user = new UserDataFactory().userNotAdmin();
		user.setAdministrador("false");
		
		given()	
			.body(user)
		.when()
			.post("/usuarios")
		.then()
			.statusCode(201)
			.body("message", is("Cadastro realizado com sucesso"))
			.body("_id", is(notNullValue()))
			.extract().path("_id");
	}
	
	@Test
	public void createUserWithTypeNull() {
		UsersPojo user = new UserDataFactory().userNotAdmin();
		user.setAdministrador(null);
		
		given()	
			.body(user)
		.when()
			.post("/usuarios")
		.then()
			.statusCode(400)
			.body("administrador", is("administrador deve ser 'true' ou 'false'"));
	}
	
	@Test
	public void createUserWithTypeIncorrect() {
		UsersPojo user = new UserDataFactory().userNotAdmin();
		user.setAdministrador("adm");
				
		given()	
			.body(user)
		.when()
			.post("/usuarios")
		.then()
			.statusCode(400)
			.body("administrador", is("administrador deve ser 'true' ou 'false'"));
	}
	
	@Test
	public void createUserWithoutType() {
		UsersPojo user = new UserDataFactory().userNotAdmin();
		
		given()	
			.body(user)
		.when()
			.post("/usuarios")
		.then()
			.statusCode(400)
			.body("administrador", is("administrador deve ser 'true' ou 'false'"));
	}
	
	@Test
	public void createdUserWithInvalidEmail() {
		UsersPojo user = new UserDataFactory().userAdmin();		
		user.setEmail("email_invalido.com");
		given()
			.body(user)
		.when()
			.post("/usuarios")
		.then()
			.statusCode(400)
			.body("email", is("email deve ser um email válido"));
	}
	
	@Test
	public void createUserNameNull() {
		UsersPojo user = new UserDataFactory().userAdmin();
		user.setNome(null);
		
		given()
			.body(user)
		.when()
			.post("/usuarios")
		.then()
			.statusCode(400)
			.body("nome", is("nome deve ser uma string"));
	}
	
	@Test
	public void createUserWithoutName() {
		UsersPojo user = new UserDataFactory().userWithoutName();
		
		given()
			.body(user)
		.when()
			.post("/usuarios")
		.then()
			.statusCode(400)
			.body("nome", is("nome deve ser uma string"));
	}
	
	@Test
	public void createUserWithoutPassword() {
		UsersPojo user = new UserDataFactory().userWithoutPassword();
		
		given()
			.log().all()
			.body(user)
		.when()
			.post("/usuarios")
		.then()
			.log().all()
			.statusCode(400)
			.body("password", is("password deve ser uma string"));
	}
	
}
