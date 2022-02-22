package tests.Carrinhos;

import org.junit.Test;

import POJO.LoginPojo;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

public class Carrinhos {
	
	private static String token;
	
	@Test
	public void testListarTodosCarrinhosCadastrados() {
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
		.when()
			.get("/carrinhos")
		.then()
			.statusCode(200)
			.contentType(ContentType.JSON)
			.body("quantidade", is(1))
			.body("carrinhos.produtos.idProduto[0]", hasItem("BeeJh5lz3k6kSIzA"))
			.body("carrinhos.produtos.idProduto[0]", hasItems("BeeJh5lz3k6kSIzA","K6leHdftCeOJj8BJ"))
			.body("carrinhos.precoTotal", hasItems(6180))
		;
	}
	@Test
	public void testListarTodosCarrinhosCadastradosPorID() {
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.pathParam("_id", "qbMqntef4iTOwWfg")
		.when()
			.get("/carrinhos/{_id}")
		.then()
			.statusCode(200)
			.contentType(ContentType.JSON)
			.body("produtos.idProduto", hasItems("BeeJh5lz3k6kSIzA","K6leHdftCeOJj8BJ"))
			.body("precoTotal", is(6180))
			.body("idUsuario", is("oUb7aGkMtSEPf6BZ"))
			.body("_id", is("qbMqntef4iTOwWfg"))
		;
	}
	@Test
	public void testListarTodosCarrinhosCadastradosPorIDInexistente() {
		given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.pathParam("_id", "qbMqntef4iTOwW00")
		.when()
			.get("/carrinhos/{_id}")
		.then()
			.statusCode(400)
			.contentType(ContentType.JSON)
			.body("message", is("Carrinho não encontrado"))
		;
	}
	@Test
	public void testCadastrarCarrinhos() {
		baseURI = "https://serverest.dev";			
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");	
		
		LoginPojo usuario = new LoginPojo();
		usuario.setEmail("fulanoqa@teste.com");
		usuario.setPassword("teste");
		
		token = given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.contentType(ContentType.JSON)
			.body(usuario)
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
			.body("{\"produtos\":[{\"idProduto\": \"rO8XJRDqTLYFazlo\", \"quantidade\": 8}, {\"idProduto\": \"nijRPSLXiKl8Lr76\", \"quantidade\": 4}]}\r\n")
		.when()
			.post("/carrinhos")
		.then()
			.statusCode(201)
			.body("message", is("Cadastro realizado com sucesso"))
			.body("_id", is(notNullValue()))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testCadastrarMaisDe1Carrinho() {
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
			.body("{\"produtos\":[{\"idProduto\": \"rO8XJRDqTLYFazlo\", \"quantidade\": 8}, {\"idProduto\": \"nijRPSLXiKl8Lr76\", \"quantidade\": 4}]}\r\n")
		.when()
			.post("/carrinhos")
		.then()
			.statusCode(400)
			.body("message", is("Não é permitido ter mais de 1 carrinho"))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testCadastrarCarrinhoDuplicado() {
		baseURI = "https://serverest.dev";			
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");	
		
		token = given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.contentType(ContentType.JSON)
			.body("	{\"email\": \"fulanolima14@qa.com.br\",\"password\": \"teste\"}\r\n")
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
			.body("{\"produtos\":[{\"idProduto\": \"rO8XJRDqTLYFazlo\", \"quantidade\": 8}, {\"idProduto\": \"rO8XJRDqTLYFazlo\", \"quantidade\": 4}]}\r\n")
		.when()
			.post("/carrinhos")
		.then()
			.statusCode(400)
			.body("message", is("Não é permitido possuir produto duplicado"))
			.body("idProdutosDuplicados", hasItem("rO8XJRDqTLYFazlo"))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testCadastrarCarrinhoProdutoNaoEncontrado() {
		baseURI = "https://serverest.dev";			
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");	
		
		token = given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.contentType(ContentType.JSON)
			.body("	{\"email\": \"fulanoli@qa.com.br\",\"password\": \"teste\"}\r\n")
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
			.body("{\"produtos\":[{\"idProduto\": \"nijRPSLXiKl8Lr\", \"quantidade\": 597}, {\"idProduto\": \"lvKyRnk1OsTK2\", \"quantidade\": 51}]}\r\n")
		.when()
			.post("/carrinhos")
		.then()
			.statusCode(400)
			.body("message", is("Produto não encontrado"))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}
	
	@Test
	public void testCadastrarCarrinhoProdutoQuantidadeInsuficiente() {
		baseURI = "https://serverest.dev";			
		System.out.println("===============================INÍCIO DOS REQUESTS==================================");	
		
		token = given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
			.contentType(ContentType.JSON)
			.body("	{\"email\": \"fulanoli@qa.com.br\",\"password\": \"teste\"}\r\n")
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
			.body("{\"produtos\":[{\"idProduto\": \"nijRPSLXiKl8Lr76\", \"quantidade\": 597}, {\"idProduto\": \"lvKyRnk1OsTK2gu5\", \"quantidade\": 51}]}\r\n")
		.when()
			.post("/carrinhos")
		.then()
			.statusCode(400)
			.body("message", is("Produto não possui quantidade suficiente"))
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
			.body("{\"produtos\":[{\"idProduto\": \"rO8XJRDqTLYFazlo\", \"quantidade\": 8}, {\"idProduto\": \"nijRPSLXiKl8Lr76\", \"quantidade\": 4}]}\r\n")
		.when()
			.post("/carrinhos")
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
			.body("{\"produtos\":[{\"idProduto\": \"rO8XJRDqTLYFazlo\", \"quantidade\": 8}, {\"idProduto\": \"nijRPSLXiKl8Lr76\", \"quantidade\": 4}]}\r\n")
		.when()
			.post("/carrinhos")
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
			.body("{\"produtos\":[{\"idProduto\": \"rO8XJRDqTLYFazlo\", \"quantidade\": 8}, {\"idProduto\": \"nijRPSLXiKl8Lr76\", \"quantidade\": 4}]}\r\n")
		.when()
			.post("/carrinhos")
		.then()
			.statusCode(401)
			.body("message", is("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"))
		;
		System.out.println("===============================FIM DOS REQUESTS==================================");
	}	
}
