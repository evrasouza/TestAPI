package tests.Refact;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;


import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import Factory.LoginDataFactory;
import Factory.ProductDataFactory;
import Pojo.ProductsPojo;
import Utils.BaseTest;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductsRefact extends BaseTest{	
	
	private static String ID_Produto;
	private static String nome;
	private static Integer preco;
	private static String descricao;
	private static Integer qtde;
	
	@BeforeClass	
	public static void login(){	
		LoginDataFactory login = new LoginDataFactory();
		login.loginAdmin();
	}
		
	@Test
	public void t01_testCadastrarProdutos() {
		ProductsPojo produto = new ProductDataFactory().getNewProduct();
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
		nome = produto.getNome();
		preco = produto.getPreco();
		descricao = produto.getDescricao();
		qtde = produto.getQuantidade();
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
		given()
			.pathParam("_id", ID_Produto)
		.when()
			.get("/produtos/{_id}")
		.then()
			.statusCode(200)
			.body("_id", is(ID_Produto))
		;		
	}
	
	@Test
	public void t04_testCadastrarProdutosComMesmoNome() {
		Map<String, String> produto = new HashMap<String, String>();
		produto.put("nome", nome);
		produto.put("preco", String.valueOf(preco));
		produto.put("descricao", descricao);
		produto.put("quantidade", String.valueOf(qtde));
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
			.queryParam("nome", nome)
		.when()
			.get("/produtos")
		.then()
			.statusCode(200)
			.body("produtos.nome", hasItem(nome))
		;
	}

	@Test
	public void t07_testListarProdutosPorPreco(){
		given()
			.queryParam("preco", preco)
		.when()
			.get("/produtos")
		.then()
			.statusCode(200)
		;
	}

	@Test
	public void t08_testListarProdutosPorDescricao(){
		given()
			.queryParam("descricao", descricao)
		.when()
			.get("/produtos")
		.then()
			.statusCode(200)
		;
	}

	@Test
	public void t09_testListarProdutosPorQuantidade(){
		given()
			.queryParam("quantidade", qtde)
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
		produto.put("nome", nome);
		produto.put("preco", String.valueOf(preco));
		produto.put("descricao", descricao);
		produto.put("quantidade", String.valueOf(qtde));
		given()
			.queryParams(produto)
		.when()
			.get("/produtos")
		.then()
			.statusCode(200)
			.body("produtos.nome", hasItem(nome))
			.body("produtos.preco", hasItem(preco))
			.body("produtos.descricao", hasItem(descricao))
			.body("produtos.quantidade", hasItem(qtde))
			.body("produtos._id", hasItem(ID_Produto))
		;
	}

	@Test
	public void t11_testTokenExpirado() {
		ProductsPojo produto = new ProductDataFactory().getNewProduct();
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
		ProductsPojo produto = new ProductDataFactory().getNewProduct();
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
	public void t14_testAusenciaToken() {
	    FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
		req.removeHeader("authorization");
		ProductsPojo produto = new ProductDataFactory().getNewProduct();
		given()
			.body(produto)
		.when()
			.post("/produtos")
		.then()
			.statusCode(401)
			.body("message", is("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"))
		;
	}	

	/*@Test
	public void t13_testRotaExclusivaAdministrador() {	
		LoginDataFactory login = new LoginDataFactory();
		login.loginUser();
				
		ProductsPojo produto = new ProductDataFactory().getNewProduct();		
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.body(produto)
		.when()
			.post("/produtos")
		.then()
			.statusCode(403)
			.body("message", is("Rota exclusiva para administradores"))
		;		
	}
	*/

}

