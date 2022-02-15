package modulos.Usuarios;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import Core.BaseTest;
import POJO.UsuarioPojo;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Usuarios extends BaseTest{
	
	private static Integer qtde;
	private static String email = "email" + System.nanoTime() + "@teste.com";
	private static String nome = "Natalia Teste";
	private static String password = "Teste";
	private static String administrador = "true";
	private static String ID_Usuario;
	
	@Test
	public void t01_testCadastrarNovoUsuario() {
		UsuarioPojo usuario = getCadastroUsuarioValido();
		ID_Usuario =
		given()	
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())	
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
	public void t02_testAlterarEmailUsuario() {
		UsuarioPojo usuario = getCadastroUsuarioValido();
		usuario.setEmail("alterado" + email);
		given()
			.pathParam("_id", ID_Usuario)
			.body(usuario)
		.when()
			.put("/usuarios/{_id}")
		.then()
			.statusCode(200)
			.body("message", is("Registro alterado com sucesso"))
		;
	}	
	
	@Test
	public void t03_testCadastrarUsuarioComEmailJaCadastrado() {
		UsuarioPojo usuario = getCadastroUsuarioValido();
		usuario.setEmail("alterado" + email);
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
	public void t04_testCadastrarUsuarioComEmailInvalido() {
		UsuarioPojo usuario = getCadastroUsuarioValido();
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
	public void t05_testListarTodosUsuarios() {
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
	public void t06_testListarUsuariosCadastradosPorID() {
		given()
			.pathParam("_id", ID_Usuario)
		.when()
			.get("/usuarios/{_id}")
		.then()
			.statusCode(200)
			.body("password", equalTo("Teste"))
			.body("administrador", equalTo("true"))
		;		
	}
		
	@Test
	public void t07_testListarUsuariosPorIDInexistente() {
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
	public void t10_testListarUsuarioPorEmail() {
		given()
			.queryParam("email", "alterado" + email)
		.when()
			.get("/usuarios")
		.then()
			.statusCode(200)
			.body("quantidade", is(1))
			.body("usuarios.email", hasItem("alterado" + email))
			.body("usuarios._id", hasItem(ID_Usuario))
		;
	}
	
	@Test
	public void t11_testListarUsuarioPorPassword() {
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
	public void t12_testListarUsuarioPorAdministrador() {
		given()
			.queryParam("administrador", administrador)
		.when()
			.get("/usuarios")
		.then()
			.statusCode(200)
		;
	}

	@Test
	public void t13_testListarUsuarioPorNomeEmailPasswordEAdministrador() {
		Map<String, String> usuario = new HashMap<String, String>();
		usuario.put("nome", nome);
		usuario.put("email", "alterado" + email);
		usuario.put("password", password);
		usuario.put("administrador", administrador);
		given()
			.queryParams(usuario)
		.when()
			.get("/usuarios")
		.then()
			.statusCode(200)
			.body("usuarios.nome", hasItem(nome))
			.body("usuarios.email", hasItem("alterado" + email))
			.body("usuarios.password", hasItem(password))
			.body("usuarios.administrador", hasItem(administrador))
		;	
	}

	private UsuarioPojo getCadastroUsuarioValido(){
		UsuarioPojo usuario = new UsuarioPojo();
		usuario.setNome(nome);
		usuario.setEmail(email);
		usuario.setPassword(password);
		usuario.setAdministrador(administrador);
		return usuario;
	}

}
