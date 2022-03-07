package tests.Refact;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import Factory.UserDataFactory;
import Pojo.UsersPojo;
import Utils.BaseTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsersRefact extends BaseTest{
	
	private static Integer qtde;
	private static String ID_Usuario;
	private static String nome;
	private static String email;
	private static String password;
	private static String administrador;
	
	@Test
	public void t01_testCadastrarNovoUsuario() {
		UsersPojo usuario = new UserDataFactory().userAdmin();		
		ID_Usuario =
		given()	
			.body(usuario)
		.when()
			.post("/usuarios")
		.then()
			.statusCode(201)
			.body("message", is("Cadastro realizado com sucesso"))
			.body("_id", is(notNullValue()))
			.extract().path("_id")
		;
		
		nome = usuario.getNome();
		email = usuario.getEmail();
		password = usuario.getPassword();
		administrador = usuario.getAdministrador();
	}
	
	@Test
	public void t02_testCadastrarUsuarioComEmailInvalido() {
		UsersPojo usuario = new UserDataFactory().userAdmin();		
		usuario.setEmail(email + " invalido");
		given()
			.body(usuario)
		.when()
			.post("/usuarios")
		.then()
			.statusCode(400)
			.body("email", is("email deve ser um email válido"))
		;
	}
	
	@Test
	public void t03_testCadastrarUsuarioComEmailJaCadastrado() {
		Map<String, String> usuario = new HashMap<String, String>();
		usuario.put("nome", nome);
		usuario.put("email", email);
		usuario.put("password", password);
		usuario.put("administrador", administrador);
		given()
			.body(usuario)
		.when()
			.post("/usuarios")
		.then()
			.statusCode(400)
			.body("message", is("Este email já está sendo usado"))
		;
	}
	
	@Test
	public void t04_testListarTodosUsuarios() {
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
	public void t05_testListarUsuariosCadastradosPorID() {
		nome = given()
			.pathParam("_id", ID_Usuario)
		.when()
			.get("/usuarios/{_id}")
		.then()
			.statusCode(200)
			.body("password", equalTo("teste"))
			.body("administrador", equalTo("true"))
			.extract().path("nome")
		;		
	}
		
	@Test
	public void t06_testListarUsuariosPorIDInexistente() {
		given()
			.pathParam("_id", ID_Usuario + "021")
		.when()
			.get("/usuarios/{_id}")
		.then()
			.statusCode(400)
			.body("message", equalTo("Usuário não encontrado"))
		;
	}
	
	@Test
	public void t07_testListarUsuarioPorEmail() {
		given()
			.queryParam("email", email)
		.when()
			.get("/usuarios")
		.then()
			.statusCode(200)
			.body("quantidade", is(1))
			.body("usuarios.email", hasItem(email))
			.body("usuarios._id", hasItem(ID_Usuario))
		;
	}
	
	@Test
	public void t08_testListarUsuarioPorNome() {
		given()
			.queryParam("nome", nome)
		.when()
			.get("/usuarios")
		.then()
			.statusCode(200)
			.body("usuarios.nome", hasItems(nome))
		;
	}

	@Test
	public void t09_testListarUsuarioPorNomeInexistente() {
		given()
			.queryParam("nome", nome + " Inexistente")
		.when()
			.get("/usuarios")
		.then()
			.statusCode(200)
			.body("quantidade", is(0))
		;
	}
	
	@Test
	public void t10_testListarUsuarioPorPassword() {
		given()
			.queryParam("password", password)
		.when()
			.get("/usuarios")
		.then()
			.statusCode(200)
			.body("usuarios.password", hasItems(password))
		;
	}
	
	@Test
	public void t11_testListarUsuarioPorAdministrador() {
		given()
			.queryParam("administrador", administrador)
		.when()
			.get("/usuarios")
		.then()
			.statusCode(200)
		;
	}

	@Test
	public void t12_testListarUsuarioPorNomeEmailPasswordEAdministrador() {
		Map<String, String> usuario = new HashMap<String, String>();
		usuario.put("nome", nome);
		usuario.put("email", email);
		usuario.put("password", password);
		usuario.put("administrador", administrador);
		given()
			.queryParams(usuario)
		.when()
			.get("/usuarios")
		.then()
			.statusCode(200)
			.body("usuarios.nome", hasItem(nome))
			.body("usuarios.email", hasItem(email))
			.body("usuarios.password", hasItem(password))
			.body("usuarios.administrador", hasItem(administrador))
		;	
	}
	
}
