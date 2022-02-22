package tests.Users;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

import Core.BaseTest;
import Factory.UserDataFactory;
import POJO.UsersPojo;
import Utils.UserUtil;

public class ListUsers extends BaseTest{
	
	private static Integer qtde;	
	public String id;
	public UsersPojo user;
	public String email;
	public String name;
	public String password;
	public String administrator;	

	
	@Before
    public void createUser() {
        id = new UserUtil().createUserId();
        user = new UserDataFactory().userAdmin();
        email = new UserUtil().createEmail();
        name = new UserUtil().createName();
        password = new UserUtil().createPassword();
        administrator = new UserUtil().createAdministrator();
    }
	
	@Test
	public void listAllUsers() {
		qtde = given()
		.when()
			.get("/usuarios")
		.then()
			.statusCode(200)
			.extract().path("quantidade")
		;	
		 given()
		 .when()
		 	.get("/usuarios")
		 .then()
		 	.body("quantidade", is(qtde))
		 ;
	}
	
	@Test
	public void listUsersByID() {
		 given()
		.when()
			.get(APP_BASE_URL_USUARIOS.concat(id))
		.then()
			.statusCode(200)
			.body("password", equalTo("teste"))
			.body("administrador", equalTo("true"))
		;		
	}
		
	@Test
	public void listUsersByIncorrectID() {
		given()
		.when()
			.get(APP_BASE_URL_USUARIOS.concat(id + "GYDFSBF"))
		.then()
			.statusCode(400)
			.body("message", equalTo("Usuário não encontrado"))
		;
	}
	
	@Test
	public void listUsersByEmail() {
		given()
			.queryParam("email", email)
		.when()
			.get(APP_BASE_URL_USUARIOS)
		.then()
			.statusCode(200)
			.body("quantidade", is(1))
			.body("usuarios.email", hasItem(email))
		;
	}
	
	@Test
	public void listUsersByName() {
		given()
			.queryParam("nome", name)
		.when()
			.get(APP_BASE_URL_USUARIOS)
		.then()
			.statusCode(200)
			.body("usuarios.nome", hasItems(name))
		;
	}

	@Test
	public void listUsersByIncorrectName() {
		given()
			.queryParam("nome", name + " Inexistente")
		.when()
			.get(APP_BASE_URL_USUARIOS)
		.then()
			.statusCode(200)
			.body("quantidade", is(0))
		;
	}
	
	@Test
	public void listUsersByPassword() {
		given()
			.queryParam("password", password)
		.when()
			.get(APP_BASE_URL_USUARIOS)
		.then()
			.statusCode(200)
			.body("usuarios.password", hasItems(password))
		;
	}
	
	@Test
	public void listUsersByAdministrator() {
		given()
			.queryParam("administrador", administrator)
		.when()
			.get(APP_BASE_URL_USUARIOS)
		.then()
			.statusCode(200)
		;
	}
	
}
