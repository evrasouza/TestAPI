package Utils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import Factory.UserDataFactory;
import Pojo.UsersPojo;

public class UserUtil extends BaseTest{
	
	public String id;
	public int amount;
	public String name;
	
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
    
    public int amountUsers(){
    	amount = given()
    			.when()
                	.get(APP_BASE_URL_USUARIOS)
                .then()
        			.statusCode(200)
        				.extract().path("quantidade");
        return amount;
    }
    
    public String nameUser(){
    	id = new UserUtil().createUserId();
    	
    	name = given()
    			.pathParam("_id", id)
    		.when()
    			.get("/usuarios/{_id}")
    		.then()
    			.statusCode(200)
	    			.body("password", equalTo("teste"))
	    			.body("administrador", equalTo("true"))
	    				.extract().path("nome");
    	System.out.println(name);
        return name;
    }
}
