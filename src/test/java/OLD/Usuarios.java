package OLD;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import Core.BaseTest;
import Factory.UserDataFactory;
import POJO.UsersPojo;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Usuarios extends BaseTest{
	
	private static Integer qtde;
	private static String ID_Usuario;
	private static String nome;
	
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
	}
	
	@Test
	public void t02_testCadastrarUsuarioComEmailInvalido() {
		UsersPojo usuario = new UserDataFactory().userAdmin();		
		usuario.setEmail("email_invalido.com");
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
	public void t03_testListarTodosUsuarios() {
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
	public void t04_testListarUsuariosCadastradosPorID() {
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
	public void t05_testListarUsuariosPorIDInexistente() {
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
	public void t06_testListarUsuarioPorNome() {
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
	public void t07_testListarUsuarioPorNomeInexistente() {
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
	public void t08_testListarUsuarioPorPassword() {
		given()
			.queryParam("password", "teste")
		.when()
			.get("/usuarios")
		.then()
			.statusCode(200)
			.body("usuarios.password", hasItems("teste"))
		;
	}
	
	@Test
	public void t09_testListarUsuarioPorAdministrador() {
		given()
			.queryParam("administrador", "true")
		.when()
			.get("/usuarios")
		.then()
			.statusCode(200)
		;
	}

	@Test
	public void t10_testListarUsuarioPorNomePasswordEAdministrador() {
		Map<String, String> usuario = new HashMap<String, String>();
		usuario.put("nome", nome);
		usuario.put("password", "teste");
		usuario.put("administrador", "true");
		given()
			.queryParams(usuario)
		.when()
			.get("/usuarios")
		.then()
			.statusCode(200)
			.body("usuarios.nome", hasItem(nome))
			.body("usuarios.password", hasItem("teste"))
			.body("usuarios.administrador", hasItem("true"))
		;	
	}

}
