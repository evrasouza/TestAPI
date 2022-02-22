package tests.Produtos;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import Core.BaseTest;
import Factory.UserDataFactory;
import POJO.LoginPojo;
import POJO.ProductsPojo;
import POJO.UsersPojo;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Produtos extends BaseTest {
	
	private static String email = "email" + System.nanoTime() + "@teste.com";
	private static String nomeProduto = "produto " + System.nanoTime() + " Dell";
	private static String nomeUsuario = "Natalia Teste";
	private static String password = "Teste";
	private static String administrador = "true";
	private static String ID_Produto;
	private static String nomeProduto2;

	@BeforeClass
	public static void login() {
		UsersPojo usuario = getCadastroUsuarioValido();
		given()	
			.body(usuario)
		.when()
			.post("/usuarios")
		.then()
			.statusCode(201)
			.body("message", is("Cadastro realizado com sucesso"))
			.body("_id", is(notNullValue()))
		;
		
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
			
	@Test
	public void t01_testCadastrarProdutos() {
		ProductsPojo produto = getCadastroProduto();
		ID_Produto =
		given()
			.body(produto)
		.when()
			.post("/produtos")
		.then()
			.statusCode(201)
			.body("message", is("Cadastro realizado com sucesso"))
			.body("_id", is(notNullValue()))
			.extract().path("_id")
		;
	}
	
	@Test
	public void t02_testListarTodosProdutosCadastrados() {
		given()
		.when()
			.get("/produtos")
		.then()
			.statusCode(200)
		;		
	}	
	
	@Test
	public void t03_testListarProdutosPorID() {
		nomeProduto2 = 
		given()
			.pathParam("_id", ID_Produto)
		.when()
			.get("/produtos/{_id}")
		.then()
			.statusCode(200)
			.body("_id", is(ID_Produto))
			.extract().path("nome")
		;		
	}
	
	@Test
	public void t04_testCadastrarProdutosComMesmoNome() {
		ProductsPojo produto = getCadastroProduto();
		produto.setNome(nomeProduto2);
		given()
			.body(produto)
		.when()
			.post("/produtos")
		.then()
			.statusCode(400)
			.body("message", is("Já existe produto com esse nome"))
		;
	}		
	
	@Test
	public void t05_testListarProdutosPorIDInexistente(){
		given()
			.pathParam("_id", ID_Produto + " Inexistente")
		.when()
			.get("/produtos/{_id}")
		.then()
			.statusCode(400)
			.body("message", is("Produto não encontrado"))
		;
	}
	
	@Test
	public void t06_testListarProdutosPorNome(){
		given()
			.queryParam("nome", nomeProduto2)
		.when()
			.get("/produtos")
		.then()
			.statusCode(200)
			.body("produtos.nome", hasItem(nomeProduto2))
		;
	}
		
	@Test
	public void t07_testListarProdutosPorPreco(){
		given()
			.queryParam("preco", 470)
		.when()
			.get("/produtos")
		.then()
			.statusCode(200)
		;
	}
	
	@Test
	public void t08_testListarProdutosPorDescricao(){
		given()
			.queryParam("descricao", "Monitor")
		.when()
			.get("/produtos")
		.then()
			.statusCode(200)
		;
	}
	
	@Test
	public void t09_testListarProdutosPorQuantidade(){
		given()
			.queryParam("quantidade", 10)
		.when()
			.get("/produtos")
		.then()
			.statusCode(200)
		;
	}
	
	@Test
	public void t10_testListarProdutosPorIDNomePrecoDescricaoQuantidade(){
		Map<String, String> produto = new HashMap<String, String>();
		produto.put("_id", ID_Produto);
		produto.put("nome", nomeProduto2);
		produto.put("preco", String.valueOf(470));
		produto.put("descricao", "Monitor");
		produto.put("quantidade", String.valueOf(10));
		given()
			.queryParams(produto)
		.when()
			.get("/produtos")
		.then()
			.statusCode(200)
			.body("produtos.nome", hasItem(nomeProduto2))
			.body("produtos.preco", hasItem(470))
			.body("produtos.descricao", hasItem("Monitor"))
			.body("produtos.quantidade", hasItem(10))
			.body("produtos._id", hasItem(ID_Produto))
		;
	}
	
	@Test
	public void t11_testTokenExpirado() {
		ProductsPojo produto = getCadastroProduto();
		given()
			.header("authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImZ1bGFub0BxYS5jb20iLCJwYXNzd29yZCI6InRlc3RlIiwiaWF0IjoxNjQ0ODY2MzM1LCJleHAiOjE2NDQ4NjY5MzV9.TBfAc8WC7GkuQpVXrKTBXyjOMSvqzh08MLlDk8RBwdM")
			.body(produto)
		.when()
			.post("/produtos")
		.then()
			.statusCode(401)
			.body("message", is("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"))
		;
	}
	
	@Test
	public void t12_testTokenInvalido() {
		ProductsPojo produto = getCadastroProduto();
		given()
			.header("authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImZ1bGFub0BxYS5jb20iLCJwYXNzd29yZCI6InRlc3RlIiwiaWF0IjoxNjQ0ODY3MTk5LCJleHAiOjE2NDQ4Njc3OTl9.3w_bxgS7uqVsxoxaxsfYvJaAldrbyyuuJ1Mq92yI555")
			.body(produto)
		.when()
			.post("/produtos")
		.then()
			.statusCode(401)
			.body("message", is("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"))
		;
	}
	
	@Test
	public void t13_testRotaExclusivaAdministrador() {
		
		UsersPojo usuario = new UserDataFactory().userAdmin();	
		email = usuario.getEmail();
		password = usuario.getPassword();
		
		given()	
		.body(usuario)
	.when()
		.post("/usuarios")
	.then()
		.statusCode(201)
		.body("message", is("Cadastro realizado com sucesso"))
		.body("_id", is(notNullValue()))
	;
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

	
	
	@Test
	public void t14_testAusenciaToken() {
	    FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
		req.removeHeader("authorization");
		ProductsPojo produto = getCadastroProduto();
		given()
			.body(produto)
		.when()
			.post("/produtos")
		.then()
			.statusCode(401)
			.body("message", is("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"))
		;
	}	
		
	private static UsersPojo getCadastroUsuarioValido(){
		UsersPojo usuario = new UsersPojo();
		usuario.setNome(nomeUsuario);
		usuario.setEmail(email);
		usuario.setPassword(password);
		usuario.setAdministrador(administrador);
		return usuario;
	}
	
	private static ProductsPojo getCadastroProduto() {
		ProductsPojo produto = new ProductsPojo();
		produto.setNome(nomeProduto);
		produto.setPreco(470);
		produto.setDescricao("Monitor");
		produto.setQuantidade(10);
		return produto;
	}
}
