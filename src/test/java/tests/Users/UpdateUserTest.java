package tests.Users;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.junit.Before;
import org.junit.Test;

import Core.BaseTest;
import Factory.UserDataFactory;
import POJO.UsersPojo;
import Utils.UserUtil;

public class UpdateUserTest extends BaseTest{
	public String id;
	public UsersPojo user;
	
	@Before
    public void createUser() {
        id = new UserUtil().createUserId();
        user = new UserDataFactory().userAdmin();
    }

    @Test
    public void testUpdateUserAdm(){
    	given()
        	.body(user)
        .when()
        	.put(APP_BASE_URL_USUARIOS.concat(id))
        .then()
        	.statusCode(200)
            	.body("message", equalTo("Registro alterado com sucesso"));
        
    }
    
    @Test
    public void testUpdateUser(){
        user.setAdministrador("false");
        given()
        	.body(user)
        .when()
        	.put(APP_BASE_URL_USUARIOS.concat(id))
        .then()
        	.statusCode(200)
            	.body("message", equalTo("Registro alterado com sucesso"));
    }

    @Test
    public void testUpdateUserIncorrectId(){
        user.setAdministrador("false");

        given()
        	.body(user)
        .when()
        	.put(APP_BASE_URL_USUARIOS.concat("id"))
        .then()
        	.statusCode(201)
        		.body("_id", notNullValue())
                .body("message", equalTo("Cadastro realizado com sucesso"));
    }

    @Test
    public void testUpdateUserWithoutTypeAdm(){
    	UsersPojo user = new UserDataFactory().userNotAdmin();
    	
        given()
        	.body(user)
        .when()
        	.put(APP_BASE_URL_USUARIOS.concat(id))
        .then()
        	.statusCode(400)
            	.body("administrador", equalTo("administrador deve ser 'true' ou 'false'"));
    }

    @Test
    public void testUpdateUserTypeAdmNull(){
        user.setAdministrador(null);

        given()
        	.body(user)
        .when()
        	.put(APP_BASE_URL_USUARIOS.concat(id))
        .then()
        	.statusCode(400)
        		.body("administrador", equalTo("administrador deve ser 'true' ou 'false'"));
    }

    @Test
    public void testUpdateUserTypeAdmIncorrect(){
        user.setAdministrador("admin");
        given()
        	.body(user)
        .when()
        	.put(APP_BASE_URL_USUARIOS.concat(id))
        .then()
        	.statusCode(400)
        		.body("administrador", equalTo("administrador deve ser 'true' ou 'false'"));
    }

    @Test
    public void testUpdateUserWithoutName(){
    	UsersPojo user = new UserDataFactory().userWithoutName();
    	
        given()
        	.body(user)
        .when()
        	.put(APP_BASE_URL_USUARIOS.concat(id))
        .then()
        	.statusCode(400)
        		.body("nome", equalTo("nome deve ser uma string"));
    }

    @Test
    public void testUpdateUserNameNull(){
        user.setNome(null);
        given()
        	.body(user)
        .when()
        	.put(APP_BASE_URL_USUARIOS.concat(id))
        .then()
        	.statusCode(400)
        		.body("nome", equalTo("nome deve ser uma string"));
    }

    @Test
    public void testUpdateUserNameEmpty(){
        user.setNome("");
        given()
        	.body(user)
        .when()
        	.put(APP_BASE_URL_USUARIOS.concat(id))
        .then()
        	.statusCode(400)
        		.body("nome", equalTo("nome não pode ficar em branco"));
    }

    @Test
    public void testUpdateUserWithoutEmail(){
    	UsersPojo user = new UserDataFactory().userWithoutEmail();
    	
        given()
        	.body(user)
        .when()
        	.put(APP_BASE_URL_USUARIOS.concat(id))
        .then()
        	.statusCode(400)
        		.body("email", equalTo("email deve ser uma string"));
    }

    @Test
    public void testUpdateUserEmailIncorrect(){
        user.setEmail("1");
        given()
        	.body(user)
        .when()
        	.put(APP_BASE_URL_USUARIOS.concat(id))
        .then()
        	.statusCode(400)
        		.body("email", equalTo("email deve ser um email válido"));
    }

    @Test
    public void testUpdateUserEmptyEmail(){
        user.setEmail("");
        given()
        	.body(user)
        .when()
        	.put(APP_BASE_URL_USUARIOS.concat(id))
        .then()
        	.statusCode(400)
        		.body("email", equalTo("email não pode ficar em branco"));
    }

    @Test
    public void testUpdateUserWithoutPassword(){
    	UsersPojo user = new UserDataFactory().userWithoutPassword();
    	
        given()
        	.body(user)
        .when()
        	.put(APP_BASE_URL_USUARIOS.concat(id))
        .then()
        	.statusCode(400)
        		.body("password", equalTo("password deve ser uma string"));
    }

    @Test
    public void testUpdateUserPasswordNull(){
        user.setPassword(null);
        given()
        	.body(user)
        .when()
        	.put(APP_BASE_URL_USUARIOS.concat(id))
        .then()
        	.statusCode(400)
        		.body("password", equalTo("password deve ser uma string"));
    }

    @Test
    public void testUpdateUserPasswordEmpty(){
        user.setPassword("");
        given()
        	.body(user)
        .when()
        	.put(APP_BASE_URL_USUARIOS.concat(id))
        .then()
        	.statusCode(400)
        		.body("password", equalTo("password não pode ficar em branco"));
    }


}
