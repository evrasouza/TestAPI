package modulos.Produtos;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

public class Produtos {
	
	@BeforeClass
	public static void setup() {
		baseURI = "https://serverest.dev";
		basePath = "/produtos";
	}
	
	@Test
	public void testListarTodosProdutos() {
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
		.when()
			.get()
		.then()
			.statusCode(200)
		;		
	}
	
	@Test
	public void testListarProdutosPorID() {
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.pathParam("_id", "0xwCHqyqZTFVniyW")
		.when()
			.get("{_id}")
		.then()
			.statusCode(200)
			.body("_id", is("0xwCHqyqZTFVniyW"))
		;		
	}
	
	@Test
	public void testListarProdutosPorIDInexistente(){
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.pathParam("_id", "0xwCHqyqZTFVni85")
		.when()
			.get("{_id}")
		.then()
			.statusCode(400)
			.body("message", is("Produto não encontrado"))
		;
	}
	
	@Test
	public void testListarProdutosPorNome(){
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.queryParam("nome", "ogitech MX Vertical42")
		.when()
			.get()
		.then()
			.statusCode(200)
			.body("produtos.nome", hasItem("Logitech MX Vertical42"))
		;
	}
	
	@Test
	public void testListarProdutosPorPreco(){
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.queryParam("preco", 470)
		.when()
			.get()
		.then()
			.statusCode(200)
		;
	}
	
	@Test
	public void testListarProdutosPorDescricao(){
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.queryParam("descricao", "Mouse")
		.when()
			.get()
		.then()
			.statusCode(200)
		;
	}
	
	@Test
	public void testListarProdutosPorQuantidade(){
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.queryParam("quantidade", 381)
		.when()
			.get()
		.then()
			.statusCode(200)
		;
	}
	
	@Test
	public void testListarProdutosPorIDNomePrecoDescricaoQuantidade(){
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.queryParam("_id", "0R6gKrRblxTmvP8o")
			.queryParam("nome", "Hacksaw Ridge")
			.queryParam("preco", 470)
			.queryParam("descricao", "mouse")
			.queryParam("quantidade", 381)
		.when()
			.get()
		.then()
			.statusCode(200)
			.body("produtos.nome", hasItem("Hacksaw Ridge"))
			.body("produtos.preco", hasItem(470))
			.body("produtos.descricao", hasItem("mouse"))
			.body("produtos.quantidade", hasItem(381))
			.body("produtos._id", hasItem("0R6gKrRblxTmvP8o"))
		;
	}


}
