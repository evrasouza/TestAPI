package Factory;

import com.github.javafaker.Faker;

import POJO.UsersPojo;

public class UserDataFactory {	
	
	Faker faker = new Faker();
	
	public String email = faker.internet().emailAddress();
	public String nome = faker.name().fullName();
	public String password = "teste";
	
	public UsersPojo userAdmin(){	
		UsersPojo user = new UsersPojo();
		
		user.setNome(nome);
		user.setEmail(email);
		user.setPassword(password);
		user.setAdministrador("true");
		
		return user;	
	}
	
	public UsersPojo userNotAdmin(){
		UsersPojo user = new UsersPojo();
		
		user.setNome(nome);
		user.setEmail(email);
		user.setPassword(password);
		
		return user;	
	}
	
	public UsersPojo userWithoutName(){
		UsersPojo user = new UsersPojo();
		
		user.setEmail(email);
		user.setPassword(password);
		user.setAdministrador("true");
		
        return user;
    }
	
    public UsersPojo userWithoutPassword(){
    	UsersPojo user = new UsersPojo();
        
    	user.setNome(nome);
		user.setEmail(email);
		user.setAdministrador("true");
        
        return user;
    }

    public UsersPojo userWithoutEmail(){
    	UsersPojo user = new UsersPojo();

    	user.setNome(nome);
		user.setPassword(password);
		user.setAdministrador("true");
        
        return user;
    }
    
    public UsersPojo userWithNoFields(){
    	UsersPojo user = new UsersPojo();

    	user.setNome("");
		user.setEmail("");
		user.setPassword("");
		user.setAdministrador("");
		
        
        return user;
    }
	

}
