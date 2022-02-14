package modulos.Produtos;

import org.junit.Test;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;


public class Produtos {
	
	private static String token;
	//private static String nome_produto = "Produto" + System.nanoTime();
	
	@Test
	public void testListarTodosProdutosCadastrados() {
		baseURI = "https://serverest.dev";			
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
		.when()
			.get("/produtos")
		.then()
			.statusCode(200)
		;		
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testListarProdutosPorID() {
		baseURI = "https://serverest.dev";			
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");		
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.pathParam("_id", "0xwCHqyqZTFVniyW")
		.when()
			.get("/produtos/{_id}")
		.then()
			.statusCode(200)
			.body("_id", is("0xwCHqyqZTFVniyW"))
		;		
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testListarProdutosPorIDInexistente(){
		baseURI = "https://serverest.dev";			
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");		
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.pathParam("_id", "0xwCHqyqZTFVni85")
		.when()
			.get("/produtos/{_id}")
		.then()
			.statusCode(400)
			.body("message", is("Produto não encontrado"))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testListarProdutosPorNome(){
		baseURI = "https://serverest.dev";			
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");		
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.queryParam("nome", "ogitech MX Vertical42")
		.when()
			.get("/produtos")
		.then()
			.statusCode(200)
			.body("produtos.nome", hasItem("Logitech MX Vertical42"))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testListarProdutosPorPreco(){
		baseURI = "https://serverest.dev";			
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");		
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.queryParam("preco", 470)
		.when()
			.get("/produtos")
		.then()
			.statusCode(200)
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testListarProdutosPorDescricao(){
		baseURI = "https://serverest.dev";			
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");		
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.queryParam("descricao", "Mouse")
		.when()
			.get("/produtos")
		.then()
			.statusCode(200)
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testListarProdutosPorQuantidade(){
		baseURI = "https://serverest.dev";			
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");		
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.queryParam("quantidade", 381)
		.when()
			.get("/produtos")
		.then()
			.statusCode(200)
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testListarProdutosPorIDNomePrecoDescricaoQuantidade(){
		baseURI = "https://serverest.dev";			
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");		
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.queryParam("_id", "0R6gKrRblxTmvP8o")
			.queryParam("nome", "Hacksaw Ridge")
			.queryParam("preco", 470)
			.queryParam("descricao", "mouse")
			.queryParam("quantidade", 381)
		.when()
			.get("/produtos")
		.then()
			.statusCode(200)
			.body("produtos.nome", hasItem("Hacksaw Ridge"))
			.body("produtos.preco", hasItem(470))
			.body("produtos.descricao", hasItem("mouse"))
			.body("produtos.quantidade", hasItem(381))
			.body("produtos._id", hasItem("0R6gKrRblxTmvP8o"))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	@Test
	public void testCadastrarProdutos() {
		baseURI = "https://serverest.dev";			
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");	
		
		token = given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.contentType(ContentType.JSON)
			.body("	{\"email\": \"fulano@qa.com\",\"password\": \"teste\"}\r\n")
		.when()
			.post("/login")
		.then()
			.extract()
			.path("authorization")
		;		
		System.out.println("======================FIM DOS REQUESTS======================");
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.header("authorization", token)
			.contentType(ContentType.JSON)
			.body("{\"nome\": \"Monitor Dell 8999\", \"preco\": 1500, \"descricao\": \"Monitor\", \"quantidade\": 600}")
		.when()
			.post("/produtos")
		.then()
			.statusCode(201)
			.body("message", is("Cadastro realizado com sucesso"))
			.body("_id", is(notNullValue()))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testCadastrarProdutosComMesmoNome() {
		baseURI = "https://serverest.dev";			
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");		
		token = given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.contentType(ContentType.JSON)
			.body("	{\"email\": \"fulano@qa.com\",\"password\": \"teste\"}\r\n")
		.when()
			.post("/login")
		.then()
			.extract()
			.path("authorization")
		;		
		System.out.println("======================FIM DOS REQUESTS======================");
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");	
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.header("authorization", token)
			.contentType(ContentType.JSON)
			.body("{\"nome\": \"Monitor Dell\", \"preco\": 1500, \"descricao\": \"Monitor\", \"quantidade\": 600}")
		.when()
			.post("/produtos")
		.then()
			.statusCode(400)
			.body("message", is("Já existe produto com esse nome"))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testTokenExpirado() {
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");		
		baseURI = "https://serverest.dev";			
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.header("authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImZ1bGFub0BxYS5jb20iLCJwYXNzd29yZCI6InRlc3RlIiwiaWF0IjoxNjQ0ODY2MzM1LCJleHAiOjE2NDQ4NjY5MzV9.TBfAc8WC7GkuQpVXrKTBXyjOMSvqzh08MLlDk8RBwdM")
			.contentType(ContentType.JSON)
			.body("{\"nome\": \"Monitor Dell 5800\", \"preco\": 1500, \"descricao\": \"Monitor\", \"quantidade\": 600}")
		.when()
			.post("/produtos")
		.then()
			.statusCode(401)
			.body("message", is("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testTokenInvalido() {
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");		
		baseURI = "https://serverest.dev";			
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.header("authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImZ1bGFub0BxYS5jb20iLCJwYXNzd29yZCI6InRlc3RlIiwiaWF0IjoxNjQ0ODY3MTk5LCJleHAiOjE2NDQ4Njc3OTl9.3w_bxgS7uqVsxoxaxsfYvJaAldrbyyuuJ1Mq92yI555")
			.contentType(ContentType.JSON)
			.body("{\"nome\": \"Monitor Dell 5808\", \"preco\": 1500, \"descricao\": \"Monitor\", \"quantidade\": 600}")
		.when()
			.post("/produtos")
		.then()
			.statusCode(401)
			.body("message", is("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testAusenciaToken() {
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");		
		baseURI = "https://serverest.dev";			
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.contentType(ContentType.JSON)
			.body("{\"nome\": \"Monitor Dell 5878\", \"preco\": 1500, \"descricao\": \"Monitor\", \"quantidade\": 600}")
		.when()
			.post("/produtos")
		.then()
			.statusCode(401)
			.body("message", is("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}	
	
	@Test
	public void testRotaExclusivaAdministrador() {
		baseURI = "https://serverest.dev";			
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");		
		token = given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.contentType(ContentType.JSON)
			.body("	{\"email\": \"fulanolima1458@qa.com.br\",\"password\": \"teste\"}\r\n")
		.when()
			.post("/login")
		.then()
			.extract()
			.path("authorization")
		;		
		System.out.println("======================FIM DOS REQUESTS======================");
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");	
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.header("authorization", token)
			.contentType(ContentType.JSON)
			.body("{\"nome\": \"Monitor Dell\", \"preco\": 1700, \"descricao\": \"Monitor\", \"quantidade\": 600}")
		.when()
			.post("/produtos")
		.then()
			.statusCode(403)
			.body("message", is("Rota exclusiva para administradores"))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
}
