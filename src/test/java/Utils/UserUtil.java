package Utils;

import static io.restassured.RestAssured.given;

import Core.BaseTest;
import Factory.UserDataFactory;
import POJO.UsersPojo;

public class UserUtil extends BaseTest{
	
	public String id;
	public String email;
	public String name;
	public String password;
	public String administrator;
	
	public String createUserId(){
    	UsersPojo user = new UserDataFactory().userAdmin();	
        id = given()
                .body(user)
            .when()
                .post("/usuarios")
            .then()
                .extract().path("_id");
        return id;
    }    
	
	public String createEmail() {
		createUserId();
		email = given()
				.when()
					.get(APP_BASE_URL_USUARIOS.concat(id))
				.then()
					.statusCode(200)
					.extract().path("email");
		return email;
	}
	
	public String createName() {
		createUserId();
		name = given()
				.when()
					.get(APP_BASE_URL_USUARIOS.concat(id))
				.then()
					.statusCode(200)
					.extract().path("nome");
		return name;
	}
	
	public String createPassword() {
		createUserId();
		password = given()
				.when()
					.get(APP_BASE_URL_USUARIOS.concat(id))
				.then()
					.statusCode(200)
					.extract().path("password");
		return password;
	}
	
	public String createAdministrator() {
		createUserId();
		administrator = given()
				.when()
					.get(APP_BASE_URL_USUARIOS.concat(id))
				.then()
					.statusCode(200)
					.extract().path("administrador");
		return administrator;
	}
}
