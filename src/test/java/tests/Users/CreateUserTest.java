package tests.Users;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import Core.BaseTest;
import Factory.UserDataFactory;
import POJO.UsersPojo;
import Utils.UserUtil;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

public class CreateUserTest extends BaseTest{
	

	public String email;
	
	
	@Before
    public void createUser() {
        email = new UserUtil().createEmail();
    }
	
	@Test
	public void createUserAdmin() {
		UsersPojo user = new UserDataFactory().userAdmin();	
		
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.body(user)
		.when()
			.post(APP_BASE_URL_USUARIOS)
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
			.post(APP_BASE_URL_USUARIOS)
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
			.post(APP_BASE_URL_USUARIOS)
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
			.post(APP_BASE_URL_USUARIOS)
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
			.post(APP_BASE_URL_USUARIOS)
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
			.post(APP_BASE_URL_USUARIOS)
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
			.post(APP_BASE_URL_USUARIOS)
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
			.post(APP_BASE_URL_USUARIOS)
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
			.post(APP_BASE_URL_USUARIOS)
		.then()
			.log().all()
			.statusCode(400)
				.body("password", is("password deve ser uma string"));
	}
	@Test
	public void t02_testCadastrarUsuarioComEmailInvalido() {
		UsersPojo usuario = new UserDataFactory().userAdmin();		
		usuario.setEmail(email + " invalido");
		given()
			.body(usuario)
		.when()
			.post(APP_BASE_URL_USUARIOS)
		.then()
			.statusCode(400)
			.body("email", is("email deve ser um email válido"))
		;
	}
	
	@Test
	public void t03_testCadastrarUsuarioComEmailJaCadastrado() {
		Map<String, String> user = new HashMap<String, String>();
		user.put("nome", "Teste");
		user.put("email", email);
		user.put("password", "teste");
		user.put("administrador", "true");
		given()
			.body(user)
		.when()
			.post(APP_BASE_URL_USUARIOS)
		.then()
			.statusCode(400)
			.body("message", is("Este email já está sendo usado"))
		;
	}
}
