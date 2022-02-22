package Utils;

import static io.restassured.RestAssured.given;

import Core.BaseTest;
import Factory.UserDataFactory;
import POJO.UsersPojo;

public class UserUtil extends BaseTest{
	
	public String id;
	
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

}
