package tests.Users;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.junit.Before;
import org.junit.Test;

import Core.BaseTest;
import Utils.UserUtil;

public class ListUserTest extends BaseTest{
	
	public String id;
	public int amount;
	public String name;
	
	@Before
    public void createUser() {
        id = new UserUtil().createUserId();
    }
	
	@Test
	public void testListUsersAllParams() {

		given()
            .queryParam("_id", id)
            .queryParam("password", "teste")
            .queryParam("administrador", "true")
		.when()
			.get(APP_BASE_URL_USUARIOS)
		.then()
			.statusCode(200)
				.body("usuarios.password[0]", equalTo("teste"))
				.body("usuarios.administrador[0]", equalTo("true"))
                .body("usuarios._id[0]", equalTo(id));
	}
	
	@Test
	public void testListUserWithoutParams() {

		given()
		.when()
			.get(APP_BASE_URL_USUARIOS)
		.then()
			.statusCode(200)
	            .body("usuarios.nome[0]", notNullValue())
	            .body("usuarios.nome[1]", notNullValue());
	}
	
    @Test
    public void testListUserTypeParamsIncorrect(){
        given()
        	.queryParam("_id", 1.0f)
            .queryParam("nome", 1)
            .queryParam("email", 1)
            .queryParam("password", 1)
            .queryParam("administrador", 1)
        .when()
        	.get(APP_BASE_URL_USUARIOS)
        .then()
        	.statusCode(400)
        		.body("email", equalTo("email deve ser uma string"))
                .body("administrador", equalTo("administrador deve ser 'true' ou 'false'"));
    }
    
    @Test
    public void testListUserParamsIncorrect(){
        given()
        	.queryParam("id", 1.0f)
        .when()
        	.get(APP_BASE_URL_USUARIOS)
        .then()
        	.statusCode(400)
        		.body("id", equalTo("id não é permitido"));
    }

    @Test
    public void testListUserForId(){
        given()
        .when()
        	.get(APP_BASE_URL_USUARIOS.concat(id))
        .then()
        	.statusCode(200)
        		.body("_id", equalTo(id));
    }

    @Test
    public void testListUserIdIncorrect(){
        given()
        .when()
        	.get(APP_BASE_URL_USUARIOS.concat("id"))
        .then()
        	.statusCode(400)
        		.body("message", equalTo("Usuário não encontrado"));
    }
    
	@Test
	public void testAmountUsers() {
		amount = new UserUtil().amountUsers();
		
		 given()
		 .when()
		 	.get(APP_BASE_URL_USUARIOS)
		 .then()
		 	.body("quantidade", is(amount))
		 ;

	}
	
    @Test
    public void testListofUsersByIDNonexistent(){
        given()
        .when()
        	.get(APP_BASE_URL_USUARIOS.concat("bb"))
        .then()
        	.statusCode(400)
        		.body("message", equalTo("Usuário não encontrado"));
    }
    
    @Test
    public void testListofUsersByName(){
    	name = new UserUtil().nameUser();
    	
    	given()
        	.queryParam("nome", name)
        .when()
        	.get(APP_BASE_URL_USUARIOS)
        .then()
	        .statusCode(200)
				.body("usuarios.nome", hasItems(name));
    }
    
    @Test
    public void testListofUsersByNameNonexistent(){
    	name = new UserUtil().nameUser();
    	
    	given()
        	.queryParam("nome", name + "Silva")
        .when()
        	.get(APP_BASE_URL_USUARIOS)
        .then()
	        .statusCode(200)
	        	.body("quantidade", is(0));
    }
    
    @Test
    public void testListofUsersByTypeAdm(){
    	name = new UserUtil().nameUser();
    	
    	given()
    	.queryParam("administrador", "true")
        .when()
        	.get(APP_BASE_URL_USUARIOS)
        .then()
	        .statusCode(200);
    }
    
    @Test
    public void testListofUsersByNotAdm(){
    	name = new UserUtil().nameUser();
    	
    	given()
    	.queryParam("administrador", "false")
        .when()
        	.get(APP_BASE_URL_USUARIOS)
        .then()
	        .statusCode(200);
    }
	
}
