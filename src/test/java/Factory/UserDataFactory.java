package Factory;

import com.github.javafaker.Faker;

import POJO.UsersPojo;

public class UserDataFactory {	
	
	Faker faker = new Faker();
	
	public String email = faker.internet().emailAddress();
	public String nome = faker.name().fullName();;
	public String password = "teste";
	public String administrador = "true";
	
	public UsersPojo userAdmin(){	
		UsersPojo usuario = new UsersPojo();
		usuario.setNome(nome);
		usuario.setEmail(email);
		usuario.setPassword(password);
		usuario.setAdministrador(administrador);
		return usuario;	
	}
	
	public UsersPojo user(){
		UsersPojo usuario = new UsersPojo();
		usuario.setNome(nome);
		usuario.setEmail(email);
		usuario.setPassword(password);
		usuario.setAdministrador("false");
		return usuario;	
	}
	

}
